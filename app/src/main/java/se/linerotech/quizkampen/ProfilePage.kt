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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import se.linerotech.quizkampen.GamePlayActivity.Companion.QUIZ_DATA
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
    }

    private fun getMyToken() {
        RetrofitClient
            .instance
            .getToken("request")
            .enqueue(object : Callback<Token> {
                override fun onResponse(
                    call: Call<Token>,
                    response: Response<Token>
                ) {
                    if (response.isSuccessful) {

                        getQuestions(response.body()!!.token)

                    }
                }

                override fun onFailure(call: Call<Token>, t: Throwable) {

                }
            })
    }

    private fun getQuestions(myToken: String) {
        //"79a71d870561286a2aeb29eb11d443676bbf7784fbf158c8d8ff7d9a07eb4e4f"
        GetQuestions
            .instance
            .getQuestions("10", myToken)
            .enqueue(object : Callback<Qustions> {
                override fun onResponse(
                    call: Call<Qustions>,
                    response: Response<Qustions>
                ) {
                    if (response.isSuccessful) {

                        val listOfRepos = response.body()?.results as? ArrayList<Result>
                        listOfRepos?.let {
                            val intent = Intent(this@ProfilePage, GamePlayActivity::class.java)
                            intent.putParcelableArrayListExtra(
                                QUIZ_DATA,
                                it
                            )
                            startActivity(intent)
                        }


                    }
                }

                override fun onFailure(call: Call<Qustions>, t: Throwable) {

                }
            })
    }

}