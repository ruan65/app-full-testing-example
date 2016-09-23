package com.redsys.gus.presentation.search;

import com.redsys.gus.data.UserRepository;
import com.redsys.gus.data.remote.model.User;
import com.redsys.gus.data.remote.model.UsersList;
import com.redsys.gus.presentation.base.BasePresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by a on 9/20/16.
 */
public class UserSearchPresenterTest {

    private static final String USER_LOGIN_RUAN = "ruan65";
    private static final String USER_LOGIN_2_REBECCA = "rebecca";

    @Mock
    UserRepository userRepository;

    @Mock
    UserSearchContract.View view;

    UserSearchPresenter userSearchPresenter;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        userSearchPresenter = new UserSearchPresenter(
                Schedulers.immediate(),
                Schedulers.immediate(),
                userRepository);

        userSearchPresenter.attachView(view);
    }

    @Test
    public void search_ValidSearchTerm_ReturnsResults() {

        UsersList usersList = getDummyUserList();

        when(userRepository.searchUsers(anyString()))
                .thenReturn(Observable.<List<User>>just(usersList.getItems()));

        userSearchPresenter.search(USER_LOGIN_RUAN);

        verify(view).showLoading();
        verify(view).hideLoading();
        verify(view).showSearchResults(usersList.getItems());
        verify(view, never()).showError(anyString());
    }

    @Test
    public void search_UserRepositoryError_ErrorMsg() {

        String errMsg = "no internet connection";
        when(userRepository.searchUsers(anyString()))
                .thenReturn(Observable.error(new IOException(errMsg)));

        userSearchPresenter.search("bullshitblablabla");

        verify(view).showLoading();
        verify(view).hideLoading();
        verify(view, never()).showSearchResults(anyList());

        verify(view).showError(errMsg);
    }

    @Test
    public void search_NoViewAttached_ThrowMvpException() {

        userSearchPresenter.detachView();

        try {
            userSearchPresenter.search(USER_LOGIN_RUAN);
        } catch (BasePresenter.MvpViewNotAttachedException ex) {
            ex.printStackTrace();
        }
        verify(view, never()).showLoading();
        verify(view, never()).showSearchResults(anyList());
    }

    private UsersList getDummyUserList() {

        List<User> githubUsers = new ArrayList<>();

        githubUsers.add(user1FullDetails());
        githubUsers.add(user2FullDetails());

        return new UsersList(githubUsers);
    }

    private User user1FullDetails() {
        User user = new User();
        user.setLogin(USER_LOGIN_RUAN);
        user.setName("Andrey");
        user.setAvatarUrl("avatar_url");
        user.setBio("Bio1");
        return user;
    }

    private User user2FullDetails() {
        User user = new User();
        user.setLogin(USER_LOGIN_2_REBECCA);
        user.setName("Rebecca Franks");
        user.setAvatarUrl("avatar_url2");
        user.setBio("Bio2");
        return user;
    }
}