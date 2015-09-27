package in.reduxpress.themoviedb;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import in.reduxpress.themoviedb.DataModels.Movie;
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
    private  ListView mList;
    private TextView mRating;
    private Boolean isFavourite;
    private SharedPreferences genreStorage;
    private SharedPreferences favourites;
    private SharedPreferences.Editor editor;
    final String FAVOURITE_PREFERENCE = "favourite";
    final String GENRE_PREFERENCE = "Genre";
    private int count = 1;
    private TextView mReleaseDate;



    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout rootView = new LinearLayout(getActivity());
        inflater.inflate(R.layout.fragment_details, rootView, true);


        genreStorage = getActivity().getApplicationContext().getSharedPreferences(GENRE_PREFERENCE, Context.MODE_PRIVATE);
        favourites = getActivity().getApplicationContext().getSharedPreferences(FAVOURITE_PREFERENCE,Context.MODE_PRIVATE);


        mBackDropImageView = (ImageView)rootView.findViewById(R.id.details_imageview_backdrop);
        mPoster = (ImageView)rootView.findViewById(R.id.details_poster_imageview);
        mTitle = (TextView)rootView.findViewById(R.id.details_movie_title);
        mDescription = (TextView)rootView.findViewById(R.id.details_description);
        mFavouriteButton = (ImageButton)rootView.findViewById(R.id.add_favourite_imagebutton);
        mBackDrop = rootView.findViewById(R.id.backdrop_parent_rl);
        trackingScrollView = (TrackingScrollView)rootView.findViewById(R.id.scroller);
        mList = (ListView)rootView.findViewById(R.id.genre_listview);
        mRating = (TextView)rootView.findViewById(R.id.review_texview_editing);
        mAddtoListButton = (ImageButton)rootView.findViewById(R.id.add_to_list);
        mShareButton = (ImageButton)rootView.findViewById(R.id.share_imageButton);
        mReleaseDate = (TextView)rootView.findViewById(R.id.release_date_textview);
        transparentDrawable= new ColorDrawable(Color.BLACK);
        transparentDrawable.setAlpha(0);

        screenWidth = getScreenDimen();



        android.app.ActionBar actionBar = getActivity().getActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setBackgroundDrawable(transparentDrawable);
        }

        Bundle b =  getArguments();
        movie = b.getParcelable("MovieDetails");

        setUpImageButtons();



        setUpGenreList(genreStorage);

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


        mImageHeight = mBackDropImageView.getLayoutParams().height;


        Log.d("", movie.getBackdrop_path());
        Log.d("Image Height: ", mImageHeight + "");


        trackingScrollView.setOnScrollChangedListener(
                new TrackingScrollView.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged(TrackingScrollView source, int l, int t, int oldl, int oldt) {
                        handleScroll(source, t);
                    }
                }
        );

        trackingScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                final int scrollY = trackingScrollView.getScrollY();
                Log.d("Scroll Y:", scrollY + "");
                final int boundY = 1077;
                Log.d("Image Height: ", mImageHeight + "");
                final float ratio = (float) (scrollY / boundY);
                Log.d("Ratio", ratio + "");
                transparentDrawable.setAlpha((int) (ratio * 255));
                Log.d("alpha", (ratio * 255) + "");

            }
        });

        mFavouriteButton.setOnClickListener(DetailsActivityFragment.this);

        return rootView;
    }

    private void setUpImageButtons() {
        mFavouriteButton.setBackgroundColor(Color.TRANSPARENT);
        mShareButton.setBackgroundColor(Color.TRANSPARENT);
        mAddtoListButton.setBackgroundColor(Color.TRANSPARENT);
        imageButtonLoaderPicasso(R.drawable.ic_share, 180, 180, mShareButton);
        imageButtonLoaderPicasso(R.drawable.ic_add, 180, 180, mAddtoListButton);
        toggleFavourite();
    }

    private void toggleFavourite() {
        mFavouriteButton.setBackgroundColor(Color.TRANSPARENT);
        editor = favourites.edit();
        setFavourites();
        if(isFavourite) {
            imageButtonLoaderPicasso(R.drawable.added_favourite, 180, 180, mFavouriteButton);
            editor.remove(movie.getMovieID());
        } else {
            imageButtonLoaderPicasso(R.drawable.ic_heart, 180, 180, mFavouriteButton);
            editor.putString(movie.getMovieID(),movie.getOriginal_title());
        }
        editor.commit();
        showFavourites();
    }



    private void setFavourites() {
        if(isPresentInFavouriteSharedPreference()) {
            isFavourite = true;
        } else {
            isFavourite = false;
        }
    }

    private Boolean isPresentInFavouriteSharedPreference() {
        Map<String,?> keys = favourites.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()) {
            Log.d("Favourite values", entry.getKey() + ": " +
                    entry.getValue().toString());
            if(entry.getKey().equals(movie.getMovieID())) {

                return true;
            } else  {
                return false;
            }
        }
        return false;
    }

    private void showFavourites() {
        Map<String,?> keys = favourites.getAll();

            for(Map.Entry<String,?> entry : keys.entrySet()) {
                Log.d("map values", entry.getKey() + ": " +
                        entry.getValue().toString());
            }
    }

    private void setUpGenreList(SharedPreferences genreStorage) {
        if (movie != null) {
            genreIDList = movie.getGenre();
        }

        if(genreIDList != null) {
            Map<String,?> keys = genreStorage.getAll();

            genreList = new ArrayList<>();

            for(int i = 0; i < genreIDList.size(); i++) {
                for(Map.Entry<String,?> entry : keys.entrySet()){
                    Log.d("map values", entry.getKey() + ": " +
                            entry.getValue().toString());
                    if(!entry.getKey().equals("firstrun")) {
                        if(Integer.valueOf(genreIDList.get(i).toString()) == Integer.valueOf(entry.getKey())) {
                            genreList.add(entry.getValue().toString());
                        } else {
                            Log.d(entry.getKey(),"couldn't find the id "+ genreIDList.get(i));
                        }
                    }

                }
            }
        }


        if(genreList != null ) {
             arrayAdapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.list_item, R.id.list_item_textview, genreList);
            for(String j: genreList) {
                Log.d("In genrelist: ",j);
            }
            mList.setAdapter(arrayAdapter);

        }


    }

    private void imageViewLoaderPicasso(String resID,int widthPx, int heightPx,ImageView view) {
        Picasso.with(getActivity())
                .load(resID)
                .resize(widthPx,heightPx)
                .into(view);
    }

    private void imageButtonLoaderPicasso(int resID,int widthPx, int heightPx,ImageButton view) {
        Picasso.with(getActivity())
                .load(resID)
                .resize(widthPx,heightPx)
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


    @Override
    public void onClick(View v) {

        if(v == mFavouriteButton) {
            toggleFavourite();
            Toast.makeText(getActivity(),"CLicke",Toast.LENGTH_SHORT).show();
        }

    }

    private class youtubeConfig {
        public static final String DEVELOPER_KEY = "AIzaSyCaKtZ7jQaMQi06WnyOqF_K6pZl92qy9Rs";
    }
}
