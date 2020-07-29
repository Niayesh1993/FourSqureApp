package com.example.routingapp.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * Created by Zohre Niayeshi on 07,July,2020 niayesh1993@gmail.com
 **/
class Location(): Serializable, Parcelable {
    @SerializedName("address")
    @Expose
    var address: String = ""
    @SerializedName("crossStreet")
    @Expose
    var crossStreet: String = ""
    @SerializedName("lat")
    @Expose
    var lat: Double = 0.0
    @SerializedName("lng")
    @Expose
    var lng: Double = 0.0
    @SerializedName("distance")
    @Expose
    var distance:Double = 0.0
    @SerializedName("postalCode")
    @Expose
    var postalCode: String = ""
    @SerializedName("cc")
    @Expose
    var cc: String = ""
    @SerializedName("city")
    @Expose
    var city: String = ""
    @SerializedName("state")
    @Expose
    var state: String = ""
    @SerializedName("country")
    @Expose
    var country: String = ""
    @SerializedName("formattedAddress")
    @Expose
    var formattedAddress: List<String>? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {

        p0?.writeString(address)
        p0?.writeString(city)
        p0?.writeString(cc)
        p0?.writeString(country)
        lat?.let { p0?.writeDouble(it) }
        lng?.let { p0?.writeDouble(it) }


    }

    override fun describeContents(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object CREATOR : Parcelable.Creator<Location> {
        override fun createFromParcel(parcel: Parcel): Location {
            return Location(parcel)
        }

        override fun newArray(size: Int): Array<Location?> {
            return arrayOfNulls(size)
        }
    }


}