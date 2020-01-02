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
import com.google.firebase.auth.GoogleAuthProvider

import kotlinx.android.synthetic.main.fragment_google_sign_in.button_sign_in_google

import patti.philippe.read_i.R


class GoogleSignInFragment : BaseFragment() {

    override val TAG: String = "GoogleSignInFragment"
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RqC_SIGN_IN_GOOGLE = 500


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
    ) = inflater.inflate(R.layout.fragment_google_sign_in, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setError(R.id.sign_in_google_error)
    }

    override fun initListeners() {
        button_sign_in_google.setOnClickListener(this)
    }

    override fun signIn() {
        super.signIn()
        startActivityForResult(googleSignInClient.signInIntent, RqC_SIGN_IN_GOOGLE)
    }

    override fun signOut() {
        super.signOut()
        googleSignInClient.signOut()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RqC_SIGN_IN_GOOGLE) {
            showProgressBar()
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
                showError(e)
            }
            hideProgressBar()
        }
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
                    showError(task.exception!!)
                }
            }
    }

    override fun onClick(v: View) {
        Log.d(TAG,"on Click in GoogleSignInFragment")
        when (v.id) {
            R.id.button_sign_in_google -> signIn()
            else -> Log.w(TAG, "clicked, but no handling")
        }
    }
}