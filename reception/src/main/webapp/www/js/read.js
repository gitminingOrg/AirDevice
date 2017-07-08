app.controller('ReadCtrl', function($http, $scope, $stateParams) {
	$http.get('/reception/status/data/'+$stateParams.token).success(function(response){
		if(response.status == 1){
			  $scope.cleanerStatus = response.contents.cleanerStatus;
			  //室内外空气质量对比
			  $scope.indoor = response.contents.deviceData;
			  $scope.outdoor = response.contents.cityData;
			  if($scope.indoor <= $scope.outdoor){
				  $scope.outPercent = 100
				  $scope.inPercent = 100 * $scope.indoor / $scope.outdoor
			  }else{
				  $scope.inPercent = 100
				  $scope.outPercent = 100 * $scope.outdoor / $scope.indoor
			  }
			  
			  //统计图
			  var airCompare = response.contents.airCompare
			    $('#chartRead').highcharts({
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
		}else{
			alert("该链接已失效")
		}
	});
})