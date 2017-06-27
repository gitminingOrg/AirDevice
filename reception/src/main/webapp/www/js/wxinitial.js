!function(){
  function configWiFi(){
	var serial = GetQueryString("serial")
	alert("serial : " + serial)
    wx.invoke('configWXDeviceWiFi', {}, function(res){
      if(res.err_msg == 'configWXDeviceWiFi:ok'){
        alert('配置成功!');
        $.ajax({
            type: "GET",
            url: "/reception/own/register/complete",
            data: { serial : serial},
            dataType: "json",
            success: function(data){
            }
        });
        window.location.href = "/reception/www/index.html#/home/device"
      } else {
        alert('配置失败！请重试');
        window.location.href = "/reception/www/index.html#/home/device"
      }
    });
  }

  wx.ready(function(){
    $('#action').click(function(){
      configWiFi();
    });
  });
}();

function GetQueryString(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}