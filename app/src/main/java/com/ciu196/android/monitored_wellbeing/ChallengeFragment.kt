package com.ciu196.android.monitored_wellbeing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ciu196.android.monitored_wellbeing.databinding.FragmentChallengeBinding
import com.ciu196.android.monitored_wellbeing.databinding.FragmentLoginBinding
import com.ciu196.android.monitored_wellbeing.databinding.FragmentMainBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.log

class ChallengeFragment : Fragment() {

    // Get a reference to the ViewModel scoped to this Fragment
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var binding: FragmentChallengeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_challenge, container, false)
        binding.checkinButton.setOnClickListener {
            val action = ChallengeFragmentDirections.actionChallengeFragmentToHeartrateFragment();
            findNavController().navigate(action)
        }
        return binding.root
    }


    /**
     * Observes the authentication state and changes the UI accordingly.
     * If there is a logged in user: (1) show a logout button and (2) display their name.
     * If there is no logged in user: show a login button
     */
}

