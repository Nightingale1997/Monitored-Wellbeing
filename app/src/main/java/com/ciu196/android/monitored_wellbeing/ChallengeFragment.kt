package com.ciu196.android.monitored_wellbeing

import User
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ciu196.android.monitored_wellbeing.databinding.FragmentChallengeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class ChallengeFragment : Fragment() {
    companion object {
        const val TAG = "ChallengeFragment"
    }


    // Write a message to the database
    private lateinit var database: DatabaseReference
// ...

    // Get a reference to the ViewModel scoped to this Fragment
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var binding: FragmentChallengeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_challenge, container, false)
        binding.checkinButton.setOnClickListener {
            //val action = ChallengeFragmentDirections.actionChallengeFragmentToHeartrateFragment()
            val action = ChallengeFragmentDirections.actionChallengeFragmentToHeartrateFragment()
            findNavController().navigate(action)
        }

        binding.navigationPoints.setOnClickListener {
            //val action = ChallengeFragmentDirections.actionChallengeFragmentToHeartrateFragment()
            val action = ChallengeFragmentDirections.actionChallengeFragmentToPointsFragment()
            findNavController().navigate(action)
        }
        return binding.root
    }




    /**
     * Observes the authentication state and changes the UI accordingly.
     * If there is a logged in user: (1) show a logout button and (2) display their name.
     * If there is no logged in user: show a login button
     */

    // Read from the database

}

