app.controller('DeviceShareCtrl',function($stateParams,$http,$scope,$state) {
	$scope.deviceID = $stateParams.deviceID;
	$scope.mode = $stateParams.mode;
	var length = screen.width < screen.height ? screen.width : screen.height
	$("#QRscan").attr("src","/reception/own/share/"+$scope.deviceID+"/"+$scope.mode+"/"+ length)
	
	$scope.back = function(){
		$state.go('chats',{deviceID : $scope.deviceID})
	}
})