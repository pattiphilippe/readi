package patti.philippe.read_i.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import patti.philippe.read_i.R

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val _tag = "ProfileFragment"

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(_tag, "onCreate")
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(_tag, "onViewCreated")

        val user = profileViewModel.currentUser
        user?.let {
            val userNameTxt: TextView = view.findViewById(R.id.user_name)
            val userEmailTxt: TextView = view.findViewById(R.id.user_email)
            userEmailTxt.text = user.email
            userNameTxt.text = user.email?.substringBefore('@')
        }
    }
}