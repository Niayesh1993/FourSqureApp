package com.example.routingapp.model.database.caller

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.routingapp.model.database.VenueEntity


/**
 * Created by Zohre Niayeshi on 09,July,2020 niayesh1993@gmail.com
 **/
@Dao
interface VenueDAO {

    @Insert
    fun saveVenues(venueEntity: VenueEntity)

    @Query(value = "Select * from VenueEntity")
    fun getAllVenues() : List<VenueEntity>

    @Query("Select * from VenueEntity WHERE VenueId =:id")
    fun selectVenue(id: String): List<VenueEntity>

    @Query("DELETE FROM VenueEntity")
    fun DeleteVenue()
}