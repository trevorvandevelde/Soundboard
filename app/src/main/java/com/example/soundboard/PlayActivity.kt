package com.example.soundboard

import android.annotation.SuppressLint
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
import android.os.Handler
import android.os.Message
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView

class PlayActivity : AppCompatActivity(){

    lateinit var mediaPlayer: MediaPlayer

    lateinit var lineBarVisualizer:LineBarVisualizer
    lateinit var lineVisualizer:LineVisualizer
    lateinit var barVisualizer: BarVisualizer
    lateinit var circleBarVisualizer: CircleBarVisualizer
    lateinit var circleVisualizer:CircleVisualizer
    lateinit var squareBarVisualizer: SquareBarVisualizer
    lateinit var elapsedtimelable: TextView
    lateinit var remainingtimelable: TextView
    lateinit var positionBar: SeekBar
    lateinit var playbutton: Button
    lateinit var playlist: ArrayList<Int>
    lateinit var thread:Thread

    var total_time:Int = 0
    var music_id = 1

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        mediaPlayer = MediaPlayer.create(this, R.raw.tokyo)
        mediaPlayer.isLooping = true

        lineVisualizer = findViewById(R.id.visualizerLine)
        barVisualizer = findViewById(R.id.visualizerBar)
        circleBarVisualizer = findViewById(R.id.visualizerCircleBar)
        lineBarVisualizer = findViewById(R.id.visualizerLineBar)
        circleVisualizer = findViewById(R.id.visualizerCircle)
        squareBarVisualizer = findViewById(R.id.visualizerSquareBar)
        playbutton = findViewById(R.id.playbutton)
        positionBar = findViewById(R.id.positionBar)
        elapsedtimelable = findViewById(R.id.elapsedTimeLabel)
        remainingtimelable = findViewById(R.id.remainingTimeLabel)

        playlist = ArrayList<Int>()
        playlist.add(R.raw.tokyo)

        total_time = mediaPlayer.duration
        positionBar.max = total_time
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

        Thread(Runnable{
            while(mediaPlayer!=null){
                try{
                    var msg = Message()
                    msg.what = mediaPlayer.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                }catch(e: InterruptedException){
                }
            }
        }).start()

        clear()
        lineVisualizer.visibility = View.VISIBLE
        lineVisualizer.setColor(ContextCompat.getColor(this, R.color.purple_700))
        lineVisualizer.setStrokeWidth(1)
        lineVisualizer.setPlayer(mediaPlayer.audioSessionId)
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
        lineVisualizer.setColor(ContextCompat.getColor(this, R.color.purple_700))
        lineVisualizer.setStrokeWidth(1)
        lineVisualizer.setPlayer(mediaPlayer.audioSessionId)
    }

    fun barVisualization(view: View?) {
        clear()
        barVisualizer.visibility = View.VISIBLE
        barVisualizer.setColor(ContextCompat.getColor(this, R.color.black))
        // define a custom number of bars we want in the visualizer it is between (10 - 256).
        barVisualizer.setDensity(80f)
        barVisualizer.setPlayer(mediaPlayer.audioSessionId)
    }

    fun circleBarVisualization(view: View?) {
        clear()
        circleBarVisualizer.visibility = View.VISIBLE
        circleBarVisualizer.setColor(ContextCompat.getColor(this, R.color.teal_200))
        circleBarVisualizer.setPlayer(mediaPlayer.audioSessionId)
    }

    fun circleVisualization(view: View?) {
        clear()
        circleVisualizer.visibility = View.VISIBLE
        circleVisualizer.setColor(ContextCompat.getColor(this, R.color.purple_500))
        // Customize the size of the circle. by default, the multipliers are 1.
        circleVisualizer.setRadiusMultiplier(2.2f)
        circleVisualizer.setStrokeWidth(2)
        circleVisualizer.setPlayer(mediaPlayer.audioSessionId)
    }

    fun squareBarVisualization(view: View?) {
        clear()
        squareBarVisualizer.visibility = View.VISIBLE
        squareBarVisualizer.setColor(ContextCompat.getColor(this, R.color.purple_200))
        // define a custom number of bars you want in the visualizer between (10 - 256).
        squareBarVisualizer.setDensity(65f)
        // Set Spacing
        squareBarVisualizer.setGap(5)
        squareBarVisualizer.setPlayer(mediaPlayer.audioSessionId)
    }

    fun lineBarVisualization(view: View?) {
        clear()
        lineBarVisualizer.visibility = View.VISIBLE
        lineBarVisualizer.setColor(ContextCompat.getColor(this, R.color.teal_200))
        // define the custom number of bars we want in the visualizer between (10 - 256).
        lineBarVisualizer.setDensity(50f)
        lineBarVisualizer.setPlayer(mediaPlayer.audioSessionId)
    }

    fun clear(){
        lineVisualizer.visibility = View.INVISIBLE
        barVisualizer.visibility = View.INVISIBLE
        circleBarVisualizer.visibility = View.INVISIBLE
        squareBarVisualizer.visibility = View.INVISIBLE
        lineBarVisualizer.visibility = View.INVISIBLE
        circleVisualizer.visibility = View.INVISIBLE
    }

    fun playClicked(view: View){
        if(mediaPlayer.isPlaying){
            mediaPlayer.pause()
            playbutton.setText("PLAY")
        }
        else{
            mediaPlayer.start()
            playbutton.setText("PAUSE")
        }
    }

    fun savetoboardClicked(view:View){
        finish()
    }

}