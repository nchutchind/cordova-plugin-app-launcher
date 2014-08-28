"use strict";
function Launcher() {};

Launcher.prototype.canLaunch = function (options, successCallback, errorCallback) {
	cordova.exec(successCallback, errorCallback, "Launcher", "canLaunch", [options]);
};

Launcher.prototype.launch = function(options, successCallback, errorCallback) {
	cordova.exec(successCallback, errorCallback, "Launcher", "launch", [options]);
}

Launcher.install = function () {
	if (!window.plugins) {
		window.plugins = {};
	}

	window.plugins.launcher = new Launcher();
	return window.plugins.launcher;
};

cordova.addConstructor(Launcher.install);