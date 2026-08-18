package ru.syrzhn.retrofithiltdemo07.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.syrzhn.data.network.Course
import ru.syrzhn.domain.toRussiaDate
import ru.syrzhn.retrofithiltdemo07.databinding.FragmentItemBinding

/**
 * [androidx.recyclerview.widget.RecyclerView.Adapter] that can display a [Course].
 */
class CoursesRecyclerViewAdapter(
    private val context: Context,
    private val values: MutableList<Course>
) : RecyclerView.Adapter<CoursesRecyclerViewAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun addItem(item: Course) {
        values.add(item)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = values[position]
        holder.idView.text = item.id.toString()
        holder.titleView.text = item.title
        holder.textView.text = item.text
        holder.priceView.text = item.price + " ₽"
        holder.rateView.text = item.rate.toString()
        holder.startDateView.text = item.startDate.toRussiaDate()
        val hasLike = item.hasLike
        if (hasLike) {
            holder.bookmark.background = ContextCompat.getDrawable(context,ru.syrzhn.retrofithiltdemo07.R.drawable.bookmark_favor)
        } else {
            holder.bookmark.background = ContextCompat.getDrawable(context,ru.syrzhn.retrofithiltdemo07.R.drawable.bookmark)
        }
        holder.publishDateView.text = item.publishDate.toRussiaDate()

        // Set click listener for the item view
        holder.itemView.setOnClickListener { view ->
            onClickListener?.onClick(position, item)
            Toast.makeText(view.context , "Recycle Click $position", Toast.LENGTH_SHORT).show()
        }

        holder.bookmark.setOnClickListener { view ->
            onClickListener?.onLikeClick(position)
            notifyItemChanged(position)
            Toast.makeText(view.context , "Recycle Click $position, has like is ${item.hasLike}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = values.size

    class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemId
        val titleView: TextView = binding.title
        val textView: TextView = binding.text
        val priceView: TextView = binding.price
        val rateView: TextView = binding.rate
        val startDateView: TextView = binding.startDate
        val publishDateView: TextView = binding.publishDate
        val bookmark: ImageView = binding.bookmark

        override fun toString(): String {
            return super.toString() + " '" + titleView.text + "'"
        }
    }

    // Set the click listener for the adapter
    fun setOnClickListener(listener: OnClickListener?) {
        this.onClickListener = listener
    }

    // Interface for the click listener
    interface OnClickListener {
        fun onClick(position: Int, item: Course)
        fun onLikeClick(position: Int)
    }
}
