package in.reduxpress.themoviedb.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import in.reduxpress.themoviedb.DataModels.Cast;
import in.reduxpress.themoviedb.DataModels.Movie;
import in.reduxpress.themoviedb.DataModels.TvShows;
import in.reduxpress.themoviedb.R;

/**
 * Created by kumardivyarajat on 27/09/15.
 */
public class GridAdapter extends BaseAdapter {
    private final static String TAG = GridAdapter.class.getSimpleName();

    private Context activity;
    private LayoutInflater inflater = null;
    private List<Cast> castList;
    private int ScreenWidth;
    private Boolean fromFavourites;
    private List<Movie> movieList;
    private List<TvShows> tvShowsList;

    public GridAdapter(Context activity,List<Cast> movieList, int screenWidth, Boolean fromFavourites) {
        this.activity = activity;
        this.fromFavourites = fromFavourites;
        this.castList = movieList;
        this.ScreenWidth = screenWidth;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
    }

    public GridAdapter(Context activity,List<Movie> movieList, int screenWidth) {
        this.activity = activity;
        this.fromFavourites = true;
        this.movieList = movieList;
        this.ScreenWidth = screenWidth;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
    }

    public GridAdapter(Context activity,List<TvShows> movieList, int screenWidth, String content) {
        this.activity = activity;
        this.fromFavourites = true;
        this.tvShowsList = movieList;
        this.ScreenWidth = screenWidth;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(fromFavourites) {
            return movieList.size();
        }
        return castList.size();
    }


    @Override
    public Object getItem(int position) {
        if(fromFavourites) {
            return movieList.get(position);
        }
        return castList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    public  class ViewHolder {
        ImageView imageView;
        TextView mActorName;
        TextView mCharacterName;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder = new ViewHolder();
        if(fromFavourites) {
            if (view == null) {
                view = inflater.inflate(R.layout.cast_grid_item, parent,false);
                holder.imageView = (ImageView)view.findViewById(R.id.cast_grid_imagview);
                holder.mActorName = (TextView)view.findViewById(R.id.actor_name);
                holder.mCharacterName = (TextView)view.findViewById(R.id.character_name);

            } else {

                // When you get a recycled item, check if it's not already the one you want to display.
                holder = (ViewHolder)view.getTag();
            }

            holder.mActorName.setText(movieList.get(position).getOriginal_title());
            holder.mCharacterName.setVisibility(View.GONE);

            if(movieList.get(position).getMoviePoster() != null) {

                holder.imageView.setImageBitmap(decodeSampledBitmapFromBitmap(
                                movieList.get(position).getMoviePoster(),
                                ScreenWidth / 2,
                                (ScreenWidth * (185 / 277)) / 2)
                );



            } else {
                Picasso picasso = Picasso.with(activity);
                picasso.load(R.drawable.no_image_placeholder)
                        .resize((ScreenWidth / 2), (int) ((ScreenWidth * (185 / 277)) / 2))
                        .into(holder.imageView);
            }



            view.setTag(holder);

        } else {
            if (view == null) {
                view = inflater.inflate(R.layout.cast_grid_item, parent,false);
                holder.imageView = (ImageView)view.findViewById(R.id.cast_grid_imagview);
                holder.mActorName = (TextView)view.findViewById(R.id.actor_name);
                holder.mCharacterName = (TextView)view.findViewById(R.id.character_name);


            } else {

                // When you get a recycled item, check if it's not already the one you want to display.
                holder = (ViewHolder)view.getTag();
            }

            holder.mActorName.setText(castList.get(position).getName());
            holder.mCharacterName.setText("( " + castList.get(position).getCharacter() + " )");

            if(!castList.get(position).getProfile_path().equals("null")) {

                StringBuilder builder = new StringBuilder("http://image.tmdb.org/t/p/w185//");
                builder.append(castList.get(position).getProfile_path());

                String url = builder.toString();


                Picasso picasso = Picasso.with(activity);
                picasso.load(url)
                        .resize((ScreenWidth / 3), (int) ((ScreenWidth * (500 / 667)) / 3))
                        .into(holder.imageView);

            } else {
                Picasso picasso = Picasso.with(activity);
                picasso.load(R.drawable.no_image_placeholder)
                        .resize((ScreenWidth / 3), (int) ((ScreenWidth * (500 / 667)) / 3))
                        .into(holder.imageView);
            }



            view.setTag(holder);

        }
        return view;
    }

    public int computeImageHeight(int screenWidth) {
        return ((int) (screenWidth * 1.5));
    }

    static Bitmap decodeSampledBitmapFromBitmap(byte[] res, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(res, 0, res.length, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(res, 0, res.length, options);
    }


    //Given the bitmap size and View size calculate a subsampling size (powers of 2)
    static int calculateInSampleSize( BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int inSampleSize = 1;	//Default subsampling size
        // See if image raw height and width is bigger than that of required view
        if (options.outHeight > reqHeight || options.outWidth > reqWidth) {
            //bigger
            final int halfHeight = options.outHeight / 2;
            final int halfWidth = options.outWidth / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

}