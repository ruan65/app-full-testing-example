package com.redsys.gus.presentation.search;

import com.redsys.gus.data.remote.model.User;
import com.redsys.gus.presentation.base.MvpPresenter;
import com.redsys.gus.presentation.base.MvpView;

import java.util.List;

/**
 * Created by a on 9/15/16.
 */
public interface UserSearchContract {

    interface View extends MvpView {

        void showSearchResults(List<User> githubUserList);

        void showError(String message);

        void showLoading();

        void hideLoading();
    }

    interface Presenter extends MvpPresenter<View> {

        void search(String term);
    }
}
