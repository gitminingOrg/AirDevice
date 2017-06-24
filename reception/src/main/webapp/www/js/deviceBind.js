app.controller( 'DeviceBindCtrl', function($scope, $http, $state, $stateParams) {
	$scope.deviceID = $stateParams.serial;
	$scope.serial = $stateParams.serial;
	//get open id	
	$http({  
        url    : '/reception/wechat/user',  
        method : "get",  
        params   : { serial : $stateParams.serial} 
    }).success(function(response){
		if(response.status == 0){
			alert("诶哟卧槽。。。我这没取到儿openID嗬")
		}else{
			alert("诶哟卧槽。。。 开始了")
			alert(JSON.stringify(response))
			 if(typeof(response.contents.redirect_url)=="undefined" || response.contents.openId == null){
				alert('redirect')
				location.href = response.contents.redirect_url
			}else if(typeof(response.contents.openId)=="undefined" || response.contents.openId == null){
				$scope.openId = response.contents.openId
				$scope.deviceName = response.contents.openId
				alert($scope.openId)
			}else{
				alert("sth wrong")
			}
			
		}
	});
	//bind user & device
//	var data = {serail : $scope.serial, open_id : $scope.openID}
//	$http({  
//        url    : '',  
//        method : "post",  
//        data   : $.param(data),  
//        headers: {  
//            'Content-Type': 'application/x-www-form-urlencoded'  
//        },  
//    }).success(function(data) { 
//    	if(data.status == 1){
//    		$state.go('home.device');
//    	}
//    })
})