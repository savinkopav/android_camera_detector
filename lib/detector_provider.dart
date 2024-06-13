import 'package:android_camera_detector/pigeon_detector.dart';

class DetectorProvider {

  static final DetectorProvider instance = DetectorProvider._();

  factory DetectorProvider() {
    return instance;
  }

  late AndroidCameraDetectorApi _androidCameraDetectorApi;

  DetectorProvider._() {
    _androidCameraDetectorApi = AndroidCameraDetectorApi();
  }

  Future<bool> hasAvailableCamera() {
    return _androidCameraDetectorApi.hasAvailableCamera();
  }
}