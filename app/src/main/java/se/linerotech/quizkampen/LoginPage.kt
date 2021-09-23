package se.linerotech.quizkampen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import se.linerotech.quizkampen.databinding.ActivityLoginPageBinding
import java.lang.Exception
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginPage : AppCompatActivity() {

    private lateinit var binding: ActivityLoginPageBinding
    private val auth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val currentUser = auth.currentUser
        if (currentUser!=null && currentUser.isEmailVerified){
            val intent = Intent(this, ProfilePage::class.java)
            startActivity(intent)
            finish()

        } else {
        authentication()
        clicklistener()

        }
     }

    private fun authentication(){
        val auth = Firebase.auth
        binding.buttonLogin.setOnClickListener{
            val email = binding.editTextEmail.text.toString().trim{ it <= ' ' }
            val password = binding.editTextPassword.text.toString().trim{ it <= ' ' }

            if (!TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.editTextEmail.error = "Needs to be and email"
            }
            if (TextUtils.isEmpty(email)) {
                binding.editTextEmail.error = "Email can not be empty"
            }
            if (TextUtils.isEmpty(password)) {
                binding.editTextPassword.error = "Password can not be empty"
            }
            if (!TextUtils.isEmpty(password) && TextUtils.isEmpty(email)) {
                binding.editTextEmail.error = "Email can not be empty"
            }
            if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                binding.editTextEmail.error = "Email can not be empty"
                binding.editTextPassword.error = "Password can not be empty"
            }
            else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            if (auth.currentUser!!.isEmailVerified) {
                                val intent = Intent(this, ProfilePage::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                        if (!task.isSuccessful) {
                            try {
                                throw task.exception!!
                            } catch (e: FirebaseAuthInvalidCredentialsException) {
                                binding.editTextPassword.error = "Invalid Password"
                            } catch (e: FirebaseAuthInvalidUserException) {
                                binding.editTextEmail.error = "Invalid email"
                            } catch (e: Exception) {
                                Log.w("Fail", "signInWithEmail:failure", task.exception)
                            }
                        }
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
