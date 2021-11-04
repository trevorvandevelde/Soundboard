package com.example.soundboard

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
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
import co.lujun.androidtagview.ColorFactory
import co.lujun.androidtagview.TagContainerLayout
import co.lujun.androidtagview.TagView
import co.lujun.androidtagview.TagView.OnTagClickListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class UploadSoundByteActivity : AppCompatActivity() {

    private lateinit var storageReference : StorageReference
    private lateinit var mAuth : FirebaseAuth
    private lateinit var uriAudio : Uri
    private lateinit var bytes : ByteArray
    private lateinit var fileName : String
    private lateinit var songUrl : String
    private lateinit var uploaderUserName : String
    private lateinit var description : String
    private lateinit var tags : Array<String>



    private lateinit var progressDialog: ProgressDialog
    private lateinit var uploadButton : Button
    private lateinit var selectAudioButton : Button
    private lateinit var uploaderUserNameEditText : EditText
    private lateinit var audioFileNameEditText : TextView


    //Tag System
    private lateinit var audioTagContainer: TagContainerLayout
    private lateinit var audioTagButton : Button
    private lateinit var audioTagText : EditText






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_sound_byte)

        storageReference = FirebaseStorage.getInstance().reference
        println("gonna make storage")
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






        audioTagContainer = findViewById(R.id.tagContainer)
        audioTagButton = findViewById(R.id.addTagButton)
        audioTagText = findViewById(R.id.editTextTag)

        audioTagContainer.tagBackgroundColor = Color.TRANSPARENT
        audioTagContainer.theme = ColorFactory.NONE
        audioTagContainer.addTag("DIY")

        audioTagButton.setOnClickListener{

            var tagText = audioTagText.text.toString()
            audioTagContainer.addTag(tagText)
            audioTagText.text = null
        }

        audioTagContainer.setOnTagClickListener(object : OnTagClickListener {
            override fun onTagClick(position: Int, text: String) {
                // ...
            }

            override fun onTagLongClick(position: Int, text: String) {
                // ...
            }

            override fun onSelectedTagDrag(position: Int, text: String) {
                // ...
            }

            override fun onTagCrossClick(position: Int) {
                audioTagContainer.removeTag(position)
            }
        })
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

            uploadDetailsToDatabase(fileName, songUrl, mAuth.uid.toString())
            //progressDialog.dismiss()


        }.addOnProgressListener {  taskSnapshot ->
            var progress = (100.0 * taskSnapshot.bytesTransferred)/taskSnapshot.totalByteCount
            var currentProgress = progress.toInt()
            progressDialog.setMessage("Uploading: " + currentProgress + "%")
            if(currentProgress == 100){
                //progressDialog.dismiss()
                Toast.makeText(this, "Uploaded audio!", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener{
            progressDialog.dismiss()
            Toast.makeText(this, "Upload Failed!", Toast.LENGTH_SHORT).show()
        }
    }

    public fun uploadDetailsToDatabase(songName : String, songUrl : String, uploader : String){
        var soundByte = SoundByte()
        soundByte.SoundByte(songName, songUrl, uploader, description, tags)
        FirebaseDatabase.getInstance().getReference("Audio").push().setValue(soundByte)
            .addOnCompleteListener{
            Toast.makeText(this, "Added File Info to Database", Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
                finish()

            }.addOnFailureListener{
                Toast.makeText(this, "Failed to Add to Database", Toast.LENGTH_SHORT).show()
            }

    }

    private fun upload() {
        if (!this::uriAudio.isInitialized){
            Toast.makeText(this, "Please select an audio clip", Toast.LENGTH_SHORT).show()
        } else {
            fileName = audioFileNameEditText.text.toString()
            uploadFileToServer(uriAudio, fileName, mAuth.uid.toString())
        }
    }
}

