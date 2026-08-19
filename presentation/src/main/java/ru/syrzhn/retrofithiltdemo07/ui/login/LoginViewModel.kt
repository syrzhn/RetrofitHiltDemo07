package ru.syrzhn.retrofithiltdemo07.ui.login

import android.util.Log
import android.util.Patterns
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.syrzhn.data.network.ApiService
import ru.syrzhn.data.network.Course
import ru.syrzhn.data.network.UsersList
import ru.syrzhn.data.network.createRetrofit
import ru.syrzhn.retrofithiltdemo07.R
import ru.syrzhn.retrofithiltdemo07.ui.list.DataStorage
import kotlin.time.Duration.Companion.milliseconds

class LoginViewModel(private val usersList: UsersList) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) =  viewModelScope.launch {
        delay(1000.milliseconds)
        // can be launched in a separate asynchronous job
        val result = usersList.isValid(username, password)

        if (result) {
            _loginResult.value =
                LoginResult(success = username)
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
