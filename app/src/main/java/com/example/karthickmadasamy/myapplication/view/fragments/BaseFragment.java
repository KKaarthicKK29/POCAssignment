package com.example.karthickmadasamy.myapplication.view.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.karthickmadasamy.myapplication.R;


/**
 * Created by Karthick.Madasamy on 12/4/2019.
 */

public class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();
    /**
     * display error message if no network connection
     */
    public void errorDialog(int msg, final boolean exitapp) {
        Log.d(TAG, "errorDialog");
        try
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            if(exitapp)
                                getActivity().finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        catch (Exception e)
        {
            Log.e(TAG,"Error in showing error dialog box : "+e.getMessage());
        }
    }

    /**
     * checking network connection. if connection is available, it will return true else false
     */
    public boolean isNetworkAvailable() {
        Log.d(TAG, "isNetworkAvailable");
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting(); // return true if network connection available
        return isConnected;
    }
}
