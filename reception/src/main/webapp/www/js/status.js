app.controller('StatusCtrl', function($http, $scope, $stateParams, $state, Chats) {
  $scope.deviceID = $stateParams.deviceID;
  $scope.doRefresh = function(){
	  $scope.init();
	  $scope.$broadcast('scroll.refreshComplete');
	  }
  
  $scope.init = function(){
	  $http.get('/reception/status/device/'+$stateParams.deviceID).success(function(response){
		  if(response.status == 1){
			  $scope.cleanerStatus = response.contents.cleanerStatus;
		  }
		  $http.get('/reception/status/'+$stateParams.deviceID+'/aqi/current').success(function(response){
			  if(response.status == 2){
					$state.go('login')
				}
			  else if(response.status == 1){
				  $('#chart1').highcharts({
					  credits: {
				            enabled: false
				        },
				        chart: {
				            type: 'bar'
				        },
				        title: {
				            align: 'left',
				            text: '空气质量对比',
				            style:{
				              fontSize: '13px'
				            }
				            
				        },
				        colors: ['#0AB2F3','#FFAA00'],
				        xAxis: {
				            categories: ['对比'],
				            title: {
				                text: null
				            }
				        },
				        yAxis: {
				            min: 0,
				            title: {
				                text: '空气质量指数',
				                align: 'high'
				            },
				        },
				        legend: {
				            align: 'right', //水平方向位置
				            verticalAlign: 'top', //垂直方向位置
				        },
				        series: [{
				            name: '室内',
				            data: [response.contents.deviceData]
				        }, {
				            name: '室外',
				            data: [response.contents.cityData]
				        }]
				    });
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
			    colors: ['#0AB2F3','#FFAA00'],
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
})