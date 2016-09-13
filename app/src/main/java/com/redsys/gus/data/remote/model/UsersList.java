package com.redsys.gus.data.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a on 9/13/16.
 */
public class UsersList {

    @SerializedName("total_count")
    @Expose
    private Integer totalCount;
    @SerializedName("incomplete_results")
    @Expose
    private Boolean incompleteResults;
    @SerializedName("items")
    @Expose
    private List<User> items = new ArrayList<User>();

    public UsersList(final List<User> githubUsers) {
        this.items = githubUsers;
    }

    /**
     * @return The totalCount
     */
    public Integer getTotalCount() {
        return totalCount;
    }


    /**
     * @return The incompleteResults
     */
    public Boolean getIncompleteResults() {
        return incompleteResults;
    }

    /**
     * @return The items
     */
    public List<User> getItems() {
        return items;
    }
}
