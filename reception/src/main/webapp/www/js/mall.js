app.controller('MallCtrl', function($scope, $http) {
//	$http.get('/reception/goods/consumer/list').success(function(response){
//		if(response.status == 1){
//			$scope.goodsList = response.contents.goodsList
//			
//		}
//	});
	//$http.get('/reception/goods/{goodsID}/cover').success(function(response){});
	$scope.goodsList = [1,2,3,4,5]
	
	$scope.goodsDetail = function(){alert('aaa')}
})