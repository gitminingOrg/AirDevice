app.controller('DeviceNameCtrl',function($stateParams,$http,$scope,$ionicPopup,$state) {
	$scope.deviceID = $stateParams.deviceID;
	$scope.init = function(){
		$scope.provinces = new Array()
		$scope.cities = new Array()
		$http.get('/reception/location/province').success(function(response){
			if(response.status == 1){
				$scope.provinces = response.contents.provinces;
			}
		})
		$http.get('/reception/own/info/name/'+$scope.deviceID).success(function(response){
			if(response.status == 1){
				$scope.deviceName = response.contents.deviceName
				$scope.provinces.push(response.contents.province)
				$scope.selectedProvince = response.contents.province
				$scope.cities.push(response.contents.city)
				$scope.selectedCity = response.contents.city;
			}
		});
	}
	$scope.init()
	$scope.configName = function(deviceName, province, city){
		deviceName.cityID = city.locationId
		deviceName.provinceID = province.locationId
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
		var cityPinyin = city.locationPinyin
		if(province.locationPinyin == 'beijing' || province.locationPinyin == 'tianjin' || province.locationPinyin == 'shanghai' || province.locationPinyin == 'chongqin'){
			cityPinyin = province.locationPinyin
		}
		$http.post('/reception/status/city/config/'+deviceName.deviceID+'/'+ cityPinyin).success(function(response){
			if(response.status != 1){
				$scope.showFailure();
			}
		})
	};
	$scope.showSuccess = function() {
        var alertPopup = $ionicPopup.alert({
          title: '',
          template: '修改成功'
        });
        alertPopup.then(function(res) {
        	$scope.init()
        	$state.go('chats',{deviceID : $scope.deviceID});
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
      
    $scope.cityRequest = function(province){
    	$http({  
	        url    : '/reception/location/city',  
	        method : "get",  
	        params   : province 
	    }).success(function(response){
	    	if(response.status == 1){
	    		$scope.cities = response.contents.cities;
	    		$scope.selectedCity = $scope.cities[0]
	    	}else{
	    		$scope.showFailure();
	    	}
	    });
    }
})