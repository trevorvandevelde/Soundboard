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
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import android.view.View.OnFocusChangeListener


/**
 * Fragment to handle changes to user profile (nickname, description, password, etc)
 */
class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var viewModel: SettingsViewModel
    private lateinit var signoutButton : Button
    private lateinit var mAuth : FirebaseAuth
    private lateinit var userReference : DatabaseReference


    private lateinit var nicknameLayout: TextInputLayout
    private lateinit var descriptionLayout: TextInputLayout
    private lateinit var oldPasswordLayout: TextInputLayout
    private lateinit var newPasswordLayout: TextInputLayout
    private lateinit var confirmNewPasswordLayout: TextInputLayout

    private lateinit var nicknameEditText : EditText
    private lateinit var descriptionEditText : EditText
    private lateinit var oldPasswordEditText : EditText
    private lateinit var newPasswordEditText : EditText
    private lateinit var confirmNewPasswordEditText : EditText


    private lateinit var changeNicknameButton: Button
    private lateinit var changeDescriptionButton : Button
    private lateinit var changePasswordButton : Button

    private lateinit var userEmail : TextView

    private lateinit var user_reference: DatabaseReference
    private lateinit var user_event_listener:  ValueEventListener
    private val datalist = ArrayList<BoardEntry>()

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

        nicknameLayout = view.findViewById(R.id.userNicknameLayout)
        descriptionLayout = view.findViewById(R.id.userDescriptionLayout)
        oldPasswordLayout = view.findViewById(R.id.userOldPasswordLayout)
        newPasswordLayout = view.findViewById(R.id.userNewPasswordLayout)
        confirmNewPasswordLayout = view.findViewById(R.id.userConfirmNewPasswordLayout)

        nicknameEditText = nicknameLayout.editText!!
        descriptionEditText = descriptionLayout.editText!!
        userEmail = view.findViewById(R.id.user_email)
        oldPasswordEditText = oldPasswordLayout.editText!!
        newPasswordEditText = newPasswordLayout.editText!!
        confirmNewPasswordEditText = confirmNewPasswordLayout.editText!!



        user_reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
        user_event_listener =  object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user: User? = snapshot.getValue(User::class.java)
                val user_fb: FirebaseUser = mAuth.currentUser!!
                if (user != null) {
                    clearAllErrors()
                    datalist.clear()
                    nicknameEditText.setText(user.getUserNickname())
                    descriptionEditText.setText(user.getUserDescription())
                    userEmail.setText(user_fb.email)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        user_reference.addValueEventListener(user_event_listener)


        //button setup
        changeNicknameButton = view.findViewById(R.id.userNicknameButton)
        changeDescriptionButton = view.findViewById(R.id.userDescriptionButton)
        changePasswordButton = view.findViewById(R.id.userPasswordButton)


        //checks if fields are valid, sets errors if invalid, makes user changes if valid
        changeNicknameButton.setOnClickListener{
            nicknameLayout.error = null
            val newNickname = nicknameEditText.text.toString()
            if(Util.isNicknameValid(newNickname)){
                userReference.child("userNickname").setValue(newNickname).addOnCompleteListener{
                    if(it.isSuccessful){
                        Util.showToast(requireContext(), "Successfully changed nickname")
                    }else{
                        Util.showToast(requireContext(),  "Error changing nickname: "+ it.exception!!.message )
                    }
                }
            }else{
                if(nicknameEditText.text.isBlank()){
                    nicknameLayout.error = "Nickname cannot be blank"
                }else{
                    nicknameLayout.error = "Nickname cannot be more than ${Util.NICKNAME_MAX_LENGTH} characters"
                }
            }
        }


        changeDescriptionButton.setOnClickListener{
            descriptionLayout.error = null
            val newDescription = descriptionEditText.text.toString()
            if(Util.isUserDescriptionValid(newDescription)){
                userReference.child("userDescription").setValue(newDescription).addOnCompleteListener{
                    if(it.isSuccessful){
                        Util.showToast(requireContext(), "Successfully changed description")
                    }else{
                        Util.showToast(requireContext(), "Error changing description: "+ it.exception!!.message)
                    }
                }
            }else{
                descriptionLayout.error = "Description cannot be more than ${Util.USER_DESCRIPTION_MAX_LENGTH} characters"
            }
        }

        //handles password change, passwords must match and user must reauthenticate with old password
        changePasswordButton.setOnClickListener{
            oldPasswordLayout.error = null
            newPasswordLayout.error = null
            confirmNewPasswordLayout.error = null

            val oldPassword = oldPasswordEditText.text.toString()
            val newPassword = newPasswordEditText.text.toString()
            val confirmNewPassword = confirmNewPasswordEditText.text.toString()

            if(Util.isPasswordValid(newPassword) && (newPassword == confirmNewPassword) ){
                val credential = EmailAuthProvider.getCredential(mAuth.currentUser!!.email!!, oldPassword)
                mAuth.currentUser!!.reauthenticate(credential).addOnCompleteListener {
                    if(it.isSuccessful){
                        mAuth.currentUser!!.updatePassword(newPassword)
                        oldPasswordEditText.setText("")
                        newPasswordEditText.setText("")
                        confirmNewPasswordEditText.setText("")
                        Util.showToast(requireContext(), "Successfully changed password")
                    }else{
                        oldPasswordLayout.error = "Password entered is not correct"
                    }
                }
            }else{
                if(newPassword != confirmNewPassword){
                    confirmNewPasswordLayout.error = "Passwords do not match"
                }

                if(newPassword.isBlank()){
                    newPasswordLayout.error = "Password cannot be blank"
                }else if(!Util.isPasswordValid(newPassword)){
                    newPasswordLayout.error = resources.getString(R.string.invalid_password_prompt)
                }

            }

        }


        //clears errors on lose focus
        nicknameEditText.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                nicknameLayout.error = null
            }
        })
        descriptionEditText.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                descriptionLayout.error = null
            }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
    }

    private fun clearAllErrors(){
        nicknameLayout.error = null
        descriptionLayout.error = null
        oldPasswordLayout.error = null
        newPasswordLayout.error = null
        confirmNewPasswordLayout.error = null
        oldPasswordEditText.setText("")
        newPasswordEditText.setText("")
        confirmNewPasswordEditText.setText("")
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