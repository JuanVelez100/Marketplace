package com.example.marketplace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class RecoverActivity : AppCompatActivity() {

    private var editUserName: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover)
        editUserName = findViewById(R.id.editUserName);
    }

    fun onReturnLogin(view: android.view.View) {
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
        getToast(resources.getString(R.string.test_login));
    }

    fun onRecovery(view: android.view.View) {
        var username = editUserName!!.text.toString();

        if (Validation(username)) {
            FirebaseAuth.getInstance().sendPasswordResetEmail(username) .addOnCompleteListener {
                if (it.isSuccessful) {

                    val loginIntent = Intent(this, LoginActivity::class.java)
                    startActivity(loginIntent)
                    getToast(resources.getString(R.string.test_login));

                } else {
                    getToast(resources.getString(R.string.test_errorAuth));
                }
            }

        }else{
            getToast(resources.getString(R.string.test_ValidateError));
        }

    }

    private fun Validation(username: String): Boolean {

        //Reset
        editUserName!!.setBackground(resources.getDrawable(R.drawable.customborderok))
        var editUserNameLayout = findViewById<TextInputLayout>(R.id.editUserNameLayout)
        editUserNameLayout!!.setHint(resources.getString(R.string.test_userExample))

        //Regex
        val email: Pattern = Pattern.compile("^[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\$")

        //Validation
        var validation : Boolean = true;

        //Validation Email
        if (username.isEmpty()) {
            editUserNameLayout!!.setHint(resources.getString(R.string.test_EmptyMail))
            editUserName!!.setBackground(resources.getDrawable(R.drawable.custombordererror))
            validation=false
        }else{
            if (!email.matcher(username).find()){
                editUserNameLayout!!.setHint(resources.getString(R.string.test_emailerror))
                editUserName!!.setBackground(resources.getDrawable(R.drawable.custombordererror))
                validation=false
            }
        }

        return  validation
    }

    private fun getToast(message: String) {
        Toast.makeText(
            applicationContext,
            message,
            Toast.LENGTH_SHORT
        ).show();
    }

}