package com.redsys.gus.presentation.base;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by a on 9/15/16.
 */
public class BasePresenter<T extends MvpView> implements MvpPresenter<T> {

    private T view;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();


    @Override
    public void attachView(T mvpView) {

        view = mvpView;
    }

    @Override
    public void detachView() {

        compositeSubscription.clear();
        view = null;
    }

    public T getView() {
        return view;
    }

    public void checkViewAttached() {

        if (!isViewAttached()) {
            throw new MvpViewNotAttachedException();
        }
    }

    private boolean isViewAttached() {
        return null != view;
    }

    public void addSubscription(Subscription subscription) {
        compositeSubscription.add(subscription);
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before requesting data to the Presenter");
        }
    }
}
