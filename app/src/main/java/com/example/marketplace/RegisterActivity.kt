package com.example.marketplace

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    private var editUserName: EditText? = null
    private var editPassword: EditText? = null
    private var editName: EditText? = null
    private var editLastName: EditText? = null
    private var editMobile: EditText? = null
    private var stTerms: Switch? = null

    var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editUserName = findViewById(R.id.editUserName);
        editPassword = findViewById(R.id.editPassword);
        editName = findViewById(R.id.editName);
        editLastName = findViewById(R.id.editLastName);
        editMobile = findViewById(R.id.editMobile);
        stTerms = findViewById(R.id.stTerms);

        title = resources.getString(R.string.test_register)

    }

    fun onRegister(view: android.view.View) {

        var username = editUserName!!.text.toString();
        var password = editPassword!!.text.toString();
        var name = editName!!.text.toString();
        var lastname = editLastName!!.text.toString();
        var mobile = editMobile!!.text.toString();
        var terms = stTerms!!.isChecked;

        if (Validation(username, password,name,lastname,mobile,terms)) {

            //Save Auth
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {

                        //Save User
                        db.collection("user").document(username).set(
                            hashMapOf("password" to password,
                                "name" to name,
                                "lastname" to lastname,
                                "mobile" to mobile,
                                "terms" to terms))


                        showHome(username, ProviderType.BASIC)
                    } else {
                        getToast(resources.getString(R.string.test_errorAuth));
                    }
                }

        }else{
            getToast(resources.getString(R.string.test_ValidateError));
        }

    }

    private fun Validation(username: String, password: String,name: String,lastname: String,mobile: String,terms: Boolean): Boolean {

        //Reset
        editUserName!!.setBackground(resources.getDrawable(R.drawable.customborderok))
        var editUserNameLayout = findViewById<TextInputLayout>( R.id.editUserNameLayout)
        editUserNameLayout!!.setHint(resources.getString(R.string.test_userExample))

        editPassword!!.setBackground(resources.getDrawable(R.drawable.customborderok))
        var editPasswordLayout = findViewById<TextInputLayout>( R.id.editPasswordLayout)
        editPasswordLayout!!.setHint(resources.getString(R.string.test_passwordExample))

        editName!!.setBackground(resources.getDrawable(R.drawable.customborderok))
        var editNameLayout = findViewById<TextInputLayout>( R.id.editNameLayout)
        editNameLayout!!.setHint(resources.getString(R.string.test_NameExample))

        editLastName!!.setBackground(resources.getDrawable(R.drawable.customborderok))
        var editLastNameLayout = findViewById<TextInputLayout>( R.id.editLastNameLayout)
        editLastNameLayout!!.setHint(resources.getString(R.string.test_LastNameExample))

        editMobile!!.setBackground(resources.getDrawable(R.drawable.customborderok))
        var editMobileLayout = findViewById<TextInputLayout>( R.id.editMobileLayout)
        editMobileLayout!!.setHint(resources.getString(R.string.test_MobileExample))

        stTerms!!.setBackground(resources.getDrawable(R.drawable.customborderok))

        //Regex
        val uppercase: Pattern = Pattern.compile("[A-Z]")
        val lowercase: Pattern = Pattern.compile("[a-z]")
        val digit: Pattern = Pattern.compile("[0-9]")
        val character: Pattern = Pattern.compile("[!#\$%&'*+/=?^_`{|}~-]")
        val email: Pattern = Pattern.compile("^[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\$")

        //Validation
        var validation : Boolean = true;

        if (name.isEmpty()) {
            editNameLayout!!.setHint(resources.getString(R.string.test_EmptyName))
            editName!!.setBackground(resources.getDrawable(R.drawable.custombordererror))
            validation=false
        }

        if (lastname.isEmpty()) {
            editLastNameLayout!!.setHint(resources.getString(R.string.test_EmptyLastName))
            editLastName!!.setBackground(resources.getDrawable(R.drawable.custombordererror))
            validation=false
        }

        if (mobile.isEmpty()) {
            editMobileLayout!!.setHint(resources.getString(R.string.test_EmptyMobile))
            editMobile!!.setBackground(resources.getDrawable(R.drawable.custombordererror))
            validation=false
        }

        if (!terms){
            stTerms!!.setBackground(resources.getDrawable(R.drawable.custombordererror))
            validation=false
        }

        //Validate Password
        if (password.isEmpty()) {
            editPasswordLayout!!.setHint(resources.getString(R.string.test_EmptyPassword))
            editPassword!!.setBackground(resources.getDrawable(R.drawable.custombordererror))
            validation=false

        }else{
            if (password.length < 8) {
                editPasswordLayout!!.setHint(resources.getString(R.string.test_minimum8))
                editPassword!!.setBackground(resources.getDrawable(R.drawable.custombordererror))
                validation=false

            }else{
                if (!lowercase.matcher(password).find()) {
                    editPasswordLayout!!.setHint(resources.getString(R.string.test_lowercase))
                    editPassword!!.setBackground(resources.getDrawable(R.drawable.custombordererror))
                    validation=false

                }else{
                    if (!uppercase.matcher(password).find()) {
                        editPasswordLayout!!.setHint(resources.getString(R.string.test_uppercase))
                        editPassword!!.setBackground(resources.getDrawable(R.drawable.custombordererror))
                        validation=false
                    }else{

                        if (!digit.matcher(password).find()){
                            editPasswordLayout!!.setHint(resources.getString(R.string.test_digit))
                            editPassword!!.setBackground(resources.getDrawable(R.drawable.custombordererror))
                            validation=false

                        }else{
                            if (!character.matcher(password).find()){
                                editPasswordLayout!!.setHint(resources.getString(R.string.test_character))
                                editPassword!!.setBackground(resources.getDrawable(R.drawable.custombordererror))
                                validation=false
                            }
                        }
                    }
                }
            }
        }

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

        return validation;
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
            Toast.LENGTH_SHORT
        ).show();
    }

    fun onReturnLogin(view: android.view.View) {
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
        getToast(resources.getString(R.string.test_login));
    }

    fun onTerms(view: android.view.View) {

        AlertDialog.Builder(this)
            .setTitle(resources.getString(R.string.test_TermsLink))
            .setMessage(resources.getString(R.string.test_TermsMessage))
            .setPositiveButton(resources.getString(R.string.test_ok),positiveButton)
            .setNegativeButton(resources.getString(R.string.test_cancel),negativeButton)
            .create().show();

    }

    val positiveButton={ _: DialogInterface, _:Int->
        stTerms!!.setChecked(true);
    }

    val negativeButton={ _: DialogInterface, _:Int->
        stTerms!!.setChecked(false);
    }


}