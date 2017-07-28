app.controller( 'DeviceCtrl', function($rootScope, $scope, $cordovaBarcodeScanner, $ionicModal, $http, $state, $ionicPopup, $interval) {
	$scope.airWaiting = true
	$scope.deviceWaiting = true
	var code = GetQueryString("code")
	var months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
	
	$scope.init = function(){
		$scope.airWaiting = true
		$scope.deviceWaiting = true
		$http({  
	        url    : '/reception/own/device',  
	        method : "get",  
	        params   : {code : code} 
	    }).then(
			function success(response) {
				if(response.data.status == 1){
					if(typeof(response.data.contents.redirect_url) != undefined && response.data.contents.redirect_url != null){
						location.href = response.data.contents.redirect_url
					}else{
						$scope.deviceList = response.data.contents.statusList;
						$scope.deviceWaiting = false
						var date = new Date();
						$scope.today = months[date.getMonth()] +" " + date.getDate()
					}
				}else {
					$state.go('login');
				}
		    }, function error(response) {
		    	$state.go('login');
		    });
		
//    	$http.get("/reception/location/phone").success(function(response){
//    		if(response.status == 1){	
//				$scope.gps = response.contents.location
//				$scope.updateCityAir($scope.gps.cityPinyin);
//        	}else{
//        		$scope.updateCityAir('beijing');
//        	}
//        });
    	
		
    	$http.get("/reception/own/user/city").success(function(response){
    		if(response.status == 1){	
				$scope.updateCityAir(response.contents.city);
        	}else{
        		$scope.updateCityAir('beijing');
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
	    		$scope.airWaiting = false
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
        		  $scope.showAlert("设备绑定","绑定成功！请下拉刷新")
        	  }else{
        		  $scope.showAlert("设备绑定","未搜索到相同局域网下的设备")
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
      $scope.choose = function(city, provinceName){
    	    var cityPinyin = city
    	  	if(provinceName == '北京'){
    	  		cityPinyin = 'beijing'
    	  	}else if(provinceName == '上海'){
    	  		cityPinyin = 'shanghai'
    	  	}else if(provinceName == '天津'){
    	  		cityPinyin = 'tianjin'
    	  	}else if(provinceName == '重庆'){
    	  		cityPinyin = 'chongqing'
    	  	}
			var data = { city : cityPinyin}
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
		    		$http.get('/reception/own/user/location/'+cityPinyin).success(function(response){
						
					})
					$scope.modal.hide();
		    	}else{
		    		$scope.showAlert('城市选择','该城市无相关空气质量数据')
		    	}
		    });
			
			
		  
	        
      }
      var timer = $interval($scope.init, 20 * 1000)
      $scope.$on('$ionicView.beforeLeave',function(){
    	  $interval.cancel(timer);
      })
      
      $scope.showAlert = function popup(title, content) {
	      var alertPopup = $ionicPopup.alert({
	        title: title,
	        template: content
	      });
	    };
	    
	   $scope.goDetail = function(deviceID){
		   $state.go('chats',{deviceID : deviceID});
	   }
})