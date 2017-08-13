!function(){
  function configWiFi(){
	var serial = GetQueryString("serial")
    wx.invoke('configWXDeviceWiFi', {}, function(res){
      if(res.err_msg == 'configWXDeviceWiFi:ok'){
        
//        $.ajax({
//            type: "GET",
//            url: "/reception/own/register/complete",
//            data: { serial : serial},
//            dataType: "json",
//            success: function(data){
//            	if(data.status == 1){
//            		alert('配置成功!');
//            	}
//            }
//        });
//        window.location.href = "/reception/www/index.html#/home/device"
    	  if(typeof(serial) != undefined && serial != null){
    		  window.location.href = "/reception/www/index.html#/device/pair/"+serial
    	  }else{
    		  window.location.href = "/reception/www/index.html#/home/device"
    	  }
    	  
      } else {
        window.close()
      }
    });
  }

  wx.ready(function(){
    $('#action').click(function(){
      configWiFi();
    });
    
    $('#return').click(function(){
    	window.location.href = "/reception/www/index.html#/home/device"
      });
  });
}();

(function() {
	window.alert = function(name) {
	$(".tip").css("display", "block")
	$(".tip .content").html(name)
	}
})()


function GetQueryString(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}