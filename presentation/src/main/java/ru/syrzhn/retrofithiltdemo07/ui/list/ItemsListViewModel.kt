package ru.syrzhn.retrofithiltdemo07.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.syrzhn.data.network.Course
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel(assistedFactory = ItemsListViewModel.Factory::class)
class ItemsListViewModel @AssistedInject constructor(@Assisted val items: MutableList<Course>) : ViewModel()  {

    @AssistedFactory
    interface Factory {
        fun create(items: MutableList<Course>): ItemsListViewModel
    }

    private val _coursesList = MutableLiveData<MutableList<Course>>()
    val coursesList: LiveData<MutableList<Course>> = _coursesList

    private val _scrollToEnd = MutableLiveData(false)
    val scrollToEnd: LiveData<Boolean> = _scrollToEnd

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    var updateWholeList = true

    init {
        loadBbyParts()
    }

    fun loadBbyParts() = viewModelScope.launch {
        updateWholeList = true
        items.sortBy { it.id }
        val temp = mutableListOf<Course>()
        temp.addAll(items)
        items.clear()
        temp.forEach { course ->
            _scrollToEnd.value = false
            _loading.value = true
            delay(1000.milliseconds)
            items.add(course)
            _coursesList.value = items
            _scrollToEnd.value = true
            _loading.value = false
        }
    }

    fun setLike(position: Int) = viewModelScope.launch {
        updateWholeList = false
        val currentItem = items[position]
        val changedItem = Course(
            id = currentItem.id,
            title = currentItem.title,
            text = currentItem.text,
            price = currentItem.price,
            rate = currentItem.rate,
            startDate = currentItem.startDate,
            hasLike = !currentItem.hasLike,
            publishDate = currentItem.publishDate
        )
        items[position] = changedItem
    }

    fun sort() = viewModelScope.launch {
        updateWholeList = true
//        val customComparator = Comparator<Course> { p1, p2 ->
//            p1.compareTo(p2)
//        }
//        items.sortWith ( customComparator )
        items.sortByDescending { it.publishDate }
        _coursesList.value = items
    }

    fun goHome() {
        updateWholeList = true
        items.sortBy { it.id }
        _coursesList.value = items
    }

    fun goFavorite() {
        updateWholeList = true
        _coursesList.value = items.filter { it.hasLike } as MutableList<Course>?
    }
}