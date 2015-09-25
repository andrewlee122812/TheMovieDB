package in.reduxpress.themoviedb;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import in.reduxpress.themoviedb.DataModels.Movie;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    Movie movie;
    ImageView mBackDrop;
    ImageView mPoster;
    TextView mTitle;
    TextView mDescription;

    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        mBackDrop = (ImageView)rootView.findViewById(R.id.details_imageview_backdrop);
        mPoster = (ImageView)rootView.findViewById(R.id.details_poster_imageview);
        mTitle = (TextView)rootView.findViewById(R.id.details_movie_title);
        mDescription = (TextView)rootView.findViewById(R.id.details_description);

        Bundle b =  getArguments();
        if(b != null) {
            Log.d("",b.size() + "");
            movie = b.getParcelable("MovieDetails");
        } else {
            Log.d("","Null");
        }

        mTitle.setText(movie.getOriginal_title());
        mDescription.setText(movie.getOverView());

        String url = movie.getPoster_path();
        url = url.replace("w500","w185");

        Picasso.with(getActivity())
                .load(movie.getBackdrop_path())
                .resize(1115,626)
                .into(mBackDrop);

        Picasso.with(getActivity())
                .load(url)
                .resize(300,450)
                .into(mPoster);

        Log.d("",movie.getBackdrop_path());

        return rootView;
    }

}
