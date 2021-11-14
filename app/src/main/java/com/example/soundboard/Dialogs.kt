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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
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
                val text = view.findViewById<EditText>(R.id.add_board_dialog).text.toString()

                val user_reference : (DatabaseReference) = FirebaseDatabase.getInstance().getReference("Users").child(
                    FirebaseAuth.getInstance().currentUser!!.uid)

                val user_event_listener =  object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        //for (ds in snapshot.child("Audio").children)
                        val user : User? =  snapshot.getValue(User::class.java)
                        if(user != null){
                            val sb : SoundBoard =  SoundBoard()
                            sb.SoundBoard(text)
                            user.addSoundBoard(sb)
                            user_reference.child("soundBoardList").setValue(user.getSoundBoardList())
                            Toast.makeText(requireContext(), "Successfully Created!", Toast.LENGTH_SHORT)
                                .show()
                        }
                        }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                }

                user_reference.addListenerForSingleValueEvent(user_event_listener)

                dismiss()
            }
            .setNegativeButton("CANCEL"){ _, _ ->
                dismiss()
            }

        return builder.create()
    }
}

class Delete_Board_Dialog: DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(
            R.layout.text_dialog_layout, null
        )
        val textview = view.findViewById<TextView>(R.id.dialog_info)
        textview.setText("Are you sure you want to delete this board? ")

        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Delete Board")
            .setView(view)
            .setPositiveButton("Yes") { _, _ ->
                dismiss()
            }
            .setNegativeButton("No") { _, _ ->
                dismiss()
            }
        return builder.create()
    }
}

class Delete_Soundbyte_Dialog: DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(
            R.layout.text_dialog_layout, null
        )
        val textview = view.findViewById<TextView>(R.id.dialog_info)
        textview.setText("Are you sure you want to delete this soundbyte from this board? ")

        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Delete Soundbyte")
            .setView(view)
            .setPositiveButton("Yes") { _, _ ->
                dismiss()
            }
            .setNegativeButton("No") { _, _ ->
                dismiss()
            }
        return builder.create()
    }
}