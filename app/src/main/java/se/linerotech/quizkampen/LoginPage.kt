package se.linerotech.quizkampen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class LoginPage : AppCompatActivity() {

    private var editTextEmail: EditText? = null
    private var editTextPassword: EditText? = null
    private var loginButton: Button? = null
    private var forgotPasswordTextView: TextView? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.buttonLogin)
        forgotPasswordTextView = findViewById(R.id.textViewForgotPassword)
        val userAuthentication = FirebaseAuth.getInstance()
                loginButton?.setOnClickListener{
            Log.d("MainActivity", "Button Clicked")
        }

        forgotPasswordTextView?.setOnClickListener{
            Log.d("MainActivity", "Forgot Password Clicked")
        }
    }
}
