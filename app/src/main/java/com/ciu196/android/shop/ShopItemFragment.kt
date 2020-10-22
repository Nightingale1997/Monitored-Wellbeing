package com.ciu196.android.shop

import User
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ciu196.android.monitored_wellbeing.LoginViewModel
import com.ciu196.android.monitored_wellbeing.PointsFragmentDirections
import com.ciu196.android.monitored_wellbeing.R
import com.ciu196.android.monitored_wellbeing.Utils
import com.ciu196.android.monitored_wellbeing.databinding.FragmentShopitemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ShopItemFragment : Fragment() {
    companion object {
        const val TAG = "PointsFragment"
    }


    // Write a message to the database
    private lateinit var database: DatabaseReference
// ...

    // Get a reference to the ViewModel scoped to this Fragment
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var binding: FragmentShopitemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        database = Firebase.database.getReference()

        database.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user: User? = dataSnapshot.getValue(User::class.java)
                    if (user != null){
                        val points: Int? = user.points // "Texas"
                        binding.available.text = points.toString()
                    }
                    else{
                        Utils.writeNewUser( FirebaseAuth.getInstance().currentUser!!.uid, FirebaseAuth.getInstance().currentUser!!.displayName!!, 0)
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_shopitem, container, false)

        binding.exit.setOnClickListener {
            //val action = ChallengeFragmentDirections.actionChallengeFragmentToHeartrateFragment()
            val action = ShopFragmentDirections.actionShopFragmentToPointsFragment()
            findNavController().navigate(action)
        }
        val gv = binding.gridview

        gv.setAdapter(ImageAdapter(requireActivity()))
        gv.onItemClickListener =
            OnItemClickListener { parent, v, position, id ->
                Toast.makeText(requireActivity(), "Image Position: $position", Toast.LENGTH_SHORT)
                    .show()
            }
        /*binding.navigationChallenges.setOnClickListener {
            //val action = ChallengeFragmentDirections.actionChallengeFragmentToHeartrateFragment()
            val action = PointsFragmentDirections.actionPointsFragmentToChallengeFragment()
            findNavController().navigate(action)
        }*/
        return binding.root
    }




    /**
     * Observes the authentication state and changes the UI accordingly.
     * If there is a logged in user: (1) show a logout button and (2) display their name.
     * If there is no logged in user: show a login button
     */

    // Read from the database

}

