package in.reduxpress.themoviedb.DataModels;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by kumardivyarajat on 25/09/15.
 */
public class Movie implements Parcelable {

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
    ArrayList genre;
    String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList getGenre() {
        return genre;
    }

    public void setGenre(ArrayList genre) {
        this.genre = genre;
    }

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

    protected Movie(Parcel in) {
        original_title = in.readString();
        byte adultVal = in.readByte();
        adult = adultVal == 0x02 ? null : adultVal != 0x00;
        backdrop_path = in.readString();
        poster_path = in.readString();
        overView = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readString();
        movieID = in.readString();
        movieBackdrop = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        moviePoster = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        if (in.readByte() == 0x01) {
            genre = new ArrayList<>();
            in.readList(genre, Genre.class.getClassLoader());
        } else {
            genre = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(original_title);
        if (adult == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (adult ? 0x01 : 0x00));
        }
        dest.writeString(backdrop_path);
        dest.writeString(poster_path);
        dest.writeString(overView);
        dest.writeString(releaseDate);
        dest.writeString(voteAverage);
        dest.writeString(movieID);
        dest.writeValue(movieBackdrop);
        dest.writeValue(moviePoster);
        if (genre == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(genre);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}