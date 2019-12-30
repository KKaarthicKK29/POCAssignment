package com.example.karthickmadasamy.myapplication.presenter;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.karthickmadasamy.myapplication.db.FeederEntity;
import com.example.karthickmadasamy.myapplication.models.FeederModel;
import com.example.karthickmadasamy.myapplication.network.NetworkClient;
import com.example.karthickmadasamy.myapplication.network.NetworkInterface;
import com.example.karthickmadasamy.myapplication.sqlite.DBHandler;
import com.example.karthickmadasamy.myapplication.sqlite.DBModel;
import com.example.karthickmadasamy.myapplication.view.fragments.FeederFragment;
import com.example.karthickmadasamy.myapplication.viewmodel.FeederViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 *RxJava and RxAndroid technique used in this project
 * With the help of Observables and Schedulers,plays major role in supporting multithreading concept in android applications.
 * Observable is a data stream that do some work and emits data.
 * Observer/Schedulers is the counter part of Observable. It receives the data emitted by Observable.
 * Created by Karthick.Madasamy on 12/4/2019.
 */

public class MainPresenter implements MainPresenterInterface {
    private String TAG = MainPresenter.this.getClass().getName();
    private FeederFragment fragment;
    public Context ctx;
    private FeederViewModel feederViewModel;
    public MainPresenter(FeederFragment frag, Context ctx) {
        this.fragment = frag;
        this.ctx = ctx;
//        feederViewModel = FeederViewModel.
        feederViewModel = ViewModelProviders.of( frag).get(FeederViewModel.class);
        feederViewModel.getAllFeeders().observe(fragment,(t) -> {
            if(null!=fragment.getAdapter()){
                fragment.getAdapter().setFeederList(t);
            }
        });


    }

    @Override
    public void getRows() {
        getObservable().subscribeWith(getObserver());
    }

    public Observable<FeederModel> getObservable(){
        return NetworkClient.getRetrofitService().create(NetworkInterface.class)
                .getRows()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public DisposableObserver<FeederModel> getObserver(){
        return new DisposableObserver<FeederModel>() {

            @Override
            public void onNext(@NonNull FeederModel model) {
                model.getRows().stream().forEach(e->{
                    if(e.getTitle()==null){
                       e.setTitle("sample");
                    }
                });
                fragment.toolbar.setTitle(model.getTitle());
                feederViewModel.insertAll(model.getRows());

            }
            @Override
            public void onError(@NonNull Throwable e) {
            }
            @Override
            public void onComplete() {
                fragment.hideProgressBar();

            }
        };
    }/*
    public DisposableObserver<FeederModel> getObserver(){
        return new DisposableObserver<FeederModel>() {

            @Override
            public void onNext(@NonNull FeederModel feederModel) {
                //DB insertorupdate
                feederViewModel.insert();
                DBHandler .getInstance(ctx).insertOrUpdateFeederRows(new DBModel(feederModel.getRows(),feederModel.getTitle()));
              //  fragment.displayFeeder(feederModel);

            }
            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
              //  fragment.hideProgressBar();
               // fragment.displayError(""+ R.string.server_error_message);
            }
            @Override
            public void onComplete() {
                Log.d(TAG,"Completed");
               // fragment.hideProgressBar();
            }
        };
    }*/
}
