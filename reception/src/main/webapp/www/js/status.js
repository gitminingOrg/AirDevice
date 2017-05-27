app.controller('StatusCtrl', function($http, $scope, $stateParams, Chats) {
  $scope.deviceID = $stateParams.deviceID;
  $http.get('/reception/status/device/'+$stateParams.deviceID).success(function(response){
	  if(response.status == 1){
		  $scope.cleanerStatus = response.contents.cleanerStatus;
		  
	  }
  });
  $scope.test = function(){
    $scope.currentCompare = null;
  }
  $scope.currentCompare = {
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
            data: [20]
        }, {
            name: '室外',
            data: [133]
        }]
  }

  $scope.historyCompare = {

    }
  $http.get('/reception/status/'+$stateParams.deviceID+'/aqi/compare').success(function(response){
	  if(response.status == 1){
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
})