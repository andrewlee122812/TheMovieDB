package in.reduxpress.themoviedb.Adapters;

/**
 * Created by kumardivyarajat on 10/06/15.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.List;

import in.reduxpress.themoviedb.DataModels.Movie;
import in.reduxpress.themoviedb.R;

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
        View v = view;
        ViewHolder holder;


        if (v == null) {
            holder = new ViewHolder();
            v = inflater.inflate(R.layout.movie_poster_image,null);
            holder.imageView = (ImageView)v.findViewById(R.id.movie_poster_imageview);
            v.setTag(holder);
        } else {

            holder = (ViewHolder) v.getTag();
        }

        int width = screenWidth/2;

        int height = computeImageHeight(width);

        Picasso.with(activity)
                .load(movieList.get(position).getPoster_path())
                .resize(width, height)
                .noFade()
                .into(holder.imageView);


        return v;
    }

    public int computeImageHeight(int screenWidth) {
        return ((int) (screenWidth * 1.66));
    }


}