package com.redsys.gus.data;

import com.redsys.gus.data.remote.model.User;

import java.util.List;

import rx.Observable;

/**
 * Created by a on 9/13/16.
 */
public interface UserRepository {

    Observable<List<User>> searchUsers(String searchTerm);
}
