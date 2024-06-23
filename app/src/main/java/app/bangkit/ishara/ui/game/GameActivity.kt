package app.bangkit.ishara.ui.game

import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import app.bangkit.ishara.ui.game.types.SignQuizFragment
import app.bangkit.ishara.ui.game.types.TextQuizFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameActivity : AppCompatActivity() {

    private lateinit var gameBinding: ActivityGameBinding
    private val gameViewModel: GameViewModel by viewModels()

    private var pref: UserPreference? = null

    private var currentStep = 0
    private var questionItems: List<QuestionItem> = emptyList()

    private var correctAnswers = 0
    private var incorrectAnswers = 0

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

            val currentFragment = supportFragmentManager.findFragmentById(R.id.frameLayout)
            var userAnswer: String? = null

            when (currentFragment) {
                is TextQuizFragment -> {
                    userAnswer = currentFragment.getUserAnswer()
                }
                is ImageQuizFragment -> {
                    userAnswer = currentFragment.getUserAnswer()
                }
                is SequenceQuizFragment -> {
                    userAnswer = currentFragment.getUserAnswer()
                }
                is SignQuizFragment -> {
                    userAnswer = currentFragment.getUserAnswer()
                }
            }

            val answer = questionItems[currentStep].correctAnswer
            var correctAnswer: String? = null
            if (answer is String) {
                correctAnswer = answer.toString()
            } else if (answer is List<*>){
              extractLetters(answer.toString())
            }
            Log.d("GameActivity", "correctAnswer: $correctAnswer")
            checkAnswer(userAnswer, correctAnswer)

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

    fun extractLetters(input: String): String {
        val cleanedInput = input.replace("[", "").replace("]", "")
        return cleanedInput.split(",").joinToString("") { it.trim() }
    }

    private fun checkAnswer(userAnswer: String?, correctAnswer: String?) {
        if (userAnswer.equals(correctAnswer, ignoreCase = true)) {
            correctAnswers++
            Toast.makeText(this, "Benar", Toast.LENGTH_SHORT).show()
            currentStep++
        } else {
            incorrectAnswers++
            Toast.makeText(this, "Salah", Toast.LENGTH_SHORT).show()

        }
    }


    private fun showQuizStep(step: Int) {
        if (step >= questionItems.size) {
            // Handle case when step is beyond the last question
            Toast.makeText(this, "Quiz Selesai, Skor: $correctAnswers", Toast.LENGTH_SHORT).show()
            return
        }

        val item = questionItems[step]
        val fragment = when (item.type) {
            "text" -> TextQuizFragment.newInstance(item)
            "image" -> ImageQuizFragment.newInstance(item)
            "sequence" -> SequenceQuizFragment.newInstance(item)
            "practice" -> SignQuizFragment.newInstance(item)
            else -> null
        }

        fragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, it)
                .commit()
        }
    }
}
