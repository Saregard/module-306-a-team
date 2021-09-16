package se.linerotech.quizkampen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.myquizgame.models.Result
import se.linerotech.quizkampen.databinding.ActivityCreateAccountBinding
import se.linerotech.quizkampen.databinding.ActivityGamePlayBinding

class GamePlayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGamePlayBinding
    private var myTextViewRandomOne: TextView? = null
    private var myTextViewRandomSecond: TextView? = null
    private lateinit var listOfMyRepos: List<Result>
    private var onclickedQuestion = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGamePlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //getMyToken()
        Start()
        val listOfRepos = intent?.getParcelableArrayListExtra<Result>(QUIZ_DATA)
        listOfRepos?.let {
            questionPreview(listOfRepos, listOfRepos.size)
        }
    }

    private fun questionPreview(theQuestion: ArrayList<Result>, items: Int) {
        binding.nextQuestionButton.setOnClickListener {
            if (onclickedQuestion > theQuestion.size) {
                binding.nextQuestionButton.isVisible = false
            } else {

                binding.textViewAnswerA.isVisible = true
                binding.textViewAnswerB.isVisible = true
                binding.textViewAnswerC.isVisible = true
                binding.textViewAnswerD.isVisible = true
                binding.gamePlayCardViewAnswerA.isVisible = true
                binding.gamePlayCardViewAnswerB.isVisible = true
                binding.gamePlayCardViewAnswerC.isVisible = true
                binding.gamePlayCardViewAnswerD.isVisible = true
                binding.textViewTimer.isVisible = true
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

                        binding.textViewQuestion.text = theQuestion[onclickedQuestion].question
                            .replace(myRegex, toChar.toString())
                            .replace(myRegex39, "'")

                        if (theQuestion[onclickedQuestion].incorrect_answers.size > 2) {
                            binding.textViewAnswerA.text =
                                allRandom[0]
                                    .replace(myRegex, toChar.toString())
                                    .replace(myRegex39, "'")
                            binding.textViewAnswerB.text =
                                allRandom[1]
                                    .replace(myRegex, toChar.toString())
                                    .replace(myRegex39, "'")
                            binding.textViewAnswerC.text =
                                allRandom[2]
                                    .replace(myRegex, toChar.toString())
                                    .replace(myRegex39, "'")
                            binding.textViewAnswerD.text =
                                allRandom[3]
                                    .replace(myRegex, toChar.toString())
                                    .replace(myRegex39, "'")

                        } else {
                            binding.textViewAnswerA.text =
                                theQuestion[onclickedQuestion].incorrect_answers[0]
                                    .replace(myRegex, toChar.toString())
                                    .replace(myRegex39, "'")
                            binding.textViewAnswerB.text =
                                theQuestion[onclickedQuestion].correct_answer
                                    .replace(myRegex, toChar.toString())
                                    .replace(myRegex39, "'")
                            binding.textViewAnswerC.isVisible = false
                            binding.textViewAnswerD.isVisible = false


                        }

                        binding.textViewTimer.text =
                            "Time left to answer: " + (millisUntilFinished / 1000).toString()
                        binding.nextQuestionButton.isVisible = false
                    }

                    override fun onFinish() {
                        onclickedQuestion++
                        binding.nextQuestionButton.text = "Next Question"
                        binding.nextQuestionButton.isVisible = true
                        binding.questionNumber.text =
                            "Finished Questions:$items / $onclickedQuestion"
                        binding.textViewAnswerA.isVisible = false
                        binding.textViewAnswerB.isVisible = false
                        binding.textViewAnswerC.isVisible = false
                        binding.textViewAnswerD.isVisible = false
                        binding.textViewTimer.isVisible = false

                        when (allRandom.last()) {
                            "0" -> binding.textViewAnswerA.isVisible = true
                            "1" -> binding.textViewAnswerB.isVisible = true
                            "2" -> binding.textViewAnswerC.isVisible = true
                            "3" -> binding.textViewAnswerD.isVisible = true
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
        binding.questionNumber.text = "Finished Questions:0"
        binding.textViewTimer.isVisible = false
        binding.gamePlayCardViewAnswerA.isVisible = false
        binding.gamePlayCardViewAnswerB.isVisible = false
        binding.gamePlayCardViewAnswerC.isVisible = false
        binding.gamePlayCardViewAnswerD.isVisible = false
        binding.nextQuestionButton.isVisible = true
        binding.nextQuestionButton.text = "Start Quiz"
    }

    companion object {
        const val QUIZ_DATA = "quizData"
    }
}