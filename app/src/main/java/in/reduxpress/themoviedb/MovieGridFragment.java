package in.reduxpress.themoviedb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import in.reduxpress.themoviedb.AsyncTasks.AsyncResponse;
import in.reduxpress.themoviedb.AsyncTasks.FetchContentTask;
import in.reduxpress.themoviedb.DataModels.Genre;
import in.reduxpress.themoviedb.DataModels.Movie;

/**
 * Created by kumardivyarajat on 10/06/15.
 */
public  class MovieGridFragment extends Fragment implements AsyncResponse{

    //private ListView mGridView;
    private ImageAdapter mImageAdapter;
    private int screenWidth;
    private int screenDPI;
    private List<Genre> genreList;
    public static int scrollX = 0;
    public static int scrollY = -1;
    private ScrollView mMainScrollView;

    public static int scrollXHL1X = 0;
    List<Movie> movieList;

    TwoWayView mHorizontalListView;
    TwoWayView mHorizontalListView1;
    TwoWayView mHorizontalListView2;

    final String SORT_BY_POPULARITY = "popularity.desc";
    final String SORT_BY_TOP_RATED = "vote_average.desc";
    final String SORT_BY_LATEST = "release_date.desc";
    final String SORT_BY_OLDEST = "release_date.asc";
    final String CONTENT = "movie";
    int flag = 0;
    FetchContentTask fetchPoplularMoviesTask;
    FetchContentTask fetchTopRatedMoviesTask;
    FetchContentTask fetchLatestMoviesTask;

    FetchGenreTask fetchGenreTask;
    SharedPreferences genreStorage;
    SharedPreferences.Editor genreEditor;
    List<Movie> popularmovieList;
    List<Movie> topRatedMovieList;
    List<Movie> latestMovieList;
    Movie movie;




    public MovieGridFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mHorizontalListView = (TwoWayView)  rootView.findViewById(R.id.list1);
        mHorizontalListView1 = (TwoWayView)  rootView.findViewById(R.id.list2);
        mHorizontalListView2 = (TwoWayView)  rootView.findViewById(R.id.list3);
        mMainScrollView = (ScrollView) rootView.findViewById(R.id.main_scrollview);

        genreStorage = getActivity().getApplicationContext().getSharedPreferences("Genre", Context.MODE_PRIVATE);
        genreEditor = genreStorage.edit();
        fetchPoplularMoviesTask = new FetchContentTask();
        fetchLatestMoviesTask = new FetchContentTask();
        fetchTopRatedMoviesTask = new FetchContentTask();
        fetchGenreTask = new FetchGenreTask();


        fetchPoplularMoviesTask.delegate = this;
        fetchLatestMoviesTask.delegate = this;
        fetchTopRatedMoviesTask.delegate = this;

        popularmovieList = new ArrayList<>();
        topRatedMovieList = new ArrayList<>();
        latestMovieList = new ArrayList<>();



        if(isNetworkAvailable()) {


            fetchPoplularMoviesTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, CONTENT, SORT_BY_POPULARITY);
            fetchTopRatedMoviesTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,CONTENT, SORT_BY_TOP_RATED);
            fetchLatestMoviesTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,CONTENT, SORT_BY_LATEST);


            fetchGenreTask.execute();


            mHorizontalListView.setOnItemClickListener(popularClickListener);
            mHorizontalListView1.setOnItemClickListener(topRatedClickListener);
            mHorizontalListView2.setOnItemClickListener(latestClickListener);

            Log.d("In launch actiivty",String.valueOf(genreStorage.getBoolean("firstrun",true)));

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(true)
                    .setMessage("No internet connection. Please check your connection and then try again.")
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = getActivity().getIntent();
                            getActivity().finish();
                            startActivity(intent);
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();

        }


        return rootView;
    }


    AdapterView.OnItemClickListener popularClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            movie = popularmovieList.get(position);
            startNewActivity();

        }
    };


    AdapterView.OnItemClickListener topRatedClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            movie = topRatedMovieList.get(position);
            startNewActivity();
        }
    };
    AdapterView.OnItemClickListener latestClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            movie = latestMovieList.get(position);
            startNewActivity();
        }
    };

    private void startNewActivity() {
        Toast.makeText(getActivity(), movie.getOriginal_title() + "", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity().getApplicationContext(),DetailsActivity.class);
        intent.putExtra("MovieDetails",movie);
        startActivity(intent);
    }




    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    @Override
    public void processFinish(List output) {
         = output;
        screenWidth = getScreenDimen();
        screenDPI = getScreenDPI();
        mImageAdapter = new ImageAdapter(getActivity(),movieList,screenWidth,screenDPI);
        String category = movieList.get(0).getCategory();
        if(category.equals(SORT_BY_POPULARITY)) {
            popularmovieList = output;

            mHorizontalListView.setAdapter(mImageAdapter);

            mHorizontalListView.setItemMargin(10);

        } else if(category.equals(SORT_BY_TOP_RATED)) {
            topRatedMovieList = output;

            mHorizontalListView1.setAdapter(mImageAdapter);

            mHorizontalListView1.setItemMargin(10);

        } else if(category.equals(SORT_BY_LATEST)) {
            latestMovieList = output;

            mHorizontalListView2.setAdapter(mImageAdapter);

            mHorizontalListView2.setItemMargin(10);

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
            genreEditor.clear();

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

    @Override

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public int getScreenDimen() {
        if(getActivity() != null ) {
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            Log.d("Screen px value:", width + "");
            return width;
        }
       return 400;
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
