package se.linerotech.quizkampen


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import se.linerotech.quizkampen.GameActivity.Companion.SCORE


import se.linerotech.quizkampen.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityResultBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val myScore=intent.getIntExtra(SCORE,0)
        binding.score.text="Score $myScore/10"


    }
}