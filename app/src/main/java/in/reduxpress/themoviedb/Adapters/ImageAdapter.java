package in.reduxpress.themoviedb.Adapters;

/**
 * Created by kumardivyarajat on 10/06/15.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import in.reduxpress.themoviedb.DataModels.Movie;

public class ImageAdapter extends BaseAdapter {

    private final static String TAG = ImageAdapter.class.getSimpleName();

    private Context activity;
    private LayoutInflater inflater = null;
    private List<Movie> movieList;
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

    @Override
    public int getCount() {
        return movieList.size();
    }


    @Override
    public Object getItem(int position) {
        return movieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    public  class ViewHolder {
        ImageView imageView;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ImageView imageView;
        String src = movieList.get(position).getPoster_path();
        if (view == null) {
            imageView = new ImageView(activity);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.WRAP_CONTENT, GridView.LayoutParams.WRAP_CONTENT));

        } else {
            imageView = (ImageView) view;

            // When you get a recycled item, check if it's not already the one you want to display.
            String newSrc = (String) imageView.getTag();

            if(newSrc.equals(src)){
                // If so, return it directly.
                return imageView;
            }
        }


        int width = screenWidth/2;

        int height = computeImageHeight(width);

        Picasso picasso = Picasso.with(activity);
        picasso.load(movieList.get(position).getPoster_path())
                .resize(width, height)
                .noFade()
                .into(imageView);
        picasso.setIndicatorsEnabled(true);

        imageView.setTag(src);

        return imageView;
    }

    public int computeImageHeight(int screenWidth) {
        return ((int) (screenWidth * 1.5));
    }


}