package patti.philippe.read_i.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.disaster_recyclerview_item.*
import kotlinx.android.synthetic.main.fragment_home.*
import patti.philippe.read_i.R

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: DisasterViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        homeViewModel.allDisasters.observe(this, Observer { disasters ->
            println("disasters changed : in changelistener of HomeFragment")
            disasters?.forEach { println("$it") }
            disasters?.let {
                fusedLocationClient.lastLocation.addOnSuccessListener {
                    println("lastlocation success")
                    println("location : $it")
                    adapter.setAlerts(disasters, it)
                }
            }
        })
    }
}
