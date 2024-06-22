package app.bangkit.ishara.ui.game.types

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.bangkit.ishara.databinding.FragmentSequenceQuizBinding
import app.bangkit.ishara.domain.adapter.SequenceAdapter
import com.bumptech.glide.Glide


class SequenceQuizFragment : Fragment() {

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

        val questionUrls = listOf(
            "https://storage.googleapis.com/ishara_file_storage/file/2024/06/18/zoZeKa75FfK4ZbI9XHzH.png",
            "https://storage.googleapis.com/ishara_file_storage/file/2024/06/18/zoZeKa75FfK4ZbI9XHzH.png",
            "https://storage.googleapis.com/ishara_file_storage/file/2024/06/18/qfCTiSQOKUQO4ZEiP0zP.png",
            "https://storage.googleapis.com/ishara_file_storage/file/2024/06/18/zoZeKa75FfK4ZbI9XHzH.png"
        )

        if (questionUrls.size >= 4) {
            Glide.with(this).load(questionUrls[0]).into(binding.ivImage1)
            Glide.with(this).load(questionUrls[1]).into(binding.ivImage2)
            Glide.with(this).load(questionUrls[2]).into(binding.ivImage3)
            Glide.with(this).load(questionUrls[3]).into(binding.ivImage4)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}