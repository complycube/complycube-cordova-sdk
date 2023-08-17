package com.plugin.complycube;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;


import android.util.Log;

import com.complycube.sdk.ComplyCubeSdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.*;



public class ComplyCubeActivity extends Activity {
//    private ComplyCube client;
//    private boolean firstTime = true;
//    private static final String TAG = "ComplyCubeBridge";
//
//    static final Map<String , DocumentType> DocumentTypesMap = new HashMap<String , DocumentType>() {{
//        put("passport", DocumentType.PASSPORT);
//        put("id", DocumentType.NATIONAL_IDENTITY_CARD);
//        put("driverLicense", DocumentType.DRIVING_LICENCE);
//        put("residentCard", DocumentType.RESIDENCE_PERMIT);
//    }};

//    private Map<String,FlowStep> createMapStringToFlowStepWithDocType(DocumentType docType){
//        HashMap flowStepMapping = new HashMap<String, FlowStep>();
//
//        flowStepMapping.put("welcome", FlowStep.WELCOME);
//        if (docType != null) {
//            flowStepMapping.put("document", new CaptureScreenStep(docType, CountryAlternatives.NO_COUNTRY));
//        }
//        else {
//            flowStepMapping.put("document",FlowStep.CAPTURE_DOCUMENT);
//        }
//
//        flowStepMapping.put("face", FlowStep.CAPTURE_FACE);
//        flowStepMapping.put("face_video", new FaceCaptureStep(FaceCaptureVariant.VIDEO));
//        flowStepMapping.put("final", FlowStep.FINAL);
//
//        return flowStepMapping;
//    }

//    private FlowStep[] generateFlowStep(ArrayList<String> flowSteps, DocumentType docType){
//        Map<String,FlowStep> mapping = createMapStringToFlowStepWithDocType(docType);
//        FlowStep[] steps = new FlowStep[flowSteps.size()];
//
//        for (int i = 0 ; i < flowSteps.size() ; i++) {
//            steps[i] = mapping.get(flowSteps.get(i));
//        }
//
//        return steps;
//    }

    @Override
    public void onStart() {
        super.onStart();

        // Write your code inside this condition
        // Here should start the process that expects the onActivityResult
//        if (firstTime == true) {
//            client = ComplyCubeFactory.create(this).getClient();
//
//            Bundle extras = getIntent().getExtras();
//            String applicantId="";
//            String token="";
//            String documentType="";
//            String locale = "en";
//            ArrayList<String> flowSteps=null;
//            if (extras != null) {
//                applicantId = extras.getString("applicant_id");
//                documentType = extras.getString("document_type");
//                token = extras.getString("token");
//                flowSteps = extras.getStringArrayList("flow_steps");
//                locale = extras.getString("locale");
//            }
//
//            FlowStep[] flow = generateFlowStep(flowSteps, DocumentTypesMap.get(documentType));
//
//            final ComplyCubeConfig config = ComplyCubeConfig.builder(this)
//                    .withToken(token)
//                    .withApplicant(applicantId)
//                    .withCustomFlow(flow)
//                    .withLocale(new Locale(locale))
//                    .build();
//            client.startActivityForResult(this,         /*must be an activity*/
//                    1,            /*this request code will be important for you on onActivityResult() to identity the complycube callback*/
//                    config);
//        }
    }

//    protected JSONObject buildCaptureJsonObject(Captures captures) throws JSONException {
//        JSONObject captureJson = new JSONObject();
//        if (captures.getDocument() == null) {
//            captureJson.put("document", null);
//        }
//
//        JSONObject docJson = new JSONObject();
//
//        DocumentSide frontSide = captures.getDocument().getFront();
//        if (frontSide != null) {
//            JSONObject docSideJson = new JSONObject();
//            docSideJson.put("id", frontSide.getId());
//            docSideJson.put("side", frontSide.getSide());
//            docSideJson.put("type", frontSide.getType());
//
//            docJson.put("front", docSideJson);
//        }
//
//        DocumentSide backSide = captures.getDocument().getBack();
//        if (backSide != null) {
//            JSONObject docSideJson = new JSONObject();
//            docSideJson.put("id", backSide.getId());
//            docSideJson.put("side", backSide.getSide());
//            docSideJson.put("type", backSide.getType());
//
//            docJson.put("back", docSideJson);
//        }
//
//        captureJson.put("document", docJson);
//
//        return captureJson;
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ComplyCubeSdk.Builder builder = new ComplyCubeSdk.Builder(this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        client.handleActivityResult(resultCode, data, new ComplyCube.ComplyCubeResultListener() {
//            @Override
//            public void userCompleted(Captures captures) {
//                Intent intent = new Intent();
//                JSONObject captureJson;
//                try {
//                    captureJson = buildCaptureJsonObject(captures);
//                } catch (JSONException e) {
//                    Log.d(TAG, "userCompleted: failed to build json result");
//                    return;
//                }
//
//                Log.d(TAG, "userCompleted: successfully returned data to plugin");
//                intent.putExtra("data", captureJson.toString());
//                setResult(Activity.RESULT_OK, intent);
//                finish();// Exit of this activity !
//
//            }
//
//            @Override
//            public void userExited(ExitCode exitCode) {
//                Intent intent = new Intent();
//                Log.d(TAG, "userExited: YES");
//                setResult(Activity.RESULT_CANCELED, intent);
//                finish();// Exit of this activity !
//            }
//
//            @Override
//            public void onError(ComplyCubeException e) {
//                Intent intent = new Intent();
//                Log.d(TAG, "onError: YES");
//                e.printStackTrace();
//                setResult(Activity.RESULT_CANCELED, intent);
//                finish();// Exit of this activity !
//            }
//        });
    }
}

