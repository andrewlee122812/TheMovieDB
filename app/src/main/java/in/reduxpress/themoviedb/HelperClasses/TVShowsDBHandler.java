package in.reduxpress.themoviedb.HelperClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import in.reduxpress.themoviedb.DataModels.TvShows;

/**
 * Created by kumardivyarajat on 30/09/15.
 */
public class TVShowsDBHandler extends SQLiteOpenHelper{
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "favourites";

    // Contacts table name
    private static final String TABLE_TVSHOWS = "tvshows";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESC = "description";
    private static final String KEY_VOTE_AVG = "avg_votes";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_ORIGIN_COUNTRY = "origin_country";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_BACKDROP_PATH = "backdrop_path";
    private static final String KEY_CONTENT = "content";
    private Context context;

    /* String original_name;
    String original_language;
    String backdrop_path;
    String first_air_date;
    String overview;
    String origin_country;
    String id;
    String poster_path;
    String voteAverage;
    String category;*/


    public TVShowsDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE "
                + TABLE_TVSHOWS
                + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_DESC + " TEXT,"
                + KEY_VOTE_AVG + " TEXT,"
                + KEY_RELEASE_DATE + " TEXT,"
                + KEY_ORIGIN_COUNTRY + " TEXT,"
                + KEY_POSTER_PATH + " BLOB,"
                + KEY_BACKDROP_PATH + " BLOB,"
                + " )";
        Log.d("Database - -- - -", CREATE_CONTACTS_TABLE);

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TVSHOWS);

        // Create tables again
        onCreate(db);
    }

    public void addContact(TvShows movie) {
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(KEY_ID, movie.getId());
        Log.d("Movie Id", movie.getId());
        Log.d("Movie", values.get(KEY_ID).toString());
        values.put(KEY_NAME, movie.getOriginal_name());
        Log.d("MOvie name: ", values.get(KEY_NAME).toString());
        values.put(KEY_DESC, movie.getOverview());        //2
        values.put(KEY_VOTE_AVG, movie.getVoteAverage()); //3
        values.put(KEY_RELEASE_DATE, movie.getFirst_air_date());//4
        values.put(KEY_ORIGIN_COUNTRY, movie.getOrigin_country());
        byte[] posterBlob = new byte[8092];
        try {
            posterBlob = FetchImagestask(movie.getPoster_path());
        } catch (IOException e) {
            e.printStackTrace();
        }
        values.put(KEY_POSTER_PATH, posterBlob);//6
        byte[] backDropBlob = new byte[0];
        try {
            backDropBlob = FetchImagestask(movie.getBackdrop_path());
        } catch (IOException e) {
            e.printStackTrace();
        }//5
        values.put(KEY_BACKDROP_PATH, backDropBlob);//7//1

        System.out.println(values.toString());


        //INSERT INTO movies(id,backdrop_path,description,avg_votes,name,release_date,adult,poster_path)

        // Inserting Row
        db.insert(TABLE_TVSHOWS, null, values);
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
    public TvShows getMovie(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TVSHOWS, new String[]{KEY_ID,
                        KEY_NAME, KEY_DESC, KEY_VOTE_AVG, KEY_RELEASE_DATE, KEY_ORIGIN_COUNTRY, KEY_POSTER_PATH, KEY_BACKDROP_PATH}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        TvShows movie = new TvShows(cursor.getString(0),
                cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7));

        return movie;
    }

    public List<TvShows> getAllContacts() {
        List<TvShows> contactList = new ArrayList<TvShows>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TVSHOWS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TvShows contact = new TvShows();
                contact.setId(cursor.getString(0));
                contact.setOriginal_name(cursor.getString(1));
                contact.setOverview(cursor.getString(2));
                contact.setVoteAverage(cursor.getString(3));
                contact.setFirst_air_date(cursor.getString(4));
                contact.setOrigin_country(cursor.getString(5));
                contact.setMoviePoster(cursor.getBlob(6));
                contact.setMovieBackdrop(cursor.getBlob(7));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public int getMoviesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TVSHOWS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Deleting single contact
    public void deleteContact(TvShows contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TVSHOWS, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});
        db.close();
    }

    public byte[]  FetchImagestask (String url1) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        URL url = new URL(url1);
        InputStream is = null;
        try {
            is = url.openStream ();
            byte[] byteChunk = new byte[8092]; // Or whatever size you want to read in at a time.
            int n;

            while ( (n = is.read(byteChunk)) > 0 ) {
                baos.write(byteChunk, 0, n);
            }
        }
        catch (IOException e) {
            System.err.printf ("Failed while reading bytes from %s: %s", url.toExternalForm(), e.getMessage());
            e.printStackTrace ();
            // Perform any other exception handling that's appropriate.
        }
        finally {
            if (is != null) {
                Log.d("Recieved image",is.toString());
                is.close();
            }
        }

        return baos.toByteArray();

    }




    public void drop() {
        if(context!= null ){
            context.deleteDatabase(DATABASE_NAME);
        }
    }


}
