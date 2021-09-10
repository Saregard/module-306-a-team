package se.linerotech.quizkampen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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
        authentication()
        clicklistener()
     }

    private fun authentication(){
        val auth = Firebase.auth
        loginButton?.setOnClickListener{
            auth.signInWithEmailAndPassword(editTextEmail?.text.toString(), editTextPassword?.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        if ( auth.currentUser!!.isEmailVerified) {
                            val intent = Intent(this, ProfilePage::class.java)
                            startActivity(intent)


                        } else {

                        }
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(this, "login success", Toast.LENGTH_SHORT).show()
                        val user = auth.currentUser


                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Fail", "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun clicklistener(){
        buttonClickToCreateAccount?.setOnClickListener{
            Log.d("MainActivity", "Create account")
            val intent = Intent(this, CreateAccount::class.java)
            startActivity(intent)
        }
    }

}
