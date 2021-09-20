package com.stanroy.todotaskmanager.presentation.tasks_list


import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stanroy.todotaskmanager.R
import com.stanroy.todotaskmanager.databinding.SingleTaskListItemBinding
import com.stanroy.todotaskmanager.domain.model.SingleTask
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class TasksListRVAdapter :
    ListAdapter<SingleTask, TasksListRVAdapter.TasksViewHolder>(diffUtilCallback) {

    companion object {
        val diffUtilCallback = object : DiffUtil.ItemCallback<SingleTask>() {
            override fun areItemsTheSame(oldItem: SingleTask, newItem: SingleTask): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: SingleTask, newItem: SingleTask): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    private lateinit var onTaskStateChange: (singleTask: SingleTask) -> Unit

    fun setOnTaskStateChangeListener(
        onStateChange: (singleTask: SingleTask) -> Unit
    ) {
        onTaskStateChange = onStateChange

    }


    inner class TasksViewHolder(binding: SingleTaskListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val resources: Resources = binding.root.resources
        val itemBinding = binding

    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TasksListRVAdapter.TasksViewHolder {
        val binding = DataBindingUtil.inflate<SingleTaskListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.single_task_list_item,
            parent, false
        )

        return TasksViewHolder(binding)
    }


    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val itemBinding = holder.itemBinding
        val resources = holder.resources
        val task = getItem(position)

        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val dateString = simpleDateFormat.format(task.taskDueDate)

        itemBinding.tvTaskTitle.text = task.taskName.lowercase()
        itemBinding.tvTaskDueDate.text =
            resources.getString(R.string.tv_due_date_title, dateString).lowercase()
        itemBinding.tvTaskCategory.text =
            resources.getString(R.string.tv_category_title, task.taskCategory.toString())
                .lowercase()



        if (!task.taskIsFinished) {
            itemBinding.cbFinishTask.isChecked = false
            itemBinding.cbFinishTask.isEnabled = true
            itemBinding.llFinishedTask.visibility = View.GONE
        } else {
            itemBinding.cbFinishTask.isChecked = true
            itemBinding.cbFinishTask.isEnabled = false
            itemBinding.llFinishedTask.visibility = View.VISIBLE
        }

        itemBinding.cbFinishTask.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                Timber.tag("RVAdapter").d(p1.toString())
                onTaskStateChange(task)
                itemBinding.cbFinishTask.isChecked = true
                itemBinding.cbFinishTask.isEnabled = false
                itemBinding.llFinishedTask.visibility = View.VISIBLE
            }

        })


    }
}