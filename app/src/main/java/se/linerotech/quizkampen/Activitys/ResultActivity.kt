package se.linerotech.quizkampen.Activitys

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_profile_page.*
import se.linerotech.quizkampen.Activitys.GameActivity.Companion.SCORE
import se.linerotech.quizkampen.databinding.ActivityResultBinding
import se.linerotech.quizkampen.utils.GamePlayAccess

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityResultBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val myScore = intent.getIntExtra(SCORE, 0)
        binding.score.text = "Score $myScore/10"

        binding.buttonPlay.setOnClickListener {
            val gamePlayAccess = GamePlayAccess()
            val intent = Intent(this, GameActivity::class.java)
            gamePlayAccess.getToken(this, this@ResultActivity, intent, savedInstanceState)
        }
        binding.buttonLogOut.setOnClickListener {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val FAIL = "fail"
    }
}
