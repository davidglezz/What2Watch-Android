package com.w2w.Other;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.w2w.API.ApiRequests;
import com.w2w.DataBase.MyDataSource;
import com.w2w.Logic.Pelicula;

public class TaskDownloadMovieInfo extends AsyncTask<String, Pelicula, Pelicula> {

    private TaskDownloadMovieInfoListener mListener;

    public TaskDownloadMovieInfo(TaskDownloadMovieInfoListener mListener) {
        this.mListener = mListener;
    }

    @Override
    protected void onPreExecute() {
        if (mListener != null)
            mListener.onPreDownload();
    }

    @Override
    protected Pelicula doInBackground(String... params) {
        Pelicula movie = params.length == 2 ?
                ApiRequests.getMovieByTitle(params[0], params[1]) :
                ApiRequests.getMovieByImdbId(params[0]);

        publishProgress(movie);

        if (movie != null && !isCancelled()) {
            String path = ApiRequests.downloadAndSavePoster(movie.getPoster());
            ApiRequests.downloadAndSavePoster(movie.getBigPoster());

            if (path != null)
                movie._setPoster(path);
        }

        return movie;
    }

    @Override
    protected void onProgressUpdate(Pelicula... result) {
        if (mListener != null)
            mListener.onDownloadedMovieInfo(result[0]);
    }

    @Override
    protected void onPostExecute(Pelicula result) {

        if (result != null) {
            MyDataSource.getInstance().saveMovie(result);
        }

        if (mListener != null)
            mListener.onDownloadedMoviePoster(result);
    }

    /* */
    public interface TaskDownloadMovieInfoListener {

        public void onPreDownload();

        public void onDownloadedMovieInfo(Pelicula result);

        public void onDownloadedMoviePoster(Pelicula result);
    }



}
