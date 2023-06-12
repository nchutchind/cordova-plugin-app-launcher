"use strict";
function Launcher() {}

// Flags
Launcher.prototype.FLAG_ACTIVITY_NEW_TASK = 0x10000000;

Launcher.prototype.canLaunch = function (options, successCallback, errorCallback) {
	options = options || {};
	options.successCallback = options.successCallback || successCallback;
	options.errorCallback = options.errorCallback || errorCallback;
	cordova.exec(options.successCallback || null, options.errorCallback || null, "Launcher", "canLaunch", [options]);
};

Launcher.prototype.launch = function(options, successCallback, errorCallback) {
	options = options || {};
	options.successCallback = options.successCallback || successCallback;
	options.errorCallback = options.errorCallback || errorCallback;
	cordova.exec(options.successCallback || null, options.errorCallback || null, "Launcher", "launch", [options]);
};

Launcher.install = function () {
	if (!window.plugins) {
		window.plugins = {};
	}

	window.plugins.launcher = new Launcher();
	return window.plugins.launcher;
};

cordova.addConstructor(Launcher.install);
