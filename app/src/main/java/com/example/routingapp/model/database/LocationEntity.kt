package com.example.routingapp.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Created by Zohre Niayeshi on 09,July,2020 niayesh1993@gmail.com
 **/
@Entity
class LocationEntity {

    @PrimaryKey(autoGenerate = true)
    var ID: Int = 0

    @ColumnInfo
    var LocationId: String = ""

    @ColumnInfo(name = "address")
    var address: String = ""

    @ColumnInfo(name = "lat")
    var lat: Double = 0.0

    @ColumnInfo(name = "lng")
    var lng: Double = 0.0

    @ColumnInfo(name = "distance")
    var distance: Double = 0.0

    @ColumnInfo(name = "postalCode")
    var postalCode: String = ""

    @ColumnInfo(name = "cc")
    var cc: String = ""

    @ColumnInfo(name = "city")
    var city: String = ""

    @ColumnInfo(name = "state")
    var state: String = ""

    @ColumnInfo(name = "country")
    var country: String = ""

}