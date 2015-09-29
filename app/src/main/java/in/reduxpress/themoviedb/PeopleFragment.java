package in.reduxpress.themoviedb;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by kumardivyarajat on 28/09/15.
 */
public class PeopleFragment extends Fragment {

    public PeopleFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fargment_people, container, false);


        return rootView;
    }
}
