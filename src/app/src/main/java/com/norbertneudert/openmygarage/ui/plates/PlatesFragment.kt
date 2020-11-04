package com.norbertneudert.openmygarage.ui.plates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.norbertneudert.openmygarage.R
import com.norbertneudert.openmygarage.data.InAppDatabase
import com.norbertneudert.openmygarage.data.entities.StoredPlate
import com.norbertneudert.openmygarage.databinding.FragmentPlatesBinding
import com.norbertneudert.openmygarage.service.ApiHandlerStoredPlates
import com.norbertneudert.openmygarage.ui.adapters.PlateAdapter
import com.norbertneudert.openmygarage.ui.plates.editor.EditPlateFragment

class PlatesFragment : Fragment(), EditPlateFragment.EditPlateDialogListener {

    private lateinit var platesViewModel: PlatesViewModel
    private lateinit var binding: FragmentPlatesBinding
    private lateinit var apiHandler: ApiHandlerStoredPlates

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_plates, container, false)

        val dataSource = InAppDatabase.getInstance(requireContext()).storedPlateDao
        apiHandler = ApiHandlerStoredPlates(dataSource)

        val viewModelFactory = PlatesViewModelFactory(dataSource, requireActivity().application)
        platesViewModel = ViewModelProviders.of(this, viewModelFactory).get(PlatesViewModel::class.java)

        binding.viewModel = platesViewModel
        binding.setLifecycleOwner(this)

        val adapter = PlateAdapter(apiHandler, this.parentFragmentManager, this)
        binding.pfList.adapter = adapter

        platesViewModel.plates.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.pfFab.setOnClickListener {
            showEditor()
        }

        return binding.root
    }

    override fun onFinishedEditing(storedPlate: StoredPlate, exists: Boolean) {
        if (exists) {
            apiHandler.updateStoredPlate(storedPlate)
        } else {
            apiHandler.addStoredPlate(storedPlate)
        }
    }

    private fun showEditor() {
        val editor = EditPlateFragment.newInstance(StoredPlate(), false)
        editor.setTargetFragment(this, 300)
        editor.show(this.parentFragmentManager, "dialog")
    }
}