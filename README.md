App-Launcher-Cordova-Plugin
===========================

Simple Cordova plugin to see if other apps are installed and launch them.

## 0. Index
1. [Description](#1-description)
2. [Usage](#2-usage)
3. [Changelog](#3-changelog)
4. [License](#4-license)

## 1. Description

This plugin allows you to check if an app is installed that can handle a specific uri and launch an app via uri on iOS and Android. Additionally, you may open an Android app using its package id.
* (iOS, Android) Check if any apps are installed that can launch via a specified uri.
* (iOS, Android) Launch an app via a specified uri.
* (Android) Check if an app is installed via its package id.
* (Android) Launch an app via its package id.

## 2. Usage
```javascript
	// Default handlers
	var successCallback = function() {
		alert("Success!");
	};
	var errorCallback = function(errMsg) {
		alert("Error! " + errMsg);
	}
```

Check to see if Facebook can be launched via uri (iOS and Android)
```javascript
	window.plugins.launcher.canLaunch({uri:'fb://'}, successCallback, errorCallback);
```

Check to see if Facebook can is installed (Android)
```javascript
	window.plugins.launcher.canLaunch({packageName:'com.facebook.katana'}, successCallback, errorCallback);
```

Launch Facebook to the logged in user's profile (iOS and Android)
```javascript
	window.plugins.launcher.canLaunch({uri:'fb://profile'}, successCallback, errorCallback);
```

Launch Facebook via package id (Android)
```javascript
	window.plugins.launcher.launch({packageName:'com.facebook.katana'}, successCallback, errorCallback);
```

## 3. Changelog
0.1.0: initial version supporting Android and iOS

## 4. License

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