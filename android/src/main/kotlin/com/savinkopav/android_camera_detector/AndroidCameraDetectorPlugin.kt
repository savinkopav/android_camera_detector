package com.savinkopav.android_camera_detector

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding

/** AndroidCameraDetectorPlugin */
class AndroidCameraDetectorPlugin: FlutterPlugin, ActivityAware {

  private var activityPluginBinding: ActivityPluginBinding? = null
  private var androidCameraDetectorApi: AndroidCameraDetectorApiImpl? = null

  override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    androidCameraDetectorApi = AndroidCameraDetectorApiImpl()
    AndroidCameraDetectorApi.setUp(binding.binaryMessenger, androidCameraDetectorApi)
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    AndroidCameraDetectorApi.setUp(binding.binaryMessenger, null)
    androidCameraDetectorApi = null
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activityPluginBinding = binding
    androidCameraDetectorApi?.onActivityAttach(activityPluginBinding!!)
  }

  override fun onDetachedFromActivity() {
    androidCameraDetectorApi?.onActivityDetach()
    activityPluginBinding = null
  }

  override fun onDetachedFromActivityForConfigChanges() {
    onDetachedFromActivity()
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    onAttachedToActivity(binding)
  }
}
