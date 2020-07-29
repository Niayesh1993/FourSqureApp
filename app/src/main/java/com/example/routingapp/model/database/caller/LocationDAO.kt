package com.example.routingapp.model.database.caller

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.routingapp.model.database.LocationEntity


/**
 * Created by Zohre Niayeshi on 09,July,2020 niayesh1993@gmail.com
 **/
@Dao
interface LocationDAO {

    @Insert
    fun saveLocation(locationEntity: LocationEntity)

    @Query("Select * from LocationEntity WHERE LocationId =:id")
    fun selectLocation(id: String): List<LocationEntity>

    @Query("DELETE FROM LocationEntity")
    fun DeleteLocation()
}