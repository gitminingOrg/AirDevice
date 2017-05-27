app.controller( 'DeviceCtrl', function($scope, $cordovaBarcodeScanner, $ionicModal, $http) {
	$http.get("/reception/own/device").then(
		function success(response) {
			if(response.data.status == 1){
				$scope.deviceList = response.data.contents.statusList;
			}
	    }, function error(response) {
	        // 请求失败执行代码
	    });
    $scope.scanBarcode = function() {
        $cordovaBarcodeScanner.scan().then(function(imageData) {
            alert(imageData.text);
            console.log("Barcode Format -> " + imageData.format);
            console.log("Cancelled -> " + imageData.cancelled);
        }, function(error) {
            console.log("An error happened -> " + error);
        });
    };

    $scope.broadcastWifi = function(ssid,password) {
        // WIFI_INFO
        var WIFI_INFO = ssid + "," + password;
        alert(WIFI_INFO);
        // translate the string into ArrayBuffer
        var UPNPSTRING = str2ab(WIFI_INFO);
        PORT = 1900;
        // convert string to ArrayBuffer - taken from Chrome Developer page
        chrome.sockets.udp.onReceive.addListener(
            (info) => {
                alert(JSON.stringify(info) + 'info'); 
            }
        );

        chrome.sockets.udp.onReceiveError.addListener(
            (error) => {
                alert(byteToString(error.data) + 'error'); 
                chrome.sockets.udp.close(error.socketId);
            }
        );

        // send  the UDP search as captures in UPNPSTRING and to port PORT, taken from plugin help text
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


    };

    $ionicModal.fromTemplateUrl('templates/wifi-link.html', {
        scope: $scope
    }).then(function(modal) {
        $scope.modal = modal;
    });
})