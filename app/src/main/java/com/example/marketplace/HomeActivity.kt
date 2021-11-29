package com.example.marketplace

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.*
import com.example.marketplace.databinding.ActivityHomeBinding
import com.facebook.login.LoginManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

enum class ProviderType{
    BASIC,
    GOOGLE,
    FACEBOOK
}

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarHome.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //setup Login
        title=resources.getString(R.string.test_Home)
        val bundle =intent.extras
        val email=bundle?.getString("email")
        val provider=bundle?.getString("provider")

        navView.getHeaderView(0).findViewById<TextView>(R.id.emailTextView).text = email;
        navView.getHeaderView(0).findViewById<TextView>(R.id.providerTextView).text=provider;

        //Save Data
        val prefs=getSharedPreferences(resources.getString(R.string.preds_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email",email)
        prefs.putString("provider",provider)
        prefs.apply()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_singoff -> {
                onSignoff();
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    fun onSignoff() {

        //Delete Data
        val prefs=getSharedPreferences(resources.getString(R.string.preds_file), Context.MODE_PRIVATE).edit()
        prefs.clear()
        prefs.apply()

        val navView: NavigationView = binding.navView
        if(navView.findViewById<TextView>(R.id.providerTextView).text == ProviderType.FACEBOOK.name){
            LoginManager.getInstance().logOut()
        }

        FirebaseAuth.getInstance().signOut()
        onBackPressed()
    }

}

