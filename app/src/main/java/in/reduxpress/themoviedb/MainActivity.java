package in.reduxpress.themoviedb;

import android.os.Bundle;

import com.blunderer.materialdesignlibrary.activities.NavigationDrawerActivity;
import com.blunderer.materialdesignlibrary.handlers.ActionBarHandler;
import com.blunderer.materialdesignlibrary.handlers.ActionBarSearchHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerAccountsHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerAccountsMenuHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerBottomHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerStyleHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerTopHandler;
import com.blunderer.materialdesignlibrary.listeners.OnSearchDynamicListener;
import com.blunderer.materialdesignlibrary.listeners.OnSearchListener;
import com.blunderer.materialdesignlibrary.listeners.OnSearchingListener;
import com.blunderer.materialdesignlibrary.models.Account;

import java.util.List;


public class MainActivity extends com.blunderer.materialdesignlibrary.activities.NavigationDrawerActivity {

    // TODO http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=c74eefc5fded173206b2b3abb1bc76a2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {

        }

        NavigationDrawerActivity navigationDrawer;

    }




    @Override
    protected boolean enableActionBarShadow() {
        return false;
    }

    @Override
    protected ActionBarHandler getActionBarHandler() {
        return new ActionBarSearchHandler(this, new OnSearchListener() {

            @Override
            public void onSearched(String text) {
                //TODO: write your code here
            }

        }).enableAutoCompletion()
                .setAutoCompletionThreshold(3)
                .enableAutoCompletionDynamic(new OnSearchDynamicListener() {

                    @Override
                    public void onSearching(String text, OnSearchingListener onSearchingListener) {
                        List<String> suggestions = null;//TODO: write your server call or anything else to retrieve suggestions.

                        // When you retrieved the suggestions call onGettingResults(results) like this:
                        //onSearchingListener.onGettingResults(suggestions);
                    }

                });
    }

    @Override
    public NavigationDrawerStyleHandler getNavigationDrawerStyleHandler() {
        return null;
    }

    @Override
    public NavigationDrawerAccountsHandler getNavigationDrawerAccountsHandler() {
        return null;
    }

    @Override
    public NavigationDrawerAccountsMenuHandler getNavigationDrawerAccountsMenuHandler() {
        return null;
    }

    @Override
    public void onNavigationDrawerAccountChange(Account account) {

    }

    @Override
    public NavigationDrawerTopHandler getNavigationDrawerTopHandler() {
        return new NavigationDrawerTopHandler(this)
                .addItem("Movies", new MovieGridFragment())
                .addItem("TV Shows", new TVShowsFragment())
                .addItem("People", new PeopleFragment())
                .addItem("Lists", new FavouritesFragment());

    }

    @Override
    public NavigationDrawerBottomHandler getNavigationDrawerBottomHandler() {
        return new NavigationDrawerBottomHandler(this)
                .addSettings(null)
                .addHelpAndFeedback(null);
    }

    @Override
    public boolean overlayActionBar() {
        return false;
    }

    @Override
    public boolean replaceActionBarTitleByNavigationDrawerItemTitle() {
        return true;
    }

    @Override
    public int defaultNavigationDrawerItemSelectedPosition() {
        return 0;
    }

    /**
     * A placeholder fragment containing a simple view.
     */



}
