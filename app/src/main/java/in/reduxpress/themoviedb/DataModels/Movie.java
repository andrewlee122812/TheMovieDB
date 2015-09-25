package in.reduxpress.themoviedb.DataModels;

import android.graphics.Bitmap;

/**
 * Created by kumardivyarajat on 25/09/15.
 */
public class Movie {

    String original_title;
    Boolean adult;
    String backdrop_path;
    String poster_path;
    String overView;
    String releaseDate;
    String voteAverage;
    String movieID;
    Bitmap movieBackdrop;
    Bitmap moviePoster;

    public Bitmap getMovieBackdrop() {
        return movieBackdrop;
    }

    public void setMovieBackdrop(Bitmap movieBackdrop) {
        this.movieBackdrop = movieBackdrop;
    }

    public Bitmap getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(Bitmap moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public Movie() {
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }
}
