package in.reduxpress.themoviedb;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.youtube.player.YouTubeBaseActivity;

import in.reduxpress.themoviedb.DataModels.Movie;
import in.reduxpress.themoviedb.DataModels.TvShows;

public class DetailsActivity extends YouTubeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        String content = intent.getStringExtra("Content");

        Bundle b = new Bundle();
        if(content == null) {
            Log.d("Details", "Received Arraylist");
            Movie movie = intent.getParcelableExtra("MovieDetails");
            b.putParcelable("MovieDetails", movie);
        } else {
            Log.d("Tv show recived", "");
            TvShows tvShows = intent.getParcelableExtra("MovieDetails");
            b.putParcelable("MovieDetails",tvShows);
            b.putString("Content",content);
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
