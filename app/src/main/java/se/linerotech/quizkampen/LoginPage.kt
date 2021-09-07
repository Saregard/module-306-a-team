package se.linerotech.quizkampen

import android.content.Intent
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
    private var buttonClickToCreateAccount: Button? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.buttonLogin)
        buttonClickToCreateAccount = findViewById(R.id.buttonlickToCreateAccount)
        val userAuthentication = FirebaseAuth.getInstance()
                loginButton?.setOnClickListener{
            Log.d("MainActivity", "Button Clicked")
        }

        buttonClickToCreateAccount?.setOnClickListener{
            Log.d("MainActivity", "Create account")
            val intent = Intent(this, CreateAccount::class.java)
            startActivity(intent)
        }

    }
}
