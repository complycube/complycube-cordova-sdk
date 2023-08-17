package com.plugin.complycube

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.complycube.sdk.ComplyCubeSdk
import com.complycube.sdk.common.data.ClientAuth
import com.complycube.sdk.common.data.Country
import com.complycube.sdk.common.data.IdentityDocumentType
import com.complycube.sdk.common.data.Stage
import com.complycube.sdk.common.data.Result
import com.complycube.sdk.presentation.theme.SdkColors
import org.json.JSONArray
import org.json.JSONObject

class ComplyCubeActivitySDK : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = getIntent().extras
        val settings_ = intent?.get("settings") as String
        // generate a map reading a JSON from settings
        val settings = JSONObject(settings_)

        val s = SettingsManager(settings)

        var builder = ComplyCubeSdk.Builder(this){

            if(it is Result.Canceled){
                //emitter.emit("ComplyCubeCancel", JSONObject().put("message", it).toString())
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
            if(it is Result.Error){
                //emitter.emit("ComplyCubeError", JSONObject().put("message", it).toString())
                val error = it as Result.Error
                var intent = Intent()
                intent.putExtra("message", error.message)
                setResult(20, intent)
                finish()
            }
            if(it is Result.Success){
                var poaResults = ArrayList<String>()
                var documents = ArrayList<String>()
                var selfievideo = ArrayList<String>()
                var selfiePhoto = ArrayList<String>()
                for(i in  it.stages){
                    ccLog("result: ${i}")
                    if(i is Result.StageResult.Document){
                        ccLog("am document: ${i.id}")
                        documents.add(i.id)
                    }
                    if(i is Result.StageResult.ProofOfAddress){
                        poaResults.add(i.id)
                    }
                    if(i is Result.StageResult.LivePhoto){
                        selfiePhoto.add(i.id)
                    }
                    if(i is Result.StageResult.LiveVideo){
                        selfievideo.add(i.id)
                    }
                }
                var result = JSONObject()
                result.put("documentIds", JSONArray(documents))
                result.put("livePhotoIds", JSONArray(selfiePhoto))
                result.put("liveVideoIds", JSONArray(selfievideo))
                result.put("poaIds", JSONArray(poaResults))
                ccLog("Emmiting success result ${result.toString()}")
                //emitter.emit("ComplyCubeSuccess", result.toString())
                var intent = Intent()
                intent.putExtra("result", result.toString())
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
        // Prepare stages
        ccLog("Stages: ${s.stages.size}")
        builder.withStages(s.stages[0], *s.stages.drop(1).toTypedArray())
        ccLog("Stages: ${s.countries.size}")
        builder.withCountries(s.countries[0], *s.countries.drop(1).toTypedArray())
        if(s.colorScheme != null){
            builder.withCustomColors(s.colorScheme as SdkColors)
        }
        builder.start(
            ClientAuth(
                s.clientToken
                ,s.clientID
            )
        )

    }
}