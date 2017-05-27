app.controller('AccountCtrl',function($scope, $rootScope, $cordovaNetwork, $stateParams) {
	$scope.deviceID = $stateParams.deviceID;
    document.addEventListener("deviceready", function () {
        var type = $cordovaNetwork.getNetwork()
        var isOnline = $cordovaNetwork.isOnline()
        var isOffline = $cordovaNetwork.isOffline()
        // listen for Online event
        $rootScope.$on('$cordovaNetwork:online', function(event, networkState){
        var onlineState = networkState;
        })
        // listen for Offline event
        $rootScope.$on('$cordovaNetwork:offline', function(event, networkState){
        var offlineState = networkState;
        })
    }, false);
})