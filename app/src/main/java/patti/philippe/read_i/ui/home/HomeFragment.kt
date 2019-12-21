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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.fragment_home.*
import patti.philippe.read_i.R
import kotlin.properties.Delegates

class HomeFragment : Fragment() {

    private var mAdapter:DisasterListAdapter? = null
    private lateinit var homeViewModel: DisasterViewModel
    private lateinit var mLocationController: LocationController
    companion object{
        val REQUEST_PERMISSION_LOCATION = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLocationController = LocationController(requireActivity())
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
            println("DISASTERS CHANGING")
            mAdapter?.setDisasters(disasters, requireContext())
        })
        mLocationController.mLastLocation.observe(this, Observer { location ->
            println("LOCATION CHANGING")
            mAdapter?.setLocation(location)
        })
    }


    override fun onResume() {
        super.onResume()
        if (mLocationController.checkPermissionForLocation(requireActivity())){
            mLocationController.startLocationUpdates(requireActivity())
        }
    }

    override fun onPause() {
        super.onPause()
        mLocationController.stopLocationUpdates()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationController.startLocationUpdates(requireActivity())
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
