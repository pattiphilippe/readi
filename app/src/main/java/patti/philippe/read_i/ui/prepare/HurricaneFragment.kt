package patti.philippe.read_i.ui.prepare

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_hurricane.*
import patti.philippe.read_i.R

class HurricaneFragment : BaseFragment(R.layout.fragment_hurricane){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hurr_info_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.hurr_info_btn) {
            moreInfoWebsite(R.string.hurr_info_website)
        }
    }
}