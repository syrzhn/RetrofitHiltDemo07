package ru.syrzhn.data.network

import ru.syrzhn.data.network.adapterdelegate.DelegateAdapterItem
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
) : Comparable <Course>, DelegateAdapterItem {

    val formatter = SimpleDateFormat("yyyy-MM-dd")

    override fun compareTo(other: Course): Int {
        val otherDate = formatter.parse(other.publishDate)
        val valueDate = formatter.parse(publishDate)
        return valueDate!! compareTo otherDate
    }

    override fun id(): Any = id

    override fun content(): Any = CourseContent(title, publishDate)

    override fun payload(other: Any): DelegateAdapterItem.Payloadable {
        if (other is CourseContent) {
            if (title != other.title) {
                return ChangePayload.TitleChanged(other.title)
            }

            if (publishDate != other.publishDate) {
                return ChangePayload.PublishDateChanged(other.publishDate)
            }
        }
        return DelegateAdapterItem.Payloadable.None
    }

    inner class CourseContent(val title: String, val publishDate: String) {
        override fun equals(other: Any?): Boolean {
            if (other is CourseContent) {
                return title == other.title && publishDate == other.publishDate
            }
            return false
        }

        override fun hashCode(): Int {
            var result = title.hashCode()
            result = 31 * result + publishDate.hashCode()
            return result
        }
    }

    sealed class ChangePayload: DelegateAdapterItem.Payloadable {
        data class TitleChanged(val title: String): ChangePayload()
        data class PublishDateChanged(val publishDate: String): ChangePayload()
    }
}