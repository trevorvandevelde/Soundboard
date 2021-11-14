package com.example.soundboard

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat

//importing required classes
import android.media.MediaPlayer
import android.view.View
import com.chibde.visualizer.BarVisualizer
import com.chibde.visualizer.CircleBarVisualizer
import com.chibde.visualizer.CircleVisualizer
import com.chibde.visualizer.LineBarVisualizer
import com.chibde.visualizer.LineVisualizer
import com.chibde.visualizer.SquareBarVisualizer
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.create
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Message
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import co.lujun.androidtagview.ColorFactory
import co.lujun.androidtagview.TagContainerLayout
import co.lujun.androidtagview.TagView
import co.lujun.androidtagview.TagView.OnTagClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class PlayActivity : AppCompatActivity(){

    lateinit var mediaPlayer: MediaPlayer


    lateinit var lineVisualizer:LineVisualizer

    lateinit var elapsedtimelable: TextView
    lateinit var remainingtimelable: TextView
    lateinit var positionBar: SeekBar
    lateinit var playbutton: FloatingActionButton
    lateinit var playlist: ArrayList<Int>
    lateinit var thread:Thread


    var total_time:Int = 0
    var music_id = 1
    var init: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        val audioUrl =  intent.getStringExtra("audio")

        if(audioUrl != "NA" && audioUrl != null) {
            val uri = Uri.parse(audioUrl)
            // mediaPlayer = MediaPlayer.create(this, R.raw.tokyo)
            mediaPlayer = MediaPlayer.create(this, uri)
        }
        else{
            mediaPlayer = MediaPlayer.create(this, R.raw.tokyo)
        }
        mediaPlayer.isLooping = true

        lineVisualizer = findViewById(R.id.visualizerLine)
        playbutton = findViewById(R.id.playbutton)
        positionBar = findViewById(R.id.positionBar)
        elapsedtimelable = findViewById(R.id.elapsedTimeLabel)
        remainingtimelable = findViewById(R.id.remainingTimeLabel)

        playlist = ArrayList<Int>()
        playlist.add(R.raw.tokyo)

        total_time = mediaPlayer.duration
        positionBar.max = total_time
        positionBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekbar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        mediaPlayer.seekTo(progress)
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            }
        )

        Thread(Runnable {
            while (mediaPlayer != null) {
                try {
                    var msg = Message()
                    msg.what = mediaPlayer.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                }
            }
        }).start()

        val play_image: ImageView = findViewById(R.id.play_image)
        val play_title: TextView = findViewById(R.id.play_title)
        val play_header: TextView = findViewById(R.id.soundbyte_header_title)
        val play_author: TextView = findViewById(R.id.soundbyte_author)


        val coverUrl = intent.getStringExtra("image")
        val title = intent.getStringExtra("title")
        val author = intent.getStringExtra("author")

        if(coverUrl != "NA"){
          Picasso.get().load(coverUrl).into(play_image)
        }
        play_title.setText(title)
        play_header.setText(title)
        play_author.setText(author)

        // adds back button fab
        val backButton: View = findViewById(R.id.back_arrow)
        backButton.setOnClickListener { view ->
            finish()
        }


        // tag container
        val tag_container: TagContainerLayout = findViewById(R.id.soundbyte_tagContainer)
        tag_container.backgroundColor = Color.TRANSPARENT
        tag_container.borderColor = Color.TRANSPARENT
        tag_container.tagBackgroundColor = Color.rgb(245, 245, 245)
        tag_container.tagBorderColor = Color.TRANSPARENT
        tag_container.removeAllTags()

        val list_tags:ArrayList<String>? = intent.getStringArrayListExtra("tags")
        if(list_tags == null){
            tag_container.addTag("DIY")
        }
        else {
            //val list_tags: List<String> = listOf("DIY")
            for (item in list_tags) {
                tag_container.addTag(item)
            }
        }

    }


    var handler = object: Handler(){
        override fun handleMessage(msg: Message){
            super.handleMessage(msg)
            var currentpostion = msg.what
            positionBar.progress = currentpostion
            var elapsedtime = createTimeLable((currentpostion))
            elapsedtimelable.text = elapsedtime
            var remainingtime = createTimeLable(total_time-currentpostion)
            remainingtimelable.text = "-$remainingtime"
        }
    }

    fun createTimeLable(time: Int):String{
        var timelable = ""
        var min = time/1000/60
        var sec = time/1000 % 60
        timelable = "$min: "
        if(sec<10){
            timelable += "0"
        }
        timelable += sec
        return timelable
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.reset()
        // mediaPlayer.release()
    }

    fun lineVisualization(view: View){
        clear()
        lineVisualizer.visibility = View.VISIBLE
        lineVisualizer.setColor(ContextCompat.getColor(this, R.color.colorAccent))
        lineVisualizer.setStrokeWidth(1)
        lineVisualizer.setPlayer(mediaPlayer.audioSessionId)
    }


    fun clear(){
        lineVisualizer.visibility = View.INVISIBLE
    }

    fun playClicked(view: View){
        if(mediaPlayer.isPlaying){
            mediaPlayer.pause()
            playbutton.setImageResource(R.drawable.ic_play_arrow)
        }
        else{
            if(init==true){
                clear()
                lineVisualizer.visibility = View.VISIBLE
                lineVisualizer.setColor(ContextCompat.getColor(this, R.color.colorAccent))
                lineVisualizer.setStrokeWidth(4)
                lineVisualizer.setPlayer(mediaPlayer.audioSessionId)
                init = false
            }
            mediaPlayer.start()
            playbutton.setImageResource(R.drawable.ic_pause)
        }
    }

    fun chooseBoard(view:View){
        val newIntent = Intent(this, ChooseBoardActivity::class.java)
        newIntent.putExtra("soundByteId", intent.getStringExtra("soundByteId") )
        startActivity(newIntent)
    }


    fun showSheetDialog(view: View){
        val sheetDialog: BottomSheetDialog = BottomSheetDialog(this)
        sheetDialog.setContentView(R.layout.save_to_board_layout)

        val board_recyclerview: RecyclerView? = view.findViewById(R.id.save_recyclerview)
        val datalist = ArrayList<BoardEntry>()

        val user_reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
        val user_event_listener =  object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //for (ds in snapshot.child("Audio").children)
                val user : User? =  snapshot.getValue(User::class.java)
                if(user != null){
                    datalist.clear()
                    val soundboards = user.getSoundBoardList()
                    for(sb in soundboards){
                        datalist.add(BoardEntry("NA", sb.getSoundBoardName(), "${sb.getSoundByteIdMap().size} soundbytes", sb.getSoundByteIdMap() ))
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        user_reference.addValueEventListener(user_event_listener)


        val adapter = SheetBoardAdapter(datalist)
        board_recyclerview?.adapter = adapter

        sheetDialog.show()
    }

}