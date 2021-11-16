package com.example.soundboard

import android.app.ProgressDialog.show
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.w3c.dom.Text

// used to show the user's information together with all of the user's boards
class ProfileFragment : Fragment(){

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private val datalist = ArrayList<BoardEntry>()
    private lateinit var discover_recyclerview : RecyclerView

    private lateinit var userDescription : TextView
    private lateinit var userNickname : TextView
    private lateinit var userImage: ImageView

    private lateinit var user_reference: DatabaseReference
    private lateinit var user_event_listener:  ValueEventListener

    private lateinit var createbutton_view: Button
    private lateinit var navView: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)

        createbutton_view = view.findViewById(R.id.add_board)

       /* if(datalist.size == 0) {
            initData()
        }
        */

        discover_recyclerview = view.findViewById(R.id.board_recyclerview)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        discover_recyclerview.layoutManager = layoutManager
        val adapter = BoardAdapter(datalist)
        discover_recyclerview.adapter = adapter


        userNickname = view.findViewById(R.id.user_name)
        userDescription = view.findViewById(R.id.user_intro)
        userImage = view.findViewById(R.id.user_image)

        val settingsFragment: Fragment = SettingsFragment()
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()

        // set the information about the user
        userDescription.setOnClickListener { view ->
            transaction.replace(R.id.fragmentContainer, settingsFragment).commit()
        }
        userNickname.setOnClickListener { view ->
            transaction.replace(R.id.fragmentContainer, settingsFragment).commit()
        }

        // used sharedprefrence to load the local saved profile image, supposed to save in firebase
        val pref : SharedPreferences =requireActivity().getSharedPreferences("saved_profile_image",
            Context.MODE_PRIVATE)
        userImage.setImageResource(pref.getInt("profile image", R.drawable.profile_icon_1))

        // get the user's boards from the firebase
        user_reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
        user_event_listener =  object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //for (ds in snapshot.child("Audio").children)
                val user : User? =  snapshot.getValue(User::class.java)
                if(user != null){
                    datalist.clear()
                    userNickname.text = user.getUserNickname()
                    userDescription.text = user.getUserDescription()
                    val soundboards = user.getSoundBoardList()
                    for(sb in soundboards){
                        datalist.add(BoardEntry("NA", sb.getSoundBoardName(), "${sb.getSoundByteIdMap().size} soundbytes", sb.getSoundByteIdMap() ))
                    }
                    // refresh our data
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        user_reference.addValueEventListener(user_event_listener)

        return view
    }

    // set the profile image
    override fun onResume() {
        super.onResume()
        val pref : SharedPreferences =requireActivity().getSharedPreferences("saved_profile_image",
            Context.MODE_PRIVATE)
        userImage.setImageResource(pref.getInt("profile image", R.drawable.profile_icon_1))
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navView = requireActivity().findViewById(R.id.nav_view)
        navView.selectedItemId = R.id.navigation_profile

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    // used to do local test, wont use in release edition
    private fun initData(){
        repeat(10){
            datalist.add(BoardEntry( "NA","Board name", "23 soundbytes", HashMap()))
        }
    }

    override fun onDestroy() {
        user_reference.removeEventListener(user_event_listener)
        super.onDestroy()
    }

    // used to pop out the dialogs in the fragment
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // pop out the add board dialog
        createbutton_view.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                Add_Board_Dialog().show(childFragmentManager, "add_board_dialog")
                true
            }
        })
        // pop out the delete board dialog
        userImage.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View) {
                change_profile_image(p0)
                true
            }
            fun change_profile_image(view: View){
                val intent = Intent(requireActivity(), ProfileImageActivity::class.java)
                startActivity(intent)
            }
        })
    }


}