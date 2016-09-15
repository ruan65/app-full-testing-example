package com.redsys.gus.data;

import com.redsys.gus.data.remote.GithubUserRestService;
import com.redsys.gus.data.remote.model.User;
import com.redsys.gus.data.remote.model.UsersList;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Created by a on 9/14/16.
 */
public class UserRepositoryImplTest {

    private static final String USER_LOGIN_RUAN65 = "ruan65";
    private static final String USER_LOGIN_2_REBECCA = "rebecca";

    @Mock
    GithubUserRestService githubUserRestService;

    private UserRepository repository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        repository = new UserRepositoryImpl(githubUserRestService);
    }

    @Test
    public void searchUsers_2000kResponse_InvokeCorrectApiCalls() {

        // Given
        when(githubUserRestService.searchGithubUsers(anyString()))
                .thenReturn(Observable.just(githubUserList()));

        when(githubUserRestService.getUser(anyString()))
                .thenReturn(
                        Observable.just(user1FullDetails()),
                        Observable.just(user2FullDetails()));

        // When
        TestSubscriber<List<User>> subscriber = new TestSubscriber<>();
        repository.searchUsers(USER_LOGIN_RUAN65).subscribe(subscriber);

        // Then
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();

        List<List<User>> onNextEvents = subscriber.getOnNextEvents();
        List<User> users = onNextEvents.get(0);

        Assert.assertEquals(USER_LOGIN_RUAN65, users.get(0).getLogin());
        Assert.assertEquals(USER_LOGIN_2_REBECCA, users.get(1).getLogin());

        verify(githubUserRestService).searchGithubUsers(USER_LOGIN_RUAN65);
        verify(githubUserRestService).getUser(USER_LOGIN_RUAN65);
        verify(githubUserRestService).getUser(USER_LOGIN_2_REBECCA);
    }

    @Test
    public void searchUsers_IOExceptionThenSuccess_SearchUsersRetried() {

        // Given

        when(githubUserRestService.searchGithubUsers(anyString()))
                .thenReturn(getIOExceptionError(), getIOExceptionError(), Observable.just(githubUserList()));

        when(githubUserRestService.getUser(anyString()))
                .thenReturn(Observable.just(user1FullDetails()), Observable.just(user2FullDetails()));

        // When

        TestSubscriber<List<User>> subscriber = new TestSubscriber<>();
        repository.searchUsers(USER_LOGIN_RUAN65).subscribe(subscriber);

        // Then

        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();

        verify(githubUserRestService, times(3)).searchGithubUsers(USER_LOGIN_RUAN65);

        verify(githubUserRestService).getUser(USER_LOGIN_RUAN65);
        verify(githubUserRestService).getUser(USER_LOGIN_2_REBECCA);
    }

    @Test
    public void searchUsers_GetUserIoExceptionThenSuccess_SearchUserRetried() {

        // Given

        when(githubUserRestService.searchGithubUsers(anyString()))
                .thenReturn(Observable.just(githubUserList()));

        when(githubUserRestService.getUser(anyString()))
                .thenReturn(getIOExceptionError(), Observable.just(user2FullDetails()),
                        Observable.just(user1FullDetails()));

        // When

        TestSubscriber<List<User>> subscriber = new TestSubscriber<>();
        repository.searchUsers(USER_LOGIN_RUAN65).subscribe(subscriber);

        // Then

        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();

        verify(githubUserRestService, times(2)).searchGithubUsers(USER_LOGIN_RUAN65);

        verify(githubUserRestService, times(2)).getUser(USER_LOGIN_RUAN65);
        verify(githubUserRestService).getUser(USER_LOGIN_2_REBECCA);

    }

    @Test
    public void searchUsers_OtherHttpError_SearchTerminatedWithError() {

        // Given

        when(githubUserRestService.searchGithubUsers(anyString()))
                .thenReturn(get403ForbiddenError());
        // When

        TestSubscriber<List<User>> subscriber = new TestSubscriber<>();
        repository.searchUsers(USER_LOGIN_RUAN65).subscribe(subscriber);

        // Then

        subscriber.awaitTerminalEvent();
        subscriber.assertError(HttpException.class);

        verify(githubUserRestService).searchGithubUsers(USER_LOGIN_RUAN65);

        verify(githubUserRestService, never()).getUser(USER_LOGIN_RUAN65);
        verify(githubUserRestService, never()).getUser(USER_LOGIN_2_REBECCA);


    }

    private Observable<UsersList> get403ForbiddenError() {
        return Observable.error(new HttpException(
                Response.error(403, ResponseBody.create(MediaType.parse("application/json"), "Forbidden"))
        ));
    }

    private UsersList githubUserList() {

        User user = new User();
        user.setLogin(USER_LOGIN_RUAN65);

        User user2 = new User();
        user2.setLogin(USER_LOGIN_2_REBECCA);

        List<User> githubUsers = new ArrayList<>();
        githubUsers.add(user);
        githubUsers.add(user2);
        UsersList usersList = new UsersList(githubUsers);
        usersList.setItems(githubUsers);
        return usersList;
    }

    private User user1FullDetails() {
        User user = new User();
        user.setLogin(USER_LOGIN_RUAN65);
        user.setName("Rigs Franks");
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

    private Observable getIOExceptionError() {
        return Observable.error(new IOException());
    }
}