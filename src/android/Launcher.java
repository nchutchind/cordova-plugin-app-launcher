package com.hutchind.cordova.plugins;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ActivityNotFoundException;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;

public class Launcher extends CordovaPlugin {
	public static final String TAG = "Launcher Plugin";
	public static final String ACTION_CAN_LAUNCH = "canLaunch";
	public static final String ACTION_LAUNCH = "launch";

	private CallbackContext callback;

	private abstract class LauncherRunnable implements Runnable {
		public CallbackContext callbackContext;
		LauncherRunnable(CallbackContext cb) {
			this.callbackContext = cb;
		}
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		callback = callbackContext;
		if (ACTION_CAN_LAUNCH.equals(action)) {
			return canLaunch(args);
		} else if (ACTION_LAUNCH.equals(action)) {
			return launch(args);
		}
		return false;
	}

	private boolean canLaunch(JSONArray args) throws JSONException {
		final JSONObject options = args.getJSONObject(0);
		final CordovaInterface mycordova = cordova;
		final CordovaPlugin plugin = this;

		if (options.has("packageName")) {
			final String appPackageName = options.getString("packageName");
			cordova.getThreadPool().execute(new LauncherRunnable(this.callback) {
				public void run() {
					final Intent intent = new Intent(Intent.ACTION_VIEW);
					String packageName = appPackageName;
					String passedActivityName = null;
					if (packageName.contains("/")) {
						String[] items = appPackageName.split("/");
						packageName = items[0];
						passedActivityName = items[1];
					}
					final ActivityInfo activity = getActivity(intent, packageName);

					if (activity != null) {
						callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
					} else {
						// check the package name
						final PackageManager pm = plugin.webView.getContext().getPackageManager();
						final Intent launchIntent = pm.getLaunchIntentForPackage(packageName);
						if (launchIntent != null) {
							callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
						}
					}
					callback.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "Application is not installed."));
				}
			});
		} else if (options.has("uri")) {
			final String uri = options.getString("uri");
			final String dataType = options.has("dataType") ? options.getString("dataType") : null;

			cordova.getThreadPool().execute(new LauncherRunnable(this.callback) {
				public void run() {
					final Intent intent = new Intent(Intent.ACTION_VIEW);
					if (dataType != null) {
						intent.setDataAndType(Uri.parse(uri), dataType);
					} else {
						intent.setData(Uri.parse(uri));
					}
					if (mycordova.getActivity().getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
						callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
					} else {
						callback.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "No application found."));
					}
				}
			});
		}

		return true;
	}

	private ActivityInfo getActivity(final Intent intent, final String appPackageName) {
		final PackageManager pm = webView.getContext().getPackageManager();
		try {
			Log.e(TAG, pm.getApplicationInfo(appPackageName, 0) + "");
		}catch (NameNotFoundException e) {
			callback.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "Application is not installed."));
		}
		return null;
	}

	private boolean launch(JSONArray args) throws JSONException {
		final JSONObject options = args.getJSONObject(0);
		if (options.has("uri") && (options.has("packageName") || options.has("dataType"))) {
			String dataType = null;
			String packageName = null;
			if (options.has("packageName")) {
				packageName = options.getString("packageName");
			}
			if (options.has("dataType")) {
				dataType = options.getString("dataType");
			}
			launchAppWithData(packageName, options.getString("uri"), dataType);
			return true;
		} else if (options.has("packageName")) {
			launchApp(options.getString("packageName"));
			return true;
		} else if (options.has("uri")) {
			launchIntent(options.getString("uri"));
			return true;
		}
		return false;
	}

	private void launchAppWithData(final String packageName, final String uri, final String dataType) throws JSONException {
		final CordovaInterface mycordova = cordova;
		final CordovaPlugin plugin = this;
		cordova.getThreadPool().execute(new LauncherRunnable(this.callback) {
			public void run() {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				if (dataType != null) {
					intent.setDataAndType(Uri.parse(uri), dataType);
				} else {
					intent.setData(Uri.parse(uri));
				}

				if (packageName != null && !packageName.equals("")) {
					intent.setPackage(packageName);
				}

				try {
					mycordova.startActivityForResult(plugin, intent, 0);
					callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
				} catch(ActivityNotFoundException e) {
					Log.e(TAG, "Error: No applications installed that can handle uri " + uri);
					e.printStackTrace();
					callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "Application not found."));
				}

			}
		});
	}

	private void launchApp(final String packageName) {
		final CordovaInterface mycordova = cordova;
		final CordovaPlugin plugin = this;

		cordova.getThreadPool().execute(new LauncherRunnable(this.callback) {
			public void run() {
				final PackageManager pm = plugin.webView.getContext().getPackageManager();
				final Intent launchIntent = pm.getLaunchIntentForPackage(packageName);
				boolean appNotFound = launchIntent == null;

				if (!appNotFound) {
					try {
						mycordova.startActivityForResult(plugin, launchIntent, 0);
					} catch (ActivityNotFoundException e) {
						Log.e(TAG, "Error: Activity for package" + packageName + " was not found.");
						e.printStackTrace();
						appNotFound = true;
					}
				}
				if (appNotFound) {
					callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "Application not found."));
				}
				callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
			}
		});
	}

	private void launchIntent(final String uri) {
		final CordovaInterface mycordova = cordova;
		final CordovaPlugin plugin = this;
		cordova.getThreadPool().execute(new LauncherRunnable(this.callback) {
			public void run() {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(uri));
				try {
					mycordova.startActivityForResult(plugin, intent, 0);
				} catch (ActivityNotFoundException e) {
					Log.e(TAG, "Error: Activity for " + uri + " was not found.");
					e.printStackTrace();
					callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "Activity not found."));
				}
				callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
			}
		});
	}
}