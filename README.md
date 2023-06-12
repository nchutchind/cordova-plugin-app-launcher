cordova-plugin-app-launcher
===========================

Simple Cordova plugin to see if other apps are installed and launch them.

## 0. Index
1. [Description](#1-description)
2. [Installation](#2-installation)
3. [Parameters](#3-parameters)
4. [Usage](#4-usage)
5. [Changelog](#5-changelog)
6. [Credits](#6-credits)
7. [License](#7-license)

## 1. Description

This plugin allows you to check if an app is installed that can handle a specific uri and launch an app via uri on iOS and Android. Additionally, you may open an Android app using its package id.
* (iOS, Android) Check if any apps are installed that can launch via a specified uri.
* (iOS, Android) Launch an app via a specified uri.
* (Android) Check if an app is installed via its package id.
* (Android) Check if an app is installed for an action name.
* (Android) Launch an app via its package id.
* (Android) Launch an app for an action name.
* (Android) Launch an app with extras included.
* (Android) Return results from a launched app once it is finished.

## 2. Installation

### Automatically (CLI / Plugman)

```
$ cordova plugin add https://github.com/nchutchind/cordova-plugin-app-launcher.git
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
<!-- 
Additionally, for iOS 9+, you may need to install the cordova-plugin-queries-schemes plugin, which will allow whitelisting of what URLs your app will be allowed to launch. 

cordova plugin add cordova-plugin-queries-schemes
-->
```
```xml
<!-- for Android -->
<feature name="Launcher">
	<param name="android-package" value="com.hutchind.cordova.plugins.launcher.Launcher" />
</feature>
```

2\. Add `www/Launcher.js` to your project and reference it in `index.html`:
```html
<script type="text/javascript" src="js/Launcher.js"></script>
```

3\. Copy the files in `src/` for iOS and/or Android into your project.

iOS: Copy `Launcher.h` and `Launcher.m` to `platforms/ios/<ProjectName>/Plugins`

Android: Copy `Launcher.java` to `platforms/android/src/com/hutchind/cordova/plugins` (you will probably need to create this path)

### PhoneGap Build (possibly outdated)

Add the following xml to your `config.xml` to always use the latest version of this plugin:
```xml
<gap:plugin name="cordova-plugin-app-launcher" />
```
or to use a specific version:
```xml
<gap:plugin name="cordova-plugin-app-launcher" version="0.3.1" />
```
For iOS 9+, the following may need to be added so that the URLs used to launch apps can be whitelisted (in this example, customSchemeName:// and fb:// would have been the URLs registered to the apps we want to be able to launch):
```xml
<gap:config-file platform="ios" parent="LSApplicationQueriesSchemes" overwrite="true">
	<array>
		<string>customSchemeName</string>
		<string>fb</string>
	</array>
</gap:config-file>
```

## 3. Parameters

List of parameters that can be passed to `window.plugins.launcher.launch` and `window.plugins.launcher.canLaunch`

**uri**, can be used in `launch` and `canLaunch` (**iOS** and **Android**)

```javascript
	window.plugins.launcher.launch({
		uri:'http://nasatv-lh.akamaihd.net/i/NASA_101@319270/master.m3u8',
	}, successCallback, errorCallback);
```

**packageName**, can be used in `launch` and `canLaunch` (**Android**)

```javascript
	window.plugins.launcher.launch({
		uri:'http://nasatv-lh.akamaihd.net/i/NASA_101@319270/master.m3u8',
		packageName:'com.mxtech.videoplayer.ad',
	}, successCallback, errorCallback);
```

**dataType**, can be used in `launch` and `canLaunch` (**Android**)

```javascript
	window.plugins.launcher.launch({
		uri:'http://nasatv-lh.akamaihd.net/i/NASA_101@319270/master.m3u8',
		packageName:'com.mxtech.videoplayer.ad',
		dataType:'application/x-mpegURL'
	}, successCallback, errorCallback);
```

**flags**, can be used in `launch` (**Android**)

```javascript
	window.plugins.launcher.launch({
		uri:'http://nasatv-lh.akamaihd.net/i/NASA_101@319270/master.m3u8',
		packageName:'com.mxtech.videoplayer.ad',
		dataType:'application/x-mpegURL',
		flags: window.plugins.launcher.FLAG_ACTIVITY_NEW_TASK,
	}, successCallback, errorCallback);
```

Abailable flags

- **FLAG_ACTIVITY_NEW_TASK**: If set, this activity will become the start of a new task (Open the app in a new activity instead of the current one).

If you are missing some flags, you can use its int value or edit and add it in the file [Launcher.js](https://github.com/nchutchind/cordova-plugin-app-launcher/blob/master/www/Launcher.js) and open a pull request, see all [Intent flags list](https://developer.android.com/reference/android/content/Intent).

**extras**, can be used in `launch` (**Android**)

More info in [Extras Data Types](#extras-data-types) and below.

```javascript
	window.plugins.launcher.launch({
		uri:'http://nasatv-lh.akamaihd.net/i/NASA_101@319270/master.m3u8',
		packageName:'com.mxtech.videoplayer.ad',
		dataType:'application/x-mpegURL',
		flags: window.plugins.launcher.FLAG_ACTIVITY_NEW_TASK,
		extras: [
			{"name":"position", "value":3000, "dataType":"int"}
		]
	}, successCallback, errorCallback);
```

**getAppList**, can be used in `canLaunch` (**Android**)

`successCallback` data will contain an array named `appList` with the package names of applications that can handle the uri specified.

```javascript
	window.plugins.launcher.canLaunch({
		uri:'http://nasatv-lh.akamaihd.net/i/NASA_101@319270/master.m3u8',
		packageName:'com.mxtech.videoplayer.ad',
		dataType:'application/x-mpegURL',
		getAppList: true,
	}, successCallback, errorCallback);
```

## 4. Usage
```javascript
	// Default handlers
	var successCallback = function(data) {
		alert("Success!");
		// if calling canLaunch() with getAppList:true, data will contain an array named "appList" with the package names of applications that can handle the uri specified.
	};
	var errorCallback = function(errMsg) {
		alert("Error! " + errMsg);
	}
```

Check to see if Facebook can be launched via uri (**iOS** and **Android**)
```javascript
	window.plugins.launcher.canLaunch({uri:'fb://'}, successCallback, errorCallback);
```

Check to see if Facebook is installed (**Android**)
```javascript
	window.plugins.launcher.canLaunch({packageName:'com.facebook.katana'}, successCallback, errorCallback);
```

Launch Facebook to the logged in user's profile (**iOS** and **Android**)
```javascript
	window.plugins.launcher.launch({uri:'fb://profile'}, successCallback, errorCallback);
```

Launch Facebook via package id (**Android**)
```javascript
	window.plugins.launcher.launch({packageName:'com.facebook.katana'}, successCallback, errorCallback);
```

Check to see if an app is installed that can play NASA TV (**Android**)
```javascript
	window.plugins.launcher.canLaunch({
		uri:'http://nasatv-lh.akamaihd.net/i/NASA_101@319270/master.m3u8',
		dataType:'application/x-mpegURL'
	}, successCallback, errorCallback);
```

Get a list of installed app packages that can play NASA TV (**Android**)
```javascript
	window.plugins.launcher.canLaunch({
		uri:'http://nasatv-lh.akamaihd.net/i/NASA_101@319270/master.m3u8',
		dataType:'application/x-mpegURL',
		getAppList: true
	}, successCallback, errorCallback);
```

Launch NASA TV video stream in MxPlayer Free (**Android**)
```javascript
	window.plugins.launcher.launch({
		packageName:'com.mxtech.videoplayer.ad',
		uri:'http://nasatv-lh.akamaihd.net/i/NASA_101@319270/master.m3u8',
		dataType:'application/x-mpegURL'
	}, successCallback, errorCallback);
```

Launch MxPlayer Free with Extras for specific videos from the sdcard, specific titles, and starting at 3 seconds in (**Android**)
```javascript
	var sdcard = "file:///sdcard/";
	var file1 = sdcard + "video1.mp4", file2 = sdcard + "video2.mp4";

	window.plugins.launcher.launch({
		packageName:'com.mxtech.videoplayer.ad',
		uri:file1,
		dataType:'video/*'
		extras: [
			{"name":"video_list", "value":[file1,file2], "dataType":"ParcelableArray", "paType":"Uri"},
			{"name":"video_list.name", "value":["Awesome Video","Sweet Title"], "dataType":"StringArray"},
			{"name":"position", "value":3000, "dataType":"int"}
		]
	}, successCallback, errorCallback);
```
Launch MxPlayer Free with Extras for a specific video with title and return results (**Android**)
```javascript
	var filename = "file:///sdcard/video.mp4";

	window.plugins.launcher.launch({
		packageName:'com.mxtech.videoplayer.ad',
		uri:filename,
		dataType:'video/*',
		extras: [
			{"name":"video_list", "value":[filename], "dataType":"ParcelableArray", "paType":"Uri"},
			{"name":"video_list.name", "value":["Whatever Title You Want"], "dataType":"StringArray"},
			{"name":"return_result", "value":true, "dataType":"Boolean"}
		],
		successCallback: function(json) {
			if (json.isActivityDone) {
				if (json.extras && json.extras.end_by) {
					if (json.data) {
						alert("MxPlayer stopped while on video: " + json.data);
					}
					if (json.extras.end_by == "user") {
						// MxPlayer stopped because the User quit
						alert("User watched " + json.extras.position + " of " + json.extras.duration + " before quitting.");
					} else {
						alert("MxPlayer finished playing video without user quitting.");
					}
				} else {
					alert("Playback finished, but we have no results from MxPlayer.");
				}
			} else {
				console.log("MxPlayer launched");
			}
		},
		errorCallback: function(err) {
			alert("There was an error launching MxPlayer.")
		}
	});
```

Check to see if Peek Acuity can be launched via an Action Name (**Android**)

<i>AndroidManifest.xml:</i>
```xml
	<activity
		android:name="org.peekvision.lite.android.visualacuity.VisualAcuityActivity">
		<intent-filter>
			<action android:name="org.peekvision.intent.action.TEST_ACUITY"/>
			<category android:name="android.intent.category.DEFAULT"/>
		</intent-filter>
	</activity>
```

<i>Typescript:</i>
```typescript
	let actionName = 'org.peekvision.intent.action.TEST_ACUITY';

	window.plugins.launcher.canLaunch({actionName: actionName},
		data => console.log("Peek Acuity can be launched"),
		errMsg => console.log("Peek Acuity not installed! " + errMsg)
	);
```

Launch Peek Acuity via an Action Name with Extras and return results (**Android**)
```typescript
	let actionName = 'org.peekvision.intent.action.TEST_ACUITY';

	let extras = [
	  {"name":"progressionLogMarArray", "value":[1.0,0.8,0.6,0.3,0.1],"dataType":"DoubleArray"},
	  {"name":"instructions",	"value":"none",		"dataType":"String"},
	  {"name":"eye",		"value":"left",		"dataType":"String"},
	  {"name":"beyondOpto",		"value":true,		"dataType":"Boolean"},
	  {"name":"testDistance",	"value":"4m",		"dataType":"String"},
	  {"name":"displayResult",	"value":false,		"dataType":"Boolean"},
	  {"name":"return_result",	"value":true,		"dataType":"Boolean"}
	];

	window.plugins.launcher.launch({actionName: actionName, extras: extras},
		json => {
			if (json.isActivityDone) {
				if (json.data) {
					console.log("data=" + json.data);
				}
				if (json.extras) {
					if (json.extras.logMar) {
						console.log("logMar=" + json.extras.logMar);
					}
					if (json.extras.averageLux) {
						console.log("averageLux=" + json.extras.averageLux);
					}
				} else {
					console.log("Peek Acuity done but no results");
				}
			} else {
				console.log("Peek Acuity launched");
			}
		},
		errMsg => console.log("Peek Acuity error launching: " + errMsg)
	 );
```

# Extras Data Types

Most datatypes that can be put into an Android Bundle are able to be passed in. You must provide the datatype to convert to.
Only Uri Parcelables are supported currently.
```javascript
	extras: [
		{"name":"myByte", "value":1, "dataType":"Byte"},
		{"name":"myByteArray", "value":[1,0,2,3], "dataType":"ByteArray"},
		{"name":"myShort", "value":5, "dataType":"Short"},
		{"name":"myShortArray", "value":[1,2,3,4], "dataType":"ShortArray"},
		{"name":"myInt", "value":2000, "dataType":"Int"},
		{"name":"myIntArray", "value":[12,34,56], "dataType":"IntArray"},
		{"name":"myIntArrayList", "value":[123,456,789], "dataType":"IntArrayList"},
		{"name":"myLong", "value":123456789101112, "dataType":"Long"},
		{"name":"myLongArray", "value":[123456789101112,121110987654321], "dataType":"LongArray"},
		{"name":"myFloat", "value":12.34, "dataType":"Float"},
		{"name":"myFloatArray", "value":[12.34,56.78], "dataType":"FloatArray"},
		{"name":"myDouble", "value":12.3456789, "dataType":"Double"},
		{"name":"myDoubleArray", "value":[12.3456789, 98.7654321], "dataType":"DoubleArray"},
		{"name":"myBoolean", "value":false, "dataType":"Boolean"},
		{"name":"myBooleanArray", "value":[true,false,true], "dataType":"BooleanArray"},
		{"name":"myString", "value":"this is a test", "dataType":"String"},
		{"name":"myStringArray", "value":["this","is", "a", "test"], "dataType":"StringArray"},
		{"name":"myStringArrayList", "value":["this","is","a","test"], "dataType":"StringArrayList"},
		{"name":"myChar", "value":"T", "dataType":"Char"},
		{"name":"myCharArray", "value":"this is a test", "dataType":"CharArray"},
		{"name":"myCharSequence", "value":"this is a test", "dataType":"CharSequence"},
		{"name":"myCharSequenceArray", "value":["this","is a", "test"], "dataType":"CharSequenceArray"},
		{"name":"myCharSequenceArrayList", "value":["this","is a", "test"], "dataType":"CharSequenceArrayList"},
		{"name":"myParcelable", "value":"http://foo", "dataType":"Parcelable", "paType":"Uri"},
		{"name":"myParcelableArray", "value":["http://foo","http://bar"], "dataType":"ParcelableArray", "paType":"Uri"},
		{"name":"myParcelableArrayList", "value":["http://foo","http://bar"], "dataType":"ParcelableArrayList", "paType":"Uri"},
		{"name":"mySparseParcelableArray", "value":{"10":"http://foo", "-25":"http://bar"}, "dataType":"SparseParcelableArray", "paType":"Uri"},
	]
```

#### Launcher.canLaunch Success Callback
No data is passed.

#### Launcher.canLaunch Error Callback
Passes a string containing an error message.

#### Launcher.launch Success Callback Data
Passes a JSON object with varying parts.

Activity launched
```javascript
	{
		isLaunched: true
	}
```

Activity finished
```javascript
	{
		isActivityDone: true
	}
```

Activity launched and data returned
```javascript
	{
		isActivityDone: true,
		data: <Uri returned from Activity, if any>,
		extras <JSON object containing data returned from Activity, if any>
	}
```

#### Launcher.launch Error Callback Data
Passes an error message as a string.

## 5. Changelog
0.4.0: Android: Added ability to launch with intent. Thanks to [@mmey3k] for the code.

0.2.0: Android: Added ability to launch activity with extras and receive data back from launched app when it is finished.

0.1.2: Added ability to check if any apps are installed that can handle a certain datatype on Android.

0.1.1: Added ability to launch a package with a data uri and datatype on Android.

0.1.0: initial version supporting Android and iOS

## 6. Credits
Special thanks to [@michael1t](https://github.com/michael1t) for sponsoring the development of the Extras portion of this plugin.

## 7. License

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
