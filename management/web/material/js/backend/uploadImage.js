/* 图片自动上传 */
var uploadCount = 0, uploadList = [], uploadSuccessCount = 0;
var uploadFilePathList = [];
var uploadCountDom = document.getElementById("uploadCount");
weui.uploader('#uploaderCustom', {
    url: '/qrcode/insight/upload',
    auto: true,
    type: 'file',
    compress: {
        width: 1600,
        height: 1600,
        quality: .6
    },
    onBeforeQueued: function(files) {
        if(["image/jpg", "image/jpeg", "image/png", "image/gif"].indexOf(this.type) < 0){
        	$.alert('请上传图片');
            return false;
        }
        if(this.size > 50 * 1024 * 1024){
        	$.alert('请上传不超过50M的图片');
            return false;
        }
        if (files.length > 5) { // 防止一下子选中过多文件
        	$.alert('最多只能上传5张图片，请重新选择');
            return false;
        }
        if (files.length <= 0) { // 防止传空
        	$.alert('请选择至少一张图片');
            return false;
        }
        if (uploadCount + 1 > 5) {
        	$.alert('最多只能上传5张图片');
            return false;
        }

        ++uploadCount;
        uploadCountDom.innerHTML = uploadCount;
    },
    onQueued: function(){
        uploadList.push(this);
    },
    onBeforeSend: function(data, headers){
        console.log('beforesend');
    },
    onProgress: function(procent){
        console.log('progress');
    },
    onSuccess: function (ret) {
        if (ret.responseCode == 'RESPONSE_OK')
        {
            // uploadSuccessCount++;
            // if (uploadSuccessCount == uploadCount)
            // {
            //     var codeId = document.getElementById('txt_des').value;
            //     $.post('/qrcode/insight/upload/' + codeId, function(data){
            //         console.log("upload" + data);
            //     });
            // }
            uploadFilePathList.push(ret.data);
        }
    },
    onError: function(err){
        console.log(this, err);
    }
});

// 手动上传按钮
document.getElementById("uploaderCustomBtn").addEventListener('click', function(){
    var codeId = document.getElementById('txt_des').value;
    console.log(uploadFilePathList.join(";"));
    $.post('/qrcode/insight/upload/' + codeId, {'filePath': uploadFilePathList.join(";")},  function(data){
        console.log("upload" + data);
    });
});

// 缩略图预览
document.querySelector('#uploaderCustomFiles').addEventListener('click', function(e){
    var target = e.target;

    while(!target.classList.contains('weui-uploader__file') && target){
        target = target.parentNode;
    }
    if(!target) return;

    var url = target.getAttribute('style') || '';
    var id = target.getAttribute('data-id');

    if(url){
        url = url.match(/url\((.*?)\)/)[1].replace(/"/g, '');
    }
    var gallery = weui.gallery(url, {
        onDelete: function(){
            weui.confirm('确定删除该图片？', function(){
                var index;
                for (var i = 0, len = uploadCustomFileList.length; i < len; ++i) {
                    var file = uploadCustomFileList[i];
                    if(file.id == id){
                        index = i;
                        break;
                    }
                }
                if(index !== undefined) uploadCustomFileList.splice(index, 1);

                target.remove();
                gallery.hide();
            });
        }
    });
});

//问题描述
$("#txt_des").on('input', function () {
    if (this.value.length >= 140) {
        $("#txt_des").val(this.value.substring(0, 140));
        return;
    }
    $("#des_count").text(this.value.length);
});