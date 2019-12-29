package patti.philippe.read_i.auth

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

open class BaseFragment : Fragment(){

    protected lateinit var auth : FirebaseAuth
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    fun setProgressBar(resId: Int) {
        progressBar = view?.findViewById(resId)
    }

    fun showProgressBar() {
        progressBar?.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar?.visibility = View.INVISIBLE
    }

    fun hideKeyboard(view: View) {
        activity?.let {
            val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onStop() {
        super.onStop()
        hideProgressBar()
    }
}