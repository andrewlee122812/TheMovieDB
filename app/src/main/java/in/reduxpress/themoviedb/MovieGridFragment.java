package in.reduxpress.themoviedb;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lucasr.twowayview.TwoWayView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import in.reduxpress.themoviedb.Adapters.ImageAdapter;
import in.reduxpress.themoviedb.DataModels.Genre;
import in.reduxpress.themoviedb.DataModels.Movie;
import in.reduxpress.themoviedb.HelperClasses.DatabaseHandler;

/**
 * Created by kumardivyarajat on 10/06/15.
 */
public  class MovieGridFragment extends Fragment implements AdapterView.OnItemClickListener{

    //private ListView mGridView;
    private ImageAdapter mImageAdapter;
    private int screenWidth;
    private int screenDPI;
    private List<Movie> movieList;
    private List<Genre> genreList;
    public static int scrollX = 0;
    public static int scrollY = -1;
    private ScrollView mMainScrollView;

    public static int scrollXHL1X = 0;

    TwoWayView mHorizontalListView;
    TwoWayView mHorizontalListView1;
    TwoWayView mHorizontalListView2;

    final String SORT_BY_POPULARITY = "popularity.desc";
    final String SORT_BY_TOP_RATED = "vote_average.desc";
    final String SORT_BY_LATEST = "release_date.desc";
    final String SORT_BY_OLDEST = "release_date.asc";
    int flag = 0;
    FetchMovies fetchMoviesTask;
    FetchGenreTask fetchGenreTask;
    SharedPreferences genreStorage;
    SharedPreferences.Editor genreEditor;


    public MovieGridFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //mGridView = (ListView)rootView.findViewById(R.id.mainGrid);
        mHorizontalListView = (TwoWayView)  rootView.findViewById(R.id.list1);
        mHorizontalListView1 = (TwoWayView)  rootView.findViewById(R.id.list2);
        mHorizontalListView2 = (TwoWayView)  rootView.findViewById(R.id.list3);
        mMainScrollView = (ScrollView) rootView.findViewById(R.id.main_scrollview);

        genreStorage = getActivity().getApplicationContext().getSharedPreferences("Genre", Context.MODE_PRIVATE);
        genreEditor = genreStorage.edit();


        if(genreStorage.getBoolean("firstrun",true)) {
            fetchGenreTask = new FetchGenreTask();
            fetchGenreTask.execute();
        }



        fetchMoviesTask = new FetchMovies();
        fetchMoviesTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,SORT_BY_POPULARITY);
       // fetchMoviesTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,SORT_BY_TOP_RATED);


        mHorizontalListView.setOnItemClickListener(MovieGridFragment.this);
        mHorizontalListView1.setOnItemClickListener(MovieGridFragment.this);
        mHorizontalListView2.setOnItemClickListener(MovieGridFragment.this);

        Log.d("In launch actiivty",String.valueOf(genreStorage.getBoolean("firstrun",true)));



        return rootView;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       Movie movie = movieList.get(position);


        Toast.makeText(getActivity(),movie.getOriginal_title()+ "",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity().getApplicationContext(),DetailsActivity.class);
        intent.putExtra("MovieDetails",movie);
        startActivity(intent);

    }


    public class FetchMovies extends AsyncTask<String,Void,List<Movie>> {

        @Override
        protected List<Movie> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader     = null;
            String movieStr    = null;
            String sort_by = params[0];
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
            screenWidth = getScreenDimen();
            screenDPI = getScreenDPI();
            DatabaseHandler db = new DatabaseHandler(getActivity());
            mImageAdapter = new ImageAdapter(getActivity(),result,screenWidth,screenDPI);
            if(flag == 0) {
                mHorizontalListView.setAdapter(mImageAdapter);
                mHorizontalListView.setItemMargin(2);
                mHorizontalListView1.setAdapter(mImageAdapter);
                mHorizontalListView1.setItemMargin(2);
                mHorizontalListView2.setAdapter(mImageAdapter);
                mHorizontalListView2.setItemMargin(2);
                flag = 1;
            } /*else if(flag == 1) {

                flag = 2;
            } else {
                Log.d("Flag value:" , flag + "out of context");
            }*/
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

                    movieList.add(movie);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            return movieList;
        }
    }

    public class FetchGenreTask extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection connection = null;
            BufferedReader reader     = null;
            String movieStr    = null;

            String checkUrl = "http://api.themoviedb.org/3/genre/movie/list?api_key=c74eefc5fded173206b2b3abb1bc76a2";
            try {
                URL url    = new URL(checkUrl);
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
                Log.e("FetchGenreTask", "Error ", e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("FetchGenreTask", "Error closing stream", e);
                    }
                }
            }

            return movieStr;
        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject myjson;
            genreList = new ArrayList<>();
            try
            {
                myjson = new JSONObject(result);
                Log.d("" ,myjson.toString());

                JSONArray genre = myjson.getJSONArray("genres");
                for(int i = 0; i < genre.length(); i++ ) {
                    JSONObject genreJSONObject = genre.getJSONObject(i);
                    Genre genre1 = new Genre();
                    genre1.setId(genreJSONObject.getString("id"));
                    genre1.setTitle(genreJSONObject.getString("name"));
                    genreEditor.putString(genre1.getId(),genre1.getTitle());
                    genreEditor.commit();
                    genreList.add(genre1);
                }

                for (Genre genre1 : genreList ) {
                    Log.d(genre1.getId(),genre1.getTitle());
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }


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

    @Override
    public void onPause()
    {
        super.onPause();
        scrollX = mMainScrollView.getScrollX();
        scrollY = mMainScrollView.getScrollY();
    }

    @Override
    public void onResume()
    {
        Log.d("X:" + scrollX + " Y:" + scrollY, "");
        super.onResume();

        if (genreStorage.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            fetchGenreTask = new FetchGenreTask();
            fetchGenreTask.execute();
            // using the following line to edit/commit prefs
            genreStorage.edit().putBoolean("firstrun", false).commit();
        }

        mMainScrollView.post(new Runnable()
        {
            @Override
            public void run()
            {
                mMainScrollView.scrollTo(scrollX, scrollY);

            }
        });
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("SCROLL_POSITION",
                new int[]{mMainScrollView.getScrollX(), mMainScrollView.getScrollY()});
        outState.putInt("SCROLL_POSITION_HL1", mHorizontalListView.getLastVisiblePosition());
        Log.d("Scroll for horizontal HL1:",mHorizontalListView.getLastVisiblePosition() +"");
    }

    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null) {
            final int[] position = savedInstanceState.getIntArray("SCROLL_POSITION");
            final int hl1position = savedInstanceState.getInt("SCROLL_POSITION_HL1");
            if (position != null )
                mMainScrollView.post(new Runnable() {
                    public void run() {
                        mMainScrollView.scrollTo(position[0], position[1]);
                        Log.d("X :" +  position[0],"Y :"+ position[1]);
                    }
                });
            if(hl1position != 0 ){
                mHorizontalListView.post(new Runnable() {
                    @Override
                    public void run() {
                        mHorizontalListView.smoothScrollToPosition(hl1position);
                        Log.d("On view state restored",hl1position+"");
                    }
                });
            }
        }
    }
}