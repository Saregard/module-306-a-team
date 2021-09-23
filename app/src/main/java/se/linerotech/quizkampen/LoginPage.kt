package se.linerotech.quizkampen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import se.linerotech.quizkampen.databinding.ActivityLoginPageBinding
import java.lang.Exception
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider

class LoginPage : AppCompatActivity() {

    private lateinit var binding: ActivityLoginPageBinding
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        googleSignIn()
        authentication()
        clicklistener()
        binding.googleLogin.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
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
    //google sign in
    private fun googleSignIn(){

        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase

                val account = task.getResult(ApiException::class.java)!!
                Log.d("e", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
                Toast.makeText(this, "${account.idToken} : Token , ${account.email}:email", Toast.LENGTH_SHORT).show()
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("e", "Google sign in failed", e)
                Toast.makeText(this, "Failed with ui", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this, "Google successful", Toast.LENGTH_SHORT).show()
                    Log.d("e", "signInWithCredential:success")
                    val user = auth.currentUser
                    checkUserLogin(user!!.isEmailVerified)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("e", "signInWithCredential:failure", task.exception)

                }
            }


    }
    private fun checkUserLogin(userString:Boolean){
        if (userString){
            val intent = Intent(this, ProfilePage::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Please verify your email!", Toast.LENGTH_SHORT).show()
        }
    }


    companion object{
        private const val RC_SIGN_IN=100

    }
}
