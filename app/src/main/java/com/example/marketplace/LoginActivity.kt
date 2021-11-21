package com.example.marketplace

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

import com.facebook.login.widget.LoginButton

import java.lang.Exception


class LoginActivity : AppCompatActivity() {

    private var editUserName: EditText? = null
    private var editPassword: EditText? = null

    private val GOOGLE_SING_IN = 100
    private val callbackManager = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {

        Thread.sleep(2000)
        setTheme(R.style.Theme_Marketplace)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString(
            resources.getString(R.string.fire_base),
            resources.getString(R.string.fire_base_message)
        )
        analytics.logEvent(resources.getString(R.string.go_markerplace), bundle)

        editUserName = findViewById(R.id.editUserName);
        editPassword = findViewById(R.id.editPassword);

        title = resources.getString(R.string.test_login)

        session()
    }

    override fun onStart() {
        super.onStart()

        val prefs =
            getSharedPreferences(resources.getString(R.string.preds_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)

        if (email == null && provider == null) {
            var loginLayout = findViewById<LinearLayout>(R.id.loginLayout);
            loginLayout.visibility = View.VISIBLE
        }


    }

    private fun session() {
        val prefs =
            getSharedPreferences(resources.getString(R.string.preds_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)

        if (email != null && provider != null) {
            var loginLayout = findViewById<LinearLayout>(R.id.loginLayout);
            loginLayout.visibility = View.INVISIBLE

            showHome(email, ProviderType.valueOf(provider))
        }

    }

    fun onLogin(view: android.view.View) {
        var username = editUserName!!.text.toString();
        var password = editPassword!!.text.toString();

        if (username.isNotEmpty() && password.isNotEmpty()) {

            FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        showHome(username, ProviderType.BASIC)
                    } else {
                        getToast(resources.getString(R.string.test_errorAuth));
                    }
                }

        } else {
            getToast(resources.getString(R.string.test_errorlogin));
        }

    }

    fun onRegister(view: android.view.View) {
        val registerIntent = Intent(this, RegisterActivity::class.java)
        startActivity(registerIntent)
        getToast(resources.getString(R.string.test_register));
    }

    private fun showHome(username: String, provider: ProviderType) {

        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", username)
            putExtra("provider", provider.toString())
        }

        startActivity(homeIntent)

        getToast(resources.getString(R.string.test_welcome));
    }

    private fun getToast(message: String) {
        Toast.makeText(
            applicationContext,
            message,
            Toast.LENGTH_LONG
        ).show();
    }

    fun googleLogin(view: android.view.View) {

        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(resources.getString(R.string.default_web_client_id2))
            .requestEmail().build()

        val googleClient = GoogleSignIn.getClient(this, googleConf)
        googleClient.signOut()
        startActivityForResult(googleClient.signInIntent, GOOGLE_SING_IN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SING_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val credencial = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credencial)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                showHome(account.email ?: "", ProviderType.GOOGLE)
                            } else {
                                getToast(resources.getString(R.string.test_errorAuth));
                            }
                        }
                }
            } catch (e: ApiException) {
                getToast(resources.getString(R.string.test_errorAuth));
            }


        }
    }

    fun facebookLogin(view: android.view.View) {

        FacebookSdk.sdkInitialize(this);
        Log.d("AppLog", "key:" + FacebookSdk.getApplicationSignature(this)+"=");

        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))

        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

                override fun onSuccess(result: LoginResult?) {

                    result?.let {

                        val token = it.accessToken
                        val credencial = FacebookAuthProvider.getCredential(token.token)

                        FirebaseAuth.getInstance().signInWithCredential(credencial)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    showHome(it.result?.user?.email ?: "", ProviderType.FACEBOOK)
                                } else {
                                    getToast(resources.getString(R.string.test_errorAuth));
                                }
                            }

                    }
                }

                override fun onCancel() {

                }

                override fun onError(error: FacebookException?) {
                    getToast(resources.getString(R.string.test_errorAuth));
                }

            })
    }

}