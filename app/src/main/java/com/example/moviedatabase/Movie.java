package com.example.moviedatabase;

public class Movie {
    private String posterUrl;
    private String title;
    private String releaseDate;
    private String overview;

    public Movie(String posterUrl, String title, String releaseDate, String overview) {
        this.posterUrl = posterUrl;
        this.title = title;
        this.releaseDate = releaseDate;
        this.overview = overview;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
