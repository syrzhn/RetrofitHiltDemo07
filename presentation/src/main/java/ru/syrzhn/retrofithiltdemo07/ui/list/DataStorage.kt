package ru.syrzhn.retrofithiltdemo07.ui.list

import ru.syrzhn.data.network.Course
import java.util.ArrayList
import java.util.HashMap

/**
 * Holder class for loaded content
 */
object DataStorage {

    /**
     * An array loaded items.
     */
    val ITEMS: MutableList<Course> = ArrayList()

    /**
     * A map of course items, by ID.
     */
    val ITEM_MAP: MutableMap<Int, Course> = HashMap()

    fun addItem(item: Course) {
        ITEMS.add(item)
        ITEM_MAP[item.id] = item
    }
}