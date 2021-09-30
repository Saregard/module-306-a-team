package se.linerotech.quizkampen.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.myquizgame.RetrofitClient
import com.example.myquizgame.models.Question
import com.example.myquizgame.models.Result
import com.example.myquizgame.models.Token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import se.linerotech.quizkampen.activitys.GameActivity
import se.linerotech.quizkampen.activitys.ResultActivity

class GamePlayAccess {


    fun validation(email:String, passwordEditText: EditText, password: String, emailEditText: EditText): Boolean {
        var check = false
        if (!TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Needs to be an email"
        }else
        if (TextUtils.isEmpty(email)) {
            emailEditText.error = "Email can not be empty"
        }else
        if (TextUtils.isEmpty(password)) {
            passwordEditText.error = "Password can not be empty"
        }else
        if (!TextUtils.isEmpty(password) && TextUtils.isEmpty(email)) {
            emailEditText.error = "Email can not be empty"
        }else
        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
            emailEditText.error = "Email can not be empty"
            passwordEditText.error = "Password can not be empty"
        } else {
            check = true
        }
        return check
    }




    fun getToken(context: Context, activity: Activity, intent: Intent, bundle: Bundle?) {
        RetrofitClient
            .instance
            .getToken("request")
            .enqueue(object : Callback<Token> {
                override fun onResponse(call: Call<Token>, response: Response<Token>) {
                    if (response.isSuccessful) {
                        getQuestion(response.body()!!.token, activity, intent, context, bundle)
                    } else {
                        Toast.makeText(context, "Couldn't receive data", Toast.LENGTH_SHORT).show()
                        Log.e(ResultActivity.FAIL, response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<Token>, t: Throwable) {
                    Toast.makeText(context, "Couldn't receive data", Toast.LENGTH_SHORT).show()
                    Log.e(ResultActivity.FAIL, t.message.toString())
                }
            })
    }

    fun getQuestion(myToken: String, activity: Activity, intent: Intent, context: Context, bundle: Bundle?) {
        val numberOfQuestions = "10"
        RetrofitClient
            .instanceTwo
            .getQuestions(numberOfQuestions, myToken)
            .enqueue(object : Callback<Question> {
                override fun onResponse(
                    call: Call<Question>,
                    response: Response<Question>
                ) {
                    if (response.isSuccessful) {
                        val listOfRepos = response.body()?.results as? ArrayList<Result>
                        listOfRepos?.let {
                            intent.putParcelableArrayListExtra(GameActivity.QUIZ_DATA, it)
                            startActivity(context, intent, bundle)
                            activity.finish()
                        }
                    } else {
                        Toast.makeText(context, "Couldn't receive data", Toast.LENGTH_SHORT).show()
                        Log.e(ResultActivity.FAIL, response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<Question>, t: Throwable) {
                    Toast.makeText(context, "Couldn't receive data", Toast.LENGTH_SHORT).show()
                    Log.e(ResultActivity.FAIL, t.message.toString())
                }
            })
    }
}
