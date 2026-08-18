package ru.syrzhn.retrofithiltdemo07.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.syrzhn.data.network.UsersList

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required to be given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                usersList = UsersList()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}