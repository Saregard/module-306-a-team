package se.linerotech.quizkampen.Activitys

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import se.linerotech.quizkampen.databinding.ActivityLoginPageBinding
import se.linerotech.quizkampen.utils.GamePlayAccess
import java.lang.Exception

class LoginPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginPageBinding
    private var validation = GamePlayAccess()
    private val auth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val currentUser = auth.currentUser
        if (currentUser != null && currentUser.isEmailVerified) {
            val intent = Intent(this, ProfilePageActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            authentication()
            clicklistener()
        }
    }

    private fun authentication() {
        val auth = Firebase.auth
        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim { it <= ' ' }
            val password = binding.editTextPassword.text.toString().trim { it <= ' ' }
           if(validation.validation(email, binding.editTextPassword, password, binding.editTextEmail))
              {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            if (auth.currentUser!!.isEmailVerified) {
                                val intent = Intent(this, ProfilePageActivity::class.java)
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

    private fun clicklistener() {
        binding.buttonlickToCreateAccount.setOnClickListener {
            Log.d("MainActivity", "Create account")
            val intent = Intent(this, CreateAccount::class.java)
            startActivity(intent)
        }
    }
}
