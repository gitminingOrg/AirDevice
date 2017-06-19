app.controller('WxinitCtrl',function($scope,$http) {
	var url = self.location.href
	alert(url)
	var data = { url : url }
	$scope.buttonShow = false;
	$http({  
	        url    : '/reception/wechat/init',  
	        method : "post",  
	        data   : $.param(data),  
	        headers: {  
	            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'  
	        },  
	    }).success(function(response){
	    	if(response.status == 1){
	    		wx.config({
	    		    debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
	    		    appId: response.contents.configuration.appId, // 必填，公众号的唯一标识
	    		    timestamp: response.contents.configuration.timestamp, // 必填，生成签名的时间戳
	    		    nonceStr: response.contents.configuration.nonceStr, // 必填，生成签名的随机串
	    		    signature: response.contents.configuration.signature,// 必填，签名，见附录1
	    		    jsApiList: ['configWXDeviceWiFi'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
	    		});
	    		wx.error(function (res) {
	    			  
	    	    });
	    		$scope.buttonShow = true;
	    		wx.ready(function(){
		    		$scope.wxinit = function(){
		    		    wx.invoke('configWXDeviceWiFi', {}, function(res){
		    			      if(res.err_msg == 'configWXDeviceWiFi:ok'){
		    			        alert('配置成功!');
		    			        wx.closeWindow();
		    			      } else {
		    			        alert('配置失败！请重试');
		    			      }
		    			    });
		    		}
	    		});
	    		
	    	}
	    });
    
})


