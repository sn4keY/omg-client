package com.norbertneudert.openmygarage.ui.plates.editor

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.norbertneudert.openmygarage.R
import com.norbertneudert.openmygarage.data.InAppDatabase
import com.norbertneudert.openmygarage.data.entities.StoredPlate
import com.norbertneudert.openmygarage.databinding.FragmentEditPlateBinding
import com.norbertneudert.openmygarage.service.StoredPlatesRepository
import kotlinx.coroutines.runBlocking
import java.util.*

class EditPlateFragment(var storedPlate: StoredPlate, val exists: Boolean) : DialogFragment() {
    companion object {
        fun newInstance(storedPlate: StoredPlate, exists: Boolean) = EditPlateFragment(storedPlate, exists)
    }

    interface EditPlateDialogListener {
        fun onFinishedEditing(storedPlate: StoredPlate, exists: Boolean)
    }

    private lateinit var binding: FragmentEditPlateBinding
    private var validName: Boolean = false
    private var validPlate: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_plate, container, false)

        binding.epfNameEt.setText(storedPlate.name)
        binding.epfNameEt.validate("Give a name!", "name", binding.epfDoneButton) { s -> s.isNotEmpty() }

        binding.epfPlateEt.setText(storedPlate.plate)
        binding.epfPlateEt.validate("Give a valid plate!", "plate", binding.epfDoneButton) { s -> s.isPlate() }

        binding.epfNationEt.setText(storedPlate.nationality)

        binding.epfManufacturerEt.setText(storedPlate.carManufacturer)

        binding.epfAutoOpenSwitch.isChecked = storedPlate.autoOpen

        binding.epfDoneButton.isEnabled = false
        binding.epfDoneButton.isClickable = false
        binding.epfDoneButton.setOnClickListener {
            val listener = targetFragment as EditPlateDialogListener
            val name = binding.epfNameEt.text.toString()
            val plate = binding.epfPlateEt.text.toString()
            val nationality = binding.epfNationEt.text.toString()
            val manufacturer = binding.epfManufacturerEt.text.toString()
            val autoOpen = binding.epfAutoOpenSwitch.isChecked
            listener.onFinishedEditing(StoredPlate(
                plate = plate.toUpperCase(
                Locale.ENGLISH), name = name, nationality = nationality.toUpperCase(Locale.ENGLISH), carManufacturer = manufacturer, autoOpen = autoOpen), exists)
            dismiss()
        }

        return binding.root
    }

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                afterTextChanged.invoke(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
        })
    }

    fun EditText.validate(message: String, source: String, button: Button, validator: (String) -> Boolean) {
        this.afterTextChanged {
            if (validator(it)) {
                this.error = null
                validateSource(source)
                checkValidationAndEnableButton(button)
            } else {
                this.error = message
            }
        }
    }

    fun validateSource(source: String) {
        when(source) {
            "plate" -> validPlate = true
            else -> validName = true
        }
    }

    fun checkValidationAndEnableButton(button: Button) {
        if (validName && validPlate) {
            button.isClickable = true
            button.isEnabled = true
        }
    }

    fun String.isPlate() : Boolean {
        val plate = this
        val repo = StoredPlatesRepository.getInstance(InAppDatabase.getInstance(requireContext()).storedPlateDao)
        var existing = false
        runBlocking {
            val storedPlate = repo.get(plate)
            existing = storedPlate != null
        }
        return if (existing) {
            false
        } else {
            this.length == 7 && this[3] == '-'
        }
    }
}