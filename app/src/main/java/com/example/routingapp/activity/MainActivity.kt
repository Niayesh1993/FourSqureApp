package com.example.routingapp.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.routingapp.R
import com.example.routingapp.utility.Constants
import com.example.routingapp.utility.SettingsManager
import com.example.routingapp.utility.Utils

/**
 * Created by Zohre Niayeshi on 07,July,2020 niayesh1993@gmail.com
 **/
class MainActivity : AppCompatActivity() {

    val client_id = "VKXHR2MKRJYMADL00X3T3M1K50YILADND1S2DQMCAK5UBV5V"
    val client_secret = "4LETZWB4LONAWQY2HWUULD0Y1ORRC4S1NW4DAB4PBLSZ3BI1"
    lateinit var utils: Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SettingsManager.init(this)
        utils = Utils(this)
        SettingsManager.setValue(Constants().CLIENT_ID, client_id)
        SettingsManager.setValue(Constants().CLIENT_SECRET, client_secret)

        val intent = Intent(this@MainActivity, MapsActivity::class.java)
        startActivity(intent)
        finish()
    }

}
