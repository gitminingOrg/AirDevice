app.controller('VipCtrl', function($scope, $http, $state) {
	$http.get('/reception/consumer/info').success(function(response){
		if (response.status == 1) {
			$scope.consumer = response.contents.consumer;
			$state
		} else {
			$state.go('login');
		}
	});
})