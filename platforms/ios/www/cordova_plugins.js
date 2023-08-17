cordova.define('cordova/plugin_list', function(require, exports, module) {
  module.exports = [
    {
      "id": "complycube-sdk-cordova.ComplyCube",
      "file": "plugins/complycube-sdk-cordova/www/complycube.js",
      "pluginId": "complycube-sdk-cordova",
      "clobbers": [
        "complycube",
        "cordova.plugins.complycube",
        "plugin.complycube"
      ]
    }
  ];
  module.exports.metadata = {
    "complycube-sdk-cordova": "1.1.5"
  };
});