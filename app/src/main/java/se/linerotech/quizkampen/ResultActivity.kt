package se.linerotech.quizkampen


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myquizgame.RetrofitClient
import com.example.myquizgame.models.Question
import com.example.myquizgame.models.Result
import com.example.myquizgame.models.Token
import kotlinx.android.synthetic.main.activity_profile_page.*
import retrofit2.Call
import retrofit2.Callback
import se.linerotech.quizkampen.GameActivity.Companion.SCORE


import se.linerotech.quizkampen.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityResultBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val userScore = intent.getIntExtra(SCORE, 0)
        binding.score.text = "Score $userScore/10"
        binding.buttonPlay.setOnClickListener {
            gameQuestion()
        }
        binding.buttonLogOut.setOnClickListener {
//            val bIntent = Intent(this, ProfilePage::class.java)
//            startActivity(bIntent)
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    private fun gameQuestion() {
        RetrofitClient
            .instance
            .getToken("request")
            .enqueue(object : Callback<Token> {
                override fun onResponse(call: Call<Token>, response: retrofit2.Response<Token>) {
                    if (response.isSuccessful) {
                        questions(response.body()!!.token)
                    } else {
                        Toast.makeText(this@ResultActivity, "Couldn't recieve data", Toast.LENGTH_SHORT).show()
                        Log.e(FAIL,response.errorBody()!!.string())
                    }
                }
                override fun onFailure(call: Call<Token>, t: Throwable) {
                    Toast.makeText(this@ResultActivity, "Couldn't recieve data", Toast.LENGTH_SHORT).show()
                    Log.e(FAIL,t.message.toString())
                }
            })
    }

    private fun questions(myToken: String) {
        val numberOfQuestions="10"
        RetrofitClient
            .instanceTwo
            .getQuestions(numberOfQuestions, myToken)
            .enqueue(object : Callback<Question> {
                override fun onResponse(
                    call: Call<Question>,
                    response: retrofit2.Response<Question>
                ) {
                    if (response.isSuccessful) {
                        val listOfRepos = response.body()?.results as? ArrayList<Result>
                        listOfRepos?.let {
                            val intent = Intent(this@ResultActivity, GameActivity::class.java)
                            intent.putParcelableArrayListExtra(GameActivity.QUIZ_DATA, it)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        Toast.makeText(this@ResultActivity, "Couldn't recieve data", Toast.LENGTH_SHORT).show()
                        Log.e(FAIL,response.errorBody()!!.string())
                    }
                }
                override fun onFailure(call: Call<Question>, t: Throwable) {
                    Toast.makeText(this@ResultActivity, "Couldn't recieve data", Toast.LENGTH_SHORT).show()
                    Log.e(FAIL,t.message.toString())
                }
            })
    }
    companion object{
        const val FAIL="fail"
    }
}
