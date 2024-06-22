package app.bangkit.ishara.ui.game.types

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.bangkit.ishara.R
import app.bangkit.ishara.data.responses.journey.QuestionItem
import app.bangkit.ishara.databinding.FragmentTextQuizBinding
import app.bangkit.ishara.ui.custom_view.CustomImageButton
import app.bangkit.ishara.ui.custom_view.ImgButton

class TextQuizFragment : Fragment() {

    private var questionItem: QuestionItem? = null

    private var _binding: FragmentTextQuizBinding? = null
    private val binding get() = _binding!!

    private lateinit var customImageButton: CustomImageButton
    private val options: ArrayList<ImgButton> = arrayListOf(
        ImgButton(id = 1, imagePath = "https://storage.googleapis.com/ishara_file_storage/file/2024/06/18/zoZeKa75FfK4ZbI9XHzH.png", name = "a", isClicked = false),
        ImgButton(id = 2, imagePath = "https://storage.googleapis.com/ishara_file_storage/file/2024/06/18/zoZeKa75FfK4ZbI9XHzH.png", name = "b", isClicked = false),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentTextQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO: add answerItem.answer to imgButton.imagePath
        val answers = questionItem?.answers

//        options.forEach { imgButton ->
//            if (questionItem.answers.answer == imgButton.name) {
//                imgButton.isClicked = true
//                imgButton.imagePath = answersItem.answer
//            }
//        }

        questionItem = arguments?.getParcelable(ARG_QUESTION_ITEM)
        val letter = extractLetterFromQuestion(questionItem?.question)
        binding.tvLetter.text = letter

        customImageButton = binding.btnOptions
        customImageButton.options = options
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
}
