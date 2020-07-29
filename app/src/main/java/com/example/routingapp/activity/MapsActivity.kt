package com.example.routingapp.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.routingapp.R
import com.example.routingapp.model.Venue
import com.example.routingapp.model.database.AppDb
import com.example.routingapp.model.database.LocationEntity
import com.example.routingapp.model.database.VenueEntity
import com.example.routingapp.service.BroadcastReceivers
import com.example.routingapp.service.LocationService
import com.example.routingapp.service.UserService
import com.example.routingapp.utility.Constants
import com.example.routingapp.utility.Error
import com.example.routingapp.utility.SettingsManager
import com.example.routingapp.utility.Utils
import com.example.routingapp.utility.api.ApiCallbackListener
import com.example.routingapp.utility.api.ApiResultModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.util.*
import com.google.android.material.snackbar.Snackbar


/**
 * Created by Zohre Niayeshi on 07,July,2020 niayesh1993@gmail.com
 **/
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, BroadcastReceivers.BroadcastListener{

    private lateinit var mMap: GoogleMap
    private var Venues: MutableList<Venue>? = null
    private var location: Location? = null
    var userService: UserService? = null
    var utils: Utils? = null
    private var snackbar: Snackbar? = null
    var Markers: MutableList<Marker> = ArrayList()
    var isGPSEnable = false
    var isNetworkEnable = false
    private var locationManager: LocationManager? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var Current_Location: Location? = null
    var receiver:BroadcastReceivers = BroadcastReceivers()
    internal var myDialog: Dialog? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        utils = Utils(this)
        userService = UserService(this)
        Venues = ArrayList()
        myDialog = Dialog(this)
        val filter = IntentFilter()
        filter.addAction(BroadcastReceivers().ACTION_LOCATION_SERVICE)
        filter.addAction(BroadcastReceivers().ACTION_NEW_LOCATION)
        registerReceiver(receiver, filter)
        receiver.broadcastReceiver = this

        //SetUp Snackbar
        snackbar = Snackbar.make(
            findViewById(R.id.drawer_layout),
            R.string.general_no_internet_connection,
            Snackbar.LENGTH_LONG
        )
        snackbar!!.setAction("", null)
        snackbar!!.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.orangeRed))

        val view = snackbar!!.getView()
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params
        val tv = view.findViewById(R.id.snackbar_text) as TextView

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tv.textAlignment = View.TEXT_ALIGNMENT_CENTER
        } else {
            tv.gravity = Gravity.CENTER_HORIZONTAL
        }
    }

    override fun onResume() {
        super.onResume()
        if (!utils!!.isMyServiceRunning(LocationService ::class.java))
        {
            val i = Intent(this@MapsActivity, LocationService::class.java)
            startService(i)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnMarkerClickListener(this)
        val originLocation = LatLng(
            SettingsManager.getDouble(Constants().PREF_LOC_LAT),
            SettingsManager.getDouble(Constants().PREF_LOC_LON)
        )
        val originMarker = mMap.addMarker(
            MarkerOptions()
                .position(originLocation)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.origin_mark))
                .title(R.string.origin.toString())
        )
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 14.0f))
        getLastLocation()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {

        if (utils!!.isGpsLocationEnabled())
        {
            fusedLocationClient!!.lastLocation
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful && task.result != null) {
                        Current_Location = task.result
                        checkDistance(Current_Location)

                    } else {
                        locationManager =
                            applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                        isGPSEnable = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
                        isNetworkEnable = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                        location = null
                        if (location == null) {
                            if (locationManager!!.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
                                locationManager!!.requestSingleUpdate(
                                    LocationManager.NETWORK_PROVIDER,
                                    object : LocationListener {
                                        override fun onLocationChanged(location: Location) {}
                                        override fun onStatusChanged(
                                            provider: String,
                                            status: Int,
                                            extras: Bundle
                                        ) {
                                        }

                                        override fun onProviderEnabled(provider: String) {}
                                        override fun onProviderDisabled(provider: String) {}
                                    },
                                    null
                                )
                                if (locationManager != null) {
                                    location =
                                        locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                                    if (location != null) {
                                        Current_Location = location
                                        checkDistance(Current_Location)
                                    }
                                }
                            }
                        }

                    }
                }
        }else
        {
            showLocationPopup()
            Fetch_from_database()

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkDistance(currentLocation: Location?) {
        val distance: Double = distance(
            currentLocation!!.latitude,
            currentLocation!!.longitude,
            SettingsManager.getDouble(Constants().PREF_LOC_LAT),
            SettingsManager.getDouble(Constants().PREF_LOC_LON)
        )
        if (distance >= Constants().CONFIG_LOCATION_DISTANCE)
        {
            SettingsManager.setValue(Constants().PREF_LOC_LAT, currentLocation!!.latitude)
            SettingsManager.setValue(Constants().PREF_LOC_LON, currentLocation!!.longitude)
            val originLocation = LatLng(
                currentLocation!!.getLatitude(),
                currentLocation!!.getLongitude()
            )
            val originMarker = mMap.addMarker(
                MarkerOptions()
                    .position(originLocation)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.origin_mark))
                    .title(R.string.origin.toString())
            )
            val ll =
                currentLocation!!.getLatitude().toString() + "," + currentLocation!!.getLongitude().toString()
            get_venue(ll)
        }
        else if (distance < Constants().CONFIG_LOCATION_DISTANCE)
        {
            val originLocation = LatLng(
                SettingsManager.getDouble(Constants().PREF_LOC_LAT),
                SettingsManager.getDouble(Constants().PREF_LOC_LON)
            )
            val originMarker = mMap.addMarker(
                MarkerOptions()
                    .position(originLocation)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.origin_mark))
                    .title(R.string.origin.toString())
            )
            Fetch_from_database()
        }
    }

    private fun distance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun get_venue(ll: String?) {
        if (utils!!.isNetworkAvailable()) {
            if (snackbar!!.isShown())
                snackbar!!.dismiss()
            try {
                userService!!.searsh_venue(ll, object : ApiCallbackListener {
                    override fun onSucceed(data: ApiResultModel) {
                        if (data.meta?.getCode() === 200) {
                            if (data!!.response!!.groups!![0]?.items!!.size > 0) {
                                for (item in data!!.response!!.groups!![0]?.items!!)
                                {
                                    item.venue?.let { Venues?.add(it) }
                                }

                                Show_venue(Venues!!)
                                Add_to_database(Venues!!)
                            }
                        }
                    }

                    override fun onError(errors: MutableList<Error>?) {}
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {

            snackbar!!.show()

        }
    }

    fun Show_venue(Venues: List<Venue>) {
        try {
            for (i in Venues.indices) {
                if (Venues[i].location != null)
                {
                    createMarker(
                        Venues[i].location?.lat!!,
                        Venues[i].location?.lng!!, Venues[i].id!!
                    )
                }

            }
        } catch (e: Exception) {
            e.message
        }
        Move_Camera()
    }

    protected fun createMarker(
        latitude: Double,
        longitude: Double,
        title: String?
    ): Marker? {
        val m = mMap.addMarker(
            MarkerOptions()
                .position(
                    LatLng(
                        latitude,
                        longitude
                    )
                ).title(title).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
        )
        Markers.add(m)
        return m
    }

    fun Move_Camera() {
        val builder = LatLngBounds.Builder()
        for (marker in Markers) {
            builder.include(marker.position)
        }
        try {
            val bounds = builder.build()
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, 0)
            mMap.moveCamera(cu)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        val id: String = p0!!.getTitle()
        gotoDetail(id)
        return false
    }


    private fun gotoDetail(id: String)
    {
        val detail = Intent(this@MapsActivity, VenueDetailActivity::class.java)
        detail.putExtra("VenueId", id)
        startActivity(detail)
    }

    fun Add_to_database(VenueList: List<Venue>)
    {
        Clear_Database()

        val thread = Thread {
            var Venue_db =
                Room.databaseBuilder(applicationContext, AppDb::class.java, "VenueDB").build()

            var Location_db =
                Room.databaseBuilder(applicationContext, AppDb::class.java, "LocationDB").build()

            try {
                for (item: Venue in VenueList) {

                        var venueEntity = VenueEntity()
                        venueEntity.ID = (0..1000).random()
                        venueEntity.VenueId = item.id!!
                        venueEntity.Vname = item.name!!
                        venueEntity.hasPerk = item.hasPerk!!
                        venueEntity.referralId = item.referralId!!
                        venueEntity.verified = item.verified!!
                        Venue_db.venueDAO().saveVenues(venueEntity)

                    if (item.location != null)
                    {
                            var locationEntity = LocationEntity()
                            locationEntity.ID = (0..100).random()
                            locationEntity.LocationId = item.id!!
                            locationEntity.address = item.location!!.address!!
                            locationEntity.cc = item.location!!.cc!!
                            locationEntity.city = item.location!!.city!!
                            locationEntity.country = item.location!!.country!!
                            locationEntity.distance = item.location!!.distance
                            locationEntity.lat = item.location!!.lat!!
                            locationEntity.lng = item.location!!.lng!!
                            locationEntity.postalCode = item.location!!.postalCode!!
                            locationEntity.state = item.location!!.state!!
                            Location_db.locationDAO().saveLocation(locationEntity)

                    }

                }
            } catch (e: Exception) {
            }

        }
        thread.start()
        SettingsManager.setValue(Constants().PREF_FETCH_DATA, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun Fetch_from_database()
    {
        val thread = Thread{
            var Venue_db =
                Room.databaseBuilder(applicationContext, AppDb::class.java, "VenueDB").build()

            var Location_db =
                Room.databaseBuilder(applicationContext, AppDb::class.java, "LocationDB").build()


            try {
                if (Venue_db.venueDAO().getAllVenues().size>1)
                {
                    Venue_db.venueDAO().getAllVenues().forEach(){
                        var venue = Venue()
                        venue.id = it.VenueId
                        venue.name = it.Vname
                        venue.referralId = it.referralId
                        venue.hasPerk = it.hasPerk

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

                            venue.location = location
                        }
                        Venues?.add(venue)
                    }
                }else
                {
                    val ll =
                        SettingsManager.getDouble(Constants().PREF_LOC_LAT).toString() + "," + SettingsManager.getDouble(Constants().PREF_LOC_LON).toString()
                    get_venue(ll)
                }

            } catch (e: Exception) {
            }

        }
        thread.start()
        thread.join()
        Venues?.let { Show_venue(it) }

    }

    fun Clear_Database()
    {
        val thread = Thread{
            var Venue_db =
                Room.databaseBuilder(applicationContext, AppDb::class.java, "VenueDB").build()

            var Location_db =
                Room.databaseBuilder(applicationContext, AppDb::class.java, "LocationDB").build()

            Venue_db.venueDAO().DeleteVenue()
            Location_db.locationDAO().DeleteLocation()
        }
        thread.start()
        thread.join()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBroadcastReceiver(intent: Intent) {
        val action = if (intent == null) "" else intent.action
        if (BroadcastReceivers().ACTION_NEW_LOCATION.equals(action))
        {
            getLastLocation()
        }
    }

    fun showLocationPopup()
    {
        val Ok: TextView
        myDialog!!.setContentView(R.layout.location_popup)
        Ok = myDialog!!.findViewById(R.id.message_icon)

        myDialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog!!.show()
    }
}
