package se.linerotech.quizkampen

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.myquizgame.models.Result
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.activity_game.*

import se.linerotech.quizkampen.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var allRandom:List<String>
    private var mySelectedItem:Pair<String,Int>?=null
    private lateinit var timer:CountDownTimer
    private var enableClick=false
    private var onClickedQuestion=0
    var selectedItem: String=""
    private var score=0
    val regexQuot = "&quot;"
    val regexUpper = "&#039;"
    val toChar = '"'
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        start()
        val listOfRepos = intent.getParcelableArrayListExtra<Result>(QUIZ_DATA)
        listOfRepos?.let {
            startAlert(listOfRepos, listOfRepos.size)

            showQuestionsOrNot(false)

            questionPreview(listOfRepos, listOfRepos.size)

        }
    }
    private fun selectItem():String {
       clickedOnA()
       clickedOnB()
       clickedOnC()
       clickedOnD()
       return selectedItem
    }
    private fun clickedOnA(){
        binding.gamePlayCardViewAnswerA.setOnClickListener {
            if (enableClick) {
                selectedItem = allRandom[0]
                mySelectedItem = Pair(selectedItem, 0)
                binding.gamePlayCardViewAnswerA.setCardBackgroundColor(Color.GRAY)
                binding.gamePlayCardViewAnswerB.setCardBackgroundColor(Color.WHITE)
                binding.gamePlayCardViewAnswerC.setCardBackgroundColor(Color.WHITE)
                binding.gamePlayCardViewAnswerD.setCardBackgroundColor(Color.WHITE)
                enableClick = false
                timer.cancel()
                timer.onFinish()
            }
        }
    }
    private fun clickedOnB(){
        binding.gamePlayCardViewAnswerB.setOnClickListener {
            if (enableClick) {
                selectedItem = allRandom[1]
                mySelectedItem = Pair(selectedItem, 1)
                binding.gamePlayCardViewAnswerA.setCardBackgroundColor(Color.WHITE)
                binding.gamePlayCardViewAnswerC.setCardBackgroundColor(Color.WHITE)
                binding.gamePlayCardViewAnswerD.setCardBackgroundColor(Color.WHITE)
                binding.gamePlayCardViewAnswerB.setCardBackgroundColor(Color.GRAY)
                enableClick=false
                timer.cancel()
                timer.onFinish()
            }
        }
    }
    private fun clickedOnC(){
        binding.gamePlayCardViewAnswerC.setOnClickListener {
            if (enableClick) {
                selectedItem = allRandom[2]
                mySelectedItem = Pair(selectedItem, 2)
                binding.gamePlayCardViewAnswerC.setCardBackgroundColor(Color.GRAY)
                binding.gamePlayCardViewAnswerA.setCardBackgroundColor(Color.WHITE)
                binding.gamePlayCardViewAnswerB.setCardBackgroundColor(Color.WHITE)
                binding.gamePlayCardViewAnswerD.setCardBackgroundColor(Color.WHITE)
                enableClick=false
                timer.cancel()
                timer.onFinish()
            }
        }
    }
    private fun clickedOnD(){
        binding.gamePlayCardViewAnswerD.setOnClickListener {
            if (enableClick) {
                selectedItem = allRandom[3]
                mySelectedItem = Pair(selectedItem, 3)
                binding.gamePlayCardViewAnswerD.setCardBackgroundColor(Color.GRAY)
                binding.gamePlayCardViewAnswerA.setCardBackgroundColor(Color.WHITE)
                binding.gamePlayCardViewAnswerB.setCardBackgroundColor(Color.WHITE)
                binding.gamePlayCardViewAnswerC.setCardBackgroundColor(Color.WHITE)
                enableClick=false
                timer.cancel()
                timer.onFinish()
            }
        }
    }

    private fun firstStart(theQuestion:ArrayList<Result>, items:Int){
        showQuestionsOrNot(true)
        setBackgroundColorForQuestions(Color.WHITE)
        enableClick=true
        binding.textViewTimer.isVisible=true

        allRandom=randomAnswer(theQuestion[onClickedQuestion].correct_answer,
            theQuestion[onClickedQuestion].incorrect_answers.size,
            theQuestion[onClickedQuestion].incorrect_answers)

        binding.textViewQuestion.text = theQuestion[onClickedQuestion].question
            .replace(regexQuot, toChar.toString())
            .replace(regexUpper, "'")

        displayAnswers(allRandom,allRandom.size)
        binding.textViewTimer.progressTintList = ColorStateList.valueOf(Color.GREEN)
        binding.textViewTimer.max=12
        binding.textViewTimer.progress
        timer = object : CountDownTimer(12000, 1000) {

            override fun onTick(millisUntilFinished: Long) {

                binding.textViewTimer.progress =(millisUntilFinished / 1000).toInt()
                if (binding.textViewTimer.progress<(textViewTimer.max/3)){
                    binding.textViewTimer.progressTintList = ColorStateList.valueOf(Color.RED)

                }else if (binding.textViewTimer.progress<(textViewTimer.max/1.5)){
                    binding.textViewTimer.progressTintList = ColorStateList.valueOf(Color.YELLOW)

                }
                binding.nextQuestionButton .isVisible = false
            }

            override fun onFinish() {
                onClickedQuestion++
                binding.nextQuestionButton.text=getString(R.string.next)
                binding.nextQuestionButton.isVisible = true
                binding.questionNumber.text="Finished Questions:$items / $onClickedQuestion"
                binding.textViewTimer.isVisible=false

                checkItem(mySelectedItem?.second.toString(), Color.RED)
                checkItem(allRandom.last(), Color.GREEN)

                if(mySelectedItem?.second.toString()==allRandom.last()){
                    Toast.makeText(this@GameActivity, "Congratulations , Correct answer!", Toast.LENGTH_SHORT).show()
                    score++
                }

            }
        }
        timer.start()
        selectItem()
    }
    private fun questionPreview(theQuestion:ArrayList<Result>, items:Int){
        binding.nextQuestionButton.setOnClickListener {
            if (onClickedQuestion == theQuestion.size) {
                binding.nextQuestionButton.isVisible = false
                val intent= Intent(this,ResultActivity::class.java)
                intent.putExtra(SCORE,score)
                startActivity(intent)
                finish()

            } else {

                showQuestionsOrNot(true)
                setBackgroundColorForQuestions(Color.WHITE)
                enableClick=true
                binding.textViewTimer.isVisible=true

                allRandom=randomAnswer(theQuestion[onClickedQuestion].correct_answer,
                    theQuestion[onClickedQuestion].incorrect_answers.size,
                    theQuestion[onClickedQuestion].incorrect_answers)

                binding.textViewQuestion.text = theQuestion[onClickedQuestion].question
                    .replace(regexQuot, toChar.toString())
                    .replace(regexUpper, "'")

                displayAnswers(allRandom,allRandom.size)
                binding.textViewTimer.progressTintList = ColorStateList.valueOf(Color.GREEN)
                binding.textViewTimer.max=12
                binding.textViewTimer.progress

                timer = object : CountDownTimer(12000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {

                        binding.textViewTimer.progress=(millisUntilFinished / 1000).toInt()
                        if (binding.textViewTimer.progress<(textViewTimer.max/3)){
                            binding.textViewTimer.progressTintList = ColorStateList.valueOf(Color.RED)

                        }else if (binding.textViewTimer.progress<(textViewTimer.max/1.5)){
                            binding.textViewTimer.progressTintList = ColorStateList.valueOf(Color.YELLOW)

                        }
                        binding.nextQuestionButton .isVisible = false
                    }

                    override fun onFinish() {
                        onClickedQuestion++
                        binding.nextQuestionButton.text=getString(R.string.next)
                        binding.nextQuestionButton.isVisible = true
                        binding.questionNumber.text="Finished Questions:$items / $onClickedQuestion"
                        binding.textViewTimer.isVisible=false

                        checkItem(mySelectedItem?.second.toString(), Color.RED)
                        checkItem(allRandom.last(), Color.GREEN)

                        if(mySelectedItem?.second.toString()==allRandom.last()){
                            Toast.makeText(this@GameActivity, "Congratulations , Correct answer!", Toast.LENGTH_SHORT).show()
                            score++
                        }




                    }
                }
                timer.start()
                selectItem()



            }
        }
    }
    private fun displayAnswers(questionData:List<String>,numberAnswers:Int) {

        if (numberAnswers == 5) {

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

        }
        if (numberAnswers == 3) {
            val randomField=(0..1).random()
            if (randomField==0){
                binding.textViewAnswerA.text =
                    allRandom[0]
                        .replace(regexQuot, toChar.toString())
                        .replace(regexUpper, "'")
                binding.textViewAnswerB.text =
                    allRandom[1]
                        .replace(regexQuot, toChar.toString())
                        .replace(regexUpper, "'")
            } else {
                binding.textViewAnswerA.text =
                    allRandom[0]
                        .replace(regexQuot, toChar.toString())
                        .replace(regexUpper, "'")
                binding.textViewAnswerB.text =
                    allRandom[1]
                        .replace(regexQuot, toChar.toString())
            }

            binding.textViewAnswerC.isVisible = false
            binding.gamePlayCardViewAnswerC.isVisible = false
            binding.textViewAnswerD.isVisible = false
            binding.gamePlayCardViewAnswerD.isVisible = false

        }

    }
    private fun randomAnswer(correctAnswer:String,amountOfQuestions:Int,questionsToGenerate:List<String>):List<String>{
        val listOfItems= mutableListOf<String>()
        var numberOfCorrectAnswer=0

        for(i in 0..amountOfQuestions){
            listOfItems.add("")
        }
        numberOfCorrectAnswer=(0..amountOfQuestions).random()
        listOfItems[numberOfCorrectAnswer] = correctAnswer
        for (i in 0..amountOfQuestions-1) {

            if (!listOfItems.contains(questionsToGenerate[i])) {

                for ((index,a) in listOfItems.withIndex()){

                    if (listOfItems[index].isNullOrEmpty()){
                        listOfItems[index]=questionsToGenerate[i]
                        break
                    }
                }

            }

        }

        listOfItems.add(numberOfCorrectAnswer.toString())

        return listOfItems
    }
    private fun showQuestionsOrNot(choice:Boolean){
        binding.cardView.isVisible=choice
        binding.textViewAnswerA.isVisible=choice
        binding.textViewAnswerB.isVisible=choice
        binding.textViewAnswerC.isVisible=choice
        binding.textViewAnswerD.isVisible=choice
        binding.gamePlayCardViewAnswerA.isVisible=choice
        binding.gamePlayCardViewAnswerB.isVisible=choice
        binding.gamePlayCardViewAnswerC.isVisible=choice
        binding.gamePlayCardViewAnswerD.isVisible=choice
    }
    private fun start(){
        binding.cardView.isVisible=false
        binding.questionNumber.text=getString(R.string.quest)
        binding.textViewTimer.isVisible=false
        binding.nextQuestionButton.isVisible=false
        binding.nextQuestionButton.text=getString(R.string.next)
    }
    private fun checkItem(option:String,color:Int){
        when (option) {
            "0" -> {
                binding.textViewAnswerA.isVisible=true
                binding.gamePlayCardViewAnswerA.setCardBackgroundColor(color)
                binding.gamePlayCardViewAnswerA.isVisible=true
            }
            "1" ->{
                binding.textViewAnswerB.isVisible=true
                binding.gamePlayCardViewAnswerB.setCardBackgroundColor(color)
                binding.gamePlayCardViewAnswerB.isVisible=true
            }
            "2" -> {
                binding.textViewAnswerC.isVisible=true
                binding.gamePlayCardViewAnswerC.setCardBackgroundColor(color)
                binding.gamePlayCardViewAnswerC.isVisible=true
            }
            "3" ->{
                binding.textViewAnswerD.isVisible=true
                binding.gamePlayCardViewAnswerD.setCardBackgroundColor(color)
                binding.gamePlayCardViewAnswerD.isVisible=true
            }
        }

    }
    private fun setBackgroundColorForQuestions(mustHaveColor:Int){
        binding.gamePlayCardViewAnswerA.setCardBackgroundColor(mustHaveColor)
        binding.gamePlayCardViewAnswerB.setCardBackgroundColor(mustHaveColor)
        binding.gamePlayCardViewAnswerC.setCardBackgroundColor(mustHaveColor)
        binding.gamePlayCardViewAnswerD.setCardBackgroundColor(mustHaveColor)
    }
    private fun startAlert(theQuestion:ArrayList<Result>, items:Int) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Welcome to 10 Blitz Quiz")
            .setMessage("In this game , you will be granted with 10 questions.To answer them you" +
                    "have 12 seconds each otherwise when timer finished it will move forward" +
                    "and you will not earn points.")
            .setNeutralButton("Let's get started!") { p0, p1 ->
                firstStart(theQuestion,items)
            }.setCancelable(false).show()
    }

    companion object {
        const val QUIZ_DATA= "quizData"
        const val SCORE="score"
    }
}