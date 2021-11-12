package com.example.soundboard

import android.widget.Toast
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.provider.MediaStore
import android.text.InputType
import android.widget.*
import java.util.*

class Add_Board_Dialog: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(
            R.layout.add_board_dialog, null
        )
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Add Board")
            .setView(view)
            .setPositiveButton("OK"){ _, _ ->
                dismiss()
            }
            .setNegativeButton("CANCEL"){ _, _ ->
                dismiss()
            }

        return builder.create()
    }
}