package com.example.routingapp.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.routingapp.utility.Constants
import com.example.routingapp.utility.SettingsManager
import com.example.routingapp.utility.Utils

/**
 * Created by Zohre Niayeshi on 11,July,2020 niayesh1993@gmail.com
 **/
class LocationService: Service(), LocationListener, BroadcastReceivers.BroadcastListener {

    var isGPSEnable = false
    var isNetworkEnable = false
    private var locationManager: LocationManager? = null
    private var location: Location? = null
    private var utils: Utils? = null
    private val TAG = javaClass.simpleName
    var receiver:BroadcastReceivers = BroadcastReceivers()


    override fun onCreate() {
        super.onCreate()
        SettingsManager.init(this)
        utils = Utils(this)
        receiver.broadcastReceiver = this

    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        rescheduleNextWake()
        getLocation()
        return START_STICKY
    }

    private fun rescheduleNextWake() {
        val intent = Intent(this, BroadcastReceivers::class.java)
        intent.action = BroadcastReceivers().ACTION_LOCATION_SERVICE
        val pIntent = PendingIntent.getBroadcast(
            applicationContext, BroadcastReceivers().REQUEST_CODE_LOCATION_SERVICE,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val nextMilis: Long = System.currentTimeMillis() + Constants().CONFIG_LOCATION_INTERVAL
        val alarm =
            this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= 23) {
            alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextMilis, pIntent)
        } else if (Build.VERSION.SDK_INT >= 19) {
            alarm.setExact(AlarmManager.RTC_WAKEUP, nextMilis, pIntent)
        } else {
            alarm[AlarmManager.RTC_WAKEUP, nextMilis] = pIntent
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {

            locationManager =
                applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            isGPSEnable = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            isNetworkEnable = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (isGPSEnable && isNetworkEnable) {

                location = null
                if (location == null) {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    if (locationManager!!.getAllProviders()
                            .contains(LocationManager.GPS_PROVIDER)
                    ) locationManager!!.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        0f,
                        this
                    )
                    if (locationManager != null) {
                        location =
                            locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        if (location != null) {
                            requestNewVenues()
                        }
                    }
                    else {
                        location = null
                        if (locationManager!!.getAllProviders()
                                .contains(LocationManager.NETWORK_PROVIDER)
                        ) locationManager!!.requestSingleUpdate(
                            LocationManager.NETWORK_PROVIDER,
                            this,
                            null
                        )
                        if (locationManager != null) {
                            location =
                                locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                            if (location != null) {
                                requestNewVenues()
                            }
                        }

                    }

                }
            }
    }

    fun requestNewVenues()
    {
        Intent().also { intent ->
            intent.setAction(BroadcastReceivers().ACTION_NEW_LOCATION)
            sendBroadcast(intent)
            }
    }


    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    @SuppressLint("MissingPermission")
    override fun onLocationChanged(location: Location?) {

        if (location!!.provider == LocationManager.GPS_PROVIDER) locationManager!!.removeUpdates(
            this
        )
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.e(TAG, provider + "Status changed to: " + status)
    }

    override fun onProviderEnabled(provider: String?) {

         Log.d(TAG, "Provider enabled: " + provider)
    }

    override fun onProviderDisabled(provider: String?) {

    }

    override fun onBroadcastReceiver(intent: Intent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}