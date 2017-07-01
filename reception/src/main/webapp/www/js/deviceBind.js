app.controller( 'DeviceBindCtrl', function($scope, $http, $state, $stateParams, $interval, $ionicLoading) {
	$scope.deviceName =new Object();
	$scope.deviceName.serial = $stateParams.serial;
	var code = GetQueryString("code")
	var url =document.location.href.toString()
	//get open id	
	$http({  
        url    : '/reception/wechat/user',  
        method : "get",  
        params   : { serial : $stateParams.serial , code : code} 
    }).success(function(response){
		if(response.status == 0){
			alert("诶哟卧槽。。。我这没取到儿openID嗬")
		}else{
			 if(typeof(response.contents.redirect_url) !=undefined && response.contents.openId == null){
				location.href = response.contents.redirect_url
			}else if(typeof(response.contents.openId) !=undefined && response.contents.openId != null){
				$scope.openId = response.contents.openId
				$http.get('/reception/location/province').success(function(response){
					if(response.status == 1){
						$scope.provinces = response.contents.provinces;
						$scope.selectedProvince = $scope.provinces[0]
					}
				})
			}else{
				alert("sth wrong")
			}
			
		}
	});
	
	//绑定设备
	$scope.bindDevice = function(deviceName, province, city){
		//bind user & device
		deviceName.openId = $scope.openId
		deviceName.cityID = city.locationId
		deviceName.provinceID = province.locationId
		var toDo = function(){
			var serial = deviceName.serial
    		$http({
    			url : '/reception/own/register/available',
    			method : "get",
    			params : {serial : serial}
    		}).success(function(availResponse){
    			if(availResponse.status == 1){
    				window.location.href= "wxinitial.html?serial="+serial
    				$ionicLoading.hide();
    			}
    		})
		}
		
		$http({  
	        url    : '/reception/own/register',  
	        method : "post",  
	        params   : deviceName
	    }).success(function(data) { 
	    	alert(data.info)
	    	if(data.status == 1){
	    		  // Setup the loader
	    		  $ionicLoading.show({
	    		    content: 'Loading',
	    		    animation: 'fade-in',
	    		    showBackdrop: true,
	    		    maxWidth: 200,
	    		    showDelay: 0
	    		  });
	    		$interval(toDo, 3000, 10);
	    	}
	    });
		var cityPinyin = city.locationPinyin
		if(province.locationPinyin == 'beijing' || province.locationPinyin == 'tianjin' || province.locationPinyin == 'shanghai' || province.locationPinyin == 'chongqin'){
			cityPinyin = province.locationPinyin
		}
		$http.post('/reception/status/city/config/'+$stateParams.serial+'/'+ cityPinyin).success(function(response){
			if(response.status != 1){
				alert('城市绑定失败')
			}
		})
		
	}
	
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
	    		
	    	}
	    });
    }

})

function GetQueryString(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}