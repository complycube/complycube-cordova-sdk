/**
 */
package com.plugin.complycube;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.plugin.complycube.ComplyCubeActivity;

import java.util.ArrayList;

public class ComplyCubeBridge extends CordovaPlugin {
  private static final String TAG = "ComplyCubeBridge";
  private CallbackContext currentCallbackContext = null;

  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);

    Log.d(TAG, "Initializing ComplyCube");
  }

  public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
    Log.i("ComplyCube", "execute " + action);
    if(action.equals("mount")) {
      //callbackContext.success("ComplyCube mounted");
      this.currentCallbackContext = callbackContext;
      Context context = cordova.getActivity().getApplicationContext();
      Log.i("ComplyCube", "JSON is " + args.getJSONObject(0).toString());
      final JSONObject options = args.getJSONObject(0);

      Intent intent = new Intent(context, ComplyCubeActivitySDK.class);
      // cordova.startActivityForResult(this, intent, 1);
      intent.putExtra("settings", options.toString());
      cordova.startActivityForResult(this, intent, 1);
    }
    if(action.equals("init")) {
      this.currentCallbackContext = callbackContext;
      final String token;
      final String applicantId;
      String locale;
      String documentType;
      final ArrayList flowSteps = new ArrayList<String>();
      JSONArray flowStepsArray;


      try {
        JSONObject options = args.getJSONObject(0);
        token = options.getString("token");
        try {
          locale = options.getString("locale");
        } catch (JSONException e) {
          locale = "en";
        }
        applicantId = options.getString("applicant_id");
        try {
          documentType = options.getString("document_type");
        } catch (JSONException e) {
          documentType = "";
        }
        flowStepsArray = options.getJSONArray("flow_steps");
        for (int i = 0; i < flowStepsArray.length(); i++) {
          flowSteps.add(flowStepsArray.getString(i));
        }
      } catch (JSONException e) {
        callbackContext.error("Error encountered: " + e.getMessage());
        return false;
      }

      Intent intent = new Intent("com.plugin.complcyube.ComplyCubeActivity");
      intent.putExtra("token", token);
      intent.putExtra("applicant_id", applicantId);
      intent.putExtra("document_type", documentType);
      intent.putExtra("flow_steps", flowSteps);
      intent.putExtra("locale", locale);
      cordova.startActivityForResult(this, intent, 1);
    }
    return true;
  }

  @Override
  public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    switch (resultCode) {
      case Activity.RESULT_OK:
        Bundle extras = data.getExtras();// Get data sent by the Intent
        String information = extras.getString("result"); // data parameter will be send from the other activity.
        PluginResult result_success = new PluginResult(PluginResult.Status.OK, information);
        result_success.setKeepCallback(true);
        currentCallbackContext.sendPluginResult(result_success);
        Log.i("ComplyCube", "RESULT_OK");
        break;
      case Activity.RESULT_CANCELED:
        Log.i("ComplyCube", "RESULT_CANCELED");
        PluginResult resultado = new PluginResult(PluginResult.Status.ERROR);
        resultado.setKeepCallback(true);
        currentCallbackContext.sendPluginResult(resultado);
        break;
      case 20:
        Bundle extras_ = data.getExtras();// Get data sent by the Intent
        String information_ = extras_.getString("error"); // data parameter will be send from the other activity.
        PluginResult resultadoe = new PluginResult(PluginResult.Status.ERROR, information_);
        Log.i("ComplyCube", "RESULT_20 " + information_);

        resultadoe.setKeepCallback(true);
        currentCallbackContext.sendPluginResult(resultadoe);
        break;
      default:
        Log.i("ComplyCube", "RESULT_OTHER");
        break;
    }
    // Handle other results if exists.
    super.onActivityResult(requestCode, resultCode, data);
  }
}