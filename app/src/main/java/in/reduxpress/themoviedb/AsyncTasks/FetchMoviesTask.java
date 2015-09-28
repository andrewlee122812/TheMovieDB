package in.reduxpress.themoviedb.AsyncTasks;

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
import java.util.ArrayList;
import java.util.List;

import in.reduxpress.themoviedb.DataModels.Movie;

/**
 * Created by kumardivyarajat on 28/09/15.
 */
public class FetchMoviesTask extends AsyncTask<String,Void,List<Movie>> {

    private List<Movie> movieList;
    public AsyncResponse delegate=null;
    String category;

    @Override
    protected List<Movie> doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader     = null;
        String movieStr    = null;
        String sort_by = params[0];
        category = sort_by;
        Uri.Builder builder1 = new Uri.Builder();
        builder1.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter("sort_by",sort_by)
                .appendQueryParameter("api_key","c74eefc5fded173206b2b3abb1bc76a2");
        String builtUrl = builder1.build().toString();
        String checkUrl = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=c74eefc5fded173206b2b3abb1bc76a2";

        Log.d("Checking URI", checkUrl.compareTo(checkUrl) + "");

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
            Log.e("MovieGridFragment", "Error ", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("MovieGridFragment", "Error closing stream", e);
                }
            }
        }
        return MoviesParser(movieStr);

    }

    @Override
    protected void onPostExecute(List<Movie> result) {

        if(result != null) {
            delegate.processFinish(result);

        } else {
            Log.d("Error occurred","" );
        }



    }

    private List<Movie> MoviesParser(String result) {
        JSONObject myjson;
        movieList = new ArrayList<>();
        try
        {
            myjson = new JSONObject(result);
            Log.d("" ,myjson.toString());
            JSONArray page1 = myjson.getJSONArray("results");
            for(int i = 0; i < page1.length(); i++ ) {
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
                JSONArray genre = movieObject.getJSONArray("genre_ids");
                ArrayList tempList = new ArrayList();
                for(int j = 0; j < genre.length(); j++ ) {
                    tempList.add(genre.get(j));
                }
                movie.setGenre(tempList);
                movie.setCategory(category);

                movieList.add(movie);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return movieList;
    }
}