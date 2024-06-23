package app.bangkit.ishara.ui.game.types

import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.bangkit.ishara.data.responses.journey.QuestionItem
import app.bangkit.ishara.databinding.FragmentSignQuizBinding
import app.bangkit.ishara.domain.helper.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat
import java.util.concurrent.Executors

class SignQuizFragment : Fragment() {

    private var _binding: FragmentSignQuizBinding? = null
    private val binding get() = _binding!!

    private var questionItem: QuestionItem? = null

    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private var isHandDetected: Boolean = false
    private var letter: String? = null

    private var displayResult: String? = null

    private var correctAnswer: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideSystemUI()
        startCamera()

        questionItem = arguments?.getParcelable(ARG_QUESTION_ITEM)

        letter = extractLetterFromQuestion(questionItem?.question)
        binding.tvLetter.text = letter
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startCamera() {
        imageClassifierHelper = ImageClassifierHelper(
            context = requireContext(),
            isHandDetectedListener = object : ImageClassifierHelper.IsHandDetectedListener {
                override fun onHandDetected() {
                    isHandDetected = true
                }

                override fun onNoHandDetected() {
                    isHandDetected = false
                }
            },
            imageClassifierListener = object : ImageClassifierHelper.ImageClassifierListener {
                override fun onError(error: String) {
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    requireActivity().runOnUiThread {
                        if (isHandDetected && !results.isNullOrEmpty() && results[0].categories.isNotEmpty()) {
                            val sortedCategories = results[0].categories.sortedByDescending { it.score }
                            val topThreeCategories = sortedCategories.take(3)
                            val displayResultText = topThreeCategories.joinToString("\n") {
                                "${it.label} " + NumberFormat.getPercentInstance().format(it.score).trim()
                            }
                            binding.tvResult.text = displayResultText

                            displayResult = sortedCategories[0].label[0].toString()
                            checkLetter(displayResult)

                        } else {
                            binding.tvResult.text = ""
                        }
                    }
                }
            }
        )

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        val displayManager = requireContext().getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        val display = displayManager.getDisplay(Display.DEFAULT_DISPLAY)
        val rotation = display?.rotation

        cameraProviderFuture.addListener({
            val resolutionSelector = ResolutionSelector.Builder()
                .setAspectRatioStrategy(AspectRatioStrategy.RATIO_16_9_FALLBACK_AUTO_STRATEGY)
                .build()

            val imageAnalyzer = ImageAnalysis.Builder()
                .setResolutionSelector(resolutionSelector)
                .setTargetRotation(rotation ?: 0)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build()

            imageAnalyzer.setAnalyzer(Executors.newSingleThreadExecutor()) { image ->
                imageClassifierHelper.classifyImage(image)
                image.close()
            }

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalyzer
                )
            } catch (exc: Exception) {
                requireActivity().runOnUiThread {
                    Toast.makeText(
                        requireContext(),
                        "Gagal memunculkan kamera.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Log.e(TAG, "startCamera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun checkLetter(detectedLetter: String?) {
        if (detectedLetter.equals(letter, ignoreCase = true)) {
            showToast("Benar!")
        }
    }

    fun getUserAnswer(): String? {
        Log.d(TAG, "getUserAnswer: $correctAnswer")
        return correctAnswer ?: "wrong"
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "SignQuizFragment"
        private const val QUIZ_LETTER_EXTRA_KEY = "QUIZ_LETTER_EXTRA"
        const val EXTRA_CAMERAX_IMAGE = "CameraX Image"
        const val CAMERAX_RESULT = 200

        private const val ARG_QUESTION_ITEM = "question_item"
        fun newInstance(questionItem: QuestionItem): SignQuizFragment {
            return SignQuizFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_QUESTION_ITEM, questionItem)
                }
            }
        }
    }
}
