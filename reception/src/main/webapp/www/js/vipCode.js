

$(document).ready(function() {
	$.ajax({
		type: "GET",
		url: "/reception/consumer/share/reference",
		success: function(data){
			if(data.status == 1){
				$('#unAuth').hide();
				$('#auth').show();
			}else{
				$('#unAuth').show();
				$('#auth').hide();
			}
		}
	});
	
	$("#fileinput_id1").fileinput({
	    uploadUrl: '/reception/consumer/share/upload',
	    language: 'zh',
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
	        var url = "http://"+window.location.host+"/reception"+result.response.contents.sharepath;
	        $('#generatePic').attr('src',url);
	        $('#hint').show();
	    } else {
	        
	    }
	});
});