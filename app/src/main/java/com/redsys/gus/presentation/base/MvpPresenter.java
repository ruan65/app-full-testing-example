package com.redsys.gus.presentation.base;

/**
 * Created by a on 9/15/16.
 */
public interface MvpPresenter<V extends MvpView> {

    void attachView(V view);
}
