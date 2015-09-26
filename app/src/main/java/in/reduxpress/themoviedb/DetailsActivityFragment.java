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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import in.reduxpress.themoviedb.DataModels.Movie;
import in.reduxpress.themoviedb.HelperClasses.TrackingScrollView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    private Movie movie;
    private ImageView mBackDropImageView;
    private View mBackDrop;
    private ImageView mPoster;
    private TextView mTitle;
    private TextView mDescription;
    private Button mButton;
    private int mImageHeight;
    private int screenWidth;
    ColorDrawable transparentDrawable;
    private TrackingScrollView trackingScrollView;
    private ListView mListview;
    private ArrayAdapter<String> arrayAdapter;
    ArrayList genreIDList;
    ArrayList<String> genreList;
    private  ListView mList;
    private TextView mRating;


    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout rootView = new LinearLayout(getActivity());
        inflater.inflate(R.layout.fragment_details, rootView, true);

        /*LinearLayout wrapper = new LinearLayout(getActivity()); // for example
        inflater.inflate(R.layout.fragment_test, wrapper, true);*/

        SharedPreferences genreStorage = getActivity().getApplicationContext().getSharedPreferences("Genre", Context.MODE_PRIVATE);




        mBackDropImageView = (ImageView)rootView.findViewById(R.id.details_imageview_backdrop);
        mPoster = (ImageView)rootView.findViewById(R.id.details_poster_imageview);
        mTitle = (TextView)rootView.findViewById(R.id.details_movie_title);
        mDescription = (TextView)rootView.findViewById(R.id.details_description);
        //mButton = (Button)rootView.findViewById(R.id.test_button);
        mBackDrop = rootView.findViewById(R.id.backdrop_parent_rl);
        trackingScrollView = (TrackingScrollView)rootView.findViewById(R.id.scroller);
        mList = (ListView)rootView.findViewById(R.id.genre_listview);
        mRating = (TextView)rootView.findViewById(R.id.review_texview_editing);
        transparentDrawable= new ColorDrawable(Color.BLACK);
        transparentDrawable.setAlpha(0);



        android.app.ActionBar actionBar = getActivity().getActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setBackgroundDrawable(transparentDrawable);
        }

        Bundle b =  getArguments();
        if(b != null) {
            Log.d("",b.size() + "");
            movie = b.getParcelable("MovieDetails");
        } else {
            Log.d("","Null");
        }

        if (movie != null) {
            genreIDList = movie.getGenre();
        }

        if(genreIDList != null) {
            Map<String,?> keys = genreStorage.getAll();

            genreList = new ArrayList<>();

            for(int i = 0; i < genreIDList.size(); i++) {
                for(Map.Entry<String,?> entry : keys.entrySet()){
                    Log.d("map values",entry.getKey() + ": " +
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
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.list_item, R.id.list_item_textview, genreList);
            for(String j: genreList) {
                Log.d("In genrelist: ",j);
            }
            mList.setAdapter(adapter);

        }




        mTitle.setText(movie.getOriginal_title());
        mDescription.setText(movie.getOverView());
        mRating.setText(movie.getVoteAverage());

        String url = movie.getPoster_path();
        url = url.replace("w500", "w185");

        screenWidth = getScreenDimen();


        Picasso.with(getActivity())
                .load(movie.getBackdrop_path())
                .resize(screenWidth, (int) (screenWidth * 0.56111111111111))
                .into(mBackDropImageView);

        Picasso.with(getActivity())
                .load(url)
                .resize(300,450)
                .into(mPoster);

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
                Log.d("Scroll Y:",scrollY + "");
                final int boundY = 1077;
                Log.d("Image Height: ", mImageHeight + "");
                final float ratio = (float) (scrollY/boundY) ;
                Log.d("Ratio",ratio + "");
                transparentDrawable.setAlpha((int) (ratio * 255));
                Log.d("alpha", (ratio * 255) + "");

            }
        });
        return rootView;
    }

    private void handleScroll(TrackingScrollView source, int top) {
        int scrolledImageHeight = Math.min(mImageHeight, Math.max(0, top));
       // Log.d("Scrolled Image Height",scrolledImageHeight + "");

        ViewGroup.MarginLayoutParams imageParams = (ViewGroup.MarginLayoutParams) mBackDropImageView.getLayoutParams();
        int newImageHeight = mImageHeight - scrolledImageHeight;
       // Log.d("newIMageHeight:" , newImageHeight +"");
        if (imageParams.height != newImageHeight) {
            // Transfer image height to margin top
            imageParams.height = newImageHeight;
            imageParams.topMargin = scrolledImageHeight;
            imageParams.width = screenWidth;

           // Log.d("Image width:", imageParams.width + "");
            // Invalidate view
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




}
