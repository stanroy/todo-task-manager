package com.stanroy.todotaskmanager.presentation.new_task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.stanroy.todotaskmanager.R
import com.stanroy.todotaskmanager.databinding.FragmentNewTaskBinding
import com.stanroy.todotaskmanager.domain.model.TaskCategory.*
import com.stanroy.todotaskmanager.presentation.utils.UIState
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class NewTaskFragment : Fragment() {

    private lateinit var binding: FragmentNewTaskBinding
    private val viewModel by viewModel<NewTaskViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_task, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        initializeDropDownCategoryAdapter()
        initializeTimePicker()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddTask.setOnClickListener {
            viewModel.onAddTaskClicked()
        }

        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.uiState.observe(viewLifecycleOwner, { uiState ->

            when (uiState) {
                UIState.BOTH_EMPTY -> {
                    Snackbar.make(
                        requireView(),
                        resources.getString(R.string.empty_fields),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                UIState.TASK_EMPTY -> {
                    Snackbar.make(
                        requireView(),
                        resources.getString(R.string.empty_task_name),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                UIState.CATEGORY_EMPTY -> {
                    Snackbar.make(
                        requireView(),
                        resources.getString(R.string.empty_task_category),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                UIState.LOADING -> {
                    showProgressBar()
                }

                UIState.STOP_LOADING -> {
                    hideProgressBar()
                }

                UIState.SUCCESS -> {
                    val action =
                        NewTaskFragmentDirections.actionNewTaskFragmentToTasksListFragment()
                    action.wasTaskAdded = true

                    findNavController().navigate(action)
                    Snackbar.make(
                        requireView(),
                        resources.getString(R.string.add_task_success),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                UIState.FAILURE -> {
                    MaterialAlertDialogBuilder(
                        requireContext(),
                        R.style.AlertBoxStyle
                    )
                        .setTitle(R.string.alert_title)
                        .setMessage(R.string.add_task_failure)
                        .setNegativeButton(resources.getString(R.string.alert_decline)) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setPositiveButton(resources.getString(R.string.alert_accept)) { dialog, _ ->
                            viewModel.onAddRetry()
                            dialog.dismiss()
                        }
                        .show()
                }
            }

        })
    }

    private fun initializeDropDownCategoryAdapter() {
        val items = arrayOf(WORK, PERSONAL, SHOPPING, HOBBY, OTHERS)
//        val adapter = NoFilterArrayAdapter(requireContext(), R.layout.dropdown_category_item, items)
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_category_item, items)

        binding.actvCategory.setAdapter(adapter)
        //binding.actvCategory.freezesText = false
    }

    private fun initializeTimePicker() {
        binding.timePicker.setIs24HourView(true)
    }

    private fun showProgressBar() {
        binding.pbLoading.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.pbLoading.visibility = View.GONE
    }
}