package com.example.moviedatabase;

import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SuggestionAdapter.ItemClicked {

    TextView tvSuggestion;
    EditText etTitle;
    ImageButton ibSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSuggestion = findViewById(R.id.tvSuggestion);
        etTitle = findViewById(R.id.etTitle);
        ibSearch = findViewById(R.id.ibSearch);

        ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(etTitle.getText().toString().trim(), 0);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (ApplicationClass.firstScreen) {
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .hide(fragmentManager.findFragmentById(R.id.moviesFragment))
                    .commit();
        } else {
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .hide(fragmentManager.findFragmentById(R.id.searchFragment))
                    .show(fragmentManager.findFragmentById(R.id.moviesFragment))
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ApplicationClass.movies.removeAll(ApplicationClass.movies);
        ApplicationClass.totalPages = 10;
        ApplicationClass.firstScreen = true;

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .hide(fragmentManager.findFragmentById(R.id.moviesFragment))
                .commit();
    }

    public void search(final String title, final int tag) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/search/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieDatabaseApi movieDatabaseApi = retrofit.create(MovieDatabaseApi.class);

        for (int i = 1; i < ApplicationClass.totalPages; ++i) {

            ApplicationClass.Page = String.valueOf(i);

            Call<Post> call = movieDatabaseApi.getPost("movie?api_key=2696829a81b1b5827d515ff121700838&query=" + title + "&page=" + ApplicationClass.Page);

            call.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Code" + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Post posts = response.body();
                    final List<Result> results = posts.getResults();
                    ApplicationClass.totalPages = posts.getTotalPages();

                    for (Result result : results) {
                        ApplicationClass.movies.add(new Movie(result.getPosterPath(), result.getTitle(), result.getReleaseDate(), result.getOverview()));
                    }
                }

                @Override
                public void onFailure(Call<Post> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Code: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
            });
        }

        Utils.delay(1, new Utils.DelayCallback() {
            @Override
            public void afterDelay() {
                ApplicationClass.firstScreen = false;

                if (tag == 0) {
                    boolean listed = true;
                    for (int i = 0; i < ApplicationClass.suggestions.size(); ++i) {
                        if (title.equals(ApplicationClass.suggestions.get(i))) {
                            listed = false;
                            break;
                        }
                    }
                    if (listed) {
                        if (ApplicationClass.suggestions.size() > 9) {
                            ApplicationClass.suggestions.remove(0);
                        }
                        try {
                            SuggestionDB db = new SuggestionDB(MainActivity.this);

                            db.open();
                            db.createEntry(title);
                            db.close();
                        } catch (SQLException e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        ApplicationClass.suggestions.add(title);
                    }
                }

                if (ApplicationClass.movies.size() == 0) {
                    Toast.makeText(MainActivity.this, "No movies!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
            }
        });
    }

    @Override
    public void onItemClicked(int index) {
        search(ApplicationClass.suggestions.get(index), 1);
    }
}

