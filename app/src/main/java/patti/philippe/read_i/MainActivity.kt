package patti.philippe.read_i

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseUser
import patti.philippe.read_i.auth.BaseFragment.Companion.EXTRA_USER
import patti.philippe.read_i.auth.BaseFragment.Companion.RsC_SIGN_OUT

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

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
    }

    override fun onBackPressed() {
        signOut()
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        val view = super.onCreateView(name, context, attrs)

        view?.let {
            //TODO move this to a better spot
            if (intent.hasExtra(EXTRA_USER)) {
                val user: FirebaseUser = intent.getParcelableExtra(EXTRA_USER)!!
                val userName = view.findViewById<TextView>(R.id.user_name)
                val userEmail = view.findViewById<TextView>(R.id.user_email)
                userName.text = user.email?.substringBefore('@')
                userEmail.text = user.email
            }
        }

        return view
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

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun signOut() {
        setResult(RsC_SIGN_OUT)
        finish()
    }
}
