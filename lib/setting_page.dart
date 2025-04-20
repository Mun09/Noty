import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:untitled/textstyle_notifier.dart';

class SettingsPage extends StatefulWidget {
  const SettingsPage({super.key});

  @override
  State<SettingsPage> createState() => _SettingsPageState();
}

class _SettingsPageState extends State<SettingsPage> {
  late Color selectedColor;
  late double selectedFontSize;

  @override
  void initState() {
    super.initState();
    selectedColor = textStyleNotifier.value.color;
    selectedFontSize = textStyleNotifier.value.fontSize;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("설정")),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            const Text("글자 색상", style: TextStyle(fontSize: 18)),
            const SizedBox(height: 8),
            Row(
              children: [
                buildColorCircle(Colors.white),
                buildColorCircle(Colors.red),
                buildColorCircle(Colors.green),
                buildColorCircle(Colors.blue),
                buildColorCircle(Colors.yellow),
              ],
            ),
            const SizedBox(height: 24),
            const Text("폰트 크기", style: TextStyle(fontSize: 18)),
            Slider(
              value: selectedFontSize,
              min: 12,
              max: 30,
              divisions: 9,
              label: selectedFontSize.round().toString(),
              onChanged: (value) {
                setState(() {
                  selectedFontSize = value;
                });
              },
            ),
            const SizedBox(height: 24),
            ElevatedButton(
              onPressed: () {
                textStyleNotifier.value = TextStyleSettings(
                  color: selectedColor,
                  fontSize: selectedFontSize,
                );
                Navigator.pop(context);
              },
              child: const Text("적용하기"),
            ),
          ],
        ),
      ),
    );
  }

  Widget buildColorCircle(Color color) {
    return GestureDetector(
      onTap: () {
        setState(() {
          selectedColor = color;
        });
      },
      child: Container(
        margin: const EdgeInsets.symmetric(horizontal: 6),
        width: 32,
        height: 32,
        decoration: BoxDecoration(
          color: color,
          shape: BoxShape.circle,
          border: Border.all(
            width: 2,
            color: selectedColor == color ? Colors.white : Colors.transparent,
          ),
        ),
      ),
    );
  }
}
