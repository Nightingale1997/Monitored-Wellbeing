package com.ciu196.android.heartbeat

import android.Manifest
import android.os.Bundle
import android.view.Surface
import android.view.TextureView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.ciu196.android.monitored_wellbeing.R

class MainActivity : AppCompatActivity() {
    private val cameraService = CameraService(this)
    private var analyzer: OutputAnalyzer? = null
    override fun onResume() {
        super.onResume()
        analyzer = OutputAnalyzer(this, findViewById(R.id.graphTextureView))
        val cameraTextureView = findViewById<TextureView>(R.id.textureView2)
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
        analyzer = OutputAnalyzer(this, findViewById(R.id.graphTextureView))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val toolbar =
            findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA),
            MY_PERMISSIONS_REQUEST_READ_CONTACTS
        )
    }
}