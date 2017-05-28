app.controller('DeviceNameCtrl',function($stateParams,$http,$scope) {
	$scope.deviceID = $stateParams.deviceID;
	$http.get('/reception/own/info/name/'+$scope.deviceID).success(function(response){
		if(response.status == 1){
			$scope.deviceName = response.contents.deviceName;
		}
	});
	$scope.configName = function(deviceName){
		var data = { deviceName : deviceName }
		$http({  
		        url    : '/reception/own/config/name/',  
		        method : "post",  
		        data   : $.param(deviceName),  
		        headers: {  
		            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'  
		        },  
		    }).success(function(response){
		    	if(response.status == 1){
		    		alert('succeed!')
		    	}else{
		    		alert('failed')
		    	}
		    });
	};
})