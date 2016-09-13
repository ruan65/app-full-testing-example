package com.redsys.gus.data.remote;

import com.redsys.gus.data.remote.model.User;
import com.redsys.gus.data.remote.model.UsersList;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by a on 9/13/16.
 */
public interface GithubUserRestService {

    @GET("/search/users?per_page=2")
    Observable<UsersList> searchGithubUsers(@Query("q") String searchTerm);

    @GET("/users/{username}")
    Observable<User> getUser(@Path("username") String username);
}
