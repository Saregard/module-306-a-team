package se.linerotech.quizkampen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.myquizgame.models.Result

class GamePlayActivity : AppCompatActivity() {

    private var myTextViewQuestion: TextView?=null
    private var myTextViewAnswerA: TextView?=null
    private var myTextViewAnswerB: TextView?=null
    private var myTextViewAnswerC: TextView?=null
    private var myTextViewAnswerD: TextView?=null
    private var myQuestionNumber: TextView?=null
    private var myNextQuestionButton: Button?=null
    private lateinit var listOfMyRepos:List<Result>
    private var onclickedQuestion=0
    private var myTimer: TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_play)
        myTextViewQuestion=findViewById(R.id.textViewQuestion)
        myQuestionNumber=findViewById(R.id.textViewQuestionNumber)
        myTextViewAnswerA=findViewById(R.id.textViewAnswerA)
        myTextViewAnswerB=findViewById(R.id.textViewAnswerB)
        myTextViewAnswerC=findViewById(R.id.textViewAnswerC)
        myTextViewAnswerD=findViewById(R.id.textViewAnswerD)
        myTimer=findViewById(R.id.textViewTimer)
        myNextQuestionButton=findViewById(R.id.nextQuestionButton)

        //getMyToken()
        val listOfRepos = intent?.getParcelableArrayListExtra<Result>(QUIZ_DATA)
        listOfRepos?.let {
            questionPreview(listOfRepos,listOfRepos.size)
        }
    }
    private fun questionPreview(theQuestion:ArrayList<Result>,items:Int){
        myNextQuestionButton?.setOnClickListener {
            if (onclickedQuestion > theQuestion.size) {
                myNextQuestionButton?.isVisible = false
            } else {
                val myRegex = "&quot;"
                val myRegex39 = "&#039;"
                val toChar = '"'
                val timer = object : CountDownTimer(12000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        myTextViewQuestion?.text = theQuestion[onclickedQuestion].question
                            .replace(myRegex, toChar.toString())
                            .replace(myRegex39, "'")
                        if (theQuestion[onclickedQuestion].incorrect_answers.size>2) {
                            myTextViewAnswerA?.text =
                                theQuestion[onclickedQuestion].incorrect_answers[0]
                                    .replace(myRegex, toChar.toString())
                                    .replace(myRegex39, "'")
                            myTextViewAnswerB?.text =
                                theQuestion[onclickedQuestion].incorrect_answers[1]
                                    .replace(myRegex, toChar.toString())
                                    .replace(myRegex39, "'")
                            myTextViewAnswerC?.text =
                                theQuestion[onclickedQuestion].incorrect_answers[2]
                                    .replace(myRegex, toChar.toString())
                                    .replace(myRegex39, "'")
                        } else {
                            myTextViewAnswerA?.text =
                                theQuestion[onclickedQuestion].incorrect_answers[0]
                                    .replace(myRegex, toChar.toString())
                                    .replace(myRegex39, "'")


                        }
                        myTextViewAnswerD?.text = theQuestion[onclickedQuestion].correct_answer
                            .replace(myRegex, toChar.toString())
                            .replace(myRegex39, "'")
                        myTimer?.text =
                            "Time left to answer: " + (millisUntilFinished / 1000).toString()
                        myNextQuestionButton?.isVisible = false
                    }

                    override fun onFinish() {
                        onclickedQuestion++
                        myNextQuestionButton?.isVisible = true
                        myQuestionNumber?.text="$items / $onclickedQuestion"
                        myTextViewAnswerA?.text =""
                        myTextViewAnswerB?.text =""
                        myTextViewAnswerC?.text =""
                        myTextViewAnswerD?.text =""


                    }
                }
                timer.start()


            }
        }
    }

    companion object {
        const val QUIZ_DATA = "questionData"
    }
}