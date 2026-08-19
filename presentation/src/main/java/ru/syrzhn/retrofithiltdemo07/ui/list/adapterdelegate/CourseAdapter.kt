package ru.syrzhn.retrofithiltdemo07.ui.list.adapterdelegate

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.syrzhn.data.network.Course
import ru.syrzhn.data.network.adapterdelegate.DelegateAdapter
import ru.syrzhn.data.network.adapterdelegate.DelegateAdapterItem
import ru.syrzhn.retrofithiltdemo07.databinding.FragmentItemBinding
import ru.syrzhn.retrofithiltdemo07.ui.list.CoursesRecyclerViewAdapter.OnClickListener

class CourseAdapter(
    private val context: Context,
    private val values: MutableList<Course>,
    private val onCourseClickListener: OnCourseClickListener
) : DelegateAdapter<Course, CourseAdapter.CourseViewHolder>(Course::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return CourseViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun bindViewHolder(
        item: Course,
        viewHolder: CourseViewHolder,
        payloads: List<DelegateAdapterItem.Payloadable>
    ) {
        viewHolder.bind(item)
    }

    override fun getItemCount(): Int = values.size

    inner class CourseViewHolder(
        private val binding: FragmentItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        val idView: TextView = binding.itemId
        val titleView: TextView = binding.title
        val textView: TextView = binding.text
        val priceView: TextView = binding.price
        val rateView: TextView = binding.rate
        val startDateView: TextView = binding.startDate
        val publishDateView: TextView = binding.publishDate
        val bookmark: ImageView = binding.bookmark

        fun bind(item: Course) {
            binding.itemId.setText(item.id)
            binding.bookmark.setOnClickListener {
                onCourseClickListener.onLikeClick(item.hasLike)
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + titleView.text + "'"
        }
    }

    // Interface for the click listener
    interface OnCourseClickListener {
        fun onClick(position: Int, item: Course)
        fun onLikeClick(isLiked: Boolean)
    }
}