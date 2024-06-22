package app.bangkit.ishara.ui.game

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import app.bangkit.ishara.R
import app.bangkit.ishara.data.preferences.UserPreference
import app.bangkit.ishara.data.preferences.dataStore
import app.bangkit.ishara.data.responses.journey.QuestionItem
import app.bangkit.ishara.databinding.ActivityGameBinding
import app.bangkit.ishara.ui.game.types.ImageQuizFragment
import app.bangkit.ishara.ui.game.types.SequenceQuizFragment
import app.bangkit.ishara.ui.game.types.TextQuizFragment
import kotlinx.coroutines.launch

class GameActivity : AppCompatActivity() {

    private lateinit var gameBinding: ActivityGameBinding
    private val gameViewModel: GameViewModel by viewModels()

    private var pref: UserPreference? = null

    private var currentStep = 0
    private var questionItems: List<QuestionItem> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        pref = UserPreference.getInstance(this.application.dataStore)
        gameBinding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(gameBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(gameBinding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        gameBinding.btnNext.setOnClickListener {
            currentStep++
            showQuizStep(currentStep)
        }

        lifecycleScope.launch {
            pref?.getJwtAccessToken()?.collect { token ->
                Log.d("ProfileFragment", "Access token: $token")
                if (token.isNotEmpty()) {
                    gameViewModel.getQuestions(1, token).observe(this@GameActivity) { response ->
                        response?.data?.let { items ->
                            questionItems = items as List<QuestionItem>
                            if (questionItems.isNotEmpty()) {
                                showQuizStep(currentStep)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showQuizStep(step: Int) {
        if (step >= questionItems.size) {
            // Handle case when step is beyond the last question
            return
        }

        val item = questionItems[step]
        val fragment = when (item.type) {
            "text" -> TextQuizFragment.newInstance(item)
            "image" -> ImageQuizFragment.newInstance(item)
//            "sequence" -> SequenceQuizFragment.newInstance(item)
            else -> null
        }

        fragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, it)
                .commit()
        }
    }
}
