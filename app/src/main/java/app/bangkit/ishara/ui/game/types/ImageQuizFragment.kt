package app.bangkit.ishara.ui.game.types

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import app.bangkit.ishara.R
import app.bangkit.ishara.data.responses.journey.AnswersItem
import app.bangkit.ishara.data.responses.journey.QuestionItem
import app.bangkit.ishara.databinding.FragmentImageQuizBinding
import app.bangkit.ishara.ui.custom_view.CustomTextButton
import app.bangkit.ishara.ui.custom_view.TxtButton
import app.bangkit.ishara.ui.game.types.TextQuizFragment.Companion
import com.bumptech.glide.Glide


class ImageQuizFragment : Fragment() {

    private var questionItem: QuestionItem? = null
    private var _binding: FragmentImageQuizBinding? = null
    private val binding get() = _binding!!

    private lateinit var customTextButton: CustomTextButton
    private val options: ArrayList<TxtButton> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentImageQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        questionItem = arguments?.getParcelable(ARG_QUESTION_ITEM)


        questionItem?.let { item ->
            item.image?.let { imageUrl ->
                Glide.with(this)
                    .load(imageUrl)
                    .into(binding.ivQuiz)
            }
            populateOptions(item.answers)
        }

        customTextButton = binding.btnOptions
        customTextButton.options = options


    }

    fun getUserAnswer(): String? {
        val selectedOption = options.find { it.isClicked }
        return selectedOption?.name
    }


    private fun populateOptions(answers: List<AnswersItem>) {
        options.clear()
        answers.forEach { answer ->
            options.add(TxtButton(id = answer.id!!, name = answer.answer!!, isClicked = false))
        }
    }

    companion object {
        private const val ARG_QUESTION_ITEM = "question_item"

        fun newInstance(questionItem: QuestionItem): ImageQuizFragment {
            return ImageQuizFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_QUESTION_ITEM, questionItem)
                }
            }
        }
    }

}