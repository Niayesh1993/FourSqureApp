package com.example.routingapp.caller

import com.example.routingapp.utility.api.ApiResultModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * Created by Zohre Niayeshi on 07,July,2020 niayesh1993@gmail.com
 **/
interface UserCaller {

    @GET("v2/venues/explore")
    fun Venue_explore(
        @Query("client_id") client_id: String?, @Query(
            "client_secret"
        ) client_secret: String?, @Query("v") v: String?, @Query(
            "ll"
        ) ll: String?
    ): Call<ApiResultModel?>?

    @GET("v2/venues/{VENUE_ID}")
    fun Venue_Detail(
        @Path("VENUE_ID") account_id: String?, @Query(
            "client_id"
        ) client_id: String?, @Query("client_secret") client_secret: String?, @Query(
            "v"
        ) v: String?
    ): Call<ApiResultModel?>?

}