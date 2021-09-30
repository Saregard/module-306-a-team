package se.linerotech.quizkampen.activitys

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.activity_profile_page.*
import se.linerotech.quizkampen.databinding.ActivityProfilePageBinding
import se.linerotech.quizkampen.utils.GamePlayAccess

class ProfilePageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfilePageBinding
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUserDetails()
        val myScore = intent.getIntExtra(GameActivity.SCORE, 0)
        binding.score.text = "Latest Score: $myScore/10"
        binding.buttonPlay.setOnClickListener {
            val gamePlayAccess = GamePlayAccess()
            val intent = Intent(this, GameActivity::class.java)
            gamePlayAccess.getToken(this, this@ProfilePageActivity, intent, savedInstanceState)
        }
        binding.changeInfo.setOnClickListener {
            changeUserName()
        }
        binding.textViewUserEmail.text = auth.currentUser!!.email
        binding.buttonLogOut.setOnClickListener {
            val bIntent = Intent(this, LoginPageActivity::class.java)
            startActivity(bIntent)
            Firebase.auth.signOut()
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun changeUserName() {
        db.collection("users")
            .document(auth.currentUser!!.email.toString())
            .set(binding.editTextName.text.toString() to binding.editTextPhone.text.toString())
            .addOnSuccessListener { documentReference ->
            }
            .addOnFailureListener { e ->
            }
        loadUserDetails()
    }

    private fun loadUserDetails() {
        db.collection("users")
            .document(auth.currentUser!!.email.toString())
            .get()
            .addOnSuccessListener { result ->
                binding.playerName.text = result.data!!.values.first().toString()
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
}
