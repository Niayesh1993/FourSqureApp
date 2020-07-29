package com.example.routingapp.utility

import android.content.Context
import com.example.routingapp.R


/**
 * Created by Zohre Niayeshi on 07,July,2020 niayesh1993@gmail.com
 **/
class NetworkError(context: Context): Error(code = 600,
    message = context.getString(R.string.error_unable_to_connect_to_remote_host) )
{

}