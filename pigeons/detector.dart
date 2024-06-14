import 'package:pigeon/pigeon.dart';

@HostApi()
abstract class AndroidCameraDetectorApi {

  @async
  bool hasAvailableCamera();

  void dispose();

}