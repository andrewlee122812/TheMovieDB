package in.reduxpress.themoviedb.HelperClasses;

/**
 * Created by kumardivyarajat on 27/09/15.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * ScrollViewの中のGridViewでも高さを可変にする<br>
 * http://stackoverflow.com/questions/8481844/gridview-height-gets-cut
 */
public class ExpandedScrollView extends GridView
{

    boolean expanded = false;

    public ExpandedScrollView(Context context)
    {
        super(context);
    }

    public ExpandedScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ExpandedScrollView(Context context, AttributeSet attrs,
                              int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public boolean isExpanded()
    {
        return expanded;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        if (isExpanded())
        {
            // Calculate entire height by providing a very large height hint.
            // View.MEASURED_SIZE_MASK represents the largest height possible.
            int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK,
                    MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        }
        else
        {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setExpanded(boolean expanded)
    {
        this.expanded = expanded;
    }
}