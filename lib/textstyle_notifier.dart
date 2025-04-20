import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class TextStyleSettings {
  Color color;
  double fontSize;

  TextStyleSettings({required this.color, required this.fontSize});
}

final textStyleNotifier = ValueNotifier<TextStyleSettings>(
  TextStyleSettings(color: Colors.white, fontSize: 16.0),
);
