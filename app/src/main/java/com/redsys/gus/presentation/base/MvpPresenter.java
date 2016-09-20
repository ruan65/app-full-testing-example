package com.redsys.gus.presentation.base;

/**
 * Created by a on 9/20/16.
 */

public interface MvpPresenter<V extends MvpView> {

    void attachView(V view);

    void detachView();

}
