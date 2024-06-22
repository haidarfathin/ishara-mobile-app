package app.bangkit.ishara.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import app.bangkit.ishara.data.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers

class GameViewModel : ViewModel() {

    fun getQuestions(levelId: Int, token: String) = liveData(Dispatchers.IO) {
        val response = ApiConfig.getApiService().getQuestionsLevel("Bearer $token", levelId)
        emit(response)
    }
}
