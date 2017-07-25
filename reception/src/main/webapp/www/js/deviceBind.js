app.controller( 'DeviceBindCtrl', function($scope, $http, $state, $stateParams, $interval, $ionicLoading, $ionicPopup) {
	$scope.deviceName =new Object();
	$scope.deviceName.serial = $stateParams.serial;
	$scope.visible = 0;
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
				$http({  
			        url    : '/reception/own/wx/bind/step',  
			        method : "get",  
			        params   : { serial : $stateParams.serial , openId : $scope.openId} 
			    }).success(function(response){
			    	if(response.status == 0){
			    		$scope.showAlert("初始化失败",response.info)
			    	}else if(response.status == 1){
			    		if(response.contents.step == 3){
			    			$scope.showAlert("成功",response.info)
			    			window.location.href= "index.html#/home/device"
			    		}else if(response.contents.step == 2){
			    			window.location.href= "wxinitial.html?serial="+$stateParams.serial
			    		}else if(response.contents.step == 1){
			    			$http.get('/reception/location/province').success(function(response){
								if(response.status == 1){
									$scope.provinces = response.contents.provinces;
								}
							})
			    			$scope.visible = 1;
			    		}
			    	}
			    });
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
	    	if(data.status == 1){
	    		var cityPinyin = city.locationPinyin
	    		if(province.locationPinyin == 'beijing' || province.locationPinyin == 'tianjin' || province.locationPinyin == 'shanghai' || province.locationPinyin == 'chongqin'){
	    			cityPinyin = province.locationPinyin
	    		}
	    		$http.post('/reception/status/city/config/'+$stateParams.serial+'/'+ cityPinyin).success(function(response){
	    			if(response.status != 1){
	    				$scope.showAlert('城市绑定失败','城市绑定失败')
	    				$http.get('/reception/own/user/location/'+cityPinyin).success(function(response){
	    					
	    				})
	    				$http.get('/reception/init/enrich/'+$stateParams.serial).success(function(response){
	    					
	    				})
	    			}
	    		})
	    		// Setup the loader
	    		$ionicLoading.show({
	    		    content: 'Loading',
	    		    animation: 'fade-in',
	    		    showBackdrop: true,
	    		    maxWidth: 200,
	    		    showDelay: 0
	    		});
	    		$interval(toDo, 3000, 10);
	    	}else if(data.status == 2){
	    		$scope.showAlert('绑定失败','该二维码已被注册')
	    	}
	    });

		
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
	
	$scope.showAlert = function popup(title, content) {
	      var alertPopup = $ionicPopup.alert({
	        title: title,
	        template: content
	      });
	    };

})

function GetQueryString(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}