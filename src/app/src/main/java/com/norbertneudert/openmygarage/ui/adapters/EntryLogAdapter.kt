package com.norbertneudert.openmygarage.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.norbertneudert.openmygarage.data.entities.EntryLog
import com.norbertneudert.openmygarage.databinding.LogItemViewBinding
import com.norbertneudert.openmygarage.util.Util

class EntryLogAdapter : ListAdapter<EntryLog, EntryLogAdapter.ViewHolder>(LogDiffCallback()){

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: LogItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EntryLog) {
            binding.plateLogTw.text = item.plate
            binding.timeLogTw.text = Util.getFormattedDateFromLong(item.entryTime)
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LogItemViewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class LogDiffCallback : DiffUtil.ItemCallback<EntryLog>() {
    override fun areItemsTheSame(oldItem: EntryLog, newItem: EntryLog): Boolean {
        return oldItem.logId == newItem.logId
    }

    override fun areContentsTheSame(oldItem: EntryLog, newItem: EntryLog): Boolean {
        return oldItem == newItem
    }
}