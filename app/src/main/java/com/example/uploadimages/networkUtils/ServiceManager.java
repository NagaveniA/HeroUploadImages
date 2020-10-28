package com.example.uploadimages.networkUtils;

import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * Created by Codebele on 08-Aug-19.
 */
public class ServiceManager extends ContextWrapper {

    Context mContext;

    public ServiceManager(Context base) {
        super(base);
        mContext = base;
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected())
                return true;
        }
        return false;
    }
}
