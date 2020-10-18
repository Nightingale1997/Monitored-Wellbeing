package com.ciu196.android.heartbeat

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.*
import android.os.HandlerThread
import android.util.Log
import android.view.Surface
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import java.util.*

internal class CameraService(private val activity: FragmentActivity) {
    private var cameraId: String = "null"
    private var cameraDevice: CameraDevice? = null
    private var previewSession: CameraCaptureSession? = null
    private var previewCaptureRequestBuilder: CaptureRequest.Builder? = null
    fun start(previewSurface: Surface) {
        val cameraManager =
            activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            cameraId =
                Objects.requireNonNull(cameraManager).cameraIdList[0]
        } catch (e: CameraAccessException) {
            Log.println(Log.ERROR, "camera", "No access to camera....")
        } catch (e: NullPointerException) {
            Log.println(Log.ERROR, "camera", "No access to camera....")
        }
        try {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.println(
                    Log.ERROR,
                    "camera",
                    "No permission to take photos"
                )
            }
            Objects.requireNonNull(cameraManager)
                .openCamera(cameraId, object : CameraDevice.StateCallback() {
                    override fun onOpened(camera: CameraDevice) {
                        cameraDevice = camera
                        val stateCallback: CameraCaptureSession.StateCallback =
                            object : CameraCaptureSession.StateCallback() {
                                override fun onConfigured(session: CameraCaptureSession) {
                                    previewSession = session
                                    try {
                                        previewCaptureRequestBuilder =
                                            cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                                        previewCaptureRequestBuilder!!.addTarget(previewSurface) // this is previewSurface
                                        previewCaptureRequestBuilder!!.set(
                                            CaptureRequest.FLASH_MODE,
                                            CaptureRequest.FLASH_MODE_TORCH
                                        )
                                        val thread = HandlerThread("CameraPreview")
                                        thread.start()
                                        previewSession!!.setRepeatingRequest(
                                            previewCaptureRequestBuilder!!.build(),
                                            null,
                                            null
                                        )
                                    } catch (e: CameraAccessException) {
                                        if (e.message != null) {
                                            Log.println(
                                                Log.ERROR,
                                                "camera",
                                                e.message
                                            )
                                        }
                                    }
                                }

                                override fun onConfigureFailed(session: CameraCaptureSession) {
                                    Log.println(
                                        Log.ERROR,
                                        "camera",
                                        "Session configuration failed"
                                    )
                                }
                            }
                        try {
                            camera.createCaptureSession(
                                listOf(previewSurface),
                                stateCallback,
                                null
                            ) //1
                        } catch (e: CameraAccessException) {
                            if (e.message != null) {
                                Log.println(
                                    Log.ERROR,
                                    "camera",
                                    e.message
                                )
                            }
                        }
                    }

                    override fun onDisconnected(camera: CameraDevice) {}
                    override fun onError(camera: CameraDevice, error: Int) {}
                }, null)
        } catch (e: CameraAccessException) {
            if (e.message != null) {
                Log.println(Log.ERROR, "camera", e.message)
            }
        } catch (e: SecurityException) {
            if (e.message != null) {
                Log.println(Log.ERROR, "camera", e.message)
            }
        }
    }

    fun stop() {
        try {
            cameraDevice!!.close()
        } catch (e: Exception) {
            Log.println(
                Log.ERROR,
                "camera",
                "cannot close camera device" + e.message
            )
        }
    }

}