package app.bangkit.ishara.ui.game.types

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.bangkit.ishara.data.responses.journey.AnswersItem
import app.bangkit.ishara.data.responses.journey.QuestionItem
import app.bangkit.ishara.databinding.FragmentTextQuizBinding
import app.bangkit.ishara.ui.custom_view.CustomImageButton
import app.bangkit.ishara.ui.custom_view.ImgButton

class TextQuizFragment : Fragment() {

    private var questionItem: QuestionItem? = null

    private var _binding: FragmentTextQuizBinding? = null
    private val binding get() = _binding!!

    private lateinit var customImageButton: CustomImageButton
    val options: ArrayList<ImgButton> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentTextQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        questionItem = arguments?.getParcelable(ARG_QUESTION_ITEM)

        questionItem?.let { item ->
            populateOptions(item.answers)
        }

        val letter = extractLetterFromQuestion(questionItem?.question)
        binding.tvLetter.text = letter

        customImageButton = binding.btnOptions
        customImageButton.options = options
    }


    private fun populateOptions(answers: List<AnswersItem>) {
        options.clear()
        Log.d("TextQuizFragment", "populateOptions: $answers")
        answers.forEach { answer ->
            options.add(
                ImgButton(
                    id = answer.id!!,
                    name = answer.answer!!,
                    imagePath = "sign_${answer.answer.lowercase()}",
                    isClicked = false
                )
            )
        }
    }

    private fun extractLetterFromQuestion(question: String?): String {
        var letter = ""
        question?.let {
            val startIndex = it.indexOf("(") + 1
            val endIndex = it.indexOf(")")
            if (startIndex in 0..<endIndex) {
                letter = it.substring(startIndex, endIndex)
            }
        }
        return letter.trim()
    }

    companion object {
        private const val ARG_QUESTION_ITEM = "question_item"
        fun newInstance(questionItem: QuestionItem): TextQuizFragment {
            return TextQuizFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_QUESTION_ITEM, questionItem)
                }
            }
        }

    }

    fun getUserAnswer(): String? {
        val selectedOption = options.find { it.isClicked }
        return selectedOption?.name
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
