//
//  ComplyCubeRNSDK.swift
//  cccomponent
//
//  Created by Kenshin Vag on 8/11/2022.
//

import Foundation
import UIKit

import ComplyCubeMobileSDK


@objc(ComplyCubeRNSDK)
class ComplyCubeRNSDK: NSObject{

  
  public var settings: NSObject = NSObject()
  func supportedEvents() -> [String]! {
      return ["ComplyCubeSuccess", "ComplyCubeError", "ComplyCubeCancel"]
    }
  
  @objc public func setSettings(_ settings: NSObject){
    self.settings = settings
  }
  
  @objc
  public func mount(){
    let settings: NSObject = self.settings as! NSObject
    DispatchQueue.main.async {
      let sm = SettingManager(request: settings as! [AnyHashable: Any])
        print("[Complycube] Setting manager set \(sm.token)")

      var m = ComplyCubeMobileSDK.FlowBuilder()
        
              m.withSDKToken(sm.token)
              .withClientId(sm.clientID)
              .withCallbackHandler(self)
              .withStages(sm.stages as! [ComplyCubeMobileSDKStage])
              .withColorScheme(sm.scheme)
      print("We're here")
        do{
            try m.start(fromVc: (UIApplication.shared.windows.first?.rootViewController?.findTopMostController())!)
        }catch {
            
        }
            
    }
  }
  private func _errorToJson(_ error: ComplyCubeError) -> [AnyHashable: Any]{
    return [
      "message": error.message,
      "errorCode": error.errorCode,
      "description": error.description
    ]
  }
  private func _resultToJson(_ result: ComplyCubeResult) -> [AnyHashable: Any]{
    return result.ids
  }
}


extension UIViewController {
    public func findTopMostController() -> UIViewController {
      var topController: UIViewController? = self
             while topController!.presentedViewController != nil {
                 topController = topController!.presentedViewController!
             }
             return topController!
    }
 }


extension ComplyCubeRNSDK: ComplyCubeMobileSDKDelegate{
  func onError(_ errors: [ComplyCubeError]){
    let errors_ = errors.map { cce in
      return self._errorToJson(cce)
    }
//    self.sendEvent(withName: "ComplyCubeError", body: errors_)
  }
  func onCancelled(_ error: ComplyCubeError){
//    self.sendEvent(withName: "ComplyCubeCancel", body: self._errorToJson(error))
    let sss: UIViewController = (UIApplication.shared.windows.first?.rootViewController?.findTopMostController())! as UIViewController
    sss.navigationController?.popToRootViewController(animated: false)
  }
  func onSuccess(_ result: ComplyCubeResult){
//    self.sendEvent(withName: "ComplyCubeSuccess", body: self._resultToJson(result))
  }
}
