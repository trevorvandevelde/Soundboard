package com.example.soundboard

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var viewModel: SettingsViewModel
    private lateinit var signoutButton : Button
    private lateinit var mAuth : FirebaseAuth
    private lateinit var userReference : DatabaseReference

    private lateinit var descriptionEditText : EditText
    private lateinit var nicknameEditText : EditText
    private lateinit var changeNicknameButton: Button
    private lateinit var changeDescriptionButton : Button



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.settings_fragment, container, false)
        mAuth = FirebaseAuth.getInstance()
        userReference = FirebaseDatabase.getInstance().reference.child("Users").child(mAuth.currentUser!!.uid)

        signoutButton = view.findViewById(R.id.signoutButton)
        signoutButton.setOnClickListener(){
            mAuth.signOut()
            requireActivity().finish()

            /*
            //another option
            val intent = Intent(requireActivity(), LaunchActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            */
        }

        nicknameEditText = view.findViewById(R.id.userNicknameEditText)
        descriptionEditText = view.findViewById(R.id.userDescriptionEditText)

        changeNicknameButton = view.findViewById(R.id.userNicknameButton)
        changeDescriptionButton = view.findViewById(R.id.userDescriptionButton)

        changeNicknameButton.setOnClickListener{
            val newNickname = nicknameEditText.text.toString()
            userReference.child("userNickname").setValue(newNickname).addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(requireContext(), "Changed nickname", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(), "Error changing nickname: "+ it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

        changeDescriptionButton.setOnClickListener{
            val newDescription = descriptionEditText.text.toString()
            userReference.child("userDescription").setValue(newDescription).addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(requireContext(), "Changed description", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(), "Error changing description: "+ it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
    }


    /**
     *  var soundByte = SoundByte()
    soundByte.SoundByte(songName, imageUrl, songUrl, uploader, description, tags)
    FirebaseDatabase.getInstance().getReference("Audio").push().setValue(soundByte)
    .addOnCompleteListener{
    Toast.makeText(this, "Added File Info to Database", Toast.LENGTH_SHORT).show()
    progressDialog.dismiss()
    finish()

    }.addOnFailureListener{
    Toast.makeText(this, "Failed to Add to Database", Toast.LENGTH_SHORT).show()
    }
     */

}