package in.reduxpress.themoviedb.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import in.reduxpress.themoviedb.DataModels.Cast;
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

    public GridAdapter(Context activity,List<Cast> movieList, int screenWidth) {
        this.activity = activity;
        this.castList = movieList;
        this.ScreenWidth = screenWidth;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
    }

    @Override
    public int getCount() {
        return castList.size();
    }


    @Override
    public Object getItem(int position) {
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


        StringBuilder builder = new StringBuilder("http://image.tmdb.org/t/p/w185//");
        builder.append(castList.get(position).getProfile_path());

        String url = builder.toString();

        Picasso picasso = Picasso.with(activity);
        picasso.load(url)
                .resize((ScreenWidth / 3), (int) ((ScreenWidth * (500 / 667)) / 3))
                .into(holder.imageView);
        picasso.setIndicatorsEnabled(true);


        view.setTag(holder);

        return view;
    }

    public int computeImageHeight(int screenWidth) {
        return ((int) (screenWidth * 1.5));
    }
}