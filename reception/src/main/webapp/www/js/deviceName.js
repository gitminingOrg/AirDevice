app.controller('DeviceNameCtrl',function($stateParams,$http,$scope,$ionicPopup,$state) {
	$scope.deviceID = $stateParams.deviceID;
	$scope.init = function(){
		$http.get('/reception/own/info/name/'+$scope.deviceID).success(function(response){
			if(response.status == 1){
				$scope.deviceName = response.contents.deviceName;
			}
		});
	}
	$scope.init()
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
		    		$scope.showSuccess();
		    	}else{
		    		$scope.showFailure();
		    	}
		    });
	};
	$scope.showSuccess = function() {
        var alertPopup = $ionicPopup.alert({
          title: '',
          template: '修改成功'
        });
        alertPopup.then(function(res) {
        	$scope.init()
        	$state.go('tab.account',{deviceID : $scope.deviceID});
        });
      };
      
  	$scope.showFailure = function() {
        var alertPopup = $ionicPopup.alert({
          title: '',
          template: '修改失败'
        });
        alertPopup.then(function(res) {
        	$scope.init()
        	console.log('config name failed');
        });
      };
})