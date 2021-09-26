package se.linerotech.quizkampen


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myquizgame.Backends.GetQuestions
import com.example.myquizgame.RetrofitClient
import com.example.myquizgame.models.Qustions
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


        val myScore = intent.getIntExtra(SCORE, 0)
        binding.score.text = "Score $myScore/10"


        binding.buttonPlay.setOnClickListener {
            getMyToken()


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

    private fun getMyToken() {
        RetrofitClient
            .instance
            .getToken("request")
            .enqueue(object : Callback<Token> {
                override fun onResponse(call: Call<Token>, response: retrofit2.Response<Token>) {
                    if (response.isSuccessful) {

                        getQuestions(response.body()!!.token)
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

                override fun onFailure(call: Call<Qustions>, t: Throwable) {
                    Toast.makeText(this@ResultActivity, "Couldn't recieve data", Toast.LENGTH_SHORT).show()
                    Log.e(FAIL,t.message.toString())
                }
            })
    }
    companion object{
        const val FAIL="fail"
    }
}
