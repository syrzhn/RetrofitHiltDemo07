package ru.syrzhn.retrofithiltdemo07.ui.login

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import ru.syrzhn.data.network.ApiService
import ru.syrzhn.data.network.UsersList
import ru.syrzhn.data.network.createRetrofit
import ru.syrzhn.retrofithiltdemo07.R
import ru.syrzhn.retrofithiltdemo07.databinding.FragmentLoginBinding
import ru.syrzhn.retrofithiltdemo07.ui.list.DataStorage

@AndroidEntryPoint
class LoginFragment : Fragment() {

    @Inject
    lateinit var usersList: UsersList

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val apiService: ApiService = createRetrofit(requireContext()).create(ApiService::class.java)
        lifecycleScope.launch {
            try {
                val response = apiService.getCourses()
                response.courses.forEach {
                    DataStorage.addItem(it)
                    Log.d("CourseData", "Id: ${it.id}, Age: ${it.title}")
                }
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]

        val usernameEditText = binding.username
        val passwordEditText = binding.password.editText
        val loginButton = binding.login
        val loadingProgressBar = binding.loading

        loginViewModel.loginFormState.observe(
            viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                loginButton.isEnabled = loginFormState.isDataValid
                loginFormState.usernameError?.let {
                    usernameEditText.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    passwordEditText?.error = getString(it)
                }
            })

        loginViewModel.loginResult.observe(
            viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer
                loadingProgressBar.visibility = View.GONE
                loginResult.error?.let {
                    showLoginFailed(it)
                }
                loginResult.success?.let {
                    updateUiWithUser(it)
                }
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    usernameEditText.text.toString(),
                    passwordEditText?.text.toString()
                )
            }
        }
        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText?.addTextChangedListener(afterTextChangedListener)
        passwordEditText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
            false
        }
        passwordEditText?.onFocusChangeListener = OnFocusChangeListener { view, hasFocus ->
            val id = view.id
            if (hasFocus)
                passwordEditText.hint = ""
            else
                passwordEditText.hint = "$id"
        }

        loginButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            loginViewModel.login(
                usernameEditText.text.toString(),
                passwordEditText?.text.toString()
            )
        }

        val vkButton = binding.buttonVK
        vkButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, "https://vk.com".toUri())
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                // Handle case where no browser or app can handle the intent
                Toast.makeText(requireContext(), "Невозможно перейти на \"VK.com\"", Toast.LENGTH_SHORT).show()
            }
        }

        val okButton = binding.buttonOK
        okButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, "https://ok.ru".toUri())
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                // Handle case where no browser or app can handle the intent
                Toast.makeText(requireContext(), "Невозможно перейти на \"ok.ru\"", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUiWithUser(message: String) {
        val welcome = getString(R.string.welcome) + " $message"
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
        findNavController().navigate(R.id.action_LoginFragment_to_itemFragment)
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
        findNavController().navigate(R.id.action_LoginFragment_to_SecondFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}