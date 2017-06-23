app.controller( 'DeviceBindCtrl', function($scope, $http, $state, $stateParams) {
	$scope.serial = $stateParams.serial;
	//get open id
	$http.get('').success(function(){});
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