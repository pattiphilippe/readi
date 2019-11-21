package patti.philippe.read_i.ui.home

import android.content.Intent
import com.google.android.gms.location.*

import android.Manifest;
import android.app.Activity
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.LiveData
import kotlin.properties.Delegates

class LocationController(val fragment: HomeFragment) {

    private val INTERVAL: Long = 20000
    private val FASTEST_INTERVAL: Long = 10000
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mLastLocation: LiveData<Location?> = LiveData<Location>(null)

    fun checkPermissionForLocation(context:Context, activity: Activity) =
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


    fun buildAlertMessageNoGps(context: Context, activity: Activity) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    activity.startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 11)
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.cancel()
                    dialog.dismiss()
                }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult:LocationResult) {
            mLastLocation = locationResult.lastLocation
        }
    }

    fun startLocationUpdates() {
        mLocationRequest.apply {
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            interval = INTERVAL
            fastestInterval = FASTEST_INTERVAL
        }

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(requireActivity())
        settingsClient.checkLocationSettings(locationSettingsRequest)

        mFusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireActivity())

        if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mFusedLocationProviderClient?.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback,
                Looper.myLooper()
        )
    }

    fun stopLocationUpdates() {
        mFusedLocationProviderClient?.removeLocationUpdates(mLocationCallback)
    }
}
