package se.linerotech.quizkampen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import se.linerotech.quizkampen.databinding.ActivityLoginPageBinding

class LoginPage : AppCompatActivity() {

    private lateinit var binding: ActivityLoginPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authentication()
        clicklistener()
     }

    private fun authentication(){
        val auth = Firebase.auth
        binding.buttonLogin.setOnClickListener{
            auth.signInWithEmailAndPassword(binding.editTextEmail.text.toString(), binding.editTextPassword.text.toString())
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
        binding.buttonlickToCreateAccount.setOnClickListener{
            Log.d("MainActivity", "Create account")
            val intent = Intent(this, CreateAccount::class.java)
            startActivity(intent)
        }
    }

}
