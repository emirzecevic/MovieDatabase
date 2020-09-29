package com.example.moviedatabase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.moviedatabase.ApplicationClass.movies;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private ArrayList<Movie> movies;
    String posterUrl = "https://image.tmdb.org/t/p/w500";

    public MovieAdapter(ArrayList<Movie> list){
        movies = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle, tvRelease, tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvRelease = itemView.findViewById(R.id.tvRelease);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
        }
    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_items, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, int position) {
        holder.tvTitle.setText(movies.get(position).getTitle());
        holder.tvRelease.setText(movies.get(position).getReleaseDate());
        holder.tvOverview.setText(movies.get(position).getOverview());

//      PLACEHOLDER IMAGE
        if (movies.get(position).getPosterUrl() == null){
            holder.ivPoster.setImageResource(R.drawable.placeholder_foreground);
        }
        else {
            Picasso.get().load(posterUrl + movies.get(position).getPosterUrl()).into(holder.ivPoster);
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
