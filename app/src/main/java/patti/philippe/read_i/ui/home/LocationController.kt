package patti.philippe.read_i.ui.home

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*

class LocationController(activity: Activity) {


    private val INTERVAL: Long = 20000
    private val FASTEST_INTERVAL: Long = 10000
    private val mLocationRequest = LocationRequest()
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    val mLastLocation: MutableLiveData<Location> = MutableLiveData()

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            mLastLocation.value = locationResult.lastLocation
        }
    }

    //TODO ? companion object and "static" methods" for all the methods in this class?

    init {
        mLocationRequest
        val locationManager =
            activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps(activity)
        }
    }

    fun checkPermissionForLocation(activity: Activity) =
        //TODO check following line
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    HomeFragment.REQUEST_PERMISSION_LOCATION
                )
                false
            }
        } else {
            true
        }


    private fun buildAlertMessageNoGps(activity: Activity) {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                startActivityForResult(activity, Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                    11, Bundle.EMPTY)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
                dialog.dismiss()
            }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    fun startLocationUpdates(activity: Activity) {
        mLocationRequest.apply {
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            interval = INTERVAL
            fastestInterval = FASTEST_INTERVAL
        }

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(activity)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(activity)

        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mFusedLocationProviderClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }

    fun stopLocationUpdates() {
        if(::mFusedLocationProviderClient.isInitialized) {
            mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
        }
    }

}