//
//  ComplyCubeC.swift
//  GreenBankPlugin
//
//  Created by Kenshin Vag on 18/5/2023.
//

import Foundation
import ComplyCubeMobileSDK

@objc(ComplyCubeBridge) class ComplyCubeBridge : CDVPlugin {
  @objc(mount:)
  func mount(command: CDVInvokedUrlCommand) {
    
      // set settings on the object
      let sdk = ComplyCubeRNSDK()
      sdk.setSettings(command.argument(at: 0) as! NSObject)
      sdk.mount()
      
      
    var pluginResult = CDVPluginResult (status: CDVCommandStatus_ERROR, messageAs: "The Plugin Failed");
    // Set the plugin result to succeed.
        pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "The plugin succeeded");
    // Send the function result back to Cordova.
    self.commandDelegate!.send(pluginResult, callbackId: command.callbackId);
  }
}
