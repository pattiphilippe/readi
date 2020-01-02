package patti.philippe.read_i.ui.home

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*
import patti.philippe.read_i.R

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var homeViewModel: DisasterViewModel
    private lateinit var mLocationController: LocationController

    companion object {
        val REQUEST_PERMISSION_LOCATION = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLocationController = LocationController(requireActivity())
        homeViewModel = ViewModelProviders.of(this).get(DisasterViewModel::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = AlertsAdapter(requireContext())
        recyclerView.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(requireContext())

        homeViewModel.allDisasters.observe(this, Observer { disasters ->
            disasters?.let { adapter.setDisasters(it) }
        })

        mLocationController.mLastLocation.observe(this, Observer {
            adapter.setLocation(it)
        })
    }


    override fun onResume() {
        super.onResume()
        if (mLocationController.checkPermissionForLocation(requireActivity())) {
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
