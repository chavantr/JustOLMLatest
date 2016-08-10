package com.mywings.justolm.Utilities;

import android.os.AsyncTask;

/**
 * Created by Tatyabhau Chavan on 5/18/2016.
 */
public class AnyAsyncTask<T> extends AsyncTask<T, T, T> {

    private AnyTask<T> anyTask = null;

    public AnyAsyncTask(AnyTask<T> anyTask) {
        this.anyTask = anyTask;
    }

    public void doTaskInbackground() {
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected T doInBackground(T... params) {
        return anyTask.doTask();
    }

    @Override
    protected void onPostExecute(T t) {
        super.onPostExecute(t);
        anyTask.onResult(t);
    }
}
