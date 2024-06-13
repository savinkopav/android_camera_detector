import 'package:flutter_test/flutter_test.dart';
import 'package:android_camera_detector/android_camera_detector.dart';
import 'package:android_camera_detector/android_camera_detector_platform_interface.dart';
import 'package:android_camera_detector/android_camera_detector_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockAndroidCameraDetectorPlatform
    with MockPlatformInterfaceMixin
    implements AndroidCameraDetectorPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final AndroidCameraDetectorPlatform initialPlatform = AndroidCameraDetectorPlatform.instance;

  test('$MethodChannelAndroidCameraDetector is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelAndroidCameraDetector>());
  });

  test('getPlatformVersion', () async {
    AndroidCameraDetector androidCameraDetectorPlugin = AndroidCameraDetector();
    MockAndroidCameraDetectorPlatform fakePlatform = MockAndroidCameraDetectorPlatform();
    AndroidCameraDetectorPlatform.instance = fakePlatform;

    expect(await androidCameraDetectorPlugin.getPlatformVersion(), '42');
  });
}
