App-Launcher-Cordova-Plugin
===========================

Simple Cordova plugin to see if other apps are installed and launch them.

## 0. Index
1. [Description](#1-description)
2. [Installation](#2-installation)
3. [Usage](#3-usage)
4. [Changelog](#4-changelog)
5. [License](#5-license)

## 1. Description

This plugin allows you to check if an app is installed that can handle a specific uri and launch an app via uri on iOS and Android. Additionally, you may open an Android app using its package id.
* (iOS, Android) Check if any apps are installed that can launch via a specified uri.
* (iOS, Android) Launch an app via a specified uri.
* (Android) Check if an app is installed via its package id.
* (Android) Launch an app via its package id.

## 2. Installation

### Automatically (CLI / Plugman)

```
$ cordova plugin add https://github.com/nchutchind/App-Launcher-Cordova-Plugin.git
```
and then (this step will modify your project):
```
$ cordova prepare
```

1\. Add the following xml to your `config.xml`:
```xml
<!-- for iOS -->
<feature name="Launcher">
	<param name="ios-package" value="Launcher" />
</feature>
```
```xml
<!-- for Android -->
<feature name="Launcher">
	<param name="android-package" value="com.hutchind.cordova.plugins.Launcher" />
</feature>
```

2\. Add `www/Launcher.js` to your project and reference it in `index.html`:
```html
<script type="text/javascript" src="js/Launcher.js"></script>
```

3\. Copy the files in `src/` for iOS and/or Android into your project.

iOS: Copy `Launcher.h` and `Launcher.m` to `platforms/ios/<ProjectName>/Plugins`

Android: Copy `Launcher.java` to `platforms/android/src/com/hutchind/cordova/plugins` (you will probably need to create this path)

### PhoneGap Build

Add the following xml to your `config.xml` to always use the latest version of this plugin:
```xml
<gap:plugin name="com.hutchind.cordova.plugins.launcher" />
```
or to use a specific version:
```xml
<gap:plugin name="com.hutchind.cordova.plugins.launcher" version="0.1.0" />
```

## 3. Usage
```javascript
	// Default handlers
	var successCallback = function() {
		alert("Success!");
	};
	var errorCallback = function(errMsg) {
		alert("Error! " + errMsg);
	}
```

Check to see if Facebook can be launched via uri (**iOS** and **Android**)
```javascript
	window.plugins.launcher.canLaunch({uri:'fb://'}, successCallback, errorCallback);
```

Check to see if Facebook can is installed (**Android**)
```javascript
	window.plugins.launcher.canLaunch({packageName:'com.facebook.katana'}, successCallback, errorCallback);
```

Launch Facebook to the logged in user's profile (**iOS** and **Android**)
```javascript
	window.plugins.launcher.canLaunch({uri:'fb://profile'}, successCallback, errorCallback);
```

Launch Facebook via package id (**Android**)
```javascript
	window.plugins.launcher.launch({packageName:'com.facebook.katana'}, successCallback, errorCallback);
```

Launch NASA TV video stream in MxPlayer Free (**Android**)
```javascript
	window.plugins.launcher.launch({
		packageName:'com.mxtech.videoplayer.ad',
		uri:'http://www.nasa.gov/multimedia/nasatv/NTV-Public-IPS.m3u8',
		dataType:'application/x-mpegURL'
	}, successCallback, errorCallback);
```

## 4. Changelog
0.1.1: Added ability to launch a package with a data uri and datatype on Android.

0.1.0: initial version supporting Android and iOS

## 5. License

[The MIT License (MIT)](http://www.opensource.org/licenses/mit-license.html)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
