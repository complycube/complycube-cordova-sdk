var exec = require('cordova/exec');

var PLUGIN_NAME = 'ComplyCube';

var ComplyCube = {
    mount: function (cb, options) {
        exec(cb, null, PLUGIN_NAME, 'mount', [options]);
    }
};

module.exports = ComplyCube;