import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:noty/setting_service.dart';
import 'package:noty/textstyle_notifier.dart';

/// A page that allows users to customize text settings including color and font size.
class SettingsPage extends StatefulWidget {
  const SettingsPage({super.key});

  @override
  State<SettingsPage> createState() => _SettingsPageState();
}

class _SettingsPageState extends State<SettingsPage> {
  static const _padding = 16.0;
  static const _colorCircleSize = 32.0;
  static const _colorCircleMargin = 6.0;
  static const _sectionSpacing = 24.0;
  static const _minFontSize = 12.0;
  static const _maxFontSize = 30.0;
  static const _fontSizeDivisions = 9;

  late Color _selectedColor;
  late double _selectedFontSize;
  bool _isLoading = true;
  String? _errorMessage;

  @override
  void initState() {
    super.initState();
    _loadSettings();
  }

  /// Loads settings from Android storage and updates the UI state.
  Future<void> _loadSettings() async {
    try {
      setState(() => _isLoading = true);
      await SettingService.loadSettingFromAndroid();

    } catch (e) {
      setState(() {
        _isLoading = false;
        _errorMessage = '설정을 불러오는 데 실패했습니다.';
      });
    }

    setState(() {
      _selectedColor = textStyleNotifier.value.color;
      _selectedFontSize = textStyleNotifier.value.fontSize;
      _isLoading = false;
    });

    print('현재 선택된 색상: $_selectedColor');
  }

  /// Saves the current settings and navigates back.
  Future<void> _saveSettings() async {
    try {
      setState(() => _isLoading = true);
      textStyleNotifier.value = TextStyleSettings(
        color: _selectedColor,
        fontSize: _selectedFontSize,
      );
      await SettingService.saveSettingToAndroid(
        _selectedColor,
        _selectedFontSize,
      );
      setState(() => _isLoading = false); // 추가
    } catch (e) {
      setState(() {
        _isLoading = false;
        _errorMessage = '설정을 저장하는 데 실패했습니다.';
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('위젯 설정'),
        elevation: 0,
        backgroundColor: Theme.of(context).primaryColor,
      ),
      body:
          _isLoading
              ? const Center(child: CircularProgressIndicator())
              : _errorMessage != null
              ? _buildErrorWidget()
              : _buildContent(),
    );
  }

  /// Builds the main content of the settings page.
  Widget _buildContent() {
    return Padding(
      padding: const EdgeInsets.all(_padding),
      child: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _buildSectionTitle('글자 색상'),
            const SizedBox(height: 8),
            _buildColorPicker(),
            const SizedBox(height: _sectionSpacing),
            _buildSectionTitle('폰트 크기'),
            _buildFontSizeSlider(),
            const SizedBox(height: _sectionSpacing),
            _buildApplyButton(),
          ],
        ),
      ),
    );
  }

  /// Builds an error display widget.
  Widget _buildErrorWidget() {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Text(_errorMessage!, style: const TextStyle(color: Colors.red)),
          const SizedBox(height: 16),
          ElevatedButton(onPressed: _loadSettings, child: const Text('다시 시도')),
        ],
      ),
    );
  }

  /// Builds a section title with consistent styling.
  Widget _buildSectionTitle(String title) {
    return Text(
      title,
      style: Theme.of(
        context,
      ).textTheme.titleLarge?.copyWith(fontWeight: FontWeight.bold),
    );
  }

  /// Builds the color selection row.
  Widget _buildColorPicker() {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children:
          [
            Colors.white,
            Colors.red,
            Colors.green,
            Colors.blue,
            Colors.yellow,
          ].map((color) => _buildColorCircle(color)).toList(),
    );
  }

  /// Builds a single color selection circle.
  Widget _buildColorCircle(Color color) {
    final isSelected = _selectedColor.toARGB32() == color.toARGB32();

    return GestureDetector(
      onTap: () => setState(() => _selectedColor = color),
      child: Stack(
        alignment: Alignment.center,
        children: [
          Container(
            margin: const EdgeInsets.symmetric(horizontal: _colorCircleMargin),
            width: _colorCircleSize,
            height: _colorCircleSize,
            decoration: BoxDecoration(
              color: color,
              shape: BoxShape.circle,
              border: Border.all(
                width: 2,
                color: isSelected
                    ? (color == Colors.white ? Colors.black : Colors.white)
                    : Colors.transparent,
              ),
              boxShadow: [
                BoxShadow(
                  color: Colors.grey.withOpacity(0.3),
                  spreadRadius: 1,
                  blurRadius: 3,
                  offset: const Offset(0, 2),
                ),
              ],
            ),
          ),
          if (isSelected)
            const Icon(
              Icons.check,
              color: Colors.black,
              size: 18,
            ),
        ],
      ),
    );
  }


  /// Builds the font size adjustment slider.
  Widget _buildFontSizeSlider() {
    return Slider(
      value: _selectedFontSize,
      min: _minFontSize,
      max: _maxFontSize,
      divisions: _fontSizeDivisions,
      label: _selectedFontSize.round().toString(),
      onChanged: (value) => setState(() => _selectedFontSize = value),
      activeColor: Colors.white,
      inactiveColor:
          Colors.white70, // Slightly translucent white for inactive part
    );
  }

  /// Builds the apply button with loading state.
  Widget _buildApplyButton() {
    return SizedBox(
      width: double.infinity,
      child: ElevatedButton(
        onPressed:
            _isLoading
                ? null
                : () async {
                  await _saveSettings();
                  if (mounted) Navigator.pop(context);
                },
        style: ElevatedButton.styleFrom(
          padding: const EdgeInsets.symmetric(vertical: 16),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(12),
          ),
        ),
        child:
            _isLoading
                ? const SizedBox(
                  width: 20,
                  height: 20,
                  child: CircularProgressIndicator(color: Colors.white),
                )
                : const Text('적용하기', style: TextStyle(fontSize: 16)),
      ),
    );
  }
}