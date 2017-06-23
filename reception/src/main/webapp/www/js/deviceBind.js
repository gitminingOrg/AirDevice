app.controller( 'DeviceBindCtrl', function($scope, $http, $state, $stateParams) {
	$scope.deviceID = $stateParams.serial;
	$scope.serial = $stateParams.serial;
	//get open id
	var data = { city : response.contents.location.cityPinyin}
	
	$http({  
        url    : '/reception/wechat/user',  
        method : "get",  
        params   : { serial : $stateParams.serial},  
    }).success(function(response){
		if(response.status == 0){
			alert("诶哟卧槽。。。我这没取到儿openID嗬")
		}else{
			$scope.openID = response.contents.openID
		}
	});
	//bind user & device
	var data = {serail : $scope.serial, open_id : $scope.openID}
	$http({  
        url    : '',  
        method : "post",  
        data   : $.param(data),  
        headers: {  
            'Content-Type': 'application/x-www-form-urlencoded'  
        },  
    }).success(function(data) { 
    	if(data.status == 1){
    		$state.go('home.device');
    	}
    })
})