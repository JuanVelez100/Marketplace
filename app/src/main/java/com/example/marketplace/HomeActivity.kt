package com.example.marketplace

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth

enum class ProviderType{
    BASIC,
    GOOGLE,
    FACEBOOK
}

class HomeActivity : AppCompatActivity() {


    private var emailTextView: TextView? = null;
    private var providerTextView: TextView? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        emailTextView = findViewById(R.id.emailTextView);
        providerTextView = findViewById(R.id.providerTextView);

        //setup
        val bundle =intent.extras
        val email=bundle?.getString("email")
        val provider=bundle?.getString("provider")
        setup(email?:"",provider?:"")

        //Save Data
        val prefs=getSharedPreferences(resources.getString(R.string.preds_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email",email)
        prefs.putString("provider",provider)
        prefs.apply()

    }

    private fun setup(email:String,provider:String) {
        title=resources.getString(R.string.test_Home)
        emailTextView?.text=email
        providerTextView?.text=provider
    }

    fun onSignoff(view: android.view.View) {

        //Delete Data
        val prefs=getSharedPreferences(resources.getString(R.string.preds_file), Context.MODE_PRIVATE).edit()
        prefs.clear()
        prefs.apply()

        if(providerTextView?.text == ProviderType.FACEBOOK.name){
            LoginManager.getInstance().logOut()
        }

        FirebaseAuth.getInstance().signOut()
        onBackPressed()
    }

}