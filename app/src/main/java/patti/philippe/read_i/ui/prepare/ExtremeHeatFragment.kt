package patti.philippe.read_i.ui.prepare

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_extreme_heat.heat_info_btn
import patti.philippe.read_i.R

class ExtremeHeatFragment : BaseFragment(R.layout.fragment_extreme_heat) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        heat_info_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.heat_info_btn) {
            moreInfoWebsite(R.string.heat_info_website)
        }
    }
}