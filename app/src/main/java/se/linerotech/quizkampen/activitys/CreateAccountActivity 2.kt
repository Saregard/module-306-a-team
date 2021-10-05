package se.linerotech.quizkampen.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_create_account.*

import se.linerotech.quizkampen.databinding.ActivityCreateAccountBinding
import se.linerotech.quizkampen.utils.GamePlayAccess
import java.lang.Exception

class CreateAccount : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountBinding
    private var validation = GamePlayAccess()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        accountCreation()

        goBackToLoginScreen.setOnClickListener{
            val bIntent = Intent (this, LoginPageActivity::class.java)
            startActivity(bIntent)


        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun accountCreation() {
        val auth = Firebase.auth
        binding.buttonSignUp.setOnClickListener {
            val email = binding.editTextSignUpEmail.text.toString().trim(){ it <= ' ' }
            val password = binding.editTextSignUpPassword.text.toString().trim(){ it <= ' ' }
            if(validation.validation(email, binding.editTextSignUpPassword, password, binding.editTextSignUpEmail))
             {
                auth.createUserWithEmailAndPassword(
                    binding.editTextSignUpEmail.text.toString(),
                    binding.editTextSignUpPassword.text.toString()
                )
                    .addOnCompleteListener(this) { task ->

                        if (task.isSuccessful && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            // Sign in success, update UI with the signed-in user's information
                            auth.currentUser!!.sendEmailVerification()
                            Toast.makeText(this, "Success create", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this, LoginPageActivity::class.java)
                            Log.d("Sign in", "createUserWithEmail:success")
                            startActivity(intent)
                            finish()

                        }
                        if (!task.isSuccessful) {
                            try {
                                throw task.exception!!
                            } catch (e: FirebaseAuthWeakPasswordException) {
                                binding.editTextSignUpPassword.error = "Weak Password"
                            } catch (e: FirebaseAuthUserCollisionException) {
                                binding.editTextSignUpEmail.error = "This email is already in use"
                            } catch (e: Exception) {
                                Log.w("Failed", "createUserWithEmail:failure", task.exception)
                                Toast.makeText(this, "Authentication failed.",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }
                    }
            }
        }
    }
}