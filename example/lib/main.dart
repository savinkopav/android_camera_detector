import 'package:flutter/material.dart';
import 'package:android_camera_detector/detector_provider.dart';
import 'package:flutter/services.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await SystemChrome.setPreferredOrientations([
    DeviceOrientation.portraitUp,
  ]);
  runApp(const App());
}

class App extends StatelessWidget {
  const App({super.key});

  @override
  Widget build(BuildContext context) {
    final theme = createTheme();
    setSystemChrome(theme);

    return MaterialApp(
      debugShowCheckedModeBanner: false,
      themeMode: ThemeMode.light,
      darkTheme: theme,
      home: const HomePage(),
    );
  }

  ThemeData createTheme() {
    return ThemeData(
      colorSchemeSeed: Colors.greenAccent,
      brightness: Brightness.dark,
    );
  }

  setSystemChrome(ThemeData theme) {
    SystemChrome.setSystemUIOverlayStyle(SystemUiOverlayStyle.light.copyWith(
      statusBarColor: Colors.transparent,
      systemNavigationBarColor: ElevationOverlay.applySurfaceTint(
        theme.colorScheme.background,
        theme.colorScheme.surfaceTint,
        3,
      ),
    ));
  }

}

class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {

  final _detectorProvider = CameraDetectorProvider();
  bool hasCamera = false;
  bool isLoading = false;

  @override
  void initState() {
    super.initState();
  }

  checkCamera(BuildContext context) async {
    hasCamera = false;

    setState(() {
      isLoading = true;
    });

    try {
      hasCamera = await _detectorProvider.hasAvailableCamera();
      if (!context.mounted) return;
      if (!hasCamera) await _showInfoDialog(context, 'Has no camera', 'Your device has no hardware camera');

    } catch (e) {
      if (!context.mounted) return;
      if (e is PlatformException && e.message == "CameraServiceIsUnavailable") {
        await _showInfoDialog(context, 'Camera service error', 'CameraServiceIsUnavailable');
      }
    }

    setState(() {
      isLoading = false;
    });
  }

  @override
  Widget build(BuildContext context) {

    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.greenAccent,
        title: const Text('Camera detector app'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            if (!isLoading) Text('hasCamera: $hasCamera'),
            Padding(
              padding: const EdgeInsets.all(5.0),
              child: !isLoading ? MaterialButton(
                color: Colors.greenAccent,
                onPressed: () => checkCamera(context),
                child: const Text('Check camera'),
              ) : const CircularProgressIndicator(),
            )
          ],
        ),
      ),
    );
  }

  Future<void> _showInfoDialog(BuildContext context, String title, String content) async {
    return await showDialog(
        context: context,
        builder: (context) {
          return AlertDialog(
            title: Text(title),
            content: Text(content),
            actions: [
              TextButton(
                onPressed: () => Navigator.of(context).pop(),
                child: const Text('Close'),
              ),
            ],
          );
        });
  }
}
