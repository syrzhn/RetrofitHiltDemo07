package ru.syrzhn.retrofithiltdemo07.Blank

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(): ViewModel() {

    // ...

}