package ru.syrzhn.retrofithiltdemo07.Blank

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import ru.syrzhn.retrofithiltdemo07.R
import ru.syrzhn.retrofithiltdemo07.TAG

@AndroidEntryPoint
class OrderFragment : Fragment() {

    @Inject
    lateinit var repository: OrderRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "fragment repository = $repository")
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_order, container, false)
    }

}