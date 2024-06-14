package com.savinkopav.android_camera_detector

import android.util.Log
import androidx.camera.lifecycle.ProcessCameraProvider
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

class AndroidCameraDetectorApiImpl: AndroidCameraDetectorApi {

    private var activityPluginBinding: ActivityPluginBinding? = null
    private var flutterPluginBinding: FlutterPlugin.FlutterPluginBinding? = null
    private var coroutineScope: CloseableCoroutineScope? = null

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
        this.coroutineScope = CloseableCoroutineScope(Dispatchers.Main.immediate + Job())
    }

    fun onActivityDetach() {
        Log.d(TAG, "onActivityDetach")
        dispose()
        this.coroutineScope = null
        this.activityPluginBinding = null
        this.flutterPluginBinding = null
    }

    override fun hasAvailableCamera(callback: (Result<Boolean>) -> Unit) {
        Log.d(TAG, "hasAvailableCamera called")

        coroutineScope?.launch {
            val result = tryToInitCamera()
            Log.d(TAG, "got result = $result, will fire callback for transferring data...")
            callback.invoke(Result.success(result))
        }

    }

    private suspend fun tryToInitCamera(): Boolean = withContext(Dispatchers.IO) {
       try {
           val cameraProviderFuture = ProcessCameraProvider.getInstance(activityPluginBinding!!.activity)
           yield()
           cameraProviderFuture.await()
           true
       } catch (e: Exception) {
           if (e is CancellationException) {
               Log.d(TAG, "coroutine was cancelled")
               throw e
           }
           Log.d(TAG, "got error via getCameras")
           false
       }
    }

    //TODO dispose method add in interface
    private fun dispose() {
        coroutineScope?.close()
    }

}

class CloseableCoroutineScope(context: CoroutineContext): Closeable, CoroutineScope {
    override val coroutineContext: CoroutineContext = context

    override fun close() {
        coroutineContext.cancelChildren()
    }
}