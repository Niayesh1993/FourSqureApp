package com.example.routingapp.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Created by Zohre Niayeshi on 09,July,2020 niayesh1993@gmail.com
 **/
@Entity
class VenueEntity {

    @PrimaryKey(autoGenerate = true)
    var ID: Int = 0

    @ColumnInfo(name = "venueId")
    var VenueId: String = ""

    @ColumnInfo(name = "name")
    var Vname: String = ""

    @ColumnInfo(name = "verified")
    var verified: Boolean = false

    @ColumnInfo(name = "referralId")
    var referralId: String = ""

    @ColumnInfo(name = "hasPerk")
    var hasPerk: Boolean = false

}