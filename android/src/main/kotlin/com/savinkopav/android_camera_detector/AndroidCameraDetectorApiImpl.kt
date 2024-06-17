package com.savinkopav.android_camera_detector

import android.app.Service
import android.hardware.camera2.CameraManager
import android.util.Log
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding

class AndroidCameraDetectorApiImpl: AndroidCameraDetectorApi {

    private var activityPluginBinding: ActivityPluginBinding? = null

    companion object {

        private const val TAG = "AndroidCameraDetector"
    }

    fun onActivityAttach(
        activityBinding: ActivityPluginBinding
    ) {
        Log.d(TAG, "onActivityAttach")
        this.activityPluginBinding = activityBinding
    }

    fun onActivityDetach() {
        Log.d(TAG, "onActivityDetach")
        this.activityPluginBinding = null
    }

    override fun hasAvailableCamera(): Boolean {
        Log.d(TAG, "hasAvailableCamera called")

        try {
            val cameraManager = activityPluginBinding!!.activity.getSystemService(Service.CAMERA_SERVICE) as CameraManager

            return cameraManager.cameraIdList.isNotEmpty()
        } catch (e: Exception) {
            throw CameraServiceException("CameraServiceIsUnavailable")
        }
    }

}