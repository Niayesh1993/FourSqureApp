package com.example.routingapp.service

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.routingapp.caller.UserCaller
import com.example.routingapp.utility.Constants
import com.example.routingapp.utility.Utils
import com.example.routingapp.utility.api.ApiCallbackListener
import com.example.routingapp.utility.api.ApiResultModel
import com.example.routingapp.utility.Helpers
import com.example.routingapp.utility.SettingsManager
import com.example.routingapp.utility.api.RetrofitManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Zohre Niayeshi on 07,July,2020 niayesh1993@gmail.com
 **/
class UserService(context: Context) {

    private val retrofit: Retrofit
    private val caller: UserCaller
    private val context: Context = context
    private val utils: Utils

    init {
        this.retrofit = RetrofitManager(
            context,
            Constants().URL_BASE
        ).getRetrofitInstance()!!
        caller = retrofit.create(UserCaller::class.java)
        this.utils = Utils(context)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun searsh_venue(
        ll: String?,
        apiCallbackListener: ApiCallbackListener
    ) {
        val id = SettingsManager.getString(Constants().CLIENT_ID)
        val secret = SettingsManager.getString(Constants().CLIENT_SECRET)
        val currentTime: String = SimpleDateFormat("yyyyMMdd",
            Locale.getDefault()).format(Date())
        val call: Call<ApiResultModel?>? =
            caller.Venue_explore(id, secret, currentTime, ll)
        call!!.enqueue(object : Callback<ApiResultModel?> {
            override fun onResponse(
                call: Call<ApiResultModel?>,
                response: Response<ApiResultModel?>
            ) {
                try {

                    Helpers().HandleResponse(response, apiCallbackListener)
                } catch (e: Exception) { // FirebaseCrash.report(e);
                    apiCallbackListener.onError(null)
                    Log.e("Error in search_venue", e.message)
                }
            }

            override fun onFailure(
                call: Call<ApiResultModel?>,
                t: Throwable
            ) {
                Helpers().HandleErrors(t, context, apiCallbackListener)
            }
        })
    }

    fun get_venue_detail(
        Venue_Id: String?,
        apiCallbackListener: ApiCallbackListener
    ) {
        val id = SettingsManager.getString(Constants().CLIENT_ID)
        val secret = SettingsManager.getString(Constants().CLIENT_SECRET)
        val currentTime: String = SimpleDateFormat("yyyyMMdd",
            Locale.getDefault()).format(Date())
        val call: Call<ApiResultModel?>? =
            caller.Venue_Detail(Venue_Id, id, secret, currentTime)
        call!!.enqueue(object : Callback<ApiResultModel?> {
            override fun onResponse(
                call: Call<ApiResultModel?>,
                response: Response<ApiResultModel?>
            ) {
                try {
                    Helpers().HandleResponse(response, apiCallbackListener)
                } catch (e: Exception) {
                    apiCallbackListener.onError(null)
                    Log.e("Error in getVenueDetail", e.message)
                }
            }

            override fun onFailure(
                call: Call<ApiResultModel?>,
                t: Throwable
            ) {
                Helpers().HandleErrors(t, context, apiCallbackListener)
            }
        })
    }
}