package in.reduxpress.themoviedb.AsyncTasks;

import java.util.List;

import in.reduxpress.themoviedb.DataModels.Movie;

/**
 * Created by kumardivyarajat on 28/09/15.
 */
public interface AsyncResponse {
    void processFinish(List<Movie> output);
}
