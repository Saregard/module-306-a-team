package se.linerotech.quizkampen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import com.example.myquizgame.models.Result

class GamePlayActivity : AppCompatActivity() {
    private var myTextViewQuestion: TextView? = null
    private var mygamePlayCardViewAnswerA: CardView? = null
    private var mygamePlayCardViewAnswerB: CardView? = null
    private var mygamePlayCardViewAnswerC: CardView? = null
    private var mygamePlayCardViewAnswerD: CardView? = null
    private var myTextViewAnswerA: TextView? = null
    private var myTextViewAnswerB: TextView? = null
    private var myTextViewAnswerC: TextView? = null
    private var myTextViewAnswerD: TextView? = null
    private var myTextViewRandomOne: TextView? = null
    private var myTextViewRandomSecond: TextView? = null
    private var myQuestionNumber: TextView? = null
    private var myNextQuestionButton: Button? = null
    private lateinit var listOfMyRepos: List<Result>
    private var onclickedQuestion = 0
    private var myTimer: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_play)
        myTextViewQuestion = findViewById(R.id.textViewQuestion)
        myTextViewAnswerA = findViewById(R.id.textViewAnswerA)
        myTextViewAnswerB = findViewById(R.id.textViewAnswerB)
        myTextViewAnswerC = findViewById(R.id.textViewAnswerC)
        myTextViewAnswerD = findViewById(R.id.textViewAnswerD)
        mygamePlayCardViewAnswerA = findViewById(R.id.gamePlayCardViewAnswerA)
        mygamePlayCardViewAnswerB = findViewById(R.id.gamePlayCardViewAnswerB)
        mygamePlayCardViewAnswerC = findViewById(R.id.gamePlayCardViewAnswerC)
        mygamePlayCardViewAnswerD = findViewById(R.id.gamePlayCardViewAnswerD)
        myQuestionNumber = findViewById(R.id.questionNumber)

        myTimer = findViewById(R.id.textViewTimer)
        myNextQuestionButton = findViewById(R.id.nextQuestionButton)

        //getMyToken()
        Start()
        val listOfRepos = intent?.getParcelableArrayListExtra<Result>(QUIZ_DATA)
        listOfRepos?.let {
            questionPreview(listOfRepos, listOfRepos.size)
        }
    }

    private fun questionPreview(theQuestion: ArrayList<Result>, items: Int) {
        myNextQuestionButton?.setOnClickListener {
            if (onclickedQuestion > theQuestion.size) {
                myNextQuestionButton?.isVisible = false
            } else {

                myTextViewAnswerA?.isVisible = true
                myTextViewAnswerB?.isVisible = true
                myTextViewAnswerC?.isVisible = true
                myTextViewAnswerD?.isVisible = true
                mygamePlayCardViewAnswerA?.isVisible = true
                mygamePlayCardViewAnswerB?.isVisible = true
                mygamePlayCardViewAnswerC?.isVisible = true
                mygamePlayCardViewAnswerD?.isVisible = true
                myTimer?.isVisible = true
                myTextViewRandomOne?.text =
                    "Incorrect before random:${theQuestion[onclickedQuestion].incorrect_answers.toString()}"
                val allRandom = randomAnswer(
                    theQuestion[onclickedQuestion].correct_answer,
                    theQuestion[onclickedQuestion].incorrect_answers.size,
                    theQuestion[onclickedQuestion].incorrect_answers
                )
                myTextViewRandomSecond?.text = "Incorrect after:$allRandom"
                val myRegex = "&quot;"
                val myRegex39 = "&#039;"
                val toChar = '"'
                val timer = object : CountDownTimer(12000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {

                        myTextViewQuestion?.text = theQuestion[onclickedQuestion].question
                            .replace(myRegex, toChar.toString())
                            .replace(myRegex39, "'")

                        if (theQuestion[onclickedQuestion].incorrect_answers.size > 2) {
                            myTextViewAnswerA?.text =
                                allRandom[0]
                                    .replace(myRegex, toChar.toString())
                                    .replace(myRegex39, "'")
                            myTextViewAnswerB?.text =
                                allRandom[1]
                                    .replace(myRegex, toChar.toString())
                                    .replace(myRegex39, "'")
                            myTextViewAnswerC?.text =
                                allRandom[2]
                                    .replace(myRegex, toChar.toString())
                                    .replace(myRegex39, "'")
                            myTextViewAnswerD?.text =
                                allRandom[3]
                                    .replace(myRegex, toChar.toString())
                                    .replace(myRegex39, "'")

                        } else {
                            myTextViewAnswerA?.text =
                                theQuestion[onclickedQuestion].incorrect_answers[0]
                                    .replace(myRegex, toChar.toString())
                                    .replace(myRegex39, "'")
                            myTextViewAnswerB?.text = theQuestion[onclickedQuestion].correct_answer
                                .replace(myRegex, toChar.toString())
                                .replace(myRegex39, "'")
                            myTextViewAnswerC?.isVisible = false
                            myTextViewAnswerD?.isVisible = false


                        }

                        myTimer?.text =
                            "Time left to answer: " + (millisUntilFinished / 1000).toString()
                        myNextQuestionButton?.isVisible = false
                    }

                    override fun onFinish() {
                        onclickedQuestion++
                        myNextQuestionButton?.text = "Next Question"
                        myNextQuestionButton?.isVisible = true
                        myQuestionNumber?.text = "Finished Questions:$items / $onclickedQuestion"
                        myTextViewAnswerA?.isVisible = false
                        myTextViewAnswerB?.isVisible = false
                        myTextViewAnswerC?.isVisible = false
                        myTextViewAnswerD?.isVisible = false
                        myTimer?.isVisible = false

                        when (allRandom.last()) {
                            "0" -> myTextViewAnswerA?.isVisible = true
                            "1" -> myTextViewAnswerB?.isVisible = true
                            "2" -> myTextViewAnswerC?.isVisible = true
                            "3" -> myTextViewAnswerD?.isVisible = true
                        }


                    }
                }
                timer.start()


            }
        }
    }


    private fun randomAnswer(
        correctAnswer: String,
        amountOfQuestions: Int,
        questionsToGenerate: List<String>
    ): List<String> {
        val listOfItems = mutableListOf<String>()
        var numberOfCorrectAnswer = 0

        for (i in 0..amountOfQuestions) {
            listOfItems.add("")
        }
        numberOfCorrectAnswer = (0..amountOfQuestions).random()
        listOfItems[numberOfCorrectAnswer] = correctAnswer
        for (i in 0..amountOfQuestions - 1) {

            if (!listOfItems.contains(questionsToGenerate[i])) {

                for ((index, a) in listOfItems.withIndex()) {

                    if (listOfItems[index].isNullOrEmpty()) {
                        listOfItems[index] = questionsToGenerate[i]
                        break
                    }
                }

            }

        }

        listOfItems.add(numberOfCorrectAnswer.toString())

        return listOfItems
    }

    private fun Start() {
        myQuestionNumber?.text = "Finished Questions:0"
        myTimer?.isVisible = false
        mygamePlayCardViewAnswerA?.isVisible = false
        mygamePlayCardViewAnswerB?.isVisible = false
        mygamePlayCardViewAnswerC?.isVisible = false
        mygamePlayCardViewAnswerD?.isVisible = false
        myNextQuestionButton?.isVisible = true
        myNextQuestionButton?.text = "Start Quiz"
    }

    companion object {
        const val QUIZ_DATA = "quizData"
    }
}