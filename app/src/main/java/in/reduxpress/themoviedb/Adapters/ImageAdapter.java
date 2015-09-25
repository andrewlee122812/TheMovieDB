package in.reduxpress.themoviedb.Adapters;

/**
 * Created by kumardivyarajat on 10/06/15.
 */

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

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
        RelativeLayout relativeLayout;

    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        View v = view;
        ViewHolder holder;


        if (v == null) {
            holder = new ViewHolder();
            v = inflater.inflate(R.layout.movie_poster_image,null);
            holder.imageView = (ImageView)v.findViewById(R.id.movie_poster_imageview);
            holder.relativeLayout = (RelativeLayout)v.findViewById(R.id.movie_poster_imageview_parent_rl);
            v.setTag(holder);
        } else {

            holder = (ViewHolder) v.getTag();
        }

        if(position % 2 == 0 ) {
            holder.relativeLayout.setBackgroundColor(Color.TRANSPARENT);
        } else {
            holder.relativeLayout.setBackgroundColor(Color.YELLOW);
        }

        holder.imageView.getLayoutParams().width = screenWidth/2;
        width  = holder.imageView.getLayoutParams().width;
        width =  width * (screenDPI / 160);
        Log.d("width of imageview: ", width + "");
        holder.imageView.getLayoutParams().height = computeImageHeight(screenWidth / 2);
        Log.d("Height in dp: ", computeImageHeight( screenWidth/ 2) + "");
        height = holder.imageView.getLayoutParams().height;
        height =  height * (screenDPI / 160);
        Log.d("height of imageview: ", height + "");


        /*holder.imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);*/



        /*Picasso.with(activity)
                .load(movieList.get(position).getPoster_path())
                .resize(155, 258)
                .into(holder.imageView);*/

        Picasso mPicasso = Picasso.with(activity);
        mPicasso.setIndicatorsEnabled(true);
        mPicasso.load(movieList.get(position).getPoster_path()).resize(width,height).into(holder.imageView);

        return v;
    }

    public int computeImageHeight(int screenWidth) {
        return ((int) (screenWidth * 1.66));
    }
}