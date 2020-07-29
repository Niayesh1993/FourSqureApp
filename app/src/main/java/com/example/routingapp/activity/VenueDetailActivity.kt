package com.example.routingapp.activity

import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.room.Room
import com.example.routingapp.R
import com.example.routingapp.model.Location
import com.example.routingapp.model.Venue
import com.example.routingapp.model.database.AppDb
import com.example.routingapp.service.UserService
import com.example.routingapp.utility.Error
import com.example.routingapp.utility.Utils
import com.example.routingapp.utility.api.ApiCallbackListener
import com.example.routingapp.utility.api.ApiResultModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.String

/**
 * Created by Zohre Niayeshi on 07,July,2020 niayesh1993@gmail.com
 **/

class VenueDetailActivity : FragmentActivity(), OnMapReadyCallback {

    var name_txt: TextView? = null
    var city_txt: TextView? = null
    var country_txt: TextView? = null
    var lat_txt: TextView? = null
    var lng_txt: TextView? = null
    var venue: Venue? = null
    var VenueId: kotlin.String? = null
    var V_location: Location? = null
    private lateinit var mMap: GoogleMap
    var userService: UserService? = null
    var utils: Utils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue_detail)

        utils = Utils(this)
        userService = UserService(this)


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.detail_map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        name_txt = findViewById(R.id.txt_name)
        city_txt = findViewById(R.id.city_Txt)
        country_txt = findViewById(R.id.country_Txt)
        lat_txt = findViewById(R.id.lat_Txt)
        lng_txt = findViewById(R.id.lng_Txt)
        venue = Venue()
        V_location = Location()

        if (intent != null) {
            VenueId = intent.getStringExtra("VenueId")
            get_venue_detail(VenueId)
        }
    }

    fun get_venue_detail(id: kotlin.String?) {
        if (utils!!.isNetworkAvailable()) {
            userService!!.get_venue_detail(id, object : ApiCallbackListener {
                override fun onSucceed(data: ApiResultModel) {
                    if (data.meta!!.getCode() === 200) {
                        if (data.response!!.venue != null) {
                            data.response!!.venue?.let { initView(it) }
                        }
                    }
                }

                override fun onError(errors: MutableList<Error>?) {

                }
            })
        } else {
            Fetch_from_database_withId(VenueId)
        }
    }

    private fun Fetch_from_database_withId(venueId: kotlin.String?) {

        val thread = Thread{
            var Venue_db =
                Room.databaseBuilder(applicationContext, AppDb::class.java, "VenueDB").build()

            var Location_db =
                Room.databaseBuilder(applicationContext, AppDb::class.java, "LocationDB").build()

                if (venueId != null) {
                    Venue_db.venueDAO().selectVenue(venueId).forEach(){
                        venue!!.id = it.VenueId
                        venue!!.name = it.Vname
                        venue!!.referralId = it.referralId
                        venue!!.hasPerk = it.hasPerk

                        Location_db.locationDAO().selectLocation(it.VenueId).forEach(){
                            var location = com.example.routingapp.model.Location()
                            location.address = it.address
                            location.cc = it.cc
                            location.city = it.city
                            location.country = it.country
                            location.distance = it.distance
                            location.lat = it.lat
                            location.lng = it.lng
                            location.state = it.state
                            location.postalCode = it.postalCode

                            venue!!.location = location
                        }
                    }
                }

        }
        thread.start()
        thread.join()
        venue?.let { initView(it) }
    }

    fun initView(venue: Venue)
    {
        V_location = venue.location
        name_txt!!.setText(venue!!.name)
        city_txt!!.setText(V_location!!.city.toString() + "-" + V_location!!.country)
        country_txt!!.setText(V_location!!.address)
        lat_txt!!.setText(String.valueOf(V_location!!.lat))
        lng_txt!!.setText(String.valueOf(V_location!!.lng))
    }

    override fun onMapReady(p0: GoogleMap) {

        mMap = p0

        if (mMap != null && V_location?.lat != null && V_location?.lng != null)
        {
                val customerLocation = LatLng(
                    V_location!!.lat!!,
                    V_location!!.lng!!
                )

                val customerMarker = mMap!!.addMarker(
                    MarkerOptions()
                        .position(customerLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                )
                mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(customerLocation, 14.0f))

        }

    }
}
