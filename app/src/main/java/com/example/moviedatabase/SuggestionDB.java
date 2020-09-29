package com.example.moviedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SuggestionDB {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_TITLE = "_title";

    private final String DATABASE_NAME = "SuggestionDB";
    private final String DATABASE_TABLE = "SuggestionTable";
    private final int DATABASE_VERSION = 2;

    private DBHelper myHelper;
    private final Context myContext;
    private SQLiteDatabase myDatabase;

    public SuggestionDB(Context context){
        myContext = context;
    }

    private class DBHelper extends SQLiteOpenHelper{

        public DBHelper (Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String sqlCode = "CREATE TABLE " + DATABASE_TABLE + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_TITLE + " TEXT NOT NULL);";

            sqLiteDatabase.execSQL(sqlCode);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(sqLiteDatabase);
        }
    }

    public SuggestionDB open() throws SQLException{
        myHelper = new DBHelper(myContext);
        myDatabase = myHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        myHelper.close();
    }

    public long createEntry(String title){
        ContentValues cv = new ContentValues();

        cv.put(KEY_TITLE, title);

        return myDatabase.insert(DATABASE_TABLE, null, cv);
    }

    public ArrayList<String> getData(){
        String [] columns = new String[] {KEY_ROWID, KEY_TITLE};
        Cursor c = myDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);

        ArrayList<String> get = new ArrayList<>();
        ArrayList<String> res = new ArrayList<>();

        int iTitle = c.getColumnIndex(KEY_TITLE);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            get.add(c.getString(iTitle));
        }

        if (get.size() <= 10){
            res = get;
        }
        else{
            for (int size = get.size()-10; size < get.size(); ++size){
                res.add(get.get(size));
            }

        }
        return res;
    }
}
