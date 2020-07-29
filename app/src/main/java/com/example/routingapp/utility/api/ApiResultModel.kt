package com.example.routingapp.utility.api


import com.example.routingapp.model.FoursqureModel.Meta
import com.example.routingapp.model.FoursqureModel.Response
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by Zohre Niayeshi on 07,July,2020 niayesh1993@gmail.com
 **/
class ApiResultModel {

    @SerializedName("meta")
    var meta: Meta? = null

    @SerializedName("response")
    @Expose
    var response: Response? = null

}