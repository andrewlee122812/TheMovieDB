package in.reduxpress.themoviedb;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

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
import java.util.Map;

import in.reduxpress.themoviedb.Adapters.GridAdapter;
import in.reduxpress.themoviedb.DataModels.Actor;
import in.reduxpress.themoviedb.DataModels.Cast;
import in.reduxpress.themoviedb.DataModels.Movie;
import in.reduxpress.themoviedb.DataModels.TvShows;
import in.reduxpress.themoviedb.DataModels.YoutubeVideo;
import in.reduxpress.themoviedb.HelperClasses.DatabaseHandler;
import in.reduxpress.themoviedb.HelperClasses.ExpandedScrollView;
import in.reduxpress.themoviedb.HelperClasses.TrackingScrollView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment implements View.OnClickListener {

    private Movie movie;
    private ImageView mBackDropImageView;
    private View mBackDrop;
    private ImageView mPoster;
    private TextView mTitle;
    private TextView mDescription;
    private ImageButton mFavouriteButton;
    private ImageButton mAddtoListButton;
    private ImageButton mShareButton;
    private int mImageHeight;
    private int screenWidth;
    private ColorDrawable transparentDrawable;
    private TrackingScrollView trackingScrollView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList genreIDList;
    private ArrayList<String> genreList;
    private ListView mList;
    private TextView mRating;
    private Boolean isFavourite;
    private SharedPreferences genreStorage;
    private SharedPreferences favourites;
    private SharedPreferences.Editor editor;
    final String FAVOURITE_PREFERENCE = "favourite";
    final String GENRE_PREFERENCE = "Genre";
    private int count = 1;
    private TextView mReleaseDate;
    private List<YoutubeVideo> mVideoList;
    private List<Cast> mCastList;
    FetchVideosTask fetchVideosTask;
    FetchCastTask fetchCastTask;
    int count1 = 0;
    LinearLayout mYoutubeParentLayout;
    ExpandedScrollView mCastView;
    GridAdapter gridAdapter;
    List<Movie> movieList;
    List<TvShows> tvShowsList;
    RelativeLayout youTubeParentParent;
    String content;
    TvShows tvShows;
    DatabaseHandler db ;


    YouTubePlayerView youTubePlayerView;


    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout rootView = new LinearLayout(getActivity());
        inflater.inflate(R.layout.fragment_details, rootView, true);


        genreStorage = getActivity().getApplicationContext().getSharedPreferences(GENRE_PREFERENCE, Context.MODE_PRIVATE);
        favourites = getActivity().getApplicationContext().getSharedPreferences(FAVOURITE_PREFERENCE, Context.MODE_PRIVATE);


        mBackDropImageView = (ImageView) rootView.findViewById(R.id.details_imageview_backdrop);
        mPoster = (ImageView) rootView.findViewById(R.id.details_poster_imageview);
        mTitle = (TextView) rootView.findViewById(R.id.details_movie_title);
        mDescription = (TextView) rootView.findViewById(R.id.details_description);
        mFavouriteButton = (ImageButton) rootView.findViewById(R.id.add_favourite_imagebutton);
        mBackDrop = rootView.findViewById(R.id.backdrop_parent_rl);
        trackingScrollView = (TrackingScrollView) rootView.findViewById(R.id.scroller);
        mList = (ListView) rootView.findViewById(R.id.genre_listview);
        mRating = (TextView) rootView.findViewById(R.id.review_texview_editing);
        mAddtoListButton = (ImageButton) rootView.findViewById(R.id.add_to_list);
        mShareButton = (ImageButton) rootView.findViewById(R.id.share_imageButton);
        mReleaseDate = (TextView) rootView.findViewById(R.id.release_date_textview);
        mYoutubeParentLayout = (LinearLayout)rootView.findViewById(R.id.youtube_view_parent);
        mCastView = (ExpandedScrollView) rootView.findViewById(R.id.cast_grid);
        youTubeParentParent = (RelativeLayout)rootView.findViewById(R.id.youtube_view_parent_parent);
       // youTubePlayerView = (YouTubePlayerView) rootView.findViewById(R.id.youtube_view1);
        transparentDrawable = new ColorDrawable(Color.BLACK);
        db = new DatabaseHandler(getActivity());
        fetchCastTask = new FetchCastTask();
        fetchVideosTask = new FetchVideosTask();

        transparentDrawable.setAlpha(0);

        screenWidth = getScreenDimen();


        setUpTransparentActionBar();

        Bundle b = getArguments();
        content = b.getString("Content");



        if(content == null ) {
            movie = b.getParcelable("MovieDetails");
            Log.d("Movie receivved", "");

            setUpImageButtons();


            mTitle.setText(movie.getOriginal_title());
            mDescription.setText(movie.getOverView());
            mRating.setText(movie.getVoteAverage());
            mReleaseDate.setText(movie.getReleaseDate());

            String url = movie.getPoster_path();
            url = url.replace("w500", "w185");


            imageViewLoaderPicasso(movie.getBackdrop_path(), screenWidth, (int) (screenWidth * 0.56111111111111), mBackDropImageView);
            imageViewLoaderPicasso(url, 300, 450, mPoster);

            ViewGroup.LayoutParams layoutParams = mBackDropImageView.getLayoutParams();
            layoutParams.width = screenWidth;
            layoutParams.height = (int) (screenWidth * 0.56111111111111);
            mBackDropImageView.setLayoutParams(layoutParams);
        } else {

            Log.d("Check ", content);
            tvShows = b.getParcelable("MovieDetails");

            setUpImageButtons();

            mTitle.setText(tvShows.getOriginal_name());
            mDescription.setText(tvShows.getOverview());
            mRating.setText(tvShows.getVoteAverage());
            mReleaseDate.setText(tvShows.getFirst_air_date());

            String url = tvShows.getPoster_path();
            url = url.replace("w500", "w185");


            imageViewLoaderPicasso(tvShows.getBackdrop_path(), screenWidth, (int) (screenWidth * 0.56111111111111), mBackDropImageView);
            imageViewLoaderPicasso(url, 300, 450, mPoster);

        }


        ViewGroup.LayoutParams layoutParams = mBackDropImageView.getLayoutParams();
        layoutParams.width = screenWidth;
        layoutParams.height = (int) (screenWidth * 0.56111111111111);
        mBackDropImageView.setLayoutParams(layoutParams);


        setUpGenreList(genreStorage);


        mImageHeight = mBackDropImageView.getLayoutParams().height;




        trackingScrollView.setOnScrollChangedListener(
                new TrackingScrollView.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged(TrackingScrollView source, int l, int t, int oldl, int oldt) {
                        handleScroll(source, t);
                        setAlphaForActionBar();
                    }
                }
        );

        Drawable.Callback mDrawableCallback = new Drawable.Callback() {
            @Override
            public void invalidateDrawable(Drawable who) {
                getActivity().getActionBar().setBackgroundDrawable(who);
            }

            @Override
            public void scheduleDrawable(Drawable who, Runnable what, long when) {
            }

            @Override
            public void unscheduleDrawable(Drawable who, Runnable what) {
            }
        };

        mFavouriteButton.setOnClickListener(DetailsActivityFragment.this);
        Log.d("Height of gridview", mCastView.getLayoutParams().height + "");
        Log.d("Width of gridview", mCastView.getLayoutParams().width + "");

        mCastView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setUpOverView(position);
            }
        });


        return rootView;
    }

    private void setAlphaForActionBar() {
        final int scrollY = trackingScrollView.getScrollY();
        Log.d("Scroll Y:", scrollY + "");
        final int boundY = 1077;
        Log.d("Image Height: ", mImageHeight + "");
        final float ratio = (float) (scrollY / boundY);
        Log.d("Ratio", ratio + "");
        transparentDrawable.setAlpha((int) (ratio * 255));
        Log.d("alpha", (ratio * 255) + "");
    }

    private void setUpTransparentActionBar() {
        android.app.ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setBackgroundDrawable(transparentDrawable);
        }
    }

    private void setUpImageButtons() {
        mFavouriteButton.setBackgroundColor(Color.TRANSPARENT);
        mShareButton.setBackgroundColor(Color.TRANSPARENT);
        mAddtoListButton.setBackgroundColor(Color.TRANSPARENT);
        imageButtonLoaderPicasso(R.mipmap.ic_share, 180, 180, mShareButton);
        imageButtonLoaderPicasso(R.mipmap.ic_add, 180, 180, mAddtoListButton);
        toggleFavourite();
    }

    private void toggleFavourite() {
        mFavouriteButton.setBackgroundColor(Color.TRANSPARENT);
        editor = favourites.edit();
        setFavourites();
        Log.d("isFavourite", isFavourite + "");
        if (isFavourite) {
            imageButtonLoaderPicasso(R.mipmap.added_favourite, 180, 180, mFavouriteButton);

            if(content == null) {
                editor.remove(movie.getMovieID());
            } else {
                editor.remove(tvShows.getId());
            }
            isFavourite = false;
        } else {
            imageButtonLoaderPicasso(R.mipmap.ic_heart, 180, 180, mFavouriteButton);
            if(content == null) {
                editor.putString(movie.getMovieID(), movie.getOriginal_title());

            } else {
                editor.putString(tvShows.getId(), tvShows.getOriginal_name());
            }
            isFavourite = true;
        }
        editor.commit();
        showFavourites();
    }


    private void setFavourites() {
        if (isPresentInFavouriteSharedPreference()) {
            isFavourite = true;
        }
        isFavourite = false;

    }

    private Boolean isPresentInFavouriteSharedPreference() {
        Map<String, ?> keys = favourites.getAll();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            Log.d("Favourite values", entry.getKey() + ": " +
                    entry.getValue().toString());
            if(content == null) {
                if (entry.getKey().equals(movie.getMovieID())) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (entry.getKey().equals(tvShows.getId())) {
                    return true;
                } else {
                    return false;
                }
            }

        }
        return false;
    }

    private void showFavourites() {
        Map<String, ?> keys = favourites.getAll();

        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            Log.d("map values", entry.getKey() + ": " +
                    entry.getValue().toString());
        }
    }

    private void setUpGenreList(SharedPreferences genreStorage) {
        if(content == null ) {
            if (movie != null) {
                genreIDList = movie.getGenre();
            }

            if (genreIDList != null) {
                Map<String, ?> keys = genreStorage.getAll();

                genreList = new ArrayList<>();

                for (int i = 0; i < genreIDList.size(); i++) {
                    for (Map.Entry<String, ?> entry : keys.entrySet()) {
                        Log.d("map values", entry.getKey() + ": " +
                                entry.getValue().toString());
                        if (!entry.getKey().equals("firstrun")) {
                            if (Integer.valueOf(genreIDList.get(i).toString()) == Integer.valueOf(entry.getKey())) {
                                genreList.add(entry.getValue().toString());
                            } else {
                                Log.d(entry.getKey(), "couldn't find the id " + genreIDList.get(i));
                            }
                        }
                    }
                }
            }


            if (genreList != null) {
                arrayAdapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.list_item, R.id.list_item_textview, genreList);
                for (String j : genreList) {
                    Log.d("In genrelist: ", j);
                }
                mList.setAdapter(arrayAdapter);
                Log.d("Genre Set up", "complete ");
                fetchVideosTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, movie.getMovieID());
            }
        } else {
            if (tvShows != null) {
                genreIDList = tvShows.getGenre();
            }

            if (genreIDList != null) {
                Map<String, ?> keys = genreStorage.getAll();

                genreList = new ArrayList<>();

                for (int i = 0; i < genreIDList.size(); i++) {
                    for (Map.Entry<String, ?> entry : keys.entrySet()) {
                        Log.d("map values", entry.getKey() + ": " +
                                entry.getValue().toString());
                        if (!entry.getKey().equals("firstrun")) {
                            if (Integer.valueOf(genreIDList.get(i).toString()) == Integer.valueOf(entry.getKey())) {
                                genreList.add(entry.getValue().toString());
                            } else {
                                Log.d(entry.getKey(), "couldn't find the id " + genreIDList.get(i));
                            }
                        }
                    }
                }
            }


            if (genreList != null) {
                arrayAdapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.list_item, R.id.list_item_textview, genreList);
                for (String j : genreList) {
                    Log.d("In genrelist: ", j);
                }
                mList.setAdapter(arrayAdapter);
                Log.d("Genre Set up", "complete ");
                fetchVideosTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, tvShows.getId());
            }
        }

    }

    private void imageViewLoaderPicasso(String resID, int widthPx, int heightPx, ImageView view) {
        Picasso.with(getActivity())
                .load(resID)
                .resize(widthPx, heightPx)
                .into(view);
    }

    private void imageButtonLoaderPicasso(int resID, int widthPx, int heightPx, ImageButton view) {
        Picasso.with(getActivity())
                .load(resID)
                .resize(widthPx, heightPx)
                .into(view);
    }


    private void handleScroll(TrackingScrollView source, int top) {
        int scrolledImageHeight = Math.min(mImageHeight, Math.max(0, top));

        ViewGroup.MarginLayoutParams imageParams = (ViewGroup.MarginLayoutParams) mBackDropImageView.getLayoutParams();
        int newImageHeight = mImageHeight - scrolledImageHeight;
        if (imageParams.height != newImageHeight) {
            imageParams.height = newImageHeight;
            imageParams.topMargin = scrolledImageHeight;
            imageParams.width = screenWidth;
            mBackDropImageView.setLayoutParams(imageParams);
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



    public class FetchVideosTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String videosStr = null;
            String movieID = params[0];
            String discover = null;
            if(content == null) {
                discover = "movie";
            } else {
                discover = "tv";
            }

            //http://api.themoviedb.org/3/movie/87101/videos?api_key=c74eefc5fded173206b2b3abb1bc76a2
            Uri.Builder builder1 = new Uri.Builder();
            builder1.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath(discover)
                    .appendPath(movieID)
                    .appendPath("videos")
                    .appendQueryParameter("api_key", "c74eefc5fded173206b2b3abb1bc76a2");
            String builtUrl = builder1.build().toString();

            try {
                URL url = new URL(builtUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();


                InputStream inputStream = connection.getInputStream();
                StringBuilder builder = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }

                videosStr = builder.toString();
            } catch (IOException e) {
                Log.e("FetchVideosTask", "Error ", e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("FetchVideosTask", "Error closing stream", e);
                    }
                }
            }
            return videosStr;
        }


        @Override
        protected void onPostExecute(String result) {

            JSONObject myjson;
            mVideoList = new ArrayList<>();
            try {
                myjson = new JSONObject(result);
                Log.d("", myjson.toString());
                JSONArray page1 = myjson.getJSONArray("results");

                for (int i = 0; i < page1.length(); i++) {

                    JSONObject movieObject = page1.getJSONObject(i);
                    YoutubeVideo youtubeVideo = new YoutubeVideo();
                    if (movieObject.getString("site").equals("YouTube")) {
                        youtubeVideo.setId(movieObject.getString("id"));
                        youtubeVideo.setKey(movieObject.getString("key"));
                        youtubeVideo.setName(movieObject.getString("name"));
                        youtubeVideo.setSite(movieObject.getString("site"));
                        youtubeVideo.setSite(movieObject.getString("size"));
                        youtubeVideo.setType(movieObject.getString("type"));
                        mVideoList.add(youtubeVideo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(mVideoList.size() != 0 ) {
                for( int i = 0; i < 1; i++ ) {
                    YouTubePlayerView youTubePlayerView = new YouTubePlayerView(getActivity());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    youTubePlayerView.setId((i + 1));
                    Log.d("Youtube " + i, "ID - " + youTubePlayerView.getId());

                    final int finalI = i;
                    params.bottomMargin = 20;
                    youTubePlayerView.setLayoutParams(params);
                    mYoutubeParentLayout.addView(youTubePlayerView);

                    Log.d("youtubePlayerView", youTubePlayerView.getLayoutParams().width + " " + youTubePlayerView.getLayoutParams().height);
                    Log.d("youtubeParentLayout",mYoutubeParentLayout.getLayoutParams().width + " " +mYoutubeParentLayout.getLayoutParams().height);


                    youTubePlayerView.initialize(youtubeConfig.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                            if (!b) {
                                youTubePlayer.cueVideo(mVideoList.get(finalI).getKey());
                                Log.d(finalI + "Youtube init", "Initialised");
                            }
                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                            Log.d( "Error " + youTubeInitializationResult.toString(), "");

                        }
                    });
                }
            } else {
                youTubeParentParent.setVisibility(View.GONE);
            }

            Log.d("FetchVideo Task: ", "complete");
            if(content == null ) {
                fetchCastTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, movie.getMovieID());
            } else {
                fetchCastTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, tvShows.getId());
            }

        }
    }

    public class FetchCastTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            //http://api.themoviedb.org/3/tv/1418/credits?api_key=c74eefc5fded173206b2b3abb1bc76a2
            //http://api.themoviedb.org/3/movie/1418/casts?api_key=c74eefc5fded173206b2b3abb1bc76a2

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String videosStr = null;
            String movieID = params[0];
            String builtUrl = "";
            if(content == null ) {
                Uri.Builder builder1 = new Uri.Builder();
                builder1.scheme("http")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("movie")
                        .appendPath(movieID)
                        .appendPath("credits")
                        .appendQueryParameter("api_key", "c74eefc5fded173206b2b3abb1bc76a2");
                builtUrl = builder1.build().toString();
                Log.d("Url for cast:",builtUrl);
            } else {
                Uri.Builder builder1 = new Uri.Builder();
                builder1.scheme("http")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("tv")
                        .appendPath(movieID)
                        .appendPath("credits")
                        .appendQueryParameter("api_key", "c74eefc5fded173206b2b3abb1bc76a2");
                builtUrl = builder1.build().toString();
                Log.d("Url for cast:",builtUrl);
            }


            try {
                URL url = new URL(builtUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();


                InputStream inputStream = connection.getInputStream();
                StringBuilder builder = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }

                videosStr = builder.toString();
            } catch (IOException e) {
                Log.e("FetchCastTask", "Error ", e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("FetchCastTask", "Error closing stream", e);
                    }
                }
            }
            return videosStr;
        }


        @Override
        protected void onPostExecute(String result) {

            JSONObject myjson;
            mCastList = new ArrayList<>();
            try {
                if(result != null) {
                    myjson = new JSONObject(result);
                    JSONArray page1 = myjson.getJSONArray("cast");

                    if(page1.length() >= 9) {
                        for (int i = 0; i < 9; i++) {

                            JSONObject movieObject = page1.getJSONObject(i);
                            Cast cast = new Cast();
                            cast.setId(movieObject.get("id").toString());
                            Log.d("Cast Id",cast.getId());
                            cast.setCharacter(movieObject.getString("character"));
                            cast.setName(movieObject.getString("name"));
                            cast.setCredit_id(movieObject.get("credit_id").toString());
                            cast.setProfile_path(movieObject.getString("profile_path"));

                            mCastList.add(cast);
                        }
                    } else {
                        for (int i = 0; i < page1.length(); i++) {

                            JSONObject movieObject = page1.getJSONObject(i);
                            Cast cast = new Cast();
                            cast.setId(movieObject.get("id").toString());
                            Log.d("Cast Id",cast.getId());
                            cast.setCharacter(movieObject.getString("character"));
                            cast.setName(movieObject.getString("name"));
                            cast.setCredit_id(movieObject.get("credit_id").toString());
                            cast.setProfile_path(movieObject.getString("profile_path"));

                            mCastList.add(cast);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            gridAdapter = new GridAdapter(getActivity(), mCastList, screenWidth);
            mCastView.setExpanded(true);
            mCastView.setAdapter(gridAdapter);
            gridAdapter.notifyDataSetChanged();

        }
    }


    public class FetchActorDetailsTask extends AsyncTask<String, Void, Actor> {

        @Override
        protected Actor doInBackground(String... params) {

            //http://api.themoviedb.org/3/person/2524?api_key=c74eefc5fded173206b2b3abb1bc76a2
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String movieStr = null;
            String actorId = params[0];
            Uri.Builder builder1 = new Uri.Builder();
            builder1.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("person")
                    .appendPath(actorId)
                    .appendQueryParameter("api_key", "c74eefc5fded173206b2b3abb1bc76a2");
            String builtUrl = builder1.build().toString();
            Log.d(builtUrl, "");


            try {
                URL url = new URL(builtUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();


                InputStream inputStream = connection.getInputStream();
                StringBuilder builder = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(inputStream));

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
        protected void onPostExecute(Actor actor) {

            Dialog profileDialog = new Dialog(getActivity());
            profileDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            profileDialog.setContentView(getActivity().getLayoutInflater().inflate(R.layout.actor_overview
                    , null));
            profileDialog.show();

            ImageView imageView = (ImageView)profileDialog.findViewById(R.id.actors_overview_imageview);
            TextView mName = (TextView)profileDialog.findViewById(R.id.actor_nsame_);
            TextView mBirography = (TextView)profileDialog.findViewById(R.id.biorapgy);
            TextView dob = (TextView)profileDialog.findViewById(R.id.birthday_textview);
            TextView place = (TextView)profileDialog.findViewById(R.id.place_textview);
            TextView deathDay = (TextView)profileDialog.findViewById(R.id.deathday);
            TextView death = (TextView)profileDialog.findViewById(R.id.death_textview);

            mName.setText(actor.getName());
            mBirography.setText(actor.getBio());
            dob.setText(actor.getBirthday());
            place.setText(actor.getPlaceOfBirth());

            if(!actor.getDeath().equals("")) {
                death.setText(actor.getDeath());
                death.setVisibility(View.VISIBLE);
                deathDay.setVisibility(View.VISIBLE);
            }

            Picasso.with(getActivity())
                    .load(actor.getProfilePath())
                    .resize(300,450)
                    .into(imageView);


//            dialog.setTitle(actor.getName());
        }

        private Actor MoviesParser(String result) {
            JSONObject myjson;
            Actor actor = new Actor();

            movieList = new ArrayList<>();
            try {
                myjson = new JSONObject(result);
                Log.d("Actor overview:", myjson.toString());

                actor.setId(myjson.getString("id"));
                Log.d("Actor overview:", myjson.getString("id"));
                actor.setBio(myjson.getString("biography"));
                Log.d("Actor overview:", myjson.getString("biography"));

                actor.setBirthday(myjson.getString("birthday"));
                Log.d("Actor overview:", myjson.getString("birthday"));

                actor.setDeath(myjson.getString("deathday"));
                Log.d("Actor overview:", myjson.getString("deathday"));


                actor.setHomepage(myjson.getString("homepage"));
                Log.d("Actor overview:", myjson.getString("homepage"));

                actor.setImdbID(myjson.getString("imdb_id"));
                Log.d("Actor overview:", myjson.getString("imdb_id"));

                actor.setName(myjson.getString("name"));
                Log.d("Actor overview:", myjson.getString("name"));

                actor.setPlaceOfBirth(myjson.getString("place_of_birth"));
                Log.d("Actor overview:", myjson.getString("place_of_birth"));

                actor.setProfilePath("http://image.tmdb.org/t/p/w185//" + myjson.getString("profile_path"));
                Log.d("Actor overview:", myjson.getString("profile_path"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Log.d("Actor overview:",actor.getName());
            return actor;

        }
    }

    @Override
    public void onClick(View v) {

        if(v == mFavouriteButton) {
            toggleFavourite();
            Toast.makeText(getActivity(),"CLicke",Toast.LENGTH_SHORT).show();

            getActivity().deleteDatabase("moviesOffline");





            /**
             * CRUD Operations
             **/
            // Inserting Contacts
            if(content == null ) {
                Log.d("Insert: ", "Inserting ..");
                db.addContact(movie);

            }

            // Reading all contacts
            Log.d("Reading: ", "Reading all contacts..");
            List<Movie> contacts = db.getAllContacts();

            for (Movie cn : contacts) {
                String log = "Id: " + cn.getMovieID() + " ,Name: " + cn.getOriginal_title() + " ,Phone: " + cn.getOverView();
                // Writing Contacts to log
                Log.d("Name: ", log);
            }

        }
    }

    private void showLists() {
        for(Cast cast : mCastList) {
            Log.d("", cast.getName());
            Log.d("" ,cast.getId());


        }

        for(YoutubeVideo cast : mVideoList) {
            Log.d("" ,cast.getName());
            Log.d("" ,cast.getKey());
        }
    }

    private void setUpOverView(int position) {
        FetchActorDetailsTask detailsTask = new FetchActorDetailsTask();
        detailsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,mCastList.get(position).getId());
    }

    private class youtubeConfig {
        public static final String DEVELOPER_KEY = "AIzaSyCaKtZ7jQaMQi06WnyOqF_K6pZl92qy9Rs";
    }




}
