package app.bangkit.ishara.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.bangkit.ishara.ui.auth.login.LoginViewModel
import app.bangkit.ishara.ui.main.MainViewModel
import app.bangkit.ishara.data.preferences.UserPreference
import app.bangkit.ishara.ui.game.ScoreViewModel

class ViewModelFactory(private val pref: UserPreference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(pref) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(pref) as T
        } else if (modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
            return ScoreViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}