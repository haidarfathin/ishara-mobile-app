package app.bangkit.ishara.ui.game.types

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.bangkit.ishara.data.responses.journey.QuestionItem
import app.bangkit.ishara.databinding.FragmentSequenceQuizBinding
import com.bumptech.glide.Glide

class SequenceQuizFragment : Fragment() {

    private var questionItem: QuestionItem? = null
    private var _binding: FragmentSequenceQuizBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSequenceQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        questionItem = arguments?.getParcelable(ARG_QUESTION_ITEM)

        questionItem?.image?.let { image ->
            val imageUrl = (image as? Map<*, *>)?.get("question")?.let { (it as List<String>)[0] }
            Glide.with(this)
                .load(imageUrl)
                .into(binding.ivImage1)
        }

        questionItem?.image?.let { image ->
            val imageUrl = (image as? Map<*, *>)?.get("question")?.let { (it as List<String>)[1] }
            Glide.with(this)
                .load(imageUrl)
                .into(binding.ivImage2)
        }

        questionItem?.image?.let { image ->
            val imageUrl = (image as? Map<*, *>)?.get("question")?.let { (it as List<String>)[2] }
            Glide.with(this)
                .load(imageUrl)
                .into(binding.ivImage3)
        }

        questionItem?.image?.let { image ->
            val imageUrl = (image as? Map<*, *>)?.get("question")?.let { (it as List<String>)[3] }
            Glide.with(this)
                .load(imageUrl)
                .into(binding.ivImage4)
        }


    }


    fun getUserAnswer(): String? {
        val input = binding.etInput.text.toString()
        return input
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_QUESTION_ITEM = "question_item"

        fun newInstance(questionItem: QuestionItem): SequenceQuizFragment {
            return SequenceQuizFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_QUESTION_ITEM, questionItem)
                }
            }
        }
    }
}
