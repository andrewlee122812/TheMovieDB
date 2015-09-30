package in.reduxpress.themoviedb.HelperClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import in.reduxpress.themoviedb.DataModels.Movie;

/**
 * Created by kumardivyarajat on 25/09/15.
 */
public class MoviesDBHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "moviesOffline";

    // Contacts table name
    private static final String TABLE_MOVIE = "movies";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESC = "description";
    private static final String KEY_VOTE_AVG = "avg_votes";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_ADULT = "adult";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_BACKDROP_PATH = "backdrop_path";


    public MoviesDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE "
                + TABLE_MOVIE
                + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_DESC + " TEXT,"
                + KEY_VOTE_AVG + " TEXT,"
                + KEY_RELEASE_DATE + " TEXT,"
                + KEY_ADULT + " TEXT,"
                + KEY_POSTER_PATH + " TEXT,"
                + KEY_BACKDROP_PATH + " TEXT"
                + " )";
        Log.d("Database - -- - -", CREATE_CONTACTS_TABLE);

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE);

        // Create tables again
        onCreate(db);
    }

    public void addContact(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();

        String CREATE_CONTACTS_TABLE = "CREATE TABLE "
                + TABLE_MOVIE
                + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_DESC + " TEXT,"
                + KEY_VOTE_AVG + " TEXT,"
                + KEY_RELEASE_DATE + " TEXT,"
                + KEY_ADULT + " TEXT,"
                + KEY_POSTER_PATH + " TEXT,"
                + KEY_BACKDROP_PATH + " TEXT"
                + ")";

        ContentValues values = new ContentValues();
        values.put(KEY_ID, movie.getMovieID());
        Log.d("Movie Id", movie.getMovieID());
        Log.d("Movie", values.get(KEY_ID).toString());
        values.put(KEY_NAME, movie.getOriginal_title());
        Log.d("MOvie name: ", values.get(KEY_NAME).toString());
        values.put(KEY_DESC, movie.getOverView());        //2
        values.put(KEY_VOTE_AVG, movie.getVoteAverage()); //3
        values.put(KEY_RELEASE_DATE, movie.getReleaseDate());//4
        values.put(KEY_ADULT, movie.getAdult());            //5
        values.put(KEY_POSTER_PATH, movie.getPoster_path());//6
        values.put(KEY_BACKDROP_PATH, movie.getBackdrop_path());//7//1

        System.out.println(values.toString());
        Log.d("Database - -- - -", CREATE_CONTACTS_TABLE);


        //INSERT INTO movies(id,backdrop_path,description,avg_votes,name,release_date,adult,poster_path)

        // Inserting Row
        db.insert(TABLE_MOVIE, null, values);
        db.close(); // Closing database connection
    }

    //String movieID,
    // String voteAverage,
    // String releaseDate,
    // String overView,
    // String poster_path,
    // String backdrop_path,
    // String adult,
    // String original_title
    public Movie getMovie(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MOVIE, new String[]{KEY_ID,
                        KEY_NAME, KEY_DESC, KEY_VOTE_AVG, KEY_RELEASE_DATE, KEY_ADULT, KEY_POSTER_PATH, KEY_BACKDROP_PATH}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Movie movie = new Movie(cursor.getColumnName(1),
                cursor.getString(3), cursor.getString(4),cursor.getString(2),cursor.getString(6),cursor.getString(7),cursor.getString(5),cursor.getString(0));

        return movie;
    }

    public List<Movie> getAllContacts() {
        List<Movie> contactList = new ArrayList<Movie>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MOVIE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Movie contact = new Movie();
                contact.setMovieID(cursor.getString(0));
                contact.setOriginal_title(cursor.getString(1));
                contact.setOverView(cursor.getString(2));
                contact.setVoteAverage(cursor.getString(3));
                contact.setReleaseDate(cursor.getString(4));
                contact.setAdult(Boolean.valueOf(cursor.getString(5)));
                contact.setPoster_path(cursor.getString(6));
                contact.setBackdrop_path(cursor.getString(7));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public int getMoviesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MOVIE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Deleting single contact
    public void deleteContact(Movie contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOVIE, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.getMovieID())});
        db.close();
    }


    public void drop() {

    }

}