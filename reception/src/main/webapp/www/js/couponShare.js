

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
	        var url = "http://"+window.location.host+"/reception"+result.response.contents.sharepath;
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