package com.redsys.gus.presentation.search;

import com.redsys.gus.data.UserRepository;
import com.redsys.gus.data.remote.model.User;
import com.redsys.gus.presentation.base.BasePresenter;

import java.util.List;

import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by a on 9/15/16.
 */
public class UserSearchPresenter extends BasePresenter<UserSearchContract.View> implements UserSearchContract.Presenter {

    private final Scheduler mainScheduler, ioScheduler;
    private UserRepository userRepository;

    public UserSearchPresenter(Scheduler mainScheduler, Scheduler ioScheduler, UserRepository userRepository) {
        this.mainScheduler = mainScheduler;
        this.ioScheduler = ioScheduler;
        this.userRepository = userRepository;
    }

    @Override
    public void search(String term) {

        checkViewAttached();
        getView().showLoading();

        Subscription subscription = userRepository.searchUsers(term)
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe(new Subscriber<List<User>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        getView().hideLoading();
                        getView().showError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<User> users) {

                        getView().hideLoading();
                        getView().showSearchResults(users);
                    }
                });

        addSubscription(subscription);
    }
}
