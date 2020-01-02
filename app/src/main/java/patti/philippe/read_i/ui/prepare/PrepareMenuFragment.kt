package patti.philippe.read_i.ui.prepare

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_prepare_menu.*
import patti.philippe.read_i.R

class PrepareMenuFragment : Fragment(R.layout.fragment_prepare_menu), View.OnClickListener {

    private val TAG = "PrepareMenuFragment"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepare_menu_hurricane_button.setOnClickListener(this)
        prepare_menu_extreme_heat_button.setOnClickListener(this)
        prepare_menu_flood_button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        Log.d(TAG, "onClick handler")
        var action = when (v?.id) {
            R.id.prepare_menu_hurricane_button ->
                PrepareMenuFragmentDirections.actionNavPrepareToHurricaneFragment()
            R.id.prepare_menu_extreme_heat_button ->
                PrepareMenuFragmentDirections.actionNavPrepareToFragmentExtremeHeat()
            R.id.prepare_menu_flood_button ->
                PrepareMenuFragmentDirections.actionNavPrepareToFragmentFlood()
            else -> null
        }
        if (action != null && v != null) {
            Log.d(TAG, "switching to new fragment")
            v.findNavController().navigate(action)
        }
    }

}