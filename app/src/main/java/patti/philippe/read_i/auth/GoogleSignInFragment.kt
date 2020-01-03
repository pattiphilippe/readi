package patti.philippe.read_i.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_google_sign_in.*
import patti.philippe.read_i.R


class GoogleSignInFragment : BaseFragment(R.layout.fragment_google_sign_in) {

    override val mTag: String = "GoogleSignInFragment"
    private lateinit var googleSignInClient: GoogleSignInClient
    private val _rqcSignInGoogle = 500


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setError(R.id.sign_in_google_error)
    }

    override fun initListeners() {
        button_sign_in_google.setOnClickListener(this)
    }

    override fun signIn() {
        super.signIn()
        startActivityForResult(googleSignInClient.signInIntent, _rqcSignInGoogle)
    }

    override fun signOut() {
        super.signOut()
        googleSignInClient.signOut()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == _rqcSignInGoogle) {
            showProgressBar()
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.w(mTag, "Google sign in failed", e)
                showError(e)
            }
            hideProgressBar()
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(mTag, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(mTag, "signInWithCredential:success")
                    signedIn(auth.currentUser!!)
                } else {
                    Log.w(mTag, "signInWithCredential:failure", task.exception)
                    showError(task.exception!!)
                }
            }
    }

    override fun onClick(v: View) {
        Log.d(mTag,"on Click in GoogleSignInFragment")
        when (v.id) {
            R.id.button_sign_in_google -> signIn()
            else -> Log.w(mTag, "clicked, but no handling")
        }
    }
}