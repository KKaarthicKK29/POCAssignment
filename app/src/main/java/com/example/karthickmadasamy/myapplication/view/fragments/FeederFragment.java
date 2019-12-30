package com.example.karthickmadasamy.myapplication.view.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.karthickmadasamy.myapplication.R;
import com.example.karthickmadasamy.myapplication.adapters.FeederAdapter;
import com.example.karthickmadasamy.myapplication.models.FeederModel;
import com.example.karthickmadasamy.myapplication.models.Rows;
import com.example.karthickmadasamy.myapplication.presenter.MainPresenter;
import com.example.karthickmadasamy.myapplication.presenter.MainViewInterface;
import com.example.karthickmadasamy.myapplication.sqlite.DBHandler;
import com.example.karthickmadasamy.myapplication.sqlite.DBRowModel;
import com.example.karthickmadasamy.myapplication.viewmodel.FeederViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
//Added SwipeRefresh action to this class
import android.support.v4.widget.SwipeRefreshLayout;
import java.util.ArrayList;
import java.util.List;


/**
 * Its our main fragment which renders UI
 * Butterknife is a view binding tool that uses annotations to generate boilerplate code for us
 * Used MVP Design pattern in our project
 * MVP pattern allows separating the presentation layer from the logic so that everything about how the UI works is agnostic from how we represent it on screen
 * Created by Karthick.Madasamy on 12/4/2019.
 */

public class FeederFragment extends BaseFragment implements MainViewInterface {
    private String TAG = FeederFragment.this.getClass().getName();

    private static final String FEEDER_LIST = "FeederEntity Adapter Data";


    @BindView(R.id.feeder_view)
    RecyclerView newsView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    FeederAdapter adapter;
    MainPresenter mainPresenter;
    private List<Rows> mRowsList;

    private FeederViewModel feederViewModel;

    public static FeederFragment newInstance() {
        return new FeederFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new FeederAdapter(this.getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.feeder_fragment, container, false);
        ButterKnife.bind(this, view);
        feederViewModel = ViewModelProviders.of(this).get(FeederViewModel.class);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        newsView.setLayoutManager(new LinearLayoutManager(getActivity()));
        newsView.setAdapter(adapter);

        initMVP();
        if(isNetworkAvailable()) {
            mainPresenter.getRows();
        }
        swipeRefresh();
        return view;
    }

    /**
     * initialize Model View Presenter
     */
    private void initMVP() {
        mainPresenter = new MainPresenter(this, getActivity());
    }
    private void swipeRefresh() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mainPresenter.getRows();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    @Override
    public void showToast(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void displayFeeder(FeederModel newsResponse) {

    }

    public FeederAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(FeederAdapter adapter) {
        this.adapter = adapter;
    }



    @Override
    public void displayError(String e) {
        if (adapter != null && adapter.getItemCount() > 0)
            errorDialog(R.string.server_error_message, false);
        else
            errorDialog(R.string.server_error_message, true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh) {
            mainPresenter.getRows();
        }
        return super.onOptionsItemSelected(item);
    }
}

