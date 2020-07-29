package com.example.routingapp.utility

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Zohre Niayeshi on 07,July,2020 niayesh1993@gmail.com
 **/
class Utils(context: Context) {

    private val context: Context = context
    private var mLocationManager: LocationManager? = null
    private var myWeakInstance: WeakReference<Utils>? = null



    fun isNetworkLocationEnabled(): Boolean {
        if (mLocationManager == null)
            mLocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return mLocationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun isGpsLocationEnabled(): Boolean {
        if (mLocationManager == null)
            mLocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return mLocationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }


    //Check if service is running
    fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                Log.e(serviceClass.name, "Running")
                return true
            }
        }
        Log.e(serviceClass.name, "Not Running")
        return false
    }

    //check if network available
    fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }

    fun checkAndRequestPermissions(activity: Activity?): Boolean {

        //int sms = ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS);
        val loc: Int = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val loc2: Int = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val write: Int = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val listPermissionsNeeded: MutableList<String> =
            ArrayList()

        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (loc2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (write != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            if (activity != null) {
                ActivityCompat.requestPermissions(
                    activity,
                    listPermissionsNeeded.toTypedArray(),
                    Constants().REQUEST_ID_MULTIPLE_PERMISSIONS
                )
            }
            return false
        }
        return true
    }

}