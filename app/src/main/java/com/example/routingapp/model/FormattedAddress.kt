package com.example.routingapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by Zohre Niayeshi on 07,July,2020 niayesh1993@gmail.com
 **/
class FormattedAddress {

    @SerializedName("0")
    @Expose
    private var A0: String? = null
    @SerializedName("1")
    @Expose
    private var A1: String? = null
    @SerializedName("2")
    @Expose
    private var A2: String? = null

    fun getA0(): String? {
        return A0
    }

    fun getA1(): String? {
        return A1
    }

    fun getA2(): String? {
        return A2
    }

    fun setA0(a0: String?) {
        A0 = a0
    }

    fun setA1(a1: String?) {
        A1 = a1
    }

    fun setA2(a2: String?) {
        A2 = a2
    }
}