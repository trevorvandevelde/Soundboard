package com.example.soundboard

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.text.method.TextKeyListener.clear
import android.text.method.TextKeyListener.clearMetaKeyState
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import co.lujun.androidtagview.ColorFactory
import co.lujun.androidtagview.TagContainerLayout
import co.lujun.androidtagview.TagView
import co.lujun.androidtagview.TagView.OnTagClickListener
import com.chibde.visualizer.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.io.IOException
import android.util.Log

import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnFailureListener

import com.google.android.gms.tasks.Task

import com.google.firebase.storage.UploadTask

import com.google.android.gms.tasks.OnSuccessListener





class UploadSoundByteActivity : AppCompatActivity() {

    private lateinit var storageReference : StorageReference
    private lateinit var mAuth : FirebaseAuth
    private lateinit var uriAudio : Uri
    private lateinit var uriImage : Uri
    private lateinit var bytes : ByteArray
    private lateinit var fileName : String
    private lateinit var songUrl : String
    private lateinit var imageUrl: String
    private lateinit var uploaderUserName : String




    private lateinit var progressDialog: ProgressDialog
    private lateinit var uploadButton : Button
    private lateinit var selectAudioButton : Button
    private lateinit var selectImage : ImageView

    private lateinit var descriptionEditText: EditText
    private lateinit var uploaderNewFileNameEditText : EditText
    // private lateinit var audioFileNameEditText : TextView


    //Tag System
    private lateinit var audioTagContainer: TagContainerLayout
    private lateinit var audioTagButton : Button
    private lateinit var audioTagText : EditText


    //Media Play System
    lateinit var mediaPlayer: MediaPlayer

    lateinit var lineBarVisualizer: LineBarVisualizer
    lateinit var lineVisualizer: LineVisualizer
    lateinit var barVisualizer: BarVisualizer
    lateinit var circleBarVisualizer: CircleBarVisualizer
    lateinit var circleVisualizer: CircleVisualizer
    lateinit var squareBarVisualizer: SquareBarVisualizer
    lateinit var elapsedtimelable: TextView
    lateinit var remainingtimelable: TextView
    lateinit var positionBar: SeekBar
    lateinit var playbutton: Button
    lateinit var playlist: ArrayList<Int>
    lateinit var thread:Thread

    var total_time:Int = 0
    var music_id = 1
    var init: Boolean = true
    var uploadedAudio: Boolean = false

    var handler = object: Handler(){

        override fun handleMessage(msg: Message){
            super.handleMessage(msg)

            var currentpostion = msg.what
            positionBar.progress = currentpostion
            println("tried to go to currentposition")
            //var elapsedtime = createTimeLable((currentpostion))
            //elapsedtimelable.text = elapsedtime
            //var remainingtime = createTimeLable(total_time-currentpostion)
            //remainingtimelable.text = "-$remainingtime"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_sound_byte)

        storageReference = FirebaseStorage.getInstance().reference
        mAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)

        selectImage = findViewById(R.id.selectImage)

        selectImage.setOnClickListener{ //change photo
            pickPhoto()
        }


        uriImage = Uri.parse("android.resource://com.example.soundboard//" + R.drawable.dartmouth)
        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uriImage)
        selectImage.setImageBitmap(bitmap)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        bytes = byteArrayOutputStream.toByteArray()



        uploadButton = findViewById(R.id.uploadAudio)
        uploadButton.setOnClickListener{
            upload()
        }

        selectAudioButton = findViewById(R.id.selectAudioButton)
        selectAudioButton.setOnClickListener{
            pickSong()
        }
        uploaderNewFileNameEditText = findViewById(R.id.uploaderNewFileNameEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)


        mediaPlayer = MediaPlayer()
        mediaPlayer.isLooping = true
        //Media Play System
        lineBarVisualizer = findViewById(R.id.visualizerLineBar)
        //playbutton = findViewById(R.id.playButton)
        positionBar = findViewById(R.id.positionBar)
        positionBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if(fromUser){
                        mediaPlayer.seekTo(progress)
                    }
                }
                override fun onStartTrackingTouch(p0: SeekBar?){
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            }
        )




        //Tag System
        audioTagContainer = findViewById(R.id.tagContainer)
        audioTagButton = findViewById(R.id.addTagButton)
        audioTagText = findViewById(R.id.editTextTag)
        audioTagContainer.tagBackgroundColor = Color.TRANSPARENT
        audioTagContainer.theme = ColorFactory.NONE
        audioTagContainer.addTag("DIY")
        audioTagButton.setOnClickListener{
            var tagText = audioTagText.text.toString()
            if (tagText != null) {
                audioTagContainer.addTag(tagText)
            }
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


            mediaPlayer = MediaPlayer.create(this, uriAudio)
            mediaPlayer.isLooping = true

            total_time = mediaPlayer.duration
            positionBar.max = total_time

            lineBarVisualizer.visibility = View.VISIBLE
            lineBarVisualizer.setColor(ContextCompat.getColor(this, R.color.black))
            // define a custom number of bars we want in the visualizer it is between (10 - 256).
            lineBarVisualizer.setDensity(80f)
            lineBarVisualizer.setPlayer(mediaPlayer.audioSessionId)
            println("tried to create media player")
            uploadedAudio = true;

            Thread(Runnable{
                while(mediaPlayer!=null){
                    try{
                        var msg = Message()
                        msg.what = mediaPlayer.currentPosition
                        handler.sendMessage(msg)
                        println("sending message")
                        Thread.sleep(1000)
                    }catch(e: InterruptedException){
                    }
                }
            }).start()



            // audioFileNameEditText.text = fileName

        }

    }


    val imageResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    { result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK){
            uriImage = result.data?.data!!
            try {


                var bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uriImage)
                //bitmap = rotateBitmap(bitmap, -90f)
                selectImage.setImageBitmap(bitmap)
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                bytes = byteArrayOutputStream.toByteArray()
            } catch (e: IOException){
                e.printStackTrace()
            }
        }

    }

    fun rotateBitmap(original: Bitmap, degrees: Float): Bitmap? {
        val matrix = Matrix()
        matrix.preRotate(degrees)
        val rotatedBitmap =
            Bitmap.createBitmap(original, 0, 0, original.width, original.height, matrix, true)
        original.recycle()
        return rotatedBitmap
    }

    private fun getFileName(uri : Uri) : String? {
        var result = uri.path
        //println(result)
        if (result != null) {
            var cut = result?.lastIndexOf('/')
            if (cut != -1){
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    private fun pickSong(){
        uploadedAudio = false
        intent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
        intent.type = "audio/*"
        audioResult.launch(intent)
    }

    private fun pickPhoto(){
        intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.type = "image/*"
        imageResult.launch(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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
            println("fileName " + fileName)

            var newAudioFileName = uploaderNewFileNameEditText.text.toString()
            var newAudioDescription = descriptionEditText.text.toString()
            var newAudioTags = audioTagContainer.tags

            uploadDetailsToDatabase(newAudioFileName, imageUrl, songUrl, mAuth.uid.toString(), newAudioDescription, newAudioTags)
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

    private fun uploadDetailsToDatabase(songName : String, imageUrl: String, songUrl : String, uploader : String, description : String, tags: MutableList<String>){
        var soundByte = SoundByte()
        soundByte.SoundByte(songName, imageUrl, songUrl, uploader, description, tags)
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
        } else if (uploaderNewFileNameEditText.text.toString().isEmpty()){
            Toast.makeText(this, "Please add a File Name", Toast.LENGTH_SHORT).show()
        } else if (descriptionEditText.text.toString().isEmpty()){
            Toast.makeText(this, "Please add a Description", Toast.LENGTH_SHORT).show()
        } else if (mAuth.uid == null) {
            Toast.makeText(this, "Login Error: Please make sure you are logged in", Toast.LENGTH_SHORT).show()
        }else {
            fileName = uploaderNewFileNameEditText.text.toString()
            uploadImageToServer(bytes, fileName)
            uploadFileToServer(uriAudio, fileName, mAuth.uid.toString())
        }
    }

    fun uploadImageToServer(image: ByteArray?, fileName: String?) {
        val uploadTask = storageReference.child("Thumbnails").child(fileName!!).putBytes(
            image!!
        )
        progressDialog.show()
        uploadTask.addOnSuccessListener { taskSnapshot ->
            val task = taskSnapshot.storage.downloadUrl
            while (!task.isComplete);
            val urlsong = task.result
            imageUrl = urlsong.toString()
            //                Log.i("image url", imageUrl);
        }.addOnFailureListener { Log.i("image url", "failed") }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.reset()
        // mediaPlayer.release()
    }

    fun onCancel(view: View){
        mediaPlayer.stop()
        mediaPlayer.reset()
        finish()
    }

    fun playClicked(view: View){
        if(mediaPlayer.isPlaying){
            mediaPlayer.pause()
        }
        else{
            mediaPlayer.start()
        }
    }

}

