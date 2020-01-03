package patti.philippe.read_i

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseUser
import patti.philippe.read_i.auth.BaseFragment.Companion.EXTRA_USER
import patti.philippe.read_i.auth.BaseFragment.Companion.RsC_SIGN_OUT

class MainActivity : AppCompatActivity() {

    //TODO use drawable values

    //TODO press back closes navigation drawer

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mainViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        mainViewModel = MainViewModel(application)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_prepare, R.id.nav_evacuate,
                R.id.nav_refuges, R.id.nav_donate, R.id.nav_profile, R.id.nav_language
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        if (intent.hasExtra(EXTRA_USER)) {
            val user: FirebaseUser = intent.getParcelableExtra(EXTRA_USER)!!
            mainViewModel.currentUser = user
            updateViewUser()
        }
        updateViewUser()

    }

    private fun updateViewUser() {
        val navView: NavigationView = findViewById(R.id.nav_view)
        val user = mainViewModel.currentUser!!
        val hView = navView.getHeaderView(0)
        val userName = hView.findViewById<TextView>(R.id.user_name)
        val userEmail = hView.findViewById<TextView>(R.id.user_email)
        userName.text = user.email?.substringBefore('@')
        userEmail.text = user.email
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                //TODO define action settings in menu
            }
            R.id.action_sign_out -> {
                signOut()
            }
            R.id.nav_prepare -> {
                println("COULD ALSO DO IT HERE BEFORE NAVIGATE")
            }
            else -> println(" clicked but not handled")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun signOut() {
        setResult(RsC_SIGN_OUT)
        finish()
    }

    //TODO PAGE ABOUT : ICONS BY ICONS8
}
