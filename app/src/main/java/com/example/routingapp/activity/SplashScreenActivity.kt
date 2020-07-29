package com.example.routingapp.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.routingapp.R
import com.example.routingapp.utility.Utils
/**
 * Created by Zohre Niayeshi on 11,July,2020 niayesh1993@gmail.com
 **/
class SplashScreenActivity : AppCompatActivity() {


    private val SPLASH_TIME_OUT = 3000
    lateinit var utils: Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        utils = Utils(this)

        //checkAndRequestPermissions_me(this);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            navigateToNextActivity(intent)
        } else {
            if (utils.checkAndRequestPermissions(this))
            {
                val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                navigateToNextActivity(intent)
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults!!)
        if (!checkPermission()) {
            // Ask again for Permissions until user accepts
            utils.checkAndRequestPermissions(this)
        } else {
            // If all the permissions are accepted then proceed to next activity
            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            navigateToNextActivity(intent)
        }
    }

    private fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) === PackageManager.PERMISSION_GRANTED
    }

    private fun navigateToNextActivity(intent: Intent) {
        Handler().postDelayed({
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT.toLong())
    }

}
