package patti.philippe.read_i.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

import kotlinx.android.synthetic.main.activity_welcome.main_layout
import kotlinx.android.synthetic.main.fragment_google_sign_in.button_sign_in_google

import patti.philippe.read_i.MainActivity
import patti.philippe.read_i.R
import patti.philippe.read_i.WelcomeActivity.Companion.EXTRA_USER
import patti.philippe.read_i.WelcomeActivity.Companion.RqC_SIGN_IN
import patti.philippe.read_i.WelcomeActivity.Companion.RsC_SIGN_OUT


class GoogleSignInFragment : BaseFragment(), View.OnClickListener {

    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        const val TAG = "GoogleSignInFragment"
        const val RqC_SIGN_IN_GOOGLE = 500
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_google_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProgressBar(R.id.progressBar)
        button_sign_in_google.setOnClickListener(this)
    }

    private fun signIn() {
        Log.d(TAG, "signing in with Google")
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RqC_SIGN_IN_GOOGLE)
    }

    private fun signOut() {
        if (::googleSignInClient.isInitialized) {
            Log.d(TAG, "signing out from Google")
            googleSignInClient.signOut()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        println("in onActivityResult in GoogleSignInFragment")
        if (requestCode == RqC_SIGN_IN_GOOGLE) {
            showProgressBar()
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
                //TODO show error sign in
            }
            hideProgressBar()
        }
        when (resultCode) {
            RsC_SIGN_OUT -> {
                signOut()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    signedIn(auth.currentUser!!)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Snackbar.make(main_layout, "Authentication Failed.", Snackbar.LENGTH_SHORT)
                        .show()
                    //TODO show error sign in
                }
            }
    }

    private fun signedIn(user: FirebaseUser) {
        Log.d(TAG, "signing in with Google succeeded")
        context?.let {
            val intent = Intent(requireContext(), MainActivity::class.java).apply {
                putExtra(EXTRA_USER, user)
            }
            startActivityForResult(intent, RqC_SIGN_IN)
        }
    }

    override fun onClick(v: View) {
        println("on Click in GoogleSignInFragment")
        when (v.id) {
            R.id.button_sign_in_google -> signIn()
            else -> println("clicked, but no handling")
        }
    }
}