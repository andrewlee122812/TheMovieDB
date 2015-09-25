package in.reduxpress.themoviedb;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import in.reduxpress.themoviedb.DataModels.Movie;

public class DetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("MovieDetails");

        Bundle b = new Bundle();
        if(movie != null) {
            Log.d("Details", "Received Arraylist");
            Log.d("Details", movie.getOriginal_title());

            b.putParcelable("MovieDetails", movie);
        }

        if (savedInstanceState == null) {

            DetailsActivityFragment newFragment = new DetailsActivityFragment();
            newFragment.setArguments(b);
            FragmentTransaction ft = getFragmentManager().beginTransaction()
                    .add(R.id.container_details, newFragment);
            ft.commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
