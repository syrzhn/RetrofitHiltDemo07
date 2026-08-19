package ru.syrzhn.retrofithiltdemo07.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import ru.syrzhn.data.network.Course
import ru.syrzhn.retrofithiltdemo07.R
import ru.syrzhn.retrofithiltdemo07.databinding.FragmentItemListBinding
import ru.syrzhn.retrofithiltdemo07.ui.list.adapterdelegate.CompositeAdapter
import ru.syrzhn.retrofithiltdemo07.ui.list.adapterdelegate.CourseAdapter

/**
 * A fragment representing a list of Items.
 */
@AndroidEntryPoint
class ItemFragment : Fragment() {

    private val compositeAdapter by lazy {
        CompositeAdapter.Builder()
            .add(CourseAdapter(
                context = requireContext(),
                values = DataStorage.ITEMS,
                onCourseClickListener = onCourseClickListener
            ))
            .build()
    }

    private val itemsListViewModel by viewModels<ItemsListViewModel>(
        extrasProducer = {
            defaultViewModelCreationExtras.withCreationCallback<ItemsListViewModel.Factory> { factory ->
                factory.create(items = DataStorage.ITEMS) // передаём динамический параметр
            }
        }
    )

    private var columnCount = 1
    private lateinit var recyclerView: RecyclerView
    private lateinit var clickListener: CoursesRecyclerViewAdapter.OnClickListener
    private lateinit var onCourseClickListener: CourseAdapter.OnCourseClickListener

    private var _binding: FragmentItemListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        _binding = FragmentItemListBinding.inflate(inflater, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            recyclerView = view
        } else {
            recyclerView = view.findViewById(R.id.courses_list)
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = CoursesRecyclerViewAdapter(requireContext(),DataStorage.ITEMS)

        clickListener = object : CoursesRecyclerViewAdapter.OnClickListener {
            override fun onClick(position: Int, item: Course) {
                // navigate to details fragment
            }

            override fun onLikeClick(position: Int ) {
                itemsListViewModel.setLike(position)
            }
        }
        (recyclerView.adapter as CoursesRecyclerViewAdapter).setOnClickListener(clickListener)

        val sortButton = view.findViewById<RelativeLayout>(R.id.sort)
        sortButton.setOnClickListener { view ->
            itemsListViewModel.sort()
        }

        val mainButton = view.findViewById<RelativeLayout>(R.id.main)
        mainButton.setOnClickListener {
            itemsListViewModel.goHome()
        }

        val favoriteButton = view.findViewById<RelativeLayout>(R.id.favorite)
        favoriteButton.setOnClickListener {
            itemsListViewModel.goFavorite()
        }

        onCourseClickListener = object : CourseAdapter.OnCourseClickListener {
            override fun onClick(position: Int, item: Course) {
                // navigate to details fragment
            }

            override fun onLikeClick(isLiked: Boolean ) {
                //itemsListViewModel.setLike(position)
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemsListViewModel.coursesList.observe(
            viewLifecycleOwner,
            Observer { itemsList ->
                if (itemsListViewModel.updateWholeList) {
                    val adapter = CoursesRecyclerViewAdapter(requireContext(), itemsList)
                    adapter.setOnClickListener(clickListener)
                    recyclerView.adapter = adapter
                }
            }
        )

        itemsListViewModel.scrollToEnd.observe(
            viewLifecycleOwner,
            Observer { scroll ->
                if (scroll) {
                    recyclerView.scrollToPosition(recyclerView.adapter!!.itemCount - 1)
                }
            }
        )

        itemsListViewModel.loading.observe(
            viewLifecycleOwner,
            Observer { load ->
                binding.loadingCourses.visibility = if (load)
                    View.VISIBLE
                else
                    View.GONE
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            ItemFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}