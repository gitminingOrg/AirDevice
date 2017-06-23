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

  // setup an abstract state for the tabs directive
  .state('tab', {
    url: '/tab/:deviceID',
    abstract: true,
    controller: 'TabCtrl',
    templateUrl: 'templates/tabs.html'
  })

  // Each tab has its own nav history stack:

  .state('tab.dash', {
    url: '/dash',
    views: {
      'tab-dash': {
        templateUrl: 'templates/tab-dash.html',
        controller: 'DashCtrl',
        reload: true,
        cache: false
      }
    }
  })

  .state('tab.chats', {
      url: '/chats',
      views: {
        'tab-chats': {
          templateUrl: 'templates/tab-status.html',
          controller: 'StatusCtrl'
        }
      }
    })
  .state('tab.account', {
    url: '/account',
    views: {
      'tab-account': {
        templateUrl: 'templates/tab-account.html',
        controller: 'AccountCtrl'
      }
    }
  })
  .state('home', {
    url: '/home',
    abstract: true,
    templateUrl: 'templates/home.html'
  })
  .state('home.device', {
    url: '/device',
    views: {
      'home-device': {
        templateUrl: 'templates/device-choose.html',
        controller: 'DeviceCtrl'         
      }
    }
  })
  .state('home.about', {
    url: '/about',
    views: {
      'home-about': {
        templateUrl: 'templates/about.html',
        controller: 'AboutCtrl'         
      }
    }
  })
  .state('home.vip', {
    url: '/vip',
    views: {
      'home-vip': {
        templateUrl: 'templates/vip-info.html',
        controller: 'VipCtrl'         
      }
    }
  })
  .state('mall', {
    url: '/mall',
    templateUrl: 'templates/mall.html',
    controller: 'MallCtrl'         
  })
  .state('login', {
    url: '/login',
    templateUrl: 'templates/login.html',
    controller: 'LoginCtrl'         
  })
  .state('deviceName', {
    url: '/device/name/:deviceID',
    templateUrl: 'templates/device-name.html',
    controller: 'DeviceNameCtrl'         
  })
  .state('deviceShare', {
	    url: '/device/share/:deviceID',
	    templateUrl: 'templates/device-share.html',
	    controller: 'DeviceShareCtrl'         
  })
  .state('deviceBind', {
	    url: '/device/bind/:serial',
	    templateUrl: 'templates/device-bind.html',
	    controller: 'DeviceBindCtrl'         
  })
  .state('wxInit', {
    url: '/wxinit',
    templateUrl: 'templates/wxinit.html',
    controller: 'WxinitCtrl'         
  });

  // if none of the above states are matched, use this as the fallback
  $urlRouterProvider.otherwise('/login');

});
