package se.linerotech.quizkampen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CreateAccount : AppCompatActivity() {
    private var mySignUpButton: Button? = null
    private var myPassword: EditText? = null
    private var myEmail: EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        mySignUpButton = findViewById(R.id.buttonSignUp)
        myPassword = findViewById(R.id.editTextSignUpPassword)
        myEmail = findViewById(R.id.editTextSignUpEmail)
        val auth = Firebase.auth

        mySignUpButton?.setOnClickListener{
            Toast.makeText(this, myEmail?.text.toString(), Toast.LENGTH_SHORT).show()

            auth.createUserWithEmailAndPassword(myEmail?.text.toString(), myPassword?.text.toString())
                .addOnCompleteListener(this) { task ->
                    Toast.makeText(this, "Success create", Toast.LENGTH_SHORT).show()
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        auth.currentUser!!.sendEmailVerification()

                        val intent = Intent(this, LoginPage::class.java)
                        Log.d("Sign in", "createUserWithEmail:success")
                        startActivity(intent)
                        finish()


                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Failed", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()

                    }
                }

        }


    }

}