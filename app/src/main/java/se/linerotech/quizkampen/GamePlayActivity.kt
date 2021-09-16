package se.linerotech.quizkampen

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.core.view.isVisible
import com.example.myquizgame.models.Result
import se.linerotech.quizkampen.databinding.ActivityGamePlayBinding

class GamePlayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGamePlayBinding
    private lateinit var mySelectedItem: Pair<String, Int>
    private lateinit var allRandom: List<String>
    private var onClickedQuestion = 0
    val regexQuot = "&quot;"
    val regexUpper = "&#039;"
    val toChar = '"'

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGamePlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        start()
        val listOfRepos = intent?.getParcelableArrayListExtra<Result>(QUIZ_DATA)
        listOfRepos?.let {
            questionPreview(listOfRepos, listOfRepos.size)
        }
    }
private fun questionPreview(theQuestion: ArrayList<Result>, items: Int) {
    binding.nextQuestionButton.setOnClickListener {
        if (onClickedQuestion > theQuestion.size) {
                binding.nextQuestionButton.isVisible = false
        } else {
                    binding.textViewAnswerA.isVisible = true
                    binding.textViewAnswerB.isVisible = true
                    binding.textViewAnswerC.isVisible = true
                    binding.textViewAnswerD.isVisible = true
                    binding.textViewAnswerA.setBackgroundColor(getColor(R.color.orange_quiz))
                    binding.textViewAnswerD.setBackgroundColor(getColor(R.color.orange_quiz))
                    binding.textViewAnswerC.setBackgroundColor(getColor(R.color.orange_quiz))
                    binding.textViewAnswerB.setBackgroundColor(getColor(R.color.orange_quiz))

                    allRandom = randomAnswer(
                    theQuestion[onClickedQuestion].correct_answer,
                    theQuestion[onClickedQuestion].incorrect_answers.size,
                    theQuestion[onClickedQuestion].incorrect_answers)


                     val timer = object : CountDownTimer(12000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {

                            binding.textViewQuestion.text = theQuestion[onClickedQuestion].question
                                .replace(regexQuot, toChar.toString())
                                .replace(regexUpper, "'")
                            if (theQuestion[onClickedQuestion].incorrect_answers.size > 2) {
                                binding.textViewAnswerA.text =
                                    allRandom[0]
                                        .replace(regexQuot, toChar.toString())
                                        .replace(regexUpper, "'")
                                binding.textViewAnswerB.text =
                                    allRandom[1]
                                        .replace(regexQuot, toChar.toString())
                                        .replace(regexUpper, "'")
                                binding.textViewAnswerC.text =
                                    allRandom[2]
                                        .replace(regexQuot, toChar.toString())
                                        .replace(regexUpper, "'")
                                binding.textViewAnswerD.text =
                                    allRandom[3]
                                        .replace(regexQuot, toChar.toString())
                                        .replace(regexUpper, "'")
                            } else {
                                binding.textViewAnswerA.text =
                                    theQuestion[onClickedQuestion].incorrect_answers[0]
                                        .replace(regexQuot, toChar.toString())
                                        .replace(regexUpper, "'")
                                binding.textViewAnswerB.text =
                                    theQuestion[onClickedQuestion].correct_answer
                                        .replace(regexQuot, toChar.toString())
                                        .replace(regexUpper, "'")

                                binding.textViewAnswerC.isVisible = false
                                binding.textViewAnswerD.isVisible = false
                            }

                            binding.nextQuestionButton.isVisible = false
                        }
                       override fun onFinish() {
                            onClickedQuestion++
                            binding.nextQuestionButton.text = getString(R.string.fquestions)
                            binding.nextQuestionButton.isVisible = true
                            binding.questionNumber.text =
                                "Finished Questions:$items / $onClickedQuestion"
                            binding.textViewAnswerA.isVisible = false
                            binding.textViewAnswerB.isVisible = false
                            binding.textViewAnswerC.isVisible = false
                            binding.textViewAnswerD.isVisible = false
                            when (mySelectedItem.second.toString()) {
                                "0" -> {
                                    binding.textViewAnswerD.setBackgroundColor(Color.RED)
                                    binding.textViewAnswerD.isVisible = true
                                }
                                "1" -> {
                                    binding.textViewAnswerB.setBackgroundColor(Color.RED)
                                    binding.textViewAnswerB.isVisible = true
                                }
                                "2" -> {
                                    binding.textViewAnswerC.setBackgroundColor(Color.RED)
                                    binding.textViewAnswerC.isVisible = true
                                }
                                "3" -> {
                                    binding.textViewAnswerD.setBackgroundColor(Color.RED)
                                    binding.textViewAnswerD.isVisible = true
                                }
                            }
                            when (allRandom.last()) {
                                "0" -> {
                                    binding.textViewAnswerD.setBackgroundColor(Color.GREEN)
                                    binding.textViewAnswerD.isVisible = true
                                }
                                "1" -> {
                                    binding.textViewAnswerB.setBackgroundColor(Color.GREEN)
                                    binding.textViewAnswerB.isVisible = true
                                }
                                "2" -> {
                                    binding.textViewAnswerC.setBackgroundColor(Color.GREEN)
                                    binding.textViewAnswerC.isVisible = true
                                }
                                "3" -> {
                                    binding.textViewAnswerD.setBackgroundColor(Color.GREEN)
                                    binding.textViewAnswerD.isVisible = true
                                }
                            }
                       }
                     }
                    timer.start()
                    selectItem()
        }
    }
}

private fun selectItem(): String {
        var selectedItem = ""
        binding.textViewAnswerA.setOnClickListener {
            selectedItem = allRandom[0]
            mySelectedItem = Pair(selectedItem, 0)
            binding.textViewAnswerA.setBackgroundColor(Color.GRAY)
            binding.textViewAnswerD.setBackgroundColor(getColor(R.color.orange_quiz))
            binding.textViewAnswerC.setBackgroundColor(getColor(R.color.orange_quiz))
            binding.textViewAnswerB.setBackgroundColor(getColor(R.color.orange_quiz))
        }
        binding.textViewAnswerD.setOnClickListener {
            selectedItem = allRandom[1]
            mySelectedItem = Pair(selectedItem, 1)
            binding.textViewAnswerA.setBackgroundColor(getColor(R.color.orange_quiz))
            binding.textViewAnswerC.setBackgroundColor(getColor(R.color.orange_quiz))
            binding.textViewAnswerB.setBackgroundColor(getColor(R.color.orange_quiz))
            binding.textViewAnswerD.setBackgroundColor(Color.GRAY)

        }
        binding.textViewAnswerC.setOnClickListener {
            selectedItem = allRandom[2]
            mySelectedItem = Pair(selectedItem, 2)
            binding.textViewAnswerC.setBackgroundColor(Color.GRAY)
            binding.textViewAnswerA.setBackgroundColor(getColor(R.color.orange_quiz))
            binding.textViewAnswerD.setBackgroundColor(getColor(R.color.orange_quiz))
            binding.textViewAnswerB.setBackgroundColor(getColor(R.color.orange_quiz))

        }
        binding.textViewAnswerB.setOnClickListener {
            selectedItem = allRandom[3]
            mySelectedItem = Pair(selectedItem, 3)

            binding.textViewAnswerB.setBackgroundColor(Color.GRAY)
            binding.textViewAnswerA.setBackgroundColor(getColor(R.color.orange_quiz))
            binding.textViewAnswerD.setBackgroundColor(getColor(R.color.orange_quiz))
            binding.textViewAnswerC.setBackgroundColor(getColor(R.color.orange_quiz))

        }

        return selectedItem
}
private fun randomAnswer(correctAnswer: String,amountOfQuestions: Int,
                         questionsToGenerate: List<String>): List<String>{
    val listOfItems = mutableListOf<String>()
    //var numberOfCorrectAnswer = 0
    for (i in 0..amountOfQuestions) {
        listOfItems.add("")
    }
        val numberOfCorrectAnswer = (0..amountOfQuestions).random()
        listOfItems[numberOfCorrectAnswer] = correctAnswer
        for (i in 0 until amountOfQuestions) {
            if (!listOfItems.contains(questionsToGenerate[i])) {
                for ((index, a) in listOfItems.withIndex()) {
                    if (listOfItems[index].isEmpty()) {
                        listOfItems[index] = questionsToGenerate[i]
                        break
                    }
                }

            }

       }
    listOfItems.add(numberOfCorrectAnswer.toString())
    return listOfItems
}

private fun start() {
        binding.questionNumber.text = getString(R.string.fin)
        binding.textViewAnswerA.isVisible = false
        binding.textViewAnswerB.isVisible = false
        binding.textViewAnswerC.isVisible = false
        binding.textViewAnswerD.isVisible = false
        binding.nextQuestionButton.isVisible = true
        binding.nextQuestionButton.text = getString(R.string.start)
}
companion object {
        const val QUIZ_DATA = "quizData"
    }
}