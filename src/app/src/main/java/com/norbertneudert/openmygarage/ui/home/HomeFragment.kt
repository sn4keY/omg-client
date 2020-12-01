package com.norbertneudert.openmygarage.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.norbertneudert.openmygarage.R
import com.norbertneudert.openmygarage.data.InAppDatabase
import com.norbertneudert.openmygarage.databinding.FragmentHomeBinding
import com.norbertneudert.openmygarage.service.EntryLogsRepository
import com.norbertneudert.openmygarage.ui.adapters.EntryLogAdapter

class HomeFragment : Fragment() {

    private lateinit var homeFragmentViewModel: HomeFragmentViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var entryLogsRepository: EntryLogsRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        val dataSource = InAppDatabase.getInstance(requireContext()).entryLogDao
        entryLogsRepository = EntryLogsRepository.getInstance(dataSource)
        val application = requireNotNull(this.activity).application
        val viewModelFactory = HomeFragmentViewModelFactory(dataSource, application)
        homeFragmentViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeFragmentViewModel::class.java)
        binding.homeFragmentViewModel = homeFragmentViewModel
        binding.setLifecycleOwner(this)

        val navController = this.findNavController()
        val adapter = EntryLogAdapter(navController)
        binding.homeRecyclerView.adapter = adapter

        homeFragmentViewModel.logs.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.homeSwipeRefreshLayout.setOnRefreshListener {
            refreshDatabase()
        }

        binding.homeFab.setOnClickListener {
            Toast.makeText(context, "Opening/Closing Gate", Toast.LENGTH_SHORT).show()
            toggleGate()
        }

        return binding.root
    }

    private fun toggleGate() {
        entryLogsRepository.toggleGate()
    }

    private fun refreshDatabase() {
        binding.homeSwipeRefreshLayout.isRefreshing = entryLogsRepository.refreshDatabase()
    }
}