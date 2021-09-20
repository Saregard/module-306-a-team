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
import kotlinx.android.synthetic.main.activity_game_play.*
import se.linerotech.quizkampen.databinding.ActivityGamePlayBinding

class GamePlayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGamePlayBinding
    private lateinit var allRandom:List<String>
    private var mySelectedItem:Pair<String,Int>?=null
    private lateinit var timer:CountDownTimer
    private var enableClick=false
    private var onClickedQuestion=0
    private var score=0
    val regexQuot = "&quot;"
    val regexUpper = "&#039;"
    val toChar = '"'

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityGamePlayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        start()
        val listOfRepos = intent.getParcelableArrayListExtra<Result>(QUIZ_DATA)
        listOfRepos?.let {
            startAlert(listOfRepos,listOfRepos.size)

            showQuestionsOrNot(false)


            questionPreview(listOfRepos,listOfRepos.size)
        }


    }


}