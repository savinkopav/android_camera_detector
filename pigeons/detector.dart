import 'package:pigeon/pigeon.dart';

@HostApi()
abstract class AndroidCameraDetectorApi {

  bool hasAvailableCamera();

}