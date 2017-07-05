app.controller('SupportCtrl',function($http,$scope, $state) {
	$scope.support = function(supportForm){
		supportForm.status = 0
		$http({  
	        url    : '/reception/own/support',  
	        method : "post",  
	        params   : supportForm
	    }).success(function(response){
	    	if(response.status == 1){
	    		alert("问题提交成功！")
	    		supportForm.name = null;
	    		supportForm.phone = null;
	    		supportForm.problem = null;
	    		$state.go("home.device")
	    	}else{
	    		alert("提交失败，请重试")
	    	}
	    });
	}
})