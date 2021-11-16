package com.example.soundboard

import android.content.Intent
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class BoardChooseAdapter(var data: List<BoardEntry>, var soundByteId: String) : RecyclerView.Adapter<BoardChooseAdapter.ViewHolder>() {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val image: ImageView = view.findViewById(R.id.board_image)
        val title: TextView = view.findViewById(R.id.board_name)
        val intro: TextView = view.findViewById(R.id.board_intro)
        val cardview: CardView = view.findViewById(R.id.cardview)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardChooseAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.board_item, parent, false)
        val viewholder = ViewHolder(view)

        viewholder.itemView.setOnClickListener {
            val position = viewholder.adapterPosition
            //val board = data[position]

            if(soundByteId != null) {
                val soundBoardId : String = position.toString()  //TODO: change to id of whatever soundboard you pick later
                val ref = FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid).child("soundBoardList").child(soundBoardId)
                    .child("soundByteIdMap").child(soundByteId).setValue(true)
                Toast.makeText(view.context, "Successfully Saved to ${data[position].title}!", Toast.LENGTH_SHORT)
                    .show()
                // idk about this structure, but it's recommended i guessss
                // https://firebase.google.com/docs/database/android/structure-data#fanout
            }else{
                println("id null")
            }

            //val intent  = Intent(parent.context, MainActivity::class.java)
            //idk how to choose a specific fragment
            //intent.putExtra("fragmentStart", "ProfileFragment")
            //parent.context.startActivity(intent)

            // add to thing
        }

        return viewholder
    }

    override fun onBindViewHolder(holder: BoardChooseAdapter.ViewHolder, position: Int) {
        val board = data[position]
        if(board.imageUrl != "NA") {
            Picasso.get().load(board.imageUrl).into(holder.image)
        } else if (board.imageUrl == "NA"){
            Picasso.get().load(getRandomImgUrl(position)).into(holder.image)
        }
        holder.title.text = board.title
        holder.intro.text = board.intro
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun getRandomImgUrl(position: Int) : String{
        val indexRandom = position%8
        if (indexRandom == 0 ){
            return color1Url
        } else if (indexRandom == 1) {
            return color2Url
        } else if (indexRandom == 2) {
            return color3Url
        } else if (indexRandom == 3) {
            return color4Url
        } else if (indexRandom == 4) {
            return color5Url
        } else if (indexRandom == 5) {
            return color6Url
        } else if (indexRandom == 6) {
            return color7Url
        } else if (indexRandom == 7) {
            return color8Url
        } else {
            return color9Url
        }

    }

    private var color1Url : String = "https://firebasestorage.googleapis.com/v0/b/soundboard2-1d1a2.appspot.com/o/Colors%2Fcolor_1.png?alt=media&token=088e874a-2e93-4778-b220-a6177e10ba49"
    private var color2Url : String = "https://firebasestorage.googleapis.com/v0/b/soundboard2-1d1a2.appspot.com/o/Colors%2Fcolor_2.png?alt=media&token=a0f046dd-b033-4409-b007-9df0df998cbe"
    private var color3Url : String = "https://firebasestorage.googleapis.com/v0/b/soundboard2-1d1a2.appspot.com/o/Colors%2Fcolor_3.png?alt=media&token=6ecf442b-c5dc-4b0b-9315-e9e1a36ccdd7"
    private var color4Url : String = "https://firebasestorage.googleapis.com/v0/b/soundboard2-1d1a2.appspot.com/o/Colors%2Fcolor_4.png?alt=media&token=c3933174-fe85-4147-8a43-bdec213d91fb"
    private var color5Url : String = "https://firebasestorage.googleapis.com/v0/b/soundboard2-1d1a2.appspot.com/o/Colors%2Fcolor_5.png?alt=media&token=c88f739f-0752-4e1b-9e2f-8692bf823198"
    private var color6Url : String = "https://firebasestorage.googleapis.com/v0/b/soundboard2-1d1a2.appspot.com/o/Colors%2Fcolor_6.png?alt=media&token=d12de9a4-da03-47b0-8f4a-9eed47925d5e"
    private var color7Url : String = "https://firebasestorage.googleapis.com/v0/b/soundboard2-1d1a2.appspot.com/o/Colors%2Fcolor_7.png?alt=media&token=bbaf10e2-e72c-4fab-8bb5-2b84272d1eda"
    private var color8Url : String = "https://firebasestorage.googleapis.com/v0/b/soundboard2-1d1a2.appspot.com/o/Colors%2Fcolor_8.png?alt=media&token=1cc9f6b7-2f44-443f-808c-28a844b1c1cc"
    private var color9Url : String = "https://firebasestorage.googleapis.com/v0/b/soundboard2-1d1a2.appspot.com/o/Colors%2Fcolor_9.png?alt=media&token=18c7a54b-ae4a-4ac4-a0da-ec5512ba4fdb"








}