package com.example.routingapp.utility

import android.content.Context
import android.util.Log
import com.example.routingapp.R
import com.example.routingapp.utility.api.ApiCallbackListener
import com.example.routingapp.utility.api.ApiResultModel
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import retrofit2.Response
import java.io.IOException
import java.lang.reflect.Type
import java.util.*

/**
 * Created by Zohre Niayeshi on 07,July,2020 niayesh1993@gmail.com
 **/
class Helpers {

    fun <T> ParseJson(jsonString: String?, t: Type?): T {
        val builder = GsonBuilder()
        val gson = builder.create()
        return gson.fromJson(jsonString, t)
    }

    @Throws(IOException::class)
    fun HandleResponse(
        response: Response<ApiResultModel?>,
        callbackListener: ApiCallbackListener
    ) {
        if (response.code() >= 200 && response.code() < 300) {
            response.body()?.let { callbackListener.onSucceed(it) }
        } else { // There might be an error.
            if (response.code() >= 400 && response.code() < 600) { // Request and server errors.
                val errors: MutableList<Error> =
                    ArrayList()
                val t = object : TypeToken<ApiResultModel?>() {}.type
                val result: ApiResultModel =
                    ParseJson(
                        response.errorBody()!!.string(),
                        t
                    )
                // If it has it's own error message return it
                if (result != null && !result.meta!!.getErrorType()!!.isEmpty()) {
                    errors.add(
                        Error(
                            response.code(),
                            result.meta!!.getErrorDetail()
                        )
                    )
                    callbackListener.onError(errors)
                } else { // Make a generic error message (mostly for error 500)
                    errors.add(Error(response.code(), R.string.server_error.toString()))
                    callbackListener.onError(errors)
                    Log.e("API Helpers", "Handle Response Error Empty Error")
                }
            } else { // undefined error code >= 600
                if (response.body() != null && response.body()!!.meta!!.getErrorType() != null) {
                }
            }
        }
    }

    fun HandleErrors(
        t: Throwable?,
        context: Context?,
        callbackListener: ApiCallbackListener
    ) {
        Log.e("API Helpers", "onFailure: ", t)
        val errors: MutableList<Error> =
            ArrayList()
        errors.add(NetworkError(context!!))
        callbackListener.onError(errors)
    }
}