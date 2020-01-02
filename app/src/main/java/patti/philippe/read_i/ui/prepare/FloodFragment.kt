package patti.philippe.read_i.ui.prepare

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_flood.flood_info_btn
import patti.philippe.read_i.R

class FloodFragment : BaseFragment(R.layout.fragment_flood){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        flood_info_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.flood_info_btn) {
            moreInfoWebsite(R.string.flood_info_website)
        }
    }
}