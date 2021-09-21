package se.linerotech.quizkampen


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myquizgame.Backends.GetQuestions
import com.example.myquizgame.RetrofitClient
import com.example.myquizgame.models.Qustions
import com.example.myquizgame.models.Result
import com.example.myquizgame.models.Token
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.activity_create_account.goBackToLoginScreen
import kotlinx.android.synthetic.main.activity_profile_page.*
import retrofit2.Call
import retrofit2.Callback
import se.linerotech.quizkampen.GameActivity.Companion.QUIZ_DATA
import se.linerotech.quizkampen.databinding.ActivityProfilePageBinding

class ProfilePage : AppCompatActivity() {

    private lateinit var binding: ActivityProfilePageBinding
    private val db = Firebase.firestore
    private val auth = Firebase.auth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonPlay.setOnClickListener {
            getMyToken()


        }
        buttonLogOut.setOnClickListener{
            val bIntent = Intent (this, LoginPage::class.java)
            startActivity(bIntent)

        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun getMyToken() {
        RetrofitClient
            .instance
            .getToken("request")
            .enqueue(object : Callback<Token> {
                override fun onResponse(call: Call<Token>, response: retrofit2.Response<Token>) {
                    if (response.isSuccessful) {

                        getQuestions(response.body()!!.token)
                    }
                }
                override fun onFailure(call: Call<Token>, t: Throwable) {
                }

            })
    }

    private fun getQuestions(myToken: String) {

        GetQuestions
            .instance
            .getQuestions("10", myToken)
            .enqueue(object : Callback<Qustions> {
                override fun onResponse(
                    call: Call<Qustions>,
                    response: retrofit2.Response<Qustions>
                ) {
                    if (response.isSuccessful) {

                        val listOfRepos = response.body()?.results as? ArrayList<Result>
                        listOfRepos?.let {
                            val intent = Intent(this@ProfilePage, GameActivity::class.java)
                            intent.putParcelableArrayListExtra(QUIZ_DATA,it)
                            startActivity(intent)
                        }


                    }
                }

                override fun onFailure(call: Call<Qustions>, t: Throwable) {

                }
            })
    }

}