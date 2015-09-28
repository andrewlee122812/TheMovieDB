package in.reduxpress.themoviedb;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

/**
 * A placeholder fragment containing a simple view.
 */
public class TestActivityFragment extends Fragment {

    public TestActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_test, container, false);

        YouTubePlayerView youTubePlayerView = (YouTubePlayerView)rootView.findViewById(R.id.test_youtube);

        youTubePlayerView.initialize(youtubeConfig.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if(!b) {
                    youTubePlayer.cueVideo("FRDdRto_3SA");
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
        return rootView;
    }

    private class youtubeConfig {
        public static final String DEVELOPER_KEY = "AIzaSyCaKtZ7jQaMQi06WnyOqF_K6pZl92qy9Rs";
    }

}
