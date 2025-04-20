import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:untitled/setting_page.dart';
import 'package:untitled/textstyle_notifier.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> with WidgetsBindingObserver {
  static const platform = MethodChannel('com.example/native');
  final TextEditingController controller = TextEditingController();

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addObserver(this);
    _loadTextFromAndroid();
  }

  @override
  void dispose() {
    WidgetsBinding.instance.removeObserver(this);
    controller.dispose();
    super.dispose();
  }

  Future<void> saveTextToAndroid(String text) async {
    try {
      await platform.invokeMethod('saveText', {'text': text});
    } on PlatformException catch (e) {
      print("Failed to save text: '${e.message}'.");
    }
  }

  Future<void> _loadTextFromAndroid() async {
    try {
      final String result = await platform.invokeMethod('loadText');
      setState(() {
        controller.text = result;
      });
    } on PlatformException catch (e) {
      print("Failed to load text: '${e.message}'.");
    }
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    if (state == AppLifecycleState.inactive || state == AppLifecycleState.paused) {
      saveTextToAndroid(controller.text); // 앱이 백그라운드로 갈 때 저장
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData.dark().copyWith(
        scaffoldBackgroundColor: Colors.black,
        textTheme: const TextTheme(
          bodyMedium: TextStyle(color: Colors.white),
        ),
        inputDecorationTheme: const InputDecorationTheme(
          labelStyle: TextStyle(color: Colors.white70),
          enabledBorder: UnderlineInputBorder(
            borderSide: BorderSide(color: Colors.transparent),
          ),
          focusedBorder: UnderlineInputBorder(
            borderSide: BorderSide(color: Colors.transparent),
          ),
        ),
      ),
      home: NotyHomePage(controller: controller,)
    );
  }
}

class NotyHomePage extends StatelessWidget {
  final TextEditingController controller;
  const NotyHomePage({super.key, required this.controller});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.black,
        actions: [
          IconButton(
            icon: const Icon(Icons.settings),
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => const SettingsPage()),
              );
            },
          )
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            Expanded(
              child: ValueListenableBuilder<TextStyleSettings>(
                valueListenable: textStyleNotifier,
                builder: (context, settings, _) {
                  return TextField(
                    controller: controller,
                    style: TextStyle(
                      color: settings.color,
                      fontSize: settings.fontSize,
                    ),
                    maxLines: null,
                    keyboardType: TextInputType.multiline,
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}
