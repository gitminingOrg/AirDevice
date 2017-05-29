var app = angular.module('starter.controllers', ['ngCordova','ionic'])
.controller('TabCtrl',function($stateParams,$scope) {
	$scope.deviceID = $stateParams.deviceID;
});


function str2ab(str) {
    var buf = new ArrayBuffer(str.length * 2); // 2 bytes for each char
    var bufView = new Uint16Array(buf);
    for (var i = 0, strLen = str.length; i < strLen; i++) {
        bufView[i] = str.charCodeAt(i);
    }
    return buf;
}

function byteToString(buf) {  
	return String.fromCharCode.apply(null, new Uint8Array(buf));
}  
