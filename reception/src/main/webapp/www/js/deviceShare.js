app.controller('DeviceShareCtrl',function($stateParams,$http,$scope) {
	$scope.deviceID = $stateParams.deviceID;
	$("#QRscan").attr("src","/reception/own/share/15CD11111122000022000001/1")
})