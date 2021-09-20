package se.linerotech.quizkampen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentContainerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import se.linerotech.quizkampen.databinding.ActivityCreateAccountBinding

class  CreateAccount : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = Firebase.auth

        binding.buttonSignUp.setOnClickListener{
            Toast.makeText(this, binding.editTextSignUpEmail.text.toString(), Toast.LENGTH_SHORT).show()

            auth.createUserWithEmailAndPassword(binding.editTextSignUpEmail.text.toString(), binding.editTextSignUpPassword.text.toString())
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


    //TODO create function


}