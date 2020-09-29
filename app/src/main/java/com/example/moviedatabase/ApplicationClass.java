package com.example.moviedatabase;

import android.app.Application;
import android.database.SQLException;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ApplicationClass extends Application {
    public static ArrayList<String> suggestions;
    public static ArrayList<Movie> movies;
    public static Boolean firstScreen, visible;
    public static int totalPages;
    public static String Page;

    @Override
    public void onCreate() {
        super.onCreate();

        visible = false;

        totalPages = 10;

        Page = "1";

        movies = new ArrayList<>();

        firstScreen = true;

        suggestions = new ArrayList<>();

        try {
            SuggestionDB db = new SuggestionDB(this);
            db.open();

            suggestions = db.getData();

            db.close();
        }
        catch (SQLException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
