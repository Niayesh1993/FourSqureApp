package com.example.routingapp.model.FoursqureModel

import com.example.routingapp.model.Venue
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by Zohre Niayeshi on 13,July,2020 niayesh1993@gmail.com
 **/
class Item {

    @SerializedName("reasons")
    @Expose
    var reasons: Any? = null

    @SerializedName("venue")
    @Expose
    var venue: Venue? = null

    @SerializedName("referralId")
    @Expose
    var referralId: String? = null

}