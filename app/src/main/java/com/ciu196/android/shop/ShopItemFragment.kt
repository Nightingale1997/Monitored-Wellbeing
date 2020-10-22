package com.ciu196.android.shop

import User
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ciu196.android.heartbeat.HeartrateFragment
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
        const val TAG = "ShopItemFragment"
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
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_shopitem, container, false)

        binding.claimButton.setEnabled(false);
        val args = ShopItemFragmentArgs.fromBundle(requireArguments())

        database = Firebase.database.getReference()
        var currentPoints = 0;
        database.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).addListenerForSingleValueEvent(
            object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user: User? = dataSnapshot.getValue(User::class.java)
                    if (user != null){
                        currentPoints = user.points!! // "Texas"
                        Log.i(TAG, "Current: " + currentPoints + " Price: " +binding.itemPrice.text.toString().toInt())
                        if(currentPoints >= binding.itemPrice.text.toString().toInt()){
                            binding.priceCheck.setImageResource(R.drawable.checkpassed)
                            binding.claimButton.text = "Claim"
                            binding.claimButton.setEnabled(true);

                            binding.claimButton.setOnClickListener {

                                Utils.writeNewUser( FirebaseAuth.getInstance().currentUser!!.uid, FirebaseAuth.getInstance().currentUser!!.displayName!!, currentPoints!!-binding.itemPrice.text.toString().toInt())
                                binding.itemTitle.setVisibility(View.INVISIBLE);
                                binding.itemDescription.setVisibility(View.INVISIBLE);
                                binding.itemPrice.setVisibility(View.INVISIBLE);
                                binding.itemImage.setVisibility(View.INVISIBLE);
                                binding.priceCheck.setVisibility(View.INVISIBLE);
                                binding.claimButton.setVisibility(View.INVISIBLE);
                                binding.shopItemScreen.setBackgroundResource(R.drawable.claim_overlay)

                            }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })





        when(args.itemId){
            0 -> {
                binding.itemTitle.text = "Stylish Water Bottle"
                binding.itemDescription.text ="Stay refreshed with this flowery water bottle\n" +
                        "after a tough session."
                binding.itemPrice.text = "360"
                binding.itemImage.setImageResource(R.drawable.bottle_details)
            }
            1 -> {
                binding.itemTitle.text = "FutureGym VETERAN Bag"
                binding.itemDescription.text ="Flex on everyone with this huge bag"
                binding.itemPrice.text = "5009"
                binding.itemImage.setImageResource(R.drawable.bag_details)
            }
            2 -> {
                binding.itemTitle.text = "FutureGym Chocolate Whey"
                binding.itemDescription.text ="Yummy yummy supplements"
                binding.itemPrice.text = "430"
                binding.itemImage.setImageResource(R.drawable.whey_details)
            }
            3 -> {
                binding.itemTitle.text = "FutureGym Dumbbels"
                binding.itemDescription.text ="Limited edition dumbbells"
                binding.itemPrice.text = "320"
                binding.itemImage.setImageResource(R.drawable.dumbbells_details)
            }
            4 -> {
                binding.itemTitle.text = "FutureGym Band"
                binding.itemDescription.text ="The future on your wrist"
                binding.itemPrice.text = "860"
                binding.itemImage.setImageResource(R.drawable.band_details)
            }
            5 -> {
                binding.itemTitle.text = "FutureGym Shirt"
                binding.itemDescription.text ="Show off your great sense of fashion!"
                binding.itemPrice.text = "250"
                binding.itemImage.setImageResource(R.drawable.shirt_details)
            }
        }



        binding.exit.setOnClickListener {
            //val action = ChallengeFragmentDirections.actionChallengeFragmentToHeartrateFragment()
            val action = ShopItemFragmentDirections.actionShopItemFragmentToShopFragment()
            findNavController().navigate(action)
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

