package patti.philippe.read_i.ui.prepare

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment(layoutId : Int) : Fragment(layoutId), View.OnClickListener{


    protected fun moreInfoWebsite(urlId: Int){
        val url = getString(urlId)
        Log.d("Prepare Fragment", "Activity More info website : $url")
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}