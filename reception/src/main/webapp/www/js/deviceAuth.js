app.controller( 'DeviceAuthCtrl', function($scope, $http, $state, $stateParams, $interval, $ionicLoading) {
	$scope.token = $stateParams.token;
	var code = GetQueryString("code")
	//get open id	
	$http({  
        url    : '/reception/wechat/auth',  
        method : "get",  
        params   : { token : $stateParams.token , code : code} 
    }).success(function(response){
		if(response.status == 0){
			alert("诶哟卧槽。。。我这没取到儿openID嗬")
		}else{
			if(typeof(response.contents.redirect_url) !=undefined && response.contents.openId == null){
				location.href = response.contents.redirect_url
			}else if(typeof(response.contents.openId) !=undefined && response.contents.openId != null){
				$scope.openId = response.contents.openId
				$http({  
			        url    : '/reception/own/wx/authorize',  
			        method : "get",  
			        params   : { token : $stateParams.token , openId : $scope.openId} 
			    }).success(function(response){
			    	if(response.status == 1){
			    		$state.go('home.device')
			    	}else{
			    		alert(response.info)
			    	}
			    });
			}else{
				alert("sth wrong")
			}
		}
	});
})

function GetQueryString(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}