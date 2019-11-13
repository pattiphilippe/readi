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
import kotlinx.android.synthetic.main.disaster_recyclerview_item.*
import kotlinx.android.synthetic.main.fragment_home.*
import patti.philippe.read_i.R

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: DisasterViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel = ViewModelProviders.of(requireActivity()).get(DisasterViewModel::class.java)
        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@HomeFragment.requireContext())
            adapter = DisasterListAdapter(this@HomeFragment.requireContext())
        }

    }
}