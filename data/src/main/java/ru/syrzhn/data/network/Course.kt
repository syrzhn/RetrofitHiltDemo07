package ru.syrzhn.data.network

import java.text.SimpleDateFormat
import java.util.Locale

data class Course(
    val id: Int,
    val title: String,
    val text: String,
    val price: String,
    val rate: Float,
    val startDate: String,
    val hasLike: Boolean,
    val publishDate: String
) : Comparable <Course> {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    override fun compareTo(other: Course): Int {
        val text1 = "2022-01-06"
        val otherDate = formatter.parse(other.publishDate)
        val valueDate = formatter.parse(publishDate)
        return valueDate!! compareTo otherDate
    }
}