package com.example.karthickmadasamy.myapplication.presenter;

import android.util.Log;

import com.example.karthickmadasamy.myapplication.R;
import com.example.karthickmadasamy.myapplication.models.FeederModel;
import com.example.karthickmadasamy.myapplication.network.NetworkClient;
import com.example.karthickmadasamy.myapplication.network.NetworkInterface;


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
    private MainViewInterface mvi;

    public MainPresenter(MainViewInterface mvi) {
        this.mvi = mvi;
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
            public void onNext(@NonNull FeederModel feederModel) {
                Log.d(TAG,"OnNext"+ feederModel.getRows());
                mvi.displayFeeder(feederModel);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG,"Error"+e);
                e.printStackTrace();
                mvi.hideProgressBar();
                mvi.displayError(""+ R.string.server_error_message);
            }

            @Override
            public void onComplete() {
                Log.d(TAG,"Completed");
                mvi.hideProgressBar();
            }
        };
    }
}
