package app.bangkit.ishara.ui.main.ui.journey

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.bangkit.ishara.R
import app.bangkit.ishara.data.models.Item
import app.bangkit.ishara.data.preferences.UserPreference
import app.bangkit.ishara.data.preferences.dataStore
import app.bangkit.ishara.databinding.FragmentJourneyBinding
import app.bangkit.ishara.domain.adapter.JourneyAdapter
import app.bangkit.ishara.ui.game.GameActivity
import app.bangkit.ishara.ui.game.types.SequenceQuizFragment
import kotlinx.coroutines.launch

class JourneyFragment : Fragment() {

    private var _binding: FragmentJourneyBinding? = null
    private val binding get() = _binding!!
    private lateinit var journeyViewModel: JourneyViewModel
    private lateinit var journeyAdapter: JourneyAdapter
    private var pref: UserPreference? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        pref = UserPreference.getInstance(requireActivity().application.dataStore)
        journeyViewModel = ViewModelProvider(this)[JourneyViewModel::class.java]
        _binding = FragmentJourneyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        journeyAdapter = JourneyAdapter()

        journeyAdapter.setOnItemClickCallback(object : JourneyAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Item) {
                when (data) {
                    is Item.StageItem -> {
                        if(data.stageData.name == "Abjad 1"){
                            Log.d(TAG, "ABJAD SATUUUUUUU")
                            //TODO: add learning page
                        }
                    }
                    is Item.LevelItem -> {
                        Log.d(TAG, "Level item clicked: ${data.levelData.name}")
                        Log.d(TAG, "Item clicked: $data")
                        if (data.levelData.isStageUnlocked == true) {
                            val intent = Intent(requireActivity(), GameActivity::class.java).apply{
                                putExtra("LEVEL_ID", data.levelData.id)
                            }
                            startActivity(intent)
//                            findNavController().navigate(R.id.action_navigation_journey_to_signQuizFragment)
                        } else {
                            showToast("Level belum terbuka")
                        }
                    }
                }
            }
        })

        binding.recyclerViewJourney.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = journeyAdapter
        }

        journeyViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            Log.d(TAG, "Is loading: $isLoading")
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.recyclerViewJourney.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        journeyViewModel.journeyList.observe(viewLifecycleOwner) { items ->
            Log.d("JourneyFragment", "Received items: $items")
            items.let {
                journeyAdapter.submitList(items)
            }
        }

        pref = UserPreference.getInstance(requireActivity().application.dataStore)

        lifecycleScope.launch {
            getAccessToken()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private suspend fun getAccessToken() {
        pref?.getJwtAccessToken()?.collect { jwtAccessToken ->
            Log.d("JourneyFragment", "Access token: $jwtAccessToken")
            if (jwtAccessToken.isNotEmpty()) {
                journeyViewModel.getJourney(jwtAccessToken)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "JourneyFragment"
    }
}
