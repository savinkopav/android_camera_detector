import 'package:android_camera_detector/pigeon_detector.dart';

class CameraDetectorProvider {

  static final CameraDetectorProvider instance = CameraDetectorProvider._();

  factory CameraDetectorProvider() {
    return instance;
  }

  late AndroidCameraDetectorApi _androidCameraDetectorApi;

  CameraDetectorProvider._() {
    _androidCameraDetectorApi = AndroidCameraDetectorApi();
  }

  Future<bool> hasAvailableCamera() {
    return _androidCameraDetectorApi.hasAvailableCamera();
  }
}