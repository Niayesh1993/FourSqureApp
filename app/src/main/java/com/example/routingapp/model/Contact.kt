package com.example.routingapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by Zohre Niayeshi on 07,July,2020 niayesh1993@gmail.com
 **/
class Contact {

    @SerializedName("phone")
    @Expose
    private var phone: String? = null
    @SerializedName("formattedPhone")
    @Expose
    private var formattedPhone: String? = null

    fun getPhone(): String? {
        return phone
    }

    fun getFormattedPhone(): String? {
        return formattedPhone
    }

    fun setPhone(phone: String?) {
        this.phone = phone
    }

    fun setFormattedPhone(formattedPhone: String?) {
        this.formattedPhone = formattedPhone
    }
}