package patti.philippe.read_i.ui.home

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.fragment_home.*
import patti.philippe.read_i.R
import patti.philippe.read_i.ui.home.LocationController.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: DisasterViewModel
    private var mAdapter:DisasterListAdapter? = null
    private val REQUEST_PERMISSION_LOCATION = 10
    private lateinit var locationController:LocationController
    private lateinit var mLocationRequest: LocationRequest


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationController = LocationController(this)
        mLocationRequest = LocationRequest()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = DisasterListAdapter(requireContext())
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        recyclerview.adapter = mAdapter
        homeViewModel = ViewModelProviders.of(requireActivity()).get(DisasterViewModel::class.java)
        homeViewModel.allDisasters.observe(this, Observer { disasters ->
            disasters?.let {
                mAdapter?.setDisasters(it)
            }
        })

        val locationManager =
                requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationController.buildAlertMessageNoGps(requireContext(), requireActivity())
        }
    }

    ///////////////////////////////LOCATION SHIT///////////////////////////////////


    override fun onResume() {
        super.onResume()
        if (locationController.checkPermissionForLocation(requireContext())) locationController.startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        locationController.stopLocationUpdates()
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationController.startLocationUpdates()
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateLocation(location: Location){
        mAdapter?.setLocation(location)
    }

}
