package com.example.routingapp.utility

import com.google.gson.annotations.SerializedName


/**
 * Created by Zohre Niayeshi on 07,July,2020 niayesh1993@gmail.com
 **/
open class Error(code: Int, message: String?) {

    @SerializedName("Code")
    var code: Int = 0
    @SerializedName("Message")
    var message: String? = null

    init {

        this.code = code
        this.message = message
    }
}