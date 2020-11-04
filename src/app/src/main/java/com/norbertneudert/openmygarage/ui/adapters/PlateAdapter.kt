package com.norbertneudert.openmygarage.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.norbertneudert.openmygarage.data.entities.StoredPlate
import com.norbertneudert.openmygarage.databinding.PlateItemViewBinding
import com.norbertneudert.openmygarage.service.ApiHandlerStoredPlates
import com.norbertneudert.openmygarage.ui.plates.editor.EditPlateFragment

class PlateAdapter(private val apiHandlerStoredPlates: ApiHandlerStoredPlates, private val supportFragmentManager: FragmentManager, private val parent: Fragment) : ListAdapter<StoredPlate, PlateAdapter.ViewHolder>(PlateDiffCallback()) {

    override fun onBindViewHolder(holder: PlateAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, parent, supportFragmentManager, apiHandlerStoredPlates)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: PlateItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StoredPlate, parent: Fragment, supportFragmentManager: FragmentManager, apiHandlerStoredPlates: ApiHandlerStoredPlates) {
            binding.piwNameTw.text = item.name
            binding.piwPlateTw.text = item.plate
            binding.piwEditButton.setOnClickListener {
                val editor = EditPlateFragment.newInstance(item, true)
                editor.setTargetFragment(parent, 300)
                editor.show(supportFragmentManager, "dialog")
            }
            binding.piwDeleteButton.setOnClickListener {
                apiHandlerStoredPlates.deleteStoredPlate(item.id)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PlateItemViewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class PlateDiffCallback : DiffUtil.ItemCallback<StoredPlate>() {
    override fun areItemsTheSame(oldItem: StoredPlate, newItem: StoredPlate): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: StoredPlate, newItem: StoredPlate): Boolean {
        return oldItem == newItem
    }
}