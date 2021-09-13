package se.linerotech.quizkampen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
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

class ProfilePage : AppCompatActivity() {
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private var myButtonPlay: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        myButtonPlay = findViewById(R.id.buttonPlay)
        myButtonPlay?.setOnClickListener {

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