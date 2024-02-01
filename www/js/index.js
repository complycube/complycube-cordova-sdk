/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

// Wait for the deviceready event before using any of Cordova's device APIs.
// See https://cordova.apache.org/docs/en/latest/cordova/events/events.html#deviceready
document.addEventListener('deviceready', onDeviceReady, false);

function onDeviceReady() {
    // Cordova is now initialized. Have fun!
    const settings = {
        clientID: '<CLIENT_ID>',
        clientToken: '<TOKEN>',
        stages: [
        {
            name: 'intro',
            heading: 'Am from Cordova',
            message: 'A message for our users',
        },
        {
            name: 'documentCapture',
            documentTypes: {
            passport: true,
            national_identity_card: ['GB', 'FR', 'DZ'],
            },
        },
        'faceCapture',
        ],

    }
    console.log("Plugin functions I have", cordova)
    var complycube = cordova.require("complycube-sdk-cordova.ComplyCube");

    complycube.start(
        settings,
        (results) => console.log("Success"), 
        (cancel_event) => console.log("Cancel event fired"), 
        (error_event) => console.log("Error event fired"), 
        (custom_event) => console.log("Custom event fired"), 
        (token_event) => console.log("Token expiry event fired"));


    console.log('Running cordova-' + cordova.platformId + '@' + cordova.version);
    document.getElementById('deviceready').classList.add('ready');
}
