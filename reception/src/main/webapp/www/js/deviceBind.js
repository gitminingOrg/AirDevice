app.controller( 'DeviceBindCtrl', function($scope, $http, $state, $stateParams) {
	$scope.deviceName =new Object();
	$scope.deviceName.serial = $stateParams.serial;
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
			 if(typeof(response.contents.redirect_url)==undefined || response.contents.openId == null){
				location.href = response.contents.redirect_url
			}else if(typeof(response.contents.openId) !=undefined && response.contents.openId != null){
				$scope.openId = response.contents.openId
				$scope.deviceName.alias = response.contents.openId
			}else{
				alert("sth wrong")
			}
			
		}
	});
	
	//绑定设备
	$scope.bindDevice = function(deviceName){
		//bind user & device
		var data = {open_id : $scope.openID, deviceForm : deviceName}
		$http({  
	        url    : '/reception/own/register',  
	        method : "post",  
	        data   : data
	    }).success(function(data) { 
	    	if(data.status == 1){
	    		window.location.href= "templates/wxinit.html"
	    	}
	    })
	}

})

function GetQueryString(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}