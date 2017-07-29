$(document).ready(function() {
	var months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
	var weekday = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']
	var date = new Date();
	var today = date.getDate() + " " + months[date.getMonth()] +" " + weekday[date.getDay()]
	$('#date').text(today)
	$.ajax({
		type: "GET",
		url: "/reception/consumer/share/reference",
		success: function(data){
			if(data.status == 1){
				$.ajax({
					type: "GET",
					url: "/reception/own/user/city",
					success: function(response){
						if(response.status == 1){
							var cityPinyin = response.contents.city;
							$.ajax({
								type: "POST",
								url: "/reception/status/city/aqi",
								data: {city : cityPinyin},
								success: function(response2){
									$('#location').text(response2.contents.cityAqi.cityName)
									$('#pm25').text("pm2.5 " +response2.contents.cityAqi.pm25)
								}
							})
						}
					}
				})
				$('#unAuth').hide();
				$('#auth').show();
				$("#code").text(data.contents.sharecode.shareCode)
				if(data.contents.sharecode.path != null){
					var url = "http://"+window.location.host+"/reception"+data.contents.sharecode.path;
			        $('#photo').attr('src',url);
			        $('#main').show();
					$('#img_choose').hide();
				}else{
					$('#img_choose').show();
					$('#main').hide();
				}
			}else{
				$('#unAuth').show();
				$('#auth').hide();
			}
		}
	});
	
	$("#fileinput_id1").fileinput({
	    uploadUrl: '/reception/consumer/share/upload',
	    language: 'zh',
	    showPreview : false,
	    showUpload: false,
	    showCaption: false,
	    showCancel: false,
	    showRemove: false,
	    showUploadedThumbs: false,
	    dropZoneEnabled: false,
	    maxFileCount: 1,
	    maxFileSize: 0,
	    allowedFileExtensions: ['jpg', 'jpeg', 'png', 'gif'],
	})
	.on("filebatchselected", function (event, files) {
        $(this).fileinput("upload");
    })
	.on("fileuploaded", function (event, result) {
	    if (result.response.status == 1) {
	        var url = "http://"+window.location.host+"/reception"+result.response.contents.sharepath.path;
	        $('#photo').attr('src',url);
	    }  
	    $('#main').show();
		$('#img_choose').hide();
	});
});

function imgShift(){
	$('#img_choose').show();
	$('#main').hide();
}