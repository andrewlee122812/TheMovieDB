package in.reduxpress.themoviedb;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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

import in.reduxpress.themoviedb.Adapters.ImageAdapter;
import in.reduxpress.themoviedb.DataModels.Movie;
import in.reduxpress.themoviedb.HelperClasses.DatabaseHandler;

/**
 * Created by kumardivyarajat on 10/06/15.
 */
public  class MovieGridFragment extends Fragment implements AdapterView.OnItemClickListener{

    private GridView mGridView;
    private ImageAdapter mImageAdapter;
    private int screenWidth;
    private int screenDPI;
    private List<Movie> movieList;

    public MovieGridFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mGridView = (GridView)rootView.findViewById(R.id.mainGrid);

        FetchMovies fetchMoviesTask = new FetchMovies();
        fetchMoviesTask.execute();

        mGridView.setOnItemClickListener(MovieGridFragment.this);

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       Movie movie = new Movie();
        movie = movieList.get(position);

        Intent intent = new Intent(getActivity().getApplicationContext(),DetailsActivity.class);
        intent.putExtra("MovieDetails",movie);
        startActivity(intent);

    }


    public class FetchMovies extends AsyncTask<Void,Void,List<Movie>> {

        @Override
        protected List<Movie> doInBackground(Void... params) {
            HttpURLConnection connection = null;
            BufferedReader reader     = null;
            String movieStr    = null;

            try {
                URL url    = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=c74eefc5fded173206b2b3abb1bc76a2");
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
                Log.e("PlaceholderFragment", "Error ", e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return MoviesParser(movieStr);

        }

        @Override
        protected void onPostExecute(List<Movie> result) {
            screenWidth = getScreenDimen();
            screenDPI = getScreenDPI();
            DatabaseHandler db = new DatabaseHandler(getActivity());
            mImageAdapter = new ImageAdapter(getActivity(),result,screenWidth,screenDPI);
            mGridView.setAdapter(mImageAdapter);
        }

        private List<Movie> MoviesParser(String result) {
            JSONObject myjson;
             movieList = new ArrayList<>();
            try
            {
                myjson = new JSONObject(result);
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
                    movie.setBackdrop_path("http://image.tmdb.org/t/p/w500//" + movieObject.getString("backdrop_path"));
                    movieList.add(movie);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            return movieList;
        }

    }

    public int getScreenDimen() {

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        Log.d("Screen px value:", width + "");
        return width;
    }

    public int getScreenDPI() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return metrics.densityDpi;
    }



}