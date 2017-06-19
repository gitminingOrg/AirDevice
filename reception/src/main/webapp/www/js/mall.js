app.controller('MallCtrl', function($scope, $http) {
	$http.get('/reception/goods/consumer/list').success(function(response){
		if(response.status == 1){
			$scope.goodsList = response.contents.goodsList
		}
		$scope.coverList = new Array();
		for(item in response.contents.goodsList) {
			var id = response.contents.goodsList[item].goodsId;
			console.log(JSON.stringify(response.contents.goodsList));
			var url = "/reception/goods/" + id + "/cover";
			$http.get(url).success(function(response){
				if(response.status == 1) {
					$scope.coverList.push("http://commander.qingair.net/reception" + response.contents.cover.path);
				}else {
					$scope.coverList.push("http://www.runoob.com/try/demo_source/41D5vU4I1NL.jpg");
				}
			});
		}
	});
	$scope.goodsDetail = function(){alert('aaa')}
})