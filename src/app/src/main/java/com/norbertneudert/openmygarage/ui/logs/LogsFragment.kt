package com.norbertneudert.openmygarage.ui.logs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.norbertneudert.openmygarage.R
import com.norbertneudert.openmygarage.data.InAppDatabase
import com.norbertneudert.openmygarage.service.ApiHandlerEntryLogs
import com.norbertneudert.openmygarage.databinding.FragmentLogsBinding
import com.norbertneudert.openmygarage.ui.adapters.EntryLogAdapter

class LogsFragment : Fragment() {

    private lateinit var logsViewModel: LogsViewModel
    private lateinit var binding: FragmentLogsBinding
    private lateinit var apiHandler: ApiHandlerEntryLogs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_logs, container, false)

        val dataSource = InAppDatabase.getInstance(requireContext()).entryLogDao
        apiHandler = ApiHandlerEntryLogs.getInstance(dataSource)

        val viewModelFactory = LogsViewModelFactory(dataSource, requireActivity().application)
        logsViewModel = ViewModelProviders.of(this, viewModelFactory).get(LogsViewModel::class.java)
        binding.viewModel = logsViewModel
        binding.setLifecycleOwner(this)

        val navController = this.findNavController()
        val adapter = EntryLogAdapter(navController)
        binding.logList.adapter = adapter

        logsViewModel.logs.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.logRefreshLayout.setOnRefreshListener {
            refreshDatabase()
        }

        return binding.root
    }

    private fun refreshDatabase() {
        binding.logRefreshLayout.isRefreshing = apiHandler.refreshDatabase()
    }
}