package com.savinkopav.android_camera_detector

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.UseCaseGroup
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding

class AndroidCameraDetectorApiImpl: AndroidCameraDetectorApi {

    private var activityPluginBinding: ActivityPluginBinding? = null
    private var flutterPluginBinding: FlutterPlugin.FlutterPluginBinding? = null

    companion object {

        private const val TAG = "AndroidCameraDetector"
    }

    fun onActivityAttach(
        pluginBinding: FlutterPlugin.FlutterPluginBinding,
        activityBinding: ActivityPluginBinding
    ) {
        Log.d(TAG, "onActivityAttach")
        this.activityPluginBinding = activityBinding
        this.flutterPluginBinding = pluginBinding
    }

    fun onActivityDetach() {
        Log.d(TAG, "onActivityDetach")
        this.activityPluginBinding = null
        this.flutterPluginBinding = null
    }

    override fun hasAvailableCamera(callback: (Result<Boolean>) -> Unit) {
        Log.d(TAG, "hasAvailableCamera called")

        try {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(activityPluginBinding!!.activity)

            cameraProviderFuture.addListener(
                {
                    try {
                        cameraProviderFuture.get()
                        callback.invoke(Result.success(true))
                    } catch (e: Exception) {
                        Log.d(TAG, "hasAvailableCamera got exception")
                        callback.invoke(Result.failure(HasNoAvailableCameraException("has_no_available_camera").initCause(e)))
                    }

                }, ContextCompat.getMainExecutor(activityPluginBinding!!.activity)
            )

        } catch (e: Exception) {
            Log.d(TAG, "hasAvailableCamera got exception")
            callback.invoke(Result.failure(HasNoAvailableCameraException("has_no_available_camera").initCause(e)))
        }

    }
}