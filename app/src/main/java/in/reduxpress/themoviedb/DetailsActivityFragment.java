package in.reduxpress.themoviedb;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import in.reduxpress.themoviedb.DataModels.Movie;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    Movie movie;
    ImageView mBackDrop;
    ImageView mPoster;

    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        mBackDrop = (ImageView)rootView.findViewById(R.id.details_imageview_backdrop);

        Bundle b =  getArguments();
        if(b != null) {
            Log.d("",b.size() + "");
            movie = b.getParcelable("MovieDetails");
        } else {
            Log.d("","Null");
        }

        Picasso.with(getActivity())
                .load(movie.getBackdrop_path())
                .resize(1080,540)
                .into(mBackDrop);

        return rootView;
    }

}
