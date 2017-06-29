app.controller( 'DeviceCtrl', function($scope, $cordovaBarcodeScanner, $ionicModal, $http, $state, $ionicPopup) {

	
	$scope.init = function(){
		$http.get("/reception/own/device").then(
			function success(response) {
				if(response.data.status == 1){
					$scope.deviceList = response.data.contents.statusList;
				}else {
					$state.go('login');
				}
		    }, function error(response) {
		    	$state.go('login');
		    });
		
    	$http.get("/reception/location/phone").success(function(response){
    		if(response.status == 2){
				$state.go('login')
			}else if(response.status == 1){	
				$scope.gps = response.contents.location
				$scope.updateCityAir($scope.gps.cityPinyin);
        	}else{
        		$scope.updateCityAir('bejing');
        	}
        });
	}
    $scope.init()
    
	$scope.doRefresh = function(){
		$scope.init();
		$scope.$broadcast('scroll.refreshComplete');
		}
    
    $scope.updateCityAir = function updateAir(city){
    	var data = { city : city }
    	$http({  
	        url    : '/reception/status/city/aqi',  
	        method : "post",  
	        params   : data,  
	    }).success(function(response){
	    	if(response.status == 2){
				$state.go('login')
			}
	    	else if(response.status == 1){
	    		$scope.airQuality = response.contents.cityAqi.aqiCategory
	    		$scope.pm25 = response.contents.cityAqi.pm25
	    		$scope.aqiData = response.contents.cityAqi.cityAqi
	    		$scope.location = response.contents.cityAqi.cityName
	    	}
	    });
    }
    //  confirm 对话框
    $scope.showConfirm = function(deviceID) {
      var confirmPopup = $ionicPopup.confirm({
        title: '设备绑定',
        template: '请确保手机与设备处于同一无线网环境下'
      });
      confirmPopup.then(function(res) {
        if(res) {
        	$http({  
                url    : '/reception/own/register/complete',  
                method : "get",  
                params   : { serial : deviceID} 
            }).success(function(response){
        	  if(response.status == 1){
        		  alert("绑定成功！请下拉刷新")
        	  }else{
        		  alert("未搜索到相同局域网下的设备")
        	  }
          })
          console.log('You are sure');
        } else {
          console.log('You are not sure');
        }
      });
    };
    
    $ionicModal.fromTemplateUrl('templates/city-choose.html', {
        scope: $scope
      }).then(function(modal) {
        $scope.modal = modal;
      });
      $http.get('/reception/own/all/cities').success(function(response){
    	  if(response.status == 1){
    		  $scope.cities = response.contents.cityList;
    	  }
      });
      $scope.choose = function(city){
			var data = { city : city}
			$http({  
		        url    : '/reception/status/city/aqi',  
		        method : "post",  
		        params   : data,  
		    }).success(function(response){
		    	if(response.status == 1){
		    		$scope.airQuality = response.contents.cityAqi.aqiCategory
		    		$scope.pm25 = response.contents.cityAqi.pm25
		    		$scope.aqiData = response.contents.cityAqi.cityAqi
		    		$scope.location = response.contents.cityAqi.cityName
		    	}
		    });
		  
	        $scope.modal.hide();
      }
})