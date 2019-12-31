package patti.philippe.read_i.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import patti.philippe.read_i.MainActivity
import patti.philippe.read_i.R

abstract class BaseFragment : Fragment(), View.OnClickListener {

    //TODO add profile page in main activity, with possibility to verify email

    protected lateinit var auth: FirebaseAuth
    private var progressBar: ProgressBar? = null
    private var errorTextView: TextView? = null
    protected abstract val TAG:String

    companion object {
        const val EXTRA_USER = "EXTRA_USER"
        const val RqC_SIGN_IN = 100
        const val RsC_SIGN_OUT = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProgressBar()
        initListeners()
    }
    abstract fun initListeners()

    fun setProgressBar() {
        progressBar = view?.findViewById(R.id.progressBar)
    }

    fun showProgressBar() {
        progressBar?.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar?.visibility = View.INVISIBLE
    }

    fun setError(resId: Int){
        errorTextView = view?.findViewById(resId)
    }
    fun showError(tr: Throwable){
        errorTextView?.visibility = View.VISIBLE
        errorTextView?.text = resources.getString(R.string.sign_in_error, tr.message)
    }
    fun hideError(){
        errorTextView?.visibility = View.GONE
    }

    override fun onStop() {
        hideProgressBar()
        hideError()
        super.onStop()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult")
        when (resultCode) {
            RsC_SIGN_OUT -> {
                signOut()
            }
        }
    }

    open protected fun signIn(){
        Log.d(TAG, "signing In")
    }

    open protected fun signOut() {
        Log.d(TAG, "signing Out")
        auth.signOut()
    }

    protected fun signedIn(user: FirebaseUser) {
        Log.d(TAG, "signedIn")
        context?.let {
            val intent = Intent(requireContext(), MainActivity::class.java).apply {
                putExtra(EXTRA_USER, user)
            }
            startActivityForResult(intent, RqC_SIGN_IN)
        }
    }
}