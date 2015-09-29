package in.reduxpress.themoviedb.DataModels;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by kumardivyarajat on 28/09/15.
 */
public class TvShows extends Movie implements Parcelable {

    String original_name;
    String original_language;
    String backdrop_path;
    String first_air_date;
    String overview;
    String origin_country;
    String id;
    String poster_path;
    String voteAverage;
    String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public ArrayList getGenre() {
        return genre;
    }

    public void setGenre(ArrayList genre) {
        this.genre = genre;
    }

    ArrayList genre;

    public String getOriginal_name() {
        return original_name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public void setFirst_air_date(String first_air_date) {
        this.first_air_date = first_air_date;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOrigin_country() {
        return origin_country;
    }

    public void setOrigin_country(String origin_country) {
        this.origin_country = origin_country;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public TvShows() {

    }

    protected TvShows(Parcel in) {
        original_name = in.readString();
        original_language = in.readString();
        backdrop_path = in.readString();
        first_air_date = in.readString();
        overview = in.readString();
        origin_country = in.readString();
        id = in.readString();
        poster_path = in.readString();
        voteAverage = in.readString();
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
        dest.writeString(original_name);
        dest.writeString(original_language);
        dest.writeString(backdrop_path);
        dest.writeString(first_air_date);
        dest.writeString(overview);
        dest.writeString(origin_country);
        dest.writeString(id);
        dest.writeString(poster_path);
        dest.writeString(voteAverage);
        if (genre == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(genre);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TvShows> CREATOR = new Parcelable.Creator<TvShows>() {
        @Override
        public TvShows createFromParcel(Parcel in) {
            return new TvShows(in);
        }

        @Override
        public TvShows[] newArray(int size) {
            return new TvShows[size];
        }
    };
}