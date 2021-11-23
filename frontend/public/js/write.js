var sel_files = [];

$(document).ready(function(){
    $("#thumbNailFullPath").on("change",handleImgsFilesSelect);
});

function fileUploadAction(){
    console.log("fileUploadAction");
    $("#thumbNailFullPath").trigger('click');
}

function handleImgsFilesSelect(e){

    //이미지 정보들을 초기화
    sel_files = [];
    $(".imgs_wrap").empty();

    var files = e.target.files;
    var filesArr = Array.prototype.slice.call(files);
  

    filesArr.forEach(function(f) {
        if(!f.type.match("image.*")){
            alert("확장자는 이미지 확장자만 가능합니다.");
            return;
        }

        sel_files.push(f);

        var reader = new FileReader();
        reader.onload = function(e){
            var img_html = "<img src = \"" + e.target.result + "\" />";
            $(".imgs_wrap").append(img_html);
        }
        reader.readAsDataURL(f);
    });
}

