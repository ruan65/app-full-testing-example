package com.redsys.gus.presentation.search;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.redsys.gus.R;
import com.redsys.gus.data.remote.model.User;
import com.redsys.gus.injection.Injection;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserSearchActivity extends AppCompatActivity implements UserSearchContract.View {

    private UserSearchContract.Presenter userSearchPresenter;
    private UsersAdapter usersAdapter;
    private SearchView searchView;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewUsers;
    private TextView textViewErrorMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);

        userSearchPresenter = new UserSearchPresenter(
                AndroidSchedulers.mainThread(),
                Schedulers.io(),
                Injection.provideUserRepo()
        );

        userSearchPresenter.attachView(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        textViewErrorMessage = (TextView) findViewById(R.id.text_view_error_msg);
        recyclerViewUsers = (RecyclerView) findViewById(R.id.recycler_view_users);

        recyclerViewUsers = (RecyclerView) findViewById(R.id.recycler_view_users);

        usersAdapter = new UsersAdapter(this, null);
        recyclerViewUsers.setAdapter(usersAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userSearchPresenter.detachView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_user_search, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                userSearchPresenter.search(query);
                toolbar.setTitle(query);
                searchItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchItem.expandActionView();
        return true;
    }

    @Override
    public void showSearchResults(List<User> githubUserList) {

        recyclerViewUsers.setVisibility(View.VISIBLE);
        textViewErrorMessage.setVisibility(View.GONE);
        usersAdapter.setItems(githubUserList);
    }

    @Override
    public void showError(String message) {

        textViewErrorMessage.setVisibility(View.VISIBLE);
        recyclerViewUsers.setVisibility(View.GONE);
        textViewErrorMessage.setText(message);

    }

    @Override
    public void showLoading() {

        progressBar.setVisibility(View.VISIBLE);
        recyclerViewUsers.setVisibility(View.GONE);
        textViewErrorMessage.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {

        progressBar.setVisibility(View.GONE);
        recyclerViewUsers.setVisibility(View.VISIBLE);
        textViewErrorMessage.setVisibility(View.GONE);
    }
}
