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
import android.os.Looper
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.gms.location.*
import kotlin.properties.Delegates

class LocationController(context: Context, activity: Activity) {


    private val INTERVAL: Long = 20000
    private val FASTEST_INTERVAL: Long = 10000
    private val mLocationRequest = LocationRequest()
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private var mLastLocation: Location by Delegates.observable(Location("")) { property, oldValue, newValue ->
        mAdapter?.setLocation(newValue)
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            mLastLocation = locationResult.lastLocation
        }
    }

    //TODO companion object and "static" methods" for all the methods in this class?

    init {
        mLocationRequest
        val locationManager =
            activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps(activity, context)
        }
    }

    fun checkPermissionForLocation(context: Context, activity: Activity) =
        //TODO check following line
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_PERMISSION_LOCATION
                )
                false
            }
        } else {
            true
        }


    private fun buildAlertMessageNoGps(context: Context, activity: Activity) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                startActivityForResult(activity, Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 11)
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
                dialog.dismiss()
            }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    fun startLocationUpdates(context: Context, activity: Activity) {
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
                context,
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
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
    }

}