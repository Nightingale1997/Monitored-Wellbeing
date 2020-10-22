package com.ciu196.android.heartbeat

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ciu196.android.monitored_wellbeing.LoginViewModel
import com.ciu196.android.monitored_wellbeing.MainActivity
import com.ciu196.android.monitored_wellbeing.PointsFragmentDirections
import com.ciu196.android.monitored_wellbeing.R
import com.ciu196.android.monitored_wellbeing.databinding.FragmentHeartrateBinding
import com.example.android.geofence.errorMessage
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.google.firebase.auth.FirebaseAuth

class HeartrateFragment : Fragment() {


    companion object {
        const val TAG = "heartrateFragment"
    }


    // Get a reference to the ViewModel scoped to this Fragment
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var binding: FragmentHeartrateBinding
    private lateinit var cameraService : CameraService;
    private var analyzer: OutputAnalyzer? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        cameraService = CameraService(requireActivity())

        //setContentView(R.layout.activity_main2)
        val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(Manifest.permission.CAMERA),
            MY_PERMISSIONS_REQUEST_READ_CONTACTS
        )

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_heartrate, container, false)
        /*binding.checkinButton.setOnClickListener {
            val action = ChallengeFragmentDirections.actionChallengeFragmentToMainActivity();
            findNavController().navigate(action)
        }*/

        binding.exit.setOnClickListener {
            val action = HeartrateFragmentDirections.actionHeartrateFragmentToChallengeFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        analyzer = OutputAnalyzer(requireActivity())
        val cameraTextureView = binding.textureView2;
        val previewSurfaceTexture = cameraTextureView.surfaceTexture
        if (previewSurfaceTexture != null) {
            // this first appears when we close the application and switch back - TextureView isn't quite ready at the first onResume.
            val previewSurface = Surface(previewSurfaceTexture)
            cameraService.start(previewSurface)
            analyzer!!.measurePulse(cameraTextureView, cameraService)
        }
    }

    override fun onPause() {
        super.onPause()
        cameraService.stop()
        if (analyzer != null) analyzer!!.stop()
        analyzer = OutputAnalyzer(requireActivity())
    }

    fun checkimage(code : Int){
    Log.i(TAG, "beep")
    val test: HeartrateFragment =
        requireActivity().supportFragmentManager.findFragmentByTag("heartrateFragment") as HeartrateFragment
    if (test != null && test.isVisible()) {
        Log.i(TAG, "boop")
        //DO STUFF
    }
}
    /**
     * Observes the authentication state and changes the UI accordingly.
     * If there is a logged in user: (1) show a logout button and (2) display their name.
     * If there is no logged in user: show a login button
     */





}


