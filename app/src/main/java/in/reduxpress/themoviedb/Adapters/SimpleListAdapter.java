package in.reduxpress.themoviedb.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import in.reduxpress.themoviedb.DataModels.Review;
import in.reduxpress.themoviedb.R;

/**
 * Created by kumardivyarajat on 30/09/15.
 */
public class SimpleListAdapter extends BaseAdapter {

    private Context activity;
    private LayoutInflater inflater = null;
    private List<Review> review;

    public SimpleListAdapter(Context activity,  List<Review> review) {
        this.activity = activity;
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.review = review;
    }


    @Override
    public int getCount() {
        return review.size()/2;
    }

    @Override
    public Object getItem(int position) {
        return review.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder = new ViewHolder();
        if (view == null) {
            view = inflater.inflate(R.layout.reviews_list_item, parent,false);
            holder.mAuthor = (TextView)view.findViewById(R.id.author_textview);
            holder.mContent = (TextView)view.findViewById(R.id.review_textview);

        } else {

            holder = (ViewHolder)view.getTag();
        }

        holder.mAuthor.setText(review.get(position).getAuthor());
        holder.mContent.setText(review.get(position).getContent());


        view.setTag(holder);

        return view;
    }

    public  class ViewHolder {

        TextView mAuthor;
        TextView mContent;
    }
}
