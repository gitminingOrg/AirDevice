app.controller('DeviceShareCtrl',function($stateParams,$http,$scope,$state) {
	$scope.deviceID = $stateParams.deviceID;
	$scope.token = $stateParams.token;
	var length = screen.width < screen.height ? screen.width : screen.height
	$("#QRscan").attr("src","/reception/own/share/token/"+$scope.token+"/"+$scope.deviceID+"/"+length)
	
	$scope.back = function(){
		$state.go('chats',{deviceID : $scope.deviceID})
	}
})