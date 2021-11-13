package com.example.soundboard

import android.content.Intent
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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

                // idk about this structure, but it's recommended i guessss
                // https://firebase.google.com/docs/database/android/structure-data#fanout
            }else{
                println("id null")
            }


            val intent  = Intent(parent.context, MainActivity::class.java)
            //idk how to choose a specific fragment
            intent.putExtra("fragmentStart", "ProfileFragment")
            parent.context.startActivity(intent)






            // add to thing
        }

        return viewholder
    }

    override fun onBindViewHolder(holder: BoardChooseAdapter.ViewHolder, position: Int) {
        val board = data[position]
        if(board.imageUrl != "NA") {
            Picasso.get().load(board.imageUrl).into(holder.image)
        }
        holder.title.text = board.title
        holder.intro.text = board.intro
    }

    override fun getItemCount(): Int {
        return data.size
    }







}