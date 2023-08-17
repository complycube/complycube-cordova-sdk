package com.plugin.complycube

import android.graphics.Color
import com.complycube.sdk.common.data.Country
import com.complycube.sdk.common.data.IdentityDocumentType
import com.complycube.sdk.common.data.ProofOfAddressDocumentType
import com.complycube.sdk.common.data.Stage
import com.complycube.sdk.presentation.theme.SdkColors
import org.json.JSONArray
import org.json.JSONObject
import kotlin.reflect.typeOf

fun ccLog(message: String) {
    System.out.println("ComplyCube: $message")
}


class SettingsManager {
    var clientID: String = ""
    var clientToken: String = ""
    var stages = arrayOf<Stage>() // Can be an array
    var colorScheme: SdkColors? = null
    var sCountries = mutableListOf<String>()
    var countries = arrayOf<Country>()

    public constructor(settings: JSONObject){
        // TODO Make it more using if has to manage errorrs
        ccLog("ComplyCube settings: $settings")
        clientID = settings.getString("clientID") ?: ""
        clientToken = settings.getString("clientToken") ?: ""
        var stages = settings.getJSONArray("stages")
        var scheme: JSONObject? = null
        if(settings.has("scheme")){
            scheme = settings.getJSONObject("scheme")
            this.buildScheme(scheme as JSONObject)
        }
        buildStages(stages)
        // Convert string countries to Country
        ccLog("Countries: ${sCountries.size}")
        for(country in sCountries.distinct()){
            countries += Country.valueOf(country)
        }
        ccLog("Stages: ${colorScheme.toString()}")
    }

    private fun stageStringToCCStage(stage: String): Stage {
        print("ComplyCube: $stage")
        return when (stage) {
            "intro" -> Stage.DefaultStage.Welcome()
            "consent" -> Stage.DefaultStage.UserConsent()
            "complete" -> Stage.DefaultStage.Complete()
            "face_capture" -> Stage.CustomStage.SelfiePhoto()
            "faceCapture" -> Stage.CustomStage.SelfiePhoto()
            "videoCapture" -> Stage.CustomStage.SelfieVideo()
            // Still the video capture and consent
            else -> Stage.DefaultStage.Complete()
        }
    }
    private fun extractCountries(countries: JSONArray): MutableList<String> {
        val countriesList = mutableListOf<String>()
        for (i in 0 until countries.length()) {
            countriesList.add(countries.getString(i) ?: "")
        }
        return countriesList
    }
    private fun documentStageMapToCCStage(stage: JSONObject): Stage{
        var ccStage: Stage? = null
        val stageName: String = stage.getString("name") ?: ""
        // Check if the stages for more information
        val useMLAssistance = if ( stage.has("useMLAssistance") ) stage.getBoolean("useMLAssistance") as Boolean ?: false else false
        val showGuidance = if( stage.has("showGuidance") ) stage.getBoolean("showGuidance") as Boolean ?: false else false
        val retryLimit = if( stage.has("retryLimit") ) stage.getInt("retryLimit") as Int ?: 0 else 0
        val liveCapture = if(stage.has("liveCapture")) stage.getBoolean("liveCapture") as Boolean ?: false else false
        val title = if(stage.has("title")) stage.getString("title") as String ?: "" else ""
        ccLog("useMLAssistace ${useMLAssistance}")
        ccLog("showGuidance ${showGuidance}")
        ccLog("retryLimit ${retryLimit}")
        ccLog("liveCapture ${liveCapture}")

        when (stageName){
            "intro" -> {
                // Get the other properties
                val heading: String = stage.getString("heading") ?: ""
                val message: String = stage.getString("message") ?: ""
                ccStage = Stage.DefaultStage.Welcome(heading,message)

            }
            "consent" -> {
                // Get the other properties
                val heading: String = stage.getString("title") ?: ""
                val message: String = stage.getString("text") ?: ""
                ccStage = Stage.DefaultStage.UserConsent(heading, message)
            }
            "documentCapture" -> {
                // Extract document types
                val documentTypes: JSONObject = stage.getJSONObject("documentTypes") as JSONObject
                // Get keys
                // Get JSONObject keys in array form

                var keyIter = documentTypes.keys()
                var keys: MutableList<String> = mutableListOf()

                while (keyIter.hasNext()){
                    keys.add(keyIter.next())
                }
                // Iterate through keys and get the values
                var ccDocumentTypes = arrayOf<IdentityDocumentType>()
                for (key in keys){
                    when (key){
                        "passport" -> {
                            // Get document type status
                            // Get type of the value
                            if(documentTypes.get(key) is Boolean){
                                ccDocumentTypes += IdentityDocumentType.Passport()
                            }else if (documentTypes.get(key) is JSONArray){
                                val countries: JSONArray = documentTypes.getJSONArray(key) as JSONArray
                                // Convert ReadableArray to MutableList
                                val ccCountries = this.extractCountries(countries)
                                this.sCountries.addAll(ccCountries)
                                ccDocumentTypes += IdentityDocumentType.Passport()
                            }
                        }
                        "residence_permit" -> {
                            // Get document type status
                            if(documentTypes.get(key) is Boolean){
                                ccDocumentTypes += IdentityDocumentType.ResidencePermit()
                            }else if (documentTypes.get(key) is JSONArray){
                                val countries: JSONArray = documentTypes.getJSONArray(key)
                                // Convert ReadableArray to MutableList
                                val ccCountries = this.extractCountries(countries)
                                this.sCountries.addAll(ccCountries)
                                ccDocumentTypes += IdentityDocumentType.ResidencePermit()
                            }
                        }
                        "driving_license" -> {
                            // Get document type status
                            if(documentTypes.get(key) is Boolean){
                                ccDocumentTypes += IdentityDocumentType.DrivingLicence()
                            }else if (documentTypes.get(key) is JSONArray){
                                val countries: JSONArray = documentTypes.getJSONArray(key)
                                // Convert ReadableArray to MutableList
                                val ccCountries = this.extractCountries(countries)
                                this.sCountries.addAll(ccCountries)
                                ccDocumentTypes += IdentityDocumentType.DrivingLicence()
                            }
                        }
                        "national_identity_card" -> {
                            // Get document type status
                            if(documentTypes.get(key) is Boolean){
                                ccDocumentTypes += IdentityDocumentType.NationalIdentityCard()
                            }else if (documentTypes.get(key) is JSONArray){
                                val countries: JSONArray = documentTypes.getJSONArray(key)
                                // Convert ReadableArray to MutableList
                                val ccCountries = this.extractCountries(countries)
                                this.sCountries.addAll(ccCountries)
                                ccDocumentTypes += IdentityDocumentType.NationalIdentityCard()
                            }
                        }
                        else -> {
                            // Fire an error
                        }
                    }
                }
                // get other properties for the document capture
                // useMLAssistance, showGuidance, retryLimit, liveCapture, title
                ccLog("MLAss $useMLAssistance")
                ccStage = Stage.CustomStage.Document(
                    identityDocumentType = ccDocumentTypes[0],
                    identityDocumentTypes = *ccDocumentTypes.drop(0).toTypedArray(),
                    isGuidanceEnabled = showGuidance,
                    useLiveCaptureOnly = false,
                    //isMLAssistantEnabled = useMLAssistance,
                    retryLimit = retryLimit
                )
            }
            "face_capture", "faceCapture" -> {
                // Get the mode property
                val mode: String = stage.getString("mode") ?: ""
                when (mode){
                    "video" -> {
                        ccStage = Stage.CustomStage.SelfieVideo(
                            showGuidance,
                            //isMLAssistantEnabled = useMLAssistance,
                            retryLimit,
                        )
                        print("Complycube Video $ccStage")
                    }
                    "photo" -> {
                        ccLog("MLAss Photo $useMLAssistance")
                        ccStage = Stage.CustomStage.SelfiePhoto(
                            showGuidance,
                            liveCapture,
                            // useMLAssistance,
                            retryLimit,
                        )
                    }
                    else -> {
                        // Fire an error
                    }
                }
            }
            "poaCapture" -> {
                // Extract document types
                val documentTypes: JSONObject = stage.getJSONObject("documentTypes") as JSONObject
                // Get keys
                // Get JSONObject keys in array form
                var keyIter = documentTypes.keys()
                var keys: MutableList<String> = mutableListOf()
                while (keyIter.hasNext()){
                    keys.add(keyIter.next())
                }
                // Iterate through keys and get the values
                var ccDocumentTypes = arrayOf<ProofOfAddressDocumentType>()
                for (key in keys) {
                    when (key) {
                        "utility_bill" -> {
                            // Get document type status
                            if(documentTypes.get(key) is Boolean){
                                ccDocumentTypes += ProofOfAddressDocumentType.UtilityBill()
                            }else if (documentTypes.get(key) is JSONArray){
                                val countries: JSONArray = documentTypes.getJSONArray(key)
                                // Convert ReadableArray to MutableList
                                val ccCountries = this.extractCountries(countries)
                                this.sCountries.addAll(ccCountries)
                                ccDocumentTypes += ProofOfAddressDocumentType.UtilityBill()
                            }
                        }
                        "bank_statement" -> {
                            // Get document type status
                            if(documentTypes.get(key) is Boolean){
                                ccDocumentTypes += ProofOfAddressDocumentType.BankStatement()
                            }else if (documentTypes.get(key) is JSONArray){
                                val countries: JSONArray = documentTypes.getJSONArray(key)
                                // Convert ReadableArray to MutableList
                                val ccCountries = this.extractCountries(countries)
                                this.sCountries.addAll(ccCountries)
                                ccDocumentTypes += ProofOfAddressDocumentType.BankStatement()
                            }

                        }
                    }
                }
                ccStage = Stage.CustomStage.ProofOfAddress(
                    proofOfAddressDocumentType = ccDocumentTypes[0],
                    proofOfAddressDocumentTypes = *ccDocumentTypes.drop(1).toTypedArray(),
                    isGuidanceEnabled = showGuidance,
                    useLiveCaptureOnly = true,
                    //isMLAssistantEnabled = useMLAssistance,
                    retryLimit = retryLimit
                )
            }
            "conscent" -> {
                // Get the concent property
                // NO Concent
            }
            else -> {
                // Error trigger
            }

        }
        if (ccStage == null){
            return Stage.DefaultStage.Welcome()
        }else
            return ccStage as Stage
    }
    private fun buildStages(stages: JSONArray) {
        if (stages == null) {
            //TODO: fire up an error
        }
        var ccStages: Array<Stage> = arrayOf()
        for (i in 0 until stages.length()) {
            if (stages.get(i) is String) {
                ccStages += stageStringToCCStage(stages.getString(i) ?: "")
            }else if (stages.get(i) is JSONObject){
                val stage: JSONObject = stages.getJSONObject(i) ?: return
                ccStages += this.documentStageMapToCCStage(stage)
            }
        }
        this.stages = ccStages
    }

    private fun buildScheme(scheme: JSONObject) {
        // Get the stages property
        var schemeColour: SdkColors = SdkColors(
            if(scheme.has("primaryButtonBgColor") && scheme.getString("primaryButtonBgColor") != null) this.colorHexToColor(scheme.getString("primaryButtonBgColor") as String) else null,
            if(scheme.has("primaryButtonTextColor") && scheme.getString("primaryButtonTextColor") != null) this.colorHexToColor(scheme.getString("primaryButtonTextColor") as String) else null,
            if(scheme.has("primaryButtonBorderColor") && scheme.getString("primaryButtonBorderColor") != null) this.colorHexToColor(scheme.getString("primaryButtonBorderColor") as String) else null,
            if(scheme.has("secondaryButtonBgColor") && scheme.getString("secondaryButtonBgColor") != null) this.colorHexToColor(scheme.getString("secondaryButtonBgColor") as String) else null,
            if(scheme.has("secondaryButtonTextColor") && scheme.getString("secondaryButtonTextColor") != null) this.colorHexToColor(scheme.getString("secondaryButtonTextColor") as String) else null,
            if(scheme.has("secondaryButtonBorderColor") && scheme.getString("secondaryButtonBorderColor") != null) this.colorHexToColor(scheme.getString("secondaryButtonBorderColor") as String) else null,
            if(scheme.has("docTypeBgColor") && scheme.getString("docTypeBgColor") != null) this.colorHexToColor(scheme.getString("docTypeBgColor") as String) else null,
            if(scheme.has("docTypeBorderColor") && scheme.getString("docTypeBorderColor") != null) this.colorHexToColor(scheme.getString("docTypeBorderColor") as String) else null,
            null, // documentSelectorIconColor = if(scheme.has("documentSelectorIconColor") && scheme.getString("documentSelectorIconColor") != null) this.colorHexToColor(scheme.getString("documentSelectorIconColor") as String) else null,
            null, // documentSelectorTitleTextColor = if(scheme.has("documentSelectorTitleTextColor") && scheme.getString("documentSelectorTitleTextColor") != null) this.colorHexToColor(scheme.getString("documentSelectorTitleTextColor") as String) else null,
            null, // documentSelectorDescriptionTextColor = if(scheme.has("documentSelectorDescriptionTextColor") && scheme.getString("documentSelectorDescriptionTextColor") != null) this.colorHexToColor(scheme.getString("documentSelectorDescriptionTextColor") as String) else null,
            if(scheme.has("popUpBgColor") && scheme.getString("popUpBgColor") != null) this.colorHexToColor(scheme.getString("popUpBgColor") as String) else null,
            null, // infoPopupIconColor = if(scheme.has("infoPopupIconColor") && scheme.getString("infoPopupIconColor") != null) this.colorHexToColor(scheme.getString("infoPopupIconColor") as String) else null,
            if(scheme.has("popUpTitleColor") && scheme.getString("popUpTitleColor") != null) this.colorHexToColor(scheme.getString("popUpTitleColor") as String) else null,
            null,// infoPopupDescriptionTextColor = if(scheme.has("infoPopupDescriptionTextColor") && scheme.getString("infoPopupDescriptionTextColor") != null) this.colorHexToColor(scheme.getString("infoPopupDescriptionTextColor") as String) else null,
            null, // errorPopupColor = if(scheme.has("errorPopupColor") && scheme.getString("errorPopupColor") != null) this.colorHexToColor(scheme.getString("errorPopupColor") as String) else null,
            null,// errorPopupIconColor = if(scheme.has("errorPopupIconColor") && scheme.getString("errorPopupIconColor") != null) this.colorHexToColor(scheme.getString("errorPopupIconColor") as String) else null,
            null, // errorPopupTitleTextColor = if(scheme.has("errorPopupTitleTextColor") && scheme.getString("errorPopupTitleTextColor") != null) this.colorHexToColor(scheme.getString("errorPopupTitleTextColor") as String) else null,
            null, // errorPopupDescriptionTextColor = if(scheme.has("errorPopupDescriptionTextColor") && scheme.getString("errorPopupDescriptionTextColor") != null) this.colorHexToColor(scheme.getString("errorPopupDescriptionTextColor") as String) else null,
            null, // cameraButtonColor = if(scheme.has("cameraButtonColor") && scheme.getString("cameraButtonColor") != null) this.colorHexToColor(scheme.getString("cameraButtonColor") as String) else null,
            if(scheme.has("headerTitle") && scheme.getString("headerTitle") != null) this.colorHexToColor(scheme.getString("headerTitle") as String) else null,
            if(scheme.has("subheaderTitle") && scheme.getString("subheaderTitle") != null) this.colorHexToColor(scheme.getString("subheaderTitle") as String) else null,
            if(scheme.has("textSecondary") && scheme.getString("textSecondary") != null) this.colorHexToColor(scheme.getString("textSecondary") as String) else null,
            null, // backgroundColor = if(scheme.has("backgroundColor") && scheme.getString("backgroundColor") != null) this.colorHexToColor(scheme.getString("backgroundColor") as String) else null,
            null, // backgroundContentColor = if(scheme.has("backgroundContentColor") && scheme.getString("backgroundContentColor") != null) this.colorHexToColor(scheme.getString("backgroundContentColor") as String) else null,
            null,// backgroundContentContrastColor = if(scheme.has("backgroundContentContrastColor") && scheme.getString("backgroundContentContrastColor") != null) this.colorHexToColor(scheme.getString("backgroundContentContrastColor") as String) else null,
            null,// backgroundDividerColor = if(scheme.has("backgroundDividerColor") && scheme.getString("backgroundDividerColor") != null) this.colorHexToColor(scheme.getString("backgroundDividerColor") as String) else null,
            null// editTextColor = if(scheme.has("editTextColor") && scheme.getString("editTextColor") != null) this.colorHexToColor(scheme.getString("editTextColor") as String) else null,
        )
        this.colorScheme = schemeColour
    }
    private fun colorHexToColor(color: String): Long {
        return Color.parseColor(color).toLong()
    }
}