package se.linerotech.quizkampen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_create_account.*

import se.linerotech.quizkampen.databinding.ActivityCreateAccountBinding
import java.lang.Exception

class CreateAccount : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountBinding
    val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        accountCreation()

        binding.goBackToLoginScreen.setOnClickListener{
//            val bIntent = Intent (this, LoginPage::class.java)
//            startActivity(bIntent)
            finish()


        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun accountCreation() {


        binding.buttonSignUp.setOnClickListener {
            val email = binding.editTextSignUpEmail.text.toString().trim(){ it <= ' ' }
            val password = binding.editTextSignUpPassword.text.toString().trim(){ it <= ' ' }

            if (!TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.editTextSignUpEmail.error = "Needs to be and email"
            }
            if (TextUtils.isEmpty(email)) {
                binding.editTextSignUpEmail.error = "Email can not be empty"
            }
            if (TextUtils.isEmpty(password)) {
                binding.editTextSignUpPassword.error = "Password can not be empty"
            } else {
                auth.createUserWithEmailAndPassword(
                    binding.editTextSignUpEmail.text.toString(),
                    binding.editTextSignUpPassword.text.toString()
                )
                    .addOnCompleteListener(this) { task->
                        if (task.isSuccessful && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            // Sign in success, update UI with the signed-in user's information
                            createSuccess()
                        }
                        if (!task.isSuccessful) {
                           createFail(task)
                        }
                    }
            }
        }
    }
    private fun createSuccess(){
        auth.currentUser!!.sendEmailVerification()
        Toast.makeText(this, "Success create", Toast.LENGTH_SHORT).show()
        finish()
    }
    private fun createFail(task:Task<AuthResult>) {
        try {
            throw task.exception!!
        } catch (e: FirebaseAuthWeakPasswordException) {
            binding.editTextSignUpPassword.error = "Weak Password"
        } catch (e: FirebaseAuthUserCollisionException) {
            binding.editTextSignUpEmail.error = "This email is already in use"
        } catch (e: Exception) {
            Log.w("Failed", "createUserWithEmail:failure", task.exception)
            Toast.makeText(
                baseContext, "Authentication failed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}