app.controller('SupportCtrl',function($http,$scope, $state,$ionicPopup) {
	$scope.support = function(supportForm){
		supportForm.status = 0
		$http({  
	        url    : '/reception/own/support',  
	        method : "post",  
	        params   : supportForm
	    }).success(function(response){
	    	if(response.status == 1){
	    		$scope.showAlert("召唤支援","问题提交成功！")
	    		supportForm.name = null;
	    		supportForm.phone = null;
	    		supportForm.problem = null;
	    		$state.go("home.device")
	    	}else{
	    		$scope.showAlert("召唤支援","提交失败，请重试")
	    	}
	    });
	}
	
	$scope.showAlert = function popup(title, content) {
	      var alertPopup = $ionicPopup.alert({
	        title: title,
	        template: content
	      });
	    };
})