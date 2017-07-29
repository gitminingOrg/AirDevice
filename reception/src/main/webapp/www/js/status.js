app.controller('StatusCtrl', function($http, $scope, $stateParams, $state, $ionicPopup,$ionicModal, $interval,$timeout) {
  $scope.percent = 90;
  $scope.deviceID = $stateParams.deviceID;
  $scope.role = 3
  $scope.tab = 2
  $scope.configHref = ''
  $scope.shareGuide = false
  $scope.advance = false
  
  $scope.doRefresh = function(){
	  $scope.init();
	  $scope.cityAir();
	  $scope.$broadcast('scroll.refreshComplete');
  }
  
  $scope.init = function(){
	  $http.get('/reception/own/user/role/'+$stateParams.deviceID).success(function(response){
		  if(response.status == 1){
			  $scope.role = response.contents.role
			  if($scope.role == 0){
				  $scope.configHref = '#/device/name/'+$stateParams.deviceID
			  }
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
			  $scope.advance = response.contents.advance
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
  }
  $scope.init();
  
  $scope.cityAir = function(){
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
			        type: 'spline'
			    },
			    title: {
			        align: 'left',
			        text: '近日空气质量对比',
			        style:{
			          fontSize: '13px'
			        }
			    },
			    colors: ['#11c1f3','#f282aa'],
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
  $scope.cityAir()
  $scope.start = function(number){
	  $scope.showStartConfirm(number)
  }
  $scope.showStartConfirm = function(powerInt) {
  	var message = '确认开启设备？'
  	if(powerInt == 0){
  		message = '确认关闭设备？'
  	}
      var confirmPopup = $ionicPopup.confirm({
        title: '设备电源',
        template: message,
        cancelText: '取消', // String (默认: 'Cancel')。一个取消按钮的文字。
        cancelType: 'button-default', // String (默认: 'button-default')。取消按钮的类型。
        okText: '确认', // String (默认: 'OK')。OK按钮的文字。
        okType: 'button-calm', // String (默认: 'button-positive')。OK按钮的类型。
      });
      confirmPopup.then(function(res) {
        if(res) {
            $scope.request = true;
            $http.get("/reception/status/"+$stateParams.deviceID+"/power/"+powerInt).then(
        		function success(response) {
        			$scope.request = false;
        			if(response.data.status != 1){
        				$scope.showAlert();
        			}else{
        				$scope.cleanerStatus.power = powerInt
        			}
        	    }, function error(response) {
        	    	$scope.request = false;
        	   });
        } else {
          	console.log('cancel')
        }
      });
    };
  
  $scope.heatControl = function(number){
	  $scope.showHeatConfirm(number)

  }
  $scope.showHeatConfirm = function(heatInt) {
	  	var message = '确认开启辅热？'
	  	if(heatInt == 0){
	  		message = '确认关闭辅热？'
	  	}
	      var confirmPopup = $ionicPopup.confirm({
	        title: '设备加热',
	        template: message,
	        cancelText: '取消', // String (默认: 'Cancel')。一个取消按钮的文字。
	        cancelType: 'button-default', // String (默认: 'button-default')。取消按钮的类型。
	        okText: '确认', // String (默认: 'OK')。OK按钮的文字。
	        okType: 'button-calm', // String (默认: 'button-positive')。OK按钮的类型。
	      });
	      confirmPopup.then(function(res) {
	        if(res) {
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
	        				$scope.cleanerStatus.heat = heatInt;
	        			}
	        	    }, function error(response) {
	        	    	$scope.request = false;
	        	        // 请求失败执行代码
	        	});
	        } else {
	          	console.log('cancel')
	        }
	      });
	    };
  
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
  
	$.post('/reception/wechat/init',{url : self.location.href}, function(response){
  		if(response.status == 1){
  		    wx.config({
  		      debug: false,
  		      beta: true,
	  		  appId: response.contents.configuration.appId, // 必填，公众号的唯一标识
			  timestamp: response.contents.configuration.timestamp, // 必填，生成签名的时间戳
			  nonceStr: response.contents.configuration.nonceStr, // 必填，生成签名的随机串
			  signature: response.contents.configuration.signature,// 必填，签名，见附录1
  		      jsApiList: ['onMenuShareTimeline','onMenuShareAppMessage','onMenuShareQQ']
  		    });
  		}
  	})

    wx.error(function (res) {
  
    });
  
  $scope.shareStatus = function(){
	  $http({  
	        url    : '/reception/status/data/share',  
	        method : "post",  
	        params   : {deviceID : $scope.deviceID} 
	    }).success(function(response){
	    	if(response.status == 1){
	    		  wx.ready(function () {
	    			    wx.onMenuShareAppMessage({              //配置分享给朋友接口
	    			        title: '果麦新风', // 分享标题
	    			        desc: '分享我的果麦新风机', // 分享描述
	    			        link: 'http://commander.qingair.net/reception/www/index.html#/read/'+response.contents.token, // 分享链接
	    			        imgUrl: 'http://commander.qingair.net/reception/www/img/GMQR.png', // 分享图标
	    			        type: '', // 分享类型,music、video或link，不填默认为link
	    			        dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
	    			        success: function () { 
	    			            // 用户确认分享后执行的回调函数
	    			        },
	    			        cancel: function () { 
	    			            // 用户取消分享后执行的回调函数
	    			        }
	    			    })
	    			 });
	    		  $scope.shareGuide = true
	    		  $timeout(function(){$scope.shareGuide = false},5000)
	    	}else{
	    		$scope.showPopup('数据分享','分享失败，请重试！')
	    	}
	    })
  }
  
  $scope.showPopup = function popup(title, content) {
      var alertPopup = $ionicPopup.alert({
        title: title,
        template: content
      });
    };
})


