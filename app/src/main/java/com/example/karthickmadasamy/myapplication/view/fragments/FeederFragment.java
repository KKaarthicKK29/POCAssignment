package com.example.karthickmadasamy.myapplication.view.fragments;

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

import butterknife.BindView;
import butterknife.ButterKnife;

//Added SwipeRefresh action to this class
import android.support.v4.widget.SwipeRefreshLayout;



/**
 * Its our main fragment which renders UI
 * Butterknife is a view binding tool that uses annotations to generate boilerplate code for us
 * Used MVP Design pattern in our project
 * MVP pattern allows separating the presentation layer from the logic so that everything about how the UI works is agnostic from how we represent it on screen
 * Created by Karthick.Madasamy on 12/4/2019.
 */

public class FeederFragment extends BaseFragment implements MainViewInterface {
    private String TAG=FeederFragment.this.getClass().getName();

    @BindView(R.id.feeder_view)
    RecyclerView newsView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;


    FeederAdapter adapter;
    MainPresenter mainPresenter;
    public static FeederFragment newInstance(){
        return new FeederFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        setHasOptionsMenu(true);
        View view=inflater.inflate(R.layout.feeder_fragment,container,false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    /**
     * initialize view
     */
    private void initView(){
        Log.d(TAG, "initView");
        initMVP();
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        newsView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getFeederList();

        //SwipeRefresh function
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFeederList();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    /**
     * initialize Model View Presenter
     */
    private void initMVP() {
        mainPresenter = new MainPresenter(this);
    }

    /**
     * get news list
     */
    private void getFeederList() {
        Log.d(TAG, "getFeederList");
        if(isNetworkAvailable()) {
            showProgressBar();
            mainPresenter.getRows();
        }
        else {
            //showing error dialog if no network detected
            if(adapter!=null && adapter.getItemCount()>0)
                errorDialog(R.string.network_error_message,false);
            else
                errorDialog(R.string.network_error_message,true);
        }

    }
    @Override
    public void showToast(String str) {
        Toast.makeText(getActivity(),str,Toast.LENGTH_LONG).show();
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
    public void displayFeeder(FeederModel feederModel) {
        if(feederModel !=null) {
            //Log.d(TAG, feederModel.getFeederList().get(1).getTitle());
            toolbar.setTitle(feederModel.getTitle());
            adapter = new FeederAdapter(feederModel.getRows(), getActivity(), new FeederAdapter.OnItemClickListener() {
                @Override
                public void onClick(Rows rows) {
                    showToast(rows.getTitle());
                }
            });
            newsView.setAdapter(adapter);

        }else{
            Log.d(TAG,"NewsResponse response empty");
        }    }

    @Override
    public void displayError(String e) {
        if(adapter!=null && adapter.getItemCount()>0)
            errorDialog(R.string.server_error_message,false);
        else
            errorDialog(R.string.server_error_message,true);
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
        if(id == R.id.refresh){
            getFeederList();
        }
        return super.onOptionsItemSelected(item);
    }
}

