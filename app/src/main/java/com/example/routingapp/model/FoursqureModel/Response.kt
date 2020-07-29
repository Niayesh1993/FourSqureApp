package com.example.routingapp.model.FoursqureModel

import com.example.routingapp.model.Venue
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by Zohre Niayeshi on 07,July,2020 niayesh1993@gmail.com
 **/
class Response {

    @SerializedName("suggestedFilters")
    @Expose
    var suggestedFilters: Any? = null

    @SerializedName("suggestedRadius")
    @Expose
    var suggestedRadius = 0

    @SerializedName("headerLocation")
    @Expose
    var headerLocation: String? = null

    @SerializedName("headerFullLocation")
    @Expose
    var headerFullLocation: String? = null

    @SerializedName("headerLocationGranularity")
    @Expose
    var headerLocationGranularity: String? = null

    @SerializedName("totalResults")
    @Expose
    var totalResults = 0

    @SerializedName("suggestedBounds")
    @Expose
    var suggestedBounds: Any? = null

    @SerializedName("groups")
    @Expose
    var groups: List<Group?>? = null

    @SerializedName("venue")
    @Expose
    var venue: Venue? = null

}