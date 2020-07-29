package com.example.routingapp.model.FoursqureModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by Zohre Niayeshi on 13,July,2020 niayesh1993@gmail.com
 **/
class Group {

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("items")
    @Expose
    var items: List<Item>? = null
}