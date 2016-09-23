package com.redsys.gus.presentation.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.redsys.gus.R;

/**
 * Created by a on 9/22/16.
 */

public class UsersAdapter extends RecyclerView.Adapter<UserViewHolder> {


    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_user, parent, false);

        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
