package com.norbertneudert.openmygarage.ui.logs.extended

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.norbertneudert.openmygarage.R
import com.norbertneudert.openmygarage.data.InAppDatabase
import com.norbertneudert.openmygarage.data.entities.EntryLog
import com.norbertneudert.openmygarage.databinding.ExtendedLogFragmentBinding
import com.norbertneudert.openmygarage.service.EntryLogsRepository
import com.norbertneudert.openmygarage.util.Util
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ExtendedLogFragment : Fragment() {

    companion object {
        fun newInstance(bundle: Bundle) : ExtendedLogFragment {
            val fragment = ExtendedLogFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var entryLog: EntryLog
    private lateinit var binding: ExtendedLogFragmentBinding
    private lateinit var image: Bitmap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        entryLog = arguments?.get("item") as EntryLog
        binding = DataBindingUtil.inflate(inflater, R.layout.extended_log_fragment, container, false)

        binding.entryLog = entryLog
        binding.extendedTwEntry.setText(Util.getInstance().getFormattedDateFromLong(entryLog.entryTime))

        val dataSource = InAppDatabase.getInstance(requireContext()).entryLogDao
        val apiHandlerEntryLogs = EntryLogsRepository.getInstance(dataSource)
        requireActivity().runOnUiThread( Runnable {
            val job = GlobalScope.launch {
                image = apiHandlerEntryLogs.getPicture(entryLog.id)
            }
            while (!job.isCompleted) {
                Log.i("Download", "Running")
            }
            if (job.isCompleted) {
                binding.extendedImage.setImageBitmap(image)
            }
        })

        return binding.root
    }
}