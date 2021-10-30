package com.example.soundboard

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


class UploadSoundByteActivity : AppCompatActivity() {

    private lateinit var storageReference : StorageReference
    private lateinit var uriAudio : Uri
    private lateinit var bytes : ByteArray
    private lateinit var fileName : String
    private lateinit var songUrl : String
    private lateinit var uploaderUserName : String



    private lateinit var progressDialog: ProgressDialog
    private lateinit var uploadButton : Button
    private lateinit var selectAudioButton : Button
    private lateinit var uploaderUserNameEditText : EditText
    private lateinit var audioFileNameEditText : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_sound_byte)

        storageReference = FirebaseStorage.getInstance().reference
        println("gonna make storage asdfadfasdfasf")
        println(storageReference)
        progressDialog = ProgressDialog(this)

        uploadButton = findViewById(R.id.uploadAudio)
        uploadButton.setOnClickListener{
            upload()
        }

        selectAudioButton = findViewById(R.id.selectAudioButton)
        selectAudioButton.setOnClickListener{
            pickSong()
        }



        uploaderUserNameEditText = findViewById(R.id.userNameEditText)
        audioFileNameEditText = findViewById(R.id.fileNameTextView)

        println("okay now in upload sound byte activity")
    }

    val audioResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    {   result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK) {
            uriAudio = result.data?.data!!
            fileName = getFileName(uriAudio)!!
            audioFileNameEditText.text = fileName

        }

    }

    public fun getFileName(uri : Uri) : String? {
        var result = uri.path
        println(result)
        if (result != null) {
            var cut = result?.lastIndexOf('/')
            if (cut != -1){
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    private fun pickSong(){
        intent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
        intent.setType("audio/*")
        audioResult.launch(intent)
    }

    private fun uploadFileToServer(uri : Uri, songName : String, uploadName : String) {
        var filePath = storageReference.child("Audios").child(songName)
        progressDialog.show()
        filePath.putFile(uri).addOnSuccessListener { taskSnapshot ->
            var uriTask = taskSnapshot.getStorage().downloadUrl
            while (!uriTask.isComplete){
                //println("in progress")
            }
            var urlSong = uriTask.result
            songUrl = urlSong.toString()
            println("success url " + songUrl)
            //uploadDetailsToDatabase(fileName, songUrl, uploaderUserName)


        }.addOnProgressListener {  taskSnapshot ->
            var progress = (100.0 * taskSnapshot.bytesTransferred)/taskSnapshot.totalByteCount
            var currentProgress = progress.toInt()
            progressDialog.setMessage("Uploading: " + currentProgress + "%")
        }.addOnFailureListener{
            progressDialog.dismiss()
            Toast.makeText(this, "Upload Failed!", Toast.LENGTH_SHORT).show()
        }
    }

    public fun uploadDetailsToDatabase(songName : String, songUrl : String, uploader : String){
        var soundByte = SoundByte()
        soundByte.SoundByte(songName, songUrl, uploader)
        //FirebaseDatabse.getInstance().getReference("Audio").push().setValue(soundByte)

    }

    private fun upload() {
        if (uriAudio == null){
            Toast.makeText(this, "Please select an audio clip", Toast.LENGTH_SHORT).show()
        } else {
            fileName = audioFileNameEditText.text.toString()
            uploadFileToServer(uriAudio, fileName, uploaderUserName)
        }
    }
}