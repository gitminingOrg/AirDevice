app.controller('PrivilegeCtrl',function($stateParams,$http,$scope,$state) {
	$scope.deviceID = $stateParams.deviceID;
	$scope.items = new Array();
	$http.get('/reception/own/device/users/'+$scope.deviceID).success(function(response){
		if(response.status == 1){
			$scope.items = response.contents.wechatUsers
		}
	});
	
	
	$scope.remove = function(item){
		$http({  
	        url    : '/reception/own/device/user/cancel',  
	        method : "post",  
	        params   : { userID : item.customerID , deviceID : $scope.deviceID} 
	    }).success(function(response){
	    	if(response.status == 1){
	    		$scope.items.splice($scope.items.indexOf(item), 1);
	    	}else{
	    		alert('删除失败')
	    	}
	    })
	}
})