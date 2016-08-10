package com.mywings.justolm.Utilities;

/**
 * Created by Tatyabhau Chavan on 5/18/2016.
 */
public interface AnyTask<T> {
    T doTask();
    T onResult(T result);
}
