import 'package:flutter/services.dart';
import 'package:noty/textstyle_notifier.dart';

class SettingService {
  static const MethodChannel platform = MethodChannel('com.example/native');

  static Future<void> saveSettingToAndroid(Color color, double fontSize) async {
    try {
      await platform.invokeMethod('saveSetting', {
        'color': color.toARGB32(),
        'fontSize': fontSize,
      });
    } on PlatformException catch (e) {
      print("Failed to save setting: '${e.message}'.");
    }
  }

  static Future<void> loadSettingFromAndroid() async {
    try {
      final result = await platform.invokeMethod('loadSetting');
      final colorValue = result['color'] as int?;
      final fontSize = result['fontSize'] as double?;

      // print("fontSize: $fontSize");

      textStyleNotifier.value = textStyleNotifier.value.copyWith(
        color:
            colorValue != null
                ? Color(colorValue)
                : textStyleNotifier.value.color,
        fontSize: fontSize ?? textStyleNotifier.value.fontSize,
      );
    } on PlatformException catch (e) {
      print("Failed to load text: '${e.message}'.");
    }
  }
}
