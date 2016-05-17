#import "Launcher.h"
#import <Cordova/CDV.h>

@implementation Launcher

- (void)canLaunch:(CDVInvokedUrlCommand*)command {
	NSDictionary* options = [command.arguments objectAtIndex:0];
	CDVPluginResult * pluginResult = nil;

	if ([options objectForKey:@"uri"]) {
		NSString *uri = [options objectForKey:@"uri"];
		if ([[UIApplication sharedApplication] canOpenURL: [NSURL URLWithString:uri]]) {
			 pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
			[self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
		} else {
			pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"No app installed that can handle that uri."];
			[self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
		}
	} else {
		pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Missing option: 'uri' required."];
		[self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
	}
}

- (void)launch:(CDVInvokedUrlCommand*)command {
	NSDictionary* options = [command.arguments objectAtIndex:0];
	CDVPluginResult * pluginResult = nil;

	if ([options objectForKey:@"uri"]) {
		NSString *uri = [options objectForKey:@"uri"];
		if ([[UIApplication sharedApplication] canOpenURL: [NSURL URLWithString:uri]]) {
			NSURL *launchURL = [NSURL URLWithString:uri];
			[[UIApplication sharedApplication] openURL: launchURL];
			pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
			[self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
		} else {
			pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"No app installed that can handle that uri."];
			[self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
		}
	} else {
		pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Missing option: 'uri' required."];
		[self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
	}
}

@end
