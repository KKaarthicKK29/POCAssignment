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
import com.example.karthickmadasamy.myapplication.sqlite.DBHandler;
import com.example.karthickmadasamy.myapplication.sqlite.DBRowModel;
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

    private static final String FEEDER_LIST = "Feeder Adapter Data";


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
    private List<Rows> mRowsList;


    public static FeederFragment newInstance() {
        return new FeederFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.feeder_fragment, container, false);
        ButterKnife.bind(this, view);
        mRowsList = new ArrayList<Rows>();
        //If restoring from state, load the list from the bundle
        if (savedInstanceState != null) {
            ArrayList<DBRowModel> feederRows = savedInstanceState.getParcelableArrayList(FEEDER_LIST);
            adapter = new FeederAdapter(feederRows, getActivity(), new FeederAdapter.OnItemClickListener() {
                @Override
                public void onClick(DBRowModel rows) {
                    showToast(rows.getTitle());
                }
            });
            newsView.setAdapter(adapter);
        } else {
            initView();
        }
        return view;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(getAdapter() !=null) {
            outState.putParcelableArrayList(FEEDER_LIST, getAdapter().getRowsList());
        }
    }
    /**
     * initialize view
     */
    private void initView() {
        initMVP();
        if ( DBHandler.getInstance(getActivity()).getRowCount() > 0) {
            displayDBFeeders(true);
            hideProgressBar();

        } else {
            getFeederList();
        }
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        newsView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
        mainPresenter = new MainPresenter(this, getActivity());
    }
    /**
     * get news list
     */
    private void getFeederList() {
        Log.d(TAG, "getFeederList");
        if (isNetworkAvailable()) {
            showProgressBar();
            mainPresenter.getRows();
        } else {
            //showing error dialog if no network detected
            if (adapter != null && adapter.getItemCount() > 0)
                errorDialog(R.string.network_error_message, false);
            else
                errorDialog(R.string.network_error_message, true);
        }

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

    public FeederAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(FeederAdapter adapter) {
        this.adapter = adapter;
    }

    public void displayDBFeeders(boolean refreshUI) {
        try {
            DBHandler dbHandler = DBHandler.getInstance(getActivity());
            ArrayList<DBRowModel> feederRows = dbHandler.getAllFeederRows();
            toolbar.setTitle(dbHandler.getToolBarTitle());
            adapter = new FeederAdapter(feederRows, getActivity(), new FeederAdapter.OnItemClickListener() {
                @Override
                public void onClick(DBRowModel rows) {
                    showToast(rows.getTitle());
                }
            });
            newsView.setAdapter(adapter);
            Log.d("Feeder",feederRows.size()+"");
            if (refreshUI) {
                getFeederList();
            }
        }catch (Exception e){

        }

    }



    @Override
    public void displayFeeder(FeederModel feederModel) {
        if (null != feederModel) {
            displayDBFeeders(false);
        }
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
            getFeederList();
        }
        return super.onOptionsItemSelected(item);
    }
}

