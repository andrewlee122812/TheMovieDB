package in.reduxpress.themoviedb.AsyncTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import in.reduxpress.themoviedb.DataModels.Movie;
import in.reduxpress.themoviedb.DataModels.TvShows;

/**
 * Created by kumardivyarajat on 28/09/15.
 */
public class FetchContentTask extends AsyncTask<String,Void,Void> {

    private ArrayList<Movie> movieList;
    private ArrayList<TvShows> tvShowsList;
    public AsyncResponse delegate=null;
    String category;
    String contentQuery;
    String PRIMARY_RELEASE_DATE_GREATER_THAN= "primary_release_date.gte";
    String PRIMARY_RELEASE_DATE_LESS_THAN= "primary_release_date.lte";


    @Override
    protected Void doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader     = null;
        String movieStr    = null;
        Calendar cal = Calendar.getInstance();
        Calendar cal1 = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String today = format1.format(cal1.getTime());
        System.out.println(today);
        String oneMonthBack = format1.format(cal.getTime());
        System.out.println(oneMonthBack);

        String content = params[0];
        String sort_by = params[1];
        category = sort_by;
        contentQuery = content;
        Uri.Builder builder1 = new Uri.Builder();
        //http://api.themoviedb.org/3/discover/movie?primary_release_date.gte=2015-09-01&sort_by=release_date.desc&api_key=c74eefc5fded173206b2b3abb1bc76a2
        //primary_release_date.gte=2015-09-01
        builder1.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("discover")
                .appendPath(content);

        if(sort_by.equals("release_date.desc")) {
            builder1.appendQueryParameter("sort_by", "popularity.desc");
            builder1.appendQueryParameter(PRIMARY_RELEASE_DATE_GREATER_THAN,oneMonthBack);
            builder1.appendQueryParameter(PRIMARY_RELEASE_DATE_LESS_THAN,today);
        } else {
            builder1.appendQueryParameter("sort_by", sort_by);
        }
        builder1.appendQueryParameter("api_key", "c74eefc5fded173206b2b3abb1bc76a2");
        String builtUrl = builder1.build().toString();
        String checkUrl = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=c74eefc5fded173206b2b3abb1bc76a2";
        Log.d("URL",builtUrl);

        try {
            URL url    = new URL(builtUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();


            InputStream inputStream = connection.getInputStream();
            StringBuilder builder   = new StringBuilder();
            reader                  = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }

            movieStr = builder.toString();
        } catch (IOException e) {
            Log.e("FetchContentTask", "Error ", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("FetchContentTask", "Error closing stream", e);
                }
            }
        }

        if(contentQuery.equals("movie")) {
            MoviesParser(movieStr);
        } else if(contentQuery.equals("tv")) {
            TvShowsParser(movieStr);
        }
     return null;
    }

    @Override
    protected void onPostExecute(Void v) {
       if(contentQuery.equals("movie")) {
           if(movieList != null) {
               delegate.processFinish(movieList);

           } else {
               Log.d("Error occurred","" );
           }
       } else if(contentQuery.equals("tv")) {
           if(tvShowsList != null) {
               delegate.processFinish(tvShowsList);

           } else {
               Log.d("Error occurred","" );
           }
       }


    }

    private Void MoviesParser(String result) {
        JSONObject myjson;
        movieList = new ArrayList<>();
        try
        {
            myjson = new JSONObject(result);
            Log.d("" ,myjson.toString());
            JSONArray page1 = myjson.getJSONArray("results");
            if(contentQuery.equals("movie")) {
                for (int i = 0; i < page1.length(); i++) {
                    JSONObject movieObject = page1.getJSONObject(i);
                    Movie movie = new Movie();
                    movie.setOriginal_title(movieObject.getString("original_title"));
                    movie.setAdult(movieObject.getBoolean("adult"));
                    movie.setOverView(movieObject.getString("overview"));
                    movie.setMovieID(movieObject.getString("id"));
                    movie.setReleaseDate(movieObject.getString("release_date"));
                    movie.setVoteAverage(movieObject.getString("vote_average"));
                    movie.setPoster_path("http://image.tmdb.org/t/p/w500//" + movieObject.getString("poster_path"));
                    movie.setBackdrop_path("http://image.tmdb.org/t/p/w780//" + movieObject.getString("backdrop_path"));

                   /* Bitmap moviePosterBitmap = getBitmapFromURL(movie.getPoster_path());
                    movie.setMoviePoster(moviePosterBitmap);
                    Bitmap movieBackDropBitmap = getBitmapFromURL(movie.getBackdrop_path());
                    movie.setMovieBackdrop(movieBackDropBitmap);*/

                    JSONArray genre = movieObject.getJSONArray("genre_ids");
                    ArrayList tempList = new ArrayList();
                    for (int j = 0; j < genre.length(); j++) {
                        tempList.add(genre.get(j));
                    }
                    movie.setGenre(tempList);
                    movie.setCategory(category);

                    movieList.add(movie);
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Void TvShowsParser(String result) {
        JSONObject myjson;
        tvShowsList = new ArrayList<>();
        Log.d("Result String:",result);

        try
        {
            myjson = new JSONObject(result);
            Log.d("" ,myjson.toString());
            JSONArray page1 = myjson.getJSONArray("results");

                for (int i = 0; i < page1.length(); i++) {
                    JSONObject movieObject = page1.getJSONObject(i);
                    TvShows movie = new TvShows();
                    movie.setOriginal_name(movieObject.getString("original_name"));
                    movie.setOverview(movieObject.getString("overview"));
                    Log.d("set in tv shows object:",movie.getOverview());
                    movie.setId(movieObject.getString("id"));
                    movie.setFirst_air_date(movieObject.getString("first_air_date"));
                    movie.setVoteAverage(movieObject.getString("vote_average"));
                    movie.setPoster_path("http://image.tmdb.org/t/p/w500//" + movieObject.getString("poster_path"));
                    movie.setBackdrop_path("http://image.tmdb.org/t/p/w780//" + movieObject.getString("backdrop_path"));
                    JSONArray genre = movieObject.getJSONArray("genre_ids");
                    ArrayList tempList = new ArrayList();
                    for (int j = 0; j < genre.length(); j++) {
                        tempList.add(genre.get(j));
                    }
                    movie.setGenre(tempList);
                    movie.setCategory(category);
                    movie.setOrigin_country(movieObject.getString("origin_country"));
                    movie.setOriginal_language(movieObject.getString("original_language"));
                    tvShowsList.add(movie);
                }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}