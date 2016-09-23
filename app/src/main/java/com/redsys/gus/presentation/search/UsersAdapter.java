package com.redsys.gus.presentation.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.redsys.gus.R;
import com.redsys.gus.data.remote.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by a on 9/22/16.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private final Context ctx;
    private List<User> items;

    public UsersAdapter(Context context, List<User> items) {
        this.ctx = context;
        this.items = items;
    }

    @Override
    public UsersAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int __) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_user, parent, false);

        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserViewHolder h, int position) {

        User u = items.get(position);

        h.textViewBio.setText(u.getBio());

        String name = u.getName();

        h.textViewName.setText(null != name ? u.getLogin() + " " + name : u.getLogin());

        Picasso.with(ctx).load(u.getAvatarUrl()).into(h.imageViewAvatar);
    }

    @Override
    public int getItemCount() {
        return null == items ? 0 : items.size();
    }

    public void setItems(List<User> userList) {
        items = userList;
        notifyDataSetChanged();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        final TextView textViewBio;
        final TextView textViewName;
        final ImageView imageViewAvatar;

        UserViewHolder(View v) {
            super(v);
            imageViewAvatar = (ImageView) v.findViewById(R.id.imageview_userprofilepic);
            textViewName = (TextView) v.findViewById(R.id.textview_username);
            textViewBio = (TextView) v.findViewById(R.id.textview_user_profile_info);
        }
    }
}
