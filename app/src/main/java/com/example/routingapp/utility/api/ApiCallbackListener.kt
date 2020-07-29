package com.example.routingapp.utility.api

import com.example.routingapp.utility.Error


/**
 * Created by Zohre Niayeshi on 07,July,2020 niayesh1993@gmail.com
 **/
interface ApiCallbackListener {

     fun onSucceed(data: ApiResultModel)
     fun onError(errors: MutableList<Error>?)
}