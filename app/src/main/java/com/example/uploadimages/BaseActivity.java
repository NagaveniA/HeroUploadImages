package com.example.uploadimages;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public abstract class BaseActivity extends AppCompatActivity {
    public static String TAG = BaseActivity.class.getName();
    private Dialog m_Dialog;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    double latitude, longitude;
    public boolean isLocationEnabled = false;
    public boolean isNetworkConnected = false;
    public boolean isConnected = false;

    public void showProgressDialog(Context context) {
        m_Dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.progress_dialog_spinner_layout, null);
        m_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        m_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        m_Dialog.setContentView(view);
        m_Dialog.setCancelable(false);
        m_Dialog.show();
    }

    public void init() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();

                updateLocationUI();
            }
        };
        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();


    }

    public void timer() {
        Timer timer = new Timer();
        TimerTask hourlyTask = new TimerTask() {
            @Override
            public void run() {
                isLocationEnabled(getApplicationContext());
                checkInternetConenction();
                Log.e("Timer", String.valueOf(isConnected));
            }
        };
        timer.schedule(hourlyTask, 2l, 10000);
    }

    public boolean validateLocation() {
        if (!isLocationEnabled) {
            init();
            return false;
        } else
            return true;

    }

    public Boolean isLocationEnabled(Context context) throws RuntimeException {
        LocationManager lm = null;
        try {
            lm = (LocationManager)
                    context.getSystemService(Context.LOCATION_SERVICE);
            assert lm != null;
            isLocationEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            Log.e("isLocationEnabled", String.valueOf(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)));
        } catch (Exception e) {

        }

        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(BaseActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }

    public void updateLocationUI() {
        if (mCurrentLocation != null) {
            try {
                updateLocation(mCurrentLocation);

            } catch (Exception e) {

            }
        }


    }

    public void updateLocation(Location mCurrentLocation) {
        try {
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMaximumFractionDigits(7);
            latitude = mCurrentLocation.getLatitude();
            longitude = mCurrentLocation.getLongitude();
            latitude = Double.valueOf(nf.format(latitude));
            longitude = Double.valueOf(nf.format(longitude));
        } catch (Exception e) {

        }

    }

    public void dismissProgressDialog() {
        try {
            if ((m_Dialog != null) && m_Dialog.isShowing()) {
                m_Dialog.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
        } catch (final Exception e) {
            // Handle or log or ignore
        } finally {
            m_Dialog = null;
        }
//        if (m_Dialog != null && m_Dialog.isShowing()) {
//            m_Dialog.dismiss();
//        }
    }

    public void showAlertDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void showShortToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

    }


    public boolean checkInternetConenction() {
        try {
            // get Connectivity Manager object to check connection
            ConnectivityManager connec
                    = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
            // Check for network connections
            if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

                isNetworkConnected = connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED;
                Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
                Log.e("isNetworkConnected", String.valueOf(isNetworkConnected));
                return true;
            } else if (
                    connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED || connec.getNetworkInfo(1).getState() ==
                            android.net.NetworkInfo.State.DISCONNECTED) {
                Log.e("Not Connected", String.valueOf(connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED || connec.getNetworkInfo(1).getState() ==
                        android.net.NetworkInfo.State.DISCONNECTED));
                isNetworkConnected = connec.getNetworkInfo(1).getState() ==
                        android.net.NetworkInfo.State.DISCONNECTED;
                Toast.makeText(this, "Not Connected ", Toast.LENGTH_LONG).show();

                return false;
            }
        } catch (Exception e) {

        }

        return false;
    }

    public static String getFileName() {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());
        return "Photo_" + timeStamp + "_.jpg";
    }
}


