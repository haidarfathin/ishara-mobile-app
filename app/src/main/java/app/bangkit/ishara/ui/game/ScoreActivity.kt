package app.bangkit.ishara.ui.game

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import app.bangkit.ishara.R
import app.bangkit.ishara.data.preferences.UserPreference
import app.bangkit.ishara.data.preferences.dataStore
import app.bangkit.ishara.databinding.ActivityScoreBinding
import app.bangkit.ishara.ui.auth.login.LoginViewModel
import app.bangkit.ishara.utils.ViewModelFactory

class ScoreActivity : AppCompatActivity() {

    private lateinit var scoreViewModel: ScoreViewModel
    private lateinit var scoreBinding: ActivityScoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scoreBinding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(scoreBinding.root)

        val correctAnswers = intent.getIntExtra("CORRECT_ANSWERS", 0)
        val levelId = intent.getIntExtra("LEVEL_ID", 0)

        if(correctAnswers <= 3){
            scoreBinding.tvMessage.text = "Tenang saja, kamu bisa mencobanya lagi!"
        } else {
            scoreBinding.tvMessage.text = "Keren! gass lanjut ke level selanjutnya!"
        }

        displayStars(correctAnswers)

        val pref = UserPreference.getInstance(application.dataStore)
        scoreViewModel = ViewModelProvider(this, ViewModelFactory(pref))[ScoreViewModel::class.java]

        scoreViewModel.isSuccess.observe(this) {
            if (it) {
                finish()
            }
        }

        scoreViewModel.isLoading.observe(this) {
            scoreBinding.progressbar.visibility = if (it) View.VISIBLE else View.GONE
            scoreBinding.btnDone.isEnabled = if (it) false else true
        }

        scoreViewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        scoreBinding.btnDone.setOnClickListener {
            scoreViewModel.postLevelStar(correctAnswers, levelId)
        }
    }

    private fun displayStars(numberOfStars: Int) {
        val starSize = resources.getDimensionPixelSize(R.dimen.star_image_score_width)
        val starMarginStart = resources.getDimensionPixelSize(R.dimen.star_image_score_margin_start)

        scoreBinding.upperRow.removeAllViews()
        scoreBinding.lowerRow.removeAllViews()

        for (i in 0 until 3) {
            val starImageView = ImageView(this)
            val params = LinearLayout.LayoutParams(starSize, starSize)
            params.marginStart = starMarginStart
            starImageView.layoutParams = params
            starImageView.adjustViewBounds = true
            starImageView.setImageResource(
                if (i < numberOfStars) R.drawable.star_active else R.drawable.star_inactive
            )
            scoreBinding.upperRow.addView(starImageView)
        }
        for (i in 0 until 2) {
            val starImageView = ImageView(this)
            val params = LinearLayout.LayoutParams(starSize, starSize)
            params.marginStart = starMarginStart
            starImageView.layoutParams = params
            starImageView.adjustViewBounds = true
            starImageView.setImageResource(
                if (i + 3 < numberOfStars) R.drawable.star_active else R.drawable.star_inactive
            )
            scoreBinding.lowerRow.addView(starImageView)
        }
    }
}
