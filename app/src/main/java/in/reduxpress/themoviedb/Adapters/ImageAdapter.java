package in.reduxpress.themoviedb.Adapters;

/**
 * Created by kumardivyarajat on 10/06/15.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import in.reduxpress.themoviedb.DataModels.Movie;
import in.reduxpress.themoviedb.DataModels.TvShows;
import in.reduxpress.themoviedb.R;

public class ImageAdapter extends BaseAdapter {

    private final static String TAG = ImageAdapter.class.getSimpleName();

    private Context activity;
    private LayoutInflater inflater = null;
    private List<Movie> movieList;
    private List<TvShows> tvShowsList;

    private int screenWidth;
    private int screenDPI;
    private int width;
    private int height;

    public ImageAdapter(Context activity,List<Movie> movieList, int screenWidth, int screenDPI) {
        this.screenWidth = screenWidth;
        this.activity = activity;
        this.screenDPI = screenDPI;
        this.movieList = movieList;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
    }

    public ImageAdapter(Context activity,List<TvShows> movieList, int screenWidth) {
        this.screenWidth = screenWidth;
        this.activity = activity;
        this.tvShowsList = movieList;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
    }

    @Override
    public int getCount() {
        if(movieList == null) {
            return tvShowsList.size();
        } else {
            return movieList.size();
        }    }


    @Override
    public Object getItem(int position) {
        if(movieList == null) {
            return tvShowsList.get(position);
        } else {
            return movieList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    public  class ViewHolder {
        ImageView imageView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder = new ViewHolder();
        if (view == null) {
            view = inflater.inflate(R.layout.horizontal_listview_item, parent,false);
            holder.imageView = (ImageView)view.findViewById(R.id.main_activity_image_poster);

        } else {

            // When you get a recycled item, check if it's not already the one you want to display.
            holder = (ViewHolder)view.getTag();
        }

        int width = screenWidth/2;

        int height = computeImageHeight(width);

        if(movieList == null) {
            Picasso picasso = Picasso.with(activity);
            picasso.load(tvShowsList.get(position).getPoster_path())
                    .resize((154*3), (231*3))
                    .noFade()
                    .into(holder.imageView);
            picasso.setIndicatorsEnabled(true);
        } else {
            Picasso picasso = Picasso.with(activity);
            picasso.load(movieList.get(position).getPoster_path())
                    .resize((154*3), (231*3))
                    .noFade()
                    .into(holder.imageView);
            picasso.setIndicatorsEnabled(true);
        }




        view.setTag(holder);

        return view;
    }

    public int computeImageHeight(int screenWidth) {
        return ((int) (screenWidth * 1.5));
    }


}