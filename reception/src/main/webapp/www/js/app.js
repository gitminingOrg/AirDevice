// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.services' is found in services.js
// 'starter.controllers' is found in controllers.js
angular.module('starter', ['ionic', 'starter.controllers', 'starter.services','highcharts-ng'])

.run(function($ionicPlatform) {
  $ionicPlatform.ready(function() {
    // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
    // for form inputs)
    if (window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
      cordova.plugins.Keyboard.disableScroll(true);

    }
    if (window.StatusBar) {
      // org.apache.cordova.statusbar required
      StatusBar.styleDefault();
    }
  });
})

.config(function($stateProvider, $urlRouterProvider) {

  // Ionic uses AngularUI Router which uses the concept of states
  // Learn more here: https://github.com/angular-ui/ui-router
  // Set up the various states which the app can be in.
  // Each state's controller can be found in controllers.js
  $stateProvider

  // Each tab has its own nav history stack:

  .state('dash', {
    url: '/tab/:deviceID/dash',
    templateUrl: 'templates/tab-dash.html',
    controller: 'DashCtrl',
    reload: true,
    cache: false
  })

  .state('chats', {
      url: '/tab/:deviceID/chats',
      templateUrl: 'templates/tab-status.html',
      controller: 'StatusCtrl',
      reload: true,
      cache: false
    })
  .state('account', {
    url: '/tab/:deviceID/account',
    templateUrl: 'templates/tab-account.html',
    controller: 'AccountCtrl',
    reload: true,
    cache: false
  })
  .state('home', {
    url: '/home',
    abstract: true,
    templateUrl: 'templates/home.html',
    reload: true,
    cache: false 
  })
  .state('home.device', {
    url: '/device',
    views: {
      'home-device': {
        templateUrl: 'templates/device-choose.html',
        controller: 'DeviceCtrl',
        reload: true,
        cache: false        
      }
    }
  })
  .state('home.support', {
    url: '/support',
    views: {
      'home-support': {
        templateUrl: 'templates/home-support.html',
        controller: 'SupportCtrl' ,
        reload: true,
        cache: false        
      }
    }
  })
  .state('home.wifi', {
    url: '/wifi',
    views: {
      'home-wifi': {
        templateUrl: 'templates/home-wifi.html',
        controller: 'WifiCtrl' ,
        reload: true,
        cache: false        
      }
    }
  })
  .state('home.vip', {
    url: '/vip',
    views: {
      'home-vip': {
        templateUrl: 'templates/vip-info.html',
        controller: 'VipCtrl',
        reload: true,
        cache: false     
      }
    }
  })
  .state('mall', {
    url: '/mall',
    templateUrl: 'templates/mall.html',
    controller: 'MallCtrl',
    reload: true,
    cache: false         
  })
  .state('login', {
    url: '/login',
    templateUrl: 'templates/login.html',
    controller: 'LoginCtrl' ,
    reload: true,
    cache: false        
  })
  .state('deviceName', {
    url: '/device/name/:deviceID',
    templateUrl: 'templates/device-name.html',
    controller: 'DeviceNameCtrl',
    reload: true,
    cache: false         
  })
  .state('deviceShare', {
	    url: '/device/share/:deviceID/:token',
	    templateUrl: 'templates/device-share.html',
	    controller: 'DeviceShareCtrl',
        reload: true,
        cache: false         
  })
  .state('deviceBind', {
	    url: '/device/bind/:serial',
	    templateUrl: 'templates/device-bind.html',
	    controller: 'DeviceBindCtrl',
        reload: true,
        cache: false         
  })
  .state('deviceAuth', {
	    url: '/device/auth/:token',
	    templateUrl: 'templates/auth-device.html',
	    controller: 'DeviceAuthCtrl',
        reload: true,
        cache: false         
  })
  .state('privilege', {
	    url: '/device/privilege/:deviceID',
	    templateUrl: 'templates/privilege.html',
	    controller: 'PrivilegeCtrl',
        reload: true,
        cache: false         
  })
  .state('devicePair', {
	    url: '/device/pair/:deviceID',
	    templateUrl: 'templates/device-pair.html',
	    controller: 'DevicePairCtrl',
        reload: true,
        cache: false         
  });

  // if none of the above states are matched, use this as the fallback
  $urlRouterProvider.otherwise('/home.device');

});
