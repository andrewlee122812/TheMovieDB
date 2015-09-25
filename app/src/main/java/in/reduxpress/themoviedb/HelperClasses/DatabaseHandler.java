package in.reduxpress.themoviedb.HelperClasses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by kumardivyarajat on 25/09/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "moviesOffline";

    // Contacts table name
    private static final String TABLE_CONTACTS = "movies";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESC = "description";
    private static final String KEY_VOTE_AVG = "avg_votes";
    private static final String KEY_RELEASE_DATE = "release date";
    private static final String KEY_ADULT = "adult";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_BACKDROP_PATH = "backdrop_path";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE "
                + TABLE_CONTACTS
                + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_DESC + " TEXT,"
                + KEY_VOTE_AVG + "TEXT,"
                + KEY_RELEASE_DATE + "TEXT,"
                + KEY_ADULT + "INTEGER,"
                + KEY_POSTER_PATH + "TEXT,"
                + KEY_BACKDROP_PATH + "TEXT"
                + ")";
        Log.d("", CREATE_CONTACTS_TABLE);

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

}