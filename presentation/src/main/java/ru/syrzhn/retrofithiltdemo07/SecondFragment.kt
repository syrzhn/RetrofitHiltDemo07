package ru.syrzhn.retrofithiltdemo07

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import ru.syrzhn.data.network.UsersList
import ru.syrzhn.retrofithiltdemo07.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class SecondFragment : Fragment() {

    @Inject
    lateinit var usersList: UsersList

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogin.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_LoginFragment)
        }

        binding.buttonList.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_itemFragment)
        }
        binding.usersList.text = usersList.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
