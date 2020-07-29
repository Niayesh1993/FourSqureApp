package com.example.routingapp.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle

/**
 * Created by Zohre Niayeshi on 11,July,2020 niayesh1993@gmail.com
 **/
class BroadcastReceivers: BroadcastReceiver() {

    private val TAG = "BroadcastReceivers"
    val ACTION_LOCATION_SERVICE = "service.LocationService"
    val ACTION_NEW_LOCATION = "action_new_location"
    val REQUEST_CODE_LOCATION_SERVICE = 12345
    var broadcastReceiver: BroadcastListener? = null

    interface BroadcastListener {
        fun onBroadcastReceiver(intent: Intent)
    }


    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null && intent.getAction() != null) {

            // Start Location Service
            if (intent.action == BroadcastReceivers().ACTION_LOCATION_SERVICE) {
                try {
                    val i = Intent(context, LocationService::class.java)
                    context!!.startService(i)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            if (intent.action == BroadcastReceivers().ACTION_NEW_LOCATION)
            {
                if (broadcastReceiver != null) {

                    broadcastReceiver!!.onBroadcastReceiver(intent)
                }

            }
        }
    }
}