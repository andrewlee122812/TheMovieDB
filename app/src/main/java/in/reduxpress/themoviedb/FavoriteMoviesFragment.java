package in.reduxpress.themoviedb;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import in.reduxpress.themoviedb.Adapters.GridAdapter;
import in.reduxpress.themoviedb.DataModels.Movie;
import in.reduxpress.themoviedb.DataModels.TvShows;
import in.reduxpress.themoviedb.HelperClasses.MoviesDBHandler;
import in.reduxpress.themoviedb.HelperClasses.TVShowsDBHandler;

/**
 * Created by kumardivyarajat on 28/09/15.
 */
public class FavoriteMoviesFragment extends Fragment implements AdapterView.OnItemClickListener {

    MoviesDBHandler dbHandler;
    TVShowsDBHandler tvShowsDBHandler;
    List<Movie> movieList;
    List<TvShows> tvShowsList;
    TextView mDummy;
    GridView mFavoritesGridView;
    GridAdapter mAdapter;

    public FavoriteMoviesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favourites, container, false);

        dbHandler = new MoviesDBHandler(getActivity());
        tvShowsDBHandler = new TVShowsDBHandler(getActivity());

        mFavoritesGridView = (GridView)rootView.findViewById(R.id.favourites_gridview);

        movieList = dbHandler.getAllContacts();

            for(Movie movie : movieList) {

                if(movie.getMovieBackdrop() !=  null) {
                    Log.d("Movie backdrop", movie.getMovieBackdrop().toString());
                } else {
                    Log.d("Movie backdrop", "null");
                }

                if(movie.getMoviePoster() !=  null) {
                    Log.d("Movie poster", movie.getMoviePoster().toString());
                } else {
                    Log.d("Movie backdrop", "null");
                }
            }


        mAdapter = new GridAdapter(getActivity(),movieList,getScreenDimen());

        mFavoritesGridView.setAdapter(mAdapter);
        mFavoritesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), movieList.get(position).getOriginal_title() + "", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity().getApplicationContext(),DetailsActivity.class);
                intent.putExtra("MovieDetails",movieList.get(position));
                intent.putExtra("FromDatabase", true);
                startActivity(intent);
            }
        });





        return rootView;
    }

    public int getScreenDimen() {

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
