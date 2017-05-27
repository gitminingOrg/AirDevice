app.controller('VipCtrl', function($scope, $http) {
	$http.get('/reception/consumer/info').success(function(response){
		if(response.status == 1){
			$scope.consumer = response.contents.consumer
		}
	});
})