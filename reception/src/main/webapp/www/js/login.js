app.controller('LoginCtrl',function($state,$http,$scope) {
	$scope.login = function(phone, passwd){
		var data = {  
		        phone : phone,   
		    };  
		    $http({  
		        url    : '/reception/consumer/login',  
		        method : "post",  
		        data   : $.param(data),  
		        headers: {  
		            'Content-Type': 'application/x-www-form-urlencoded'  
		        },  
		    }).success(function(data) { 
		    	if(data.status == 1){
		    		$state.go('home.device');
		    	}
		    	
		    })
	};
	
	$scope.register = function(){} 
})