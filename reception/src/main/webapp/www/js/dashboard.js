app.controller('DashCtrl', function($scope, $ionicPopup,$ionicModal, Chats, $http, $timeout, $stateParams) {
	$scope.deviceID = $stateParams.deviceID;
	$scope.doRefresh = function(){
		$scope.init();
		$scope.$broadcast('scroll.refreshComplete');
		}
    $scope.gaugeChart = {
      credits: {
            enabled: false
        },
        chart: {
            type: 'solidgauge'
        },
        title: null,
        pane: {
            center: ['50%', '65%'],
            size: '130%',
            startAngle: -90,
            endAngle: 90,
            background: {
                backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || '#EEE',
                innerRadius: '60%',
                outerRadius: '100%',
                shape: 'arc'
            }
        },
        tooltip: {
            enabled: false
        },
        // the value axis
        yAxis: {
            stops: [
                [0.125, '#55BF3B'], // green
                [0.25, '#DDDF0D'], // yellow
                [0.375, '#DF5353'], // red
                [0.5, '#533B79'], // green
                [0.75, '#111111'], // yellow
            ],
            lineWidth: 0,
            minorTickInterval: 50,
            tickPixelInterval: 100,
            tickWidth: 0,
            min: 0,
            max: 400,
            title: {
                text: '空气质量指数',
                y: 100
            },
            labels:{
                y: 10
            }
        },
        plotOptions: {
            solidgauge: {
                dataLabels: {
                    y: -50,
                    borderWidth: 0,
                    useHTML: true
                }
            }
        },
        series: [{
            name: '空气污染指数',
            data: [50],
            dataLabels: {
                format: '<div style="text-align:center"><span style="font-size:25px;color:' +
                ((Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black') + '">{y}</span><br/>' +
                '<span style="font-size:12px;color:silver">AQI</span></div>'
            },
            tooltip: {
                valueSuffix: ' AQI'
            }
        }] 
    }
    
    $scope.init = function dashInit(){
    	$http.get("/reception/status/device/"+$stateParams.deviceID).then(
    			function success(response) {
    				if(response.data.status == 1){
    					$scope.cleanerStatus = response.data.contents.cleanerStatus;
    					//电源打开
    					if(response.data.contents.cleanerStatus.power == 0){
    						$scope.power = false
    						$('#dash_power').attr("checked", false)
    					}else{
    						$scope.power = true
    						$('#dash_power').attr("checked", true)
    					}
    					//辅热打开
    					if(response.data.contents.cleanerStatus.heat == 0){
    						$scope.heat = false
    						$('#dash_heat').attr("checked", false)
    					}else{
    						$scope.heat = true
    						$('#dash_heat').attr("checked", true)
    					}
    					//杀菌打开
    					if(response.data.contents.cleanerStatus.uv == 0){
    						$scope.uv = false
    						$('#dash_uv').attr("checked", false)
    					}else{
    						$scope.uv = true
    						$('#dash_uv').attr("checked", true)
    					}
    					$scope.mode = response.data.contents.cleanerStatus.workMode
    				}
    		    }, function error(response) {
    		        // 请求失败执行代码
    		});  
    	
    	$http.get("/reception/location/"+$stateParams.deviceID).success(function(response){
        	if(response.status == 1){
        		
        		var data = { city : response.contents.location.cityPinyin}
        		$http({  
    		        url    : '/reception/status/city/aqi',  
    		        method : "post",  
    		        params   : data,  
    		    }).success(function(response){
    		    	if(response.status == 1){
    		    		$scope.airQuality = response.contents.cityAqi.aqiCategory
    		    		$scope.aqiData = response.contents.cityAqi.cityAqi
    		    		$scope.location = response.contents.cityAqi.cityName
    		            var point = Highcharts.charts[0].series[0].points[0];
    		            point.update(parseInt($scope.aqiData));
    		    	}
    		    });
        		
        		$http.post('/reception/status/city/config/'+$scope.deviceID+'/'+response.contents.location.cityPinyin).success(function(data){
        			
        		});
        	}
        });
    	
    }
    
    $scope.showAlert = function popup() {
        var alertPopup = $ionicPopup.alert({
          title: '无法远程控制',
          template: '请确保设备正确连网'
        });
      };
      
    $scope.start = function(){
        $scope.power = !$scope.power;
        var powerInt = 0
        if($scope.power){
        	powerInt = 1
        }
        $scope.powerRequest = true;
        $http.get("/reception/status/"+$stateParams.deviceID+"/power/"+powerInt).then(
    		function success(response) {
    			$scope.powerRequest = false;
    			if(response.data.status != 1){
    				$scope.showAlert();
    				//$scope.power = !$scope.power;
    			}
    	    }, function error(response) {
    	    	$scope.powerRequest = false;
    	        // 请求失败执行代码
    	});
    }
    
    $scope.heatControl = function(){
        $scope.heat = !$scope.heat;
        var heatInt = 0
        if($scope.heat){
        	heatInt = 1
        }
        $scope.heatRequest = true;
        $http.get("/reception/status/"+$stateParams.deviceID+"/heat/"+heatInt).then(
    		function success(response) {
    			$scope.heatRequest = false;
    			if(response.data.status != 1){
    				$scope.showAlert();
    				//$scope.heat = !$scope.heat;
    			}
    	    }, function error(response) {
    	    	$scope.heatRequest = false;
    	        // 请求失败执行代码
    	});
    }
    
    $scope.uvControl = function(){
        $scope.uv = !$scope.uv;
        var uvInt = 0
        if($scope.uv){
        	uvInt = 1
        }
        $scope.uvRequest = true;
        $http.get("/reception/status/"+$stateParams.deviceID+"/uv/"+uvInt).then(
    		function success(response) {
    			$scope.uvRequest = false;
    			if(response.data.status != 1){
    				$scope.showAlert();
    				//$scope.uv = !$scope.uv;
    			}
    	    }, function error(response) {
    	    	$scope.uvRequest = false;
    	        // 请求失败执行代码
    	});
    }
    
    $scope.lightControl = function(light){
    	$scope.lightRequest = true;
    	$http.get("/reception/status/"+$stateParams.deviceID+"/light/"+light).then(
        		function success(response) {
        			$scope.lightRequest = false;
        			if(response.data.status != 1){
        				$scope.showAlert();
        				//$scope.uv = !$scope.uv;
        			}
        	    }, function error(response) {
        	    	$scope.lightRequest = false;
        	        // 请求失败执行代码
        	});
    }
    $scope.cycleControl = function(cycle){
    	$scope.cycleRequest = true;
    	$http.get("/reception/status/"+$stateParams.deviceID+"/cycle/"+cycle).then(
        		function success(response) {
        			$scope.cycleRequest = false;
        			if(response.data.status != 1){
        				$scope.showAlert();
        				//$scope.uv = !$scope.uv;
        			}
        	    }, function error(response) {
        	    	$scope.cycleRequest = false;
        	        // 请求失败执行代码
        	});
    }
    $scope.velocityControl = function(velocity){
    	$scope.velocityRequest = true;
    	var value = velocity * 100;
    	$http.get("/reception/status/"+$stateParams.deviceID+"/velocity/"+value).then(
        		function success(response) {
        			$scope.velocityRequest = false;
        			if(response.data.status != 1){
        				$scope.showAlert();
        				//$scope.uv = !$scope.uv;
        			}
        	    }, function error(response) {
        	    	$scope.velocityRequest = false;
        	        // 请求失败执行代码
        	});
    }
    $scope.setMode = function(mode){
    	$scope.velocityRequest = true;
        $scope.mode = mode;
        $http.get("/reception/status/"+$stateParams.deviceID+"/workmode/"+mode).then(
        		function success(response) {
        			$scope.velocityRequest = false;
        			if(response.data.status != 1){
        				$scope.showAlert();
        				//$scope.uv = !$scope.uv;
        			}
        	    }, function error(response) {
        	    	$scope.velocityRequest = false;
        	        // 请求失败执行代码
        	});
    }

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
    	  	$http.post('/reception/status/city/config/'+$scope.deviceID+'/'+city).success(function(data){
    	  		if(data.status != 1){
    	  			console.log('set device city failed');
    	  			return;
    	  		}
    	  	    $http.get("/reception/status/city/info/"+$stateParams.deviceID).success(function(response){
    	  	    	if(response.status == 1){
    	  	    		
    	  	    		var data = { city : response.contents.deviceCity.city}
    	  	    		$http({  
    	  			        url    : '/reception/status/city/aqi',  
    	  			        method : "post",  
    	  			        params   : data,  
    	  			    }).success(function(response){
    	  			    	if(response.status == 1){
    	  			    		$scope.airQuality = response.contents.cityAqi.aqiCategory
    	  			    		$scope.aqiData = response.contents.cityAqi.cityAqi
    	  			    		$scope.location = response.contents.cityAqi.cityName
    	  			    		var point = Highcharts.charts[0].series[0].points[0];
    	  			            point.update(parseInt($scope.aqiData));
    	  			    	}
    	  			    });
    	  	    	}
    	  	    });
    	  	});
            $scope.modal.hide();
      }

    })
