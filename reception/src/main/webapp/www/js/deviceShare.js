app.controller('DeviceShareCtrl',function($stateParams,$http,$scope) {
	$scope.deviceID = $stateParams.deviceID;
	$scope.mode = 0;
	$scope.controlQR = function(){
		$("#QRscan").attr("src","/reception/own/share/"+$scope.deviceID+"/0")
		$scope.mode = 1
	}
	$scope.readonlyQR = function(){
		$("#QRscan").attr("src","/reception/own/share/"+$scope.deviceID+"/1")
		$scope.mode = 2
	}	
})