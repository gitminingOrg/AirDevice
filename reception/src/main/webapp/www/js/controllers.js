angular.module('starter.controllers', ['ngCordova'])
.controller('DeviceCtrl', function($scope, $cordovaBarcodeScanner, $ionicModal) {

    $scope.scanBarcode = function() {
        $cordovaBarcodeScanner.scan().then(function(imageData) {
            alert(imageData.text);
            console.log("Barcode Format -> " + imageData.format);
            console.log("Cancelled -> " + imageData.cancelled);
        }, function(error) {
            console.log("An error happened -> " + error);
        });
    };

    $scope.broadcastWifi = function(ssid,password) {
        // WIFI_INFO
        var WIFI_INFO = ssid + "," + password;
        alert(WIFI_INFO);
        // translate the string into ArrayBuffer
        var UPNPSTRING = str2ab(WIFI_INFO);
        PORT = 1900;
        // convert string to ArrayBuffer - taken from Chrome Developer page
        chrome.sockets.udp.onReceive.addListener(
            (info) => {
                alert(JSON.stringify(info) + 'info'); 
            }
        );

        chrome.sockets.udp.onReceiveError.addListener(
            (error) => {
                alert(byteToString(error.data) + 'error'); 
                chrome.sockets.udp.close(error.socketId);
            }
        );

        // send  the UDP search as captures in UPNPSTRING and to port PORT, taken from plugin help text
        chrome.sockets.udp.create((createInfo) => {
            chrome.sockets.udp.bind(createInfo.socketId, '0.0.0.0', PORT, (bindresult) => {
                chrome.sockets.udp.setMulticastTimeToLive(createInfo.socketId, 2, (ttlresult) => {
                    chrome.sockets.udp.setBroadcast(createInfo.socketId, true, function(sbresult) {
                        chrome.sockets.udp.send(createInfo.socketId, UPNPSTRING, '255.255.255.255', PORT, (sendresult) => {
                            if (sendresult < 0) {
                                console.log('send fail: ' + sendresult);
                            } else {
                                console.log('sendTo: success ' + PORT, createInfo, bindresult, ttlresult, sbresult, sendresult);
                            }
                        });
                    });
                });
            });
        });


    };

    $ionicModal.fromTemplateUrl('templates/wifi-link.html', {
        scope: $scope
    }).then(function(modal) {
        $scope.modal = modal;
    });
})
.controller('VipCtrl', function($scope) {


})
.controller('AboutCtrl', function($scope) {


})
.controller('MallCtrl', function($scope) {


})
.controller('DashCtrl', function($scope, $ionicModal, Chats, $http, $timeout) {
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

    $scope.power = false
    $scope.location = "北京"
    $scope.airQuality = "优"
    $scope.mode = 3
    $scope.start = function(){
        $scope.power = !$scope.power;
    }
    $scope.setMode = function(mode){
        $scope.mode = mode
    }

    $ionicModal.fromTemplateUrl('templates/city-choose.html', {
        scope: $scope
      }).then(function(modal) {
        $scope.modal = modal;
      });
      
      $scope.cities = Chats.all();
      $scope.choose = function(city){
        if(city.sub){
            $scope.cities = city.sub ;
        }else{
            $scope.location = city.name;
            var point = Highcharts.charts[0].series[0].points[0];
            point.update(100);
            $scope.airQuality = "良";
            $scope.modal.hide();
            $scope.cities = Chats.all();
        } 
      }
      
      $scope.createContact = function(u) {        
        $scope.contacts.push({ name: u.firstName + ' ' + u.lastName });
        $scope.modal.hide();
      };

    })

.controller('ChatsCtrl', function($scope, Chats) {
  // With the new view caching in Ionic, Controllers are only called
  // when they are recreated or on app start, instead of every page change.
  // To listen for when this page is active (for example, to refresh data),
  // listen for the $ionicView.enter event:
  //
  //$scope.$on('$ionicView.enter', function(e) {
  //});
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
        colors: ['#0AB2F3','#D68F00'],
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
    credits: {
            enabled: false
        },
    chart: {
        type: 'column'
    },
    title: {
        align: 'left',
        text: '近7日空气质量对比',
        style:{
          fontSize: '13px'
        }
    },
    colors: ['#0AB2F3','#D68F00'],
    xAxis: {
        categories: [
            'Jun',
            'Jul',
            'Aug',
            'Sep',
            'Oct',
            'Nov',
            'Dec'
        ],
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
        data: [59.0, 59.6, 52.4, 65.2, 59.3, 51.2, 22.2]

    }, {
        name: '室外',
        data: [57.4, 60.4, 47.6, 39.1, 46.8, 51.1, 44.4]

    }]
    }
})

.controller('ChatDetailCtrl', function($scope, $stateParams) {
  $scope.chartConfig = {
    chart: {
        type: 'column'
    },
    title: {
        text: 'Monthly Average Rainfall'
    },
    xAxis: {
        categories: [
            'Jun',
            'Jul',
            'Aug',
            'Sep',
            'Oct',
            'Nov',
            'Dec'
        ],
        crosshair: true
    },
    yAxis: {
        min: 0,
        title: {
            text: 'Rainfall (mm)'
        }
    },
    tooltip: {
        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
            '<td style="padding:0"><b>{point.y:.1f} mm</b></td></tr>',
        footerFormat: '</table>',
        shared: true,
        useHTML: true
    },
    series: [{
        name: 'London',
        data: [59.0, 59.6, 52.4, 65.2, 59.3, 51.2]

    }, {
        name: 'Berlin',
        data: [57.4, 60.4, 47.6, 39.1, 46.8, 51.1]

    }]
    }
})

.controller('AccountCtrl',function($scope, $rootScope, $cordovaNetwork) {
    document.addEventListener("deviceready", function () {
        var type = $cordovaNetwork.getNetwork()
        var isOnline = $cordovaNetwork.isOnline()
        var isOffline = $cordovaNetwork.isOffline()
        // listen for Online event
        $rootScope.$on('$cordovaNetwork:online', function(event, networkState){
        var onlineState = networkState;
        })
        // listen for Offline event
        $rootScope.$on('$cordovaNetwork:offline', function(event, networkState){
        var offlineState = networkState;
        })
    }, false);

    $scope.settings = {
        enableFriends: true
    };


});


function str2ab(str) {
    var buf = new ArrayBuffer(str.length * 2); // 2 bytes for each char
    var bufView = new Uint16Array(buf);
    for (var i = 0, strLen = str.length; i < strLen; i++) {
        bufView[i] = str.charCodeAt(i);
    }
    return buf;
}

function byteToString(arr) {  
    if(typeof arr === 'string') {  
        return arr;  
    }  
    var str = '',  
        _arr = arr;  
    for(var i = 0; i < _arr.length; i++) {  
        var one = _arr[i].toString(2),  
            v = one.match(/^1+?(?=0)/);  
        if(v && one.length == 8) {  
            var bytesLength = v[0].length;  
            var store = _arr[i].toString(2).slice(7 - bytesLength);  
            for(var st = 1; st < bytesLength; st++) {  
                store += _arr[st + i].toString(2).slice(2);  
            }  
            str += String.fromCharCode(parseInt(store, 2));  
            i += bytesLength - 1;  
        } else {  
            str += String.fromCharCode(_arr[i]);  
        }  
    }  
    return str;  
}  
