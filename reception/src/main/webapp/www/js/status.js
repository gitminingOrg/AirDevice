app.controller('StatusCtrl', function($http, $scope, $stateParams, $state, $ionicPopup,$ionicModal, $interval) {
  $scope.percent = 90;
  $scope.deviceID = $stateParams.deviceID;
  $scope.role = 3
  $scope.tab = 2
  $scope.doRefresh = function(){
	  $scope.init();
	  $scope.$broadcast('scroll.refreshComplete');
  }
  
  $scope.init = function(){
	  $http.get('/reception/own/user/role/'+$stateParams.deviceID).success(function(response){
		  if(response.status == 1){
			  $scope.role = response.contents.role
		  }
	  })
	  
	  $http.get('/reception/own/info/name/'+$stateParams.deviceID).success(function(response){
		  if(response.status != 1){
				$state.go('login')
		  }else{
			  $scope.deviceName = response.contents.deviceName
		  }
	  })
	  
	  $http.get('/reception/status/device/'+$stateParams.deviceID).success(function(response){
		  if(response.status == 2){
				$state.go('login')
			}
		  else if(response.status == 1){
			  $scope.cleanerStatus = response.contents.cleanerStatus;
		  }
		  $http.get('/reception/status/'+$stateParams.deviceID+'/aqi/current').success(function(response){
			  if(response.status == 2){
					$state.go('login')
				}
			  else if(response.status == 1){
				  $scope.indoor = response.contents.deviceData;
				  $scope.outdoor = response.contents.cityData;
				  if($scope.indoor <= $scope.outdoor){
					  $scope.outPercent = 100
					  $scope.inPercent = 100 * $scope.indoor / $scope.outdoor
				  }else{
					  $scope.inPercent = 100
					  $scope.outPercent = 100 * $scope.outdoor / $scope.indoor
				  }
			  }
		  });
		  
		  
	  });
	  
	  $http.get('/reception/status/'+$stateParams.deviceID+'/aqi/compare').success(function(response){
		  if(response.status == 2){
				$state.go('login')
			}
		  else if(response.status == 1){
			  var airCompare = response.contents.airCompare
			    $('#chart2').highcharts({
			        credits: {
			            enabled: false
			        },
			    chart: {
			        type: 'column'
			    },
			    title: {
			        align: 'left',
			        text: '近日空气质量对比',
			        style:{
			          fontSize: '13px'
			        }
			    },
			    colors: ['#11c1f3','#ef473a'],
			    xAxis: {
			        categories: airCompare.dates,
			        crosshair: true
			    },
			    yAxis: {
			        min: 0,
			        title: {
			            text: '空气污染指数'
			        }
			    },
			    legend: {
			        align: 'right', //水平方向位置
			        verticalAlign: 'top', //垂直方向位置
			    },
			    series: [{
			        name: '室内',
			        data: airCompare.insides

			    }, {
			        name: '室外',
			        data: airCompare.outsides

			    }]
			    });
		  }
	  });
  }
  $scope.init();
  
  $scope.start = function(number){
      var powerInt = number
      $scope.request = true;
      $http.get("/reception/status/"+$stateParams.deviceID+"/power/"+powerInt).then(
  		function success(response) {
  			$scope.request = false;
  			if(response.data.status != 1){
  				$scope.showAlert();
  			}else{
  				$scope.cleanerStatus.power = number
  			}
  	    }, function error(response) {
  	    	$scope.request = false;
  	        // 请求失败执行代码
  	});
  }
  
  $scope.heatControl = function(number){
      var heatInt = number
      $scope.request = true;
      $http.get("/reception/status/"+$stateParams.deviceID+"/heat/"+heatInt).then(
  		function success(response) {
  			$scope.request = false;
  			if(response.data.status == 2){
					$state.go('login')
				}
  			else if(response.data.status != 1){
  				$scope.showAlert();
  				//$scope.heat = !$scope.heat;
  			}else{
  				$scope.cleanerStatus.heat = number;
  			}
  	    }, function error(response) {
  	    	$scope.request = false;
  	        // 请求失败执行代码
  	});
  }
  
  $scope.uvControl = function(){
      $scope.cleanerStatus.uv = 1- $scope.cleanerStatus.uv;
      var uvInt = 0
      if($scope.cleanerStatus.uv){
      	uvInt = 1
      }
      $scope.uvRequest = true;
      $http.get("/reception/status/"+$stateParams.deviceID+"/uv/"+uvInt).then(
  		function success(response) {
  			$scope.uvRequest = false;
  			if(response.data.status == 2){
					$state.go('login')
				}
  			else if(response.data.status != 1){
  				$scope.showAlert();
  				//$scope.uv = !$scope.uv;
  			}
  	    }, function error(response) {
  	    	$scope.uvRequest = false;
  	        // 请求失败执行代码
  	});
  }
  $scope.cycleControl = function(){
	    $scope.cleanerStatus.cycle = 1- $scope.cleanerStatus.cycle;
	    cycle = $scope.cleanerStatus.cycle;
	  	$scope.cycleRequest = true;
	  	$http.get("/reception/status/"+$stateParams.deviceID+"/cycle/"+cycle).then(
	      		function success(response) {
	      			$scope.cycleRequest = false;
	      			if(response.data.status == 2){
	  					$state.go('login')
	  				}
	      			else if(response.data.status != 1){
	      				$scope.showAlert();
	      				//$scope.uv = !$scope.uv;
	      			}
	      	    }, function error(response) {
	      	    	$scope.cycleRequest = false;
	      	        // 请求失败执行代码
	      	});
	  }
  
  $scope.lightControl = function(light){
  	$scope.lightRequest = true;
  	$http.get("/reception/status/"+$stateParams.deviceID+"/light/"+light).then(
      		function success(response) {
      			$scope.lightRequest = false;
      			if(response.data.status == 2){
  					$state.go('login')
  				}
      			else if(response.data.status != 1){
      				$scope.showAlert();
      				//$scope.uv = !$scope.uv;
      			}
      	    }, function error(response) {
      	    	$scope.lightRequest = false;
      	        // 请求失败执行代码
      	});
  }

  $scope.velocityControl = function(velocity){
  	$scope.request = true;
  	var value = velocity;
  	$http.get("/reception/status/"+$stateParams.deviceID+"/velocity/"+value).then(
      		function success(response) {
      			$scope.request = false;
      			if(response.data.status == 2){
  					$state.go('login')
  				}
      			else if(response.data.status != 1){
      				$scope.showAlert();
      				//$scope.uv = !$scope.uv;
      			}
      	    }, function error(response) {
      	    	$scope.request = false;
      	        // 请求失败执行代码
      	});
  }
  
  $scope.deviceShare = function(mode){
	  $http.get("/reception/own/share/"+$scope.deviceID+"/"+mode).success(function(response){
		  if(response.status == 1){
			  $state.go('deviceShare', {deviceID: response.contents.deviceID , token: response.contents.token})
		  }else{
			  $scope.showAlert();
		  }
	  })
	  
  }
  
  $scope.setMode = function(mode){
  	$scope.request = true;
      
      $http.get("/reception/status/"+$stateParams.deviceID+"/workmode/"+mode).then(
      		function success(response) {
      			$scope.request = false;
      			if(response.data.status == 2){
  					$state.go('login')
  				}
      			else if(response.data.status != 1){
      				$scope.showAlert();
      				//$scope.uv = !$scope.uv;
      			}else{
      				$scope.cleanerStatus.workMode = mode;
      			}
      	    }, function error(response) {
      	    	$scope.request = false;
      	        // 请求失败执行代码
      	});
  }
  $scope.showAlert = function popup() {
      var alertPopup = $ionicPopup.alert({
        title: '无法远程控制',
        template: '请确保设备正确连网'
      });
    };
    
  $scope.tabChange = function(tab){
	  $scope.tab = tab
  }
  
  $scope.userManage = function(deviceID){
	  $state.go('privilege',{deviceID : deviceID})
  }
  var timer = $interval($scope.init, 20 * 1000)
  $scope.$on('$ionicView.beforeLeave',function(){
	  $interval.cancel(timer);
  })
  
})