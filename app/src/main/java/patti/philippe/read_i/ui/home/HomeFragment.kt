package patti.philippe.read_i.ui.home

import android.Manifest
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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.fragment_home.*
import patti.philippe.read_i.R
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: DisasterViewModel
    private val REQUEST_PERMISSION_LOCATION = 10
    lateinit var mLastLocation: Location
    internal lateinit var mLocationRequest: LocationRequest
    private val INTERVAL: Long = 2000
    private val FASTEST_INTERVAL: Long = 1000
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null

    lateinit var btnStartupdate: Button
    lateinit var btnStopUpdates: Button
    lateinit var txtLat: TextView
    lateinit var txtLong: TextView
    lateinit var txtTime: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = DisasterListAdapter(requireContext())
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        recyclerview.adapter = adapter
        homeViewModel = ViewModelProviders.of(requireActivity()).get(DisasterViewModel::class.java)
        homeViewModel.allDisasters.observe(this, Observer { disasters ->
            disasters?.let {
                adapter.setAlerts(it)
            }
        })
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            buildAlertMessageNoGps()
        }
        btnStartupdate = view.findViewById(R.id.btn_start_upds)
        btnStopUpdates = view.findViewById(R.id.btn_stop_upds)
        txtLat = view.findViewById(R.id.txtLat)
        txtLong = view.findViewById(R.id.txtLong)
        txtTime = view.findViewById(R.id.txtTime)
        btnStartupdate.setOnClickListener {
            if (checkPermissionForLocation(requireContext())) {
                startLocationUpdates()
                btnStartupdate.isEnabled = false
                btnStopUpdates.isEnabled = true
            }
        }

        btnStopUpdates.setOnClickListener {
            stopLocationUpdates()
            txtTime.text = "Updates Stoped"
            btnStartupdate.isEnabled = true
            btnStopUpdates.isEnabled = false
        }
    }

    fun checkPermissionForLocation(context: Context) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_PERMISSION_LOCATION
                )
                false
            }
        } else {
            true
        }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 11)
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
                dialog.dismiss()
            }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // do work here
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    fun onLocationChanged(location: Location) {
        mLastLocation = location
        val date: Date = Calendar.getInstance().time
        val sdf = SimpleDateFormat("hh:mm:ss a")
        txtTime.text = "Updated at : " + sdf.format(date)
        txtLat.text = "LATITUDE : " + mLastLocation.latitude
        txtLong.text = "LONGITUDE : " + mLastLocation.longitude
    }

    protected fun startLocationUpdates() {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.apply {
            priority = LocationRequest.PRIORITY_LOW_POWER
            interval = INTERVAL
            fastestInterval = FASTEST_INTERVAL
        }

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
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
        mFusedLocationProviderClient!!.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }

    private fun stopLocationUpdates(){
        mFusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)
    }

}
