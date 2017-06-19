app.controller( 'DeviceCtrl', function($scope, $cordovaBarcodeScanner, $ionicModal, $http, $state) {
	$http.get("/reception/own/device").then(
		function success(response) {
			if(response.data.status == 1){
				$scope.deviceList = response.data.contents.statusList;
			}else {
				$state.go('login');
			}
	    }, function error(response) {
	    	$state.go('login');
	    });
    $scope.scanBarcode = function() {
        $cordovaBarcodeScanner.scan().then(function(imageData) {
            var data = imageData.text
            var reg=new RegExp("^http");
            if(reg.test(data)){
            	$http.get(data).success(function(response){
            		
            	})
            }
        }, function(error) {
            console.log("An error happened -> " + error);
        });
    };

    $scope.broadcastWifi = function(ssid,password) {
        // WIFI_INFO
        var WIFI_INFO = ssid + "," + password;
        // translate the string into ArrayBuffer
        var UPNPSTRING = str2ab(WIFI_INFO);
        PORT = 1900;

        // send the UDP search as captures in UPNPSTRING and to port PORT, taken
		// from plugin help text
        chrome.sockets.udp.create((createInfo) => {
            chrome.sockets.udp.bind(createInfo.socketId, '0.0.0.0', PORT, (bindresult) => {
                chrome.sockets.udp.setMulticastTimeToLive(createInfo.socketId, 2, (ttlresult) => {
                    chrome.sockets.udp.setBroadcast(createInfo.socketId, true, function(sbresult) {
                        chrome.sockets.udp.send(createInfo.socketId, UPNPSTRING, '255.255.255.255', PORT, (sendresult) => {
                            if (sendresult < 0) {
                                console.log('send fail: ' + sendresult);
                            } else {
                                console.log('sendTo: success ' + PORT, createInfo, bindresult, ttlresult, sbresult, sendresult);
                            }
                        });
                    });
                });
            });
        });
        // convert string to ArrayBuffer - taken from Chrome Developer page
// chrome.sockets.udp.onReceive.addListener(
// (info) => {
// var data = byteToString(info.data)
// if(data == 'OK' || data == 'ok' || data == 'Ok'){
// alert('设备接收成功')
// chrome.sockets.udp.close(info.socketId);
// }else{
// alert('设备未接收成功')
// }
// }
// );
//
// chrome.sockets.udp.onReceiveError.addListener(
// (error) => {
// alert(byteToString(error.data) + 'error');
// chrome.sockets.udp.close(error.socketId);
// }
// );

    };

    
    $ionicModal.fromTemplateUrl('templates/wifi-link.html', {
        scope: $scope
    }).then(function(modal) {
        $scope.modal = modal;
    });
})