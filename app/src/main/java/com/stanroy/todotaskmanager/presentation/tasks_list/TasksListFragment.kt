package com.stanroy.todotaskmanager.presentation.tasks_list

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.stanroy.todotaskmanager.R
import com.stanroy.todotaskmanager.databinding.FragmentTasksListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class TasksListFragment : Fragment() {

    private val viewModel by viewModel<TasksListViewModel>()
    private lateinit var binding: FragmentTasksListBinding
    private lateinit var recyclerViewAdapter: TasksListRVAdapter
    private val navArgs by navArgs<TasksListFragmentArgs>()
    private var wasTaskAdded: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        wasTaskAdded = navArgs.wasTaskAdded
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when (item.title) {
            resources.getString(R.string.menu_item_clear_tasks) -> {
                viewModel.onClearTasksClicked()
            }
        }


        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView()
        handleTaskStateChanged()

        if (wasTaskAdded) {
            viewModel.onNewTaskAdded()
            Timber.tag("ListFrag").d("New Task Added")
        }


        viewModel.currentTaskList.observe(viewLifecycleOwner, { taskList ->
            if (taskList.isEmpty() || taskList == null) {
                showEmptyListMessage()
                recyclerViewAdapter.submitList(emptyList())
            } else if (recyclerViewAdapter.currentList != taskList) {
                hideEmptyListMessage()
                recyclerViewAdapter.submitList(taskList.sortedBy {
                    it.taskIsFinished
                })
            }
        })

        viewModel.singleItemChanged.observe(viewLifecycleOwner, { itemChanged ->
            val list = recyclerViewAdapter.currentList
            val recyclerView = binding.rvTasks
            if (itemChanged && !recyclerView.isComputingLayout && recyclerView.scrollState == SCROLL_STATE_IDLE) {
                recyclerViewAdapter.submitList(list.sortedBy { it.taskIsFinished })
            }

        })


        binding.fabAddTask.setOnClickListener {
            val action = TasksListFragmentDirections.actionTasksListFragmentToNewTaskFragment()
            findNavController().navigate(action)
        }

    }

    private fun handleTaskStateChanged() {
        recyclerViewAdapter.setOnTaskStateChangeListener { task ->
            viewModel.onTaskFinished(task)
        }
    }

    private fun initializeRecyclerView() {
        recyclerViewAdapter = TasksListRVAdapter()
        binding.rvTasks.apply {
            adapter = recyclerViewAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        recyclerViewAdapter.submitList(emptyList())
    }

    private fun showEmptyListMessage() {
        binding.tvEmptyListMsg.visibility = View.VISIBLE
    }

    private fun hideEmptyListMessage() {
        binding.tvEmptyListMsg.visibility = View.GONE
    }
}