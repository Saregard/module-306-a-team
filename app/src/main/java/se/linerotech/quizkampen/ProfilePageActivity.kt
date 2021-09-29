package se.linerotech.quizkampen


import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myquizgame.RetrofitClient
import com.example.myquizgame.models.Qustions
import com.example.myquizgame.models.Result
import com.example.myquizgame.models.Token
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.activity_profile_page.*
import retrofit2.Call
import retrofit2.Callback
import se.linerotech.quizkampen.GameActivity.Companion.QUIZ_DATA
import se.linerotech.quizkampen.databinding.ActivityProfilePageBinding

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
            getMyToken()
        }
        binding.changeInfo.setOnClickListener {
            changeUserName()
        }
        binding.textViewUserEmail.text = auth.currentUser!!.email
        binding.buttonLogOut.setOnClickListener{
            val bIntent = Intent (this, LoginPageActivity::class.java)
            startActivity(bIntent)
            Firebase.auth.signOut()
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun changeUserName(){
        db.collection("users")
            .document(auth.currentUser!!.email.toString())
            .set(binding.editTextName.text.toString() to binding.editTextPhone.text.toString())
            .addOnSuccessListener { documentReference ->
            }
            .addOnFailureListener { e ->
            }
       loadUserDetails()
    }
    private fun loadUserDetails(){
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
    private fun getMyToken() {
        RetrofitClient
            .instance
            .getToken("request")
            .enqueue(object : Callback<Token> {
                override fun onResponse(call: Call<Token>, response: retrofit2.Response<Token>) {
                    if (response.isSuccessful) {

                        getQuestions(response.body()!!.token)
                    } else {
                        Toast.makeText(this@ProfilePageActivity, "Couldn't recieve data", Toast.LENGTH_SHORT).show()
                        Log.e(ResultActivity.FAIL,response.errorBody()!!.string())
                    }
                }
                override fun onFailure(call: Call<Token>, t: Throwable) {
                    Toast.makeText(this@ProfilePageActivity, "Couldn't recieve data", Toast.LENGTH_SHORT).show()
                    Log.e(ResultActivity.FAIL,t.message.toString())
                }

            })
    }

    private fun getQuestions(myToken: String) {
        val numberOfQuestions="10"
        RetrofitClient
            .instanceTwo
            .getQuestions(numberOfQuestions, myToken)
            .enqueue(object : Callback<Qustions> {
                override fun onResponse(
                    call: Call<Qustions>,
                    response: retrofit2.Response<Qustions>
                ) {
                    if (response.isSuccessful) {

                        val listOfRepos = response.body()?.results as? ArrayList<Result>
                        listOfRepos?.let {
                            val intent = Intent(this@ProfilePageActivity, GameActivity::class.java)
                            intent.putParcelableArrayListExtra(QUIZ_DATA,it)
                            startActivity(intent)
                        }


                    } else {
                        Toast.makeText(this@ProfilePageActivity, "Couldn't recieve data", Toast.LENGTH_SHORT).show()
                        Log.e(ResultActivity.FAIL,response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<Qustions>, t: Throwable) {
                    Toast.makeText(this@ProfilePageActivity, "Couldn't recieve data", Toast.LENGTH_SHORT).show()
                    Log.e(ResultActivity.FAIL,t.message.toString())
                }
            })
    }

}