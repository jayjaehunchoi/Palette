// // 이미지 정보들을 담을 배열
// var sel_files = [];

// $(document).ready(function(){
//     $("#input_imgs").on("change",handleImgsFilesSelect);
// });

// function fileUploadAction(){
//     console.log("fileUploaeAction");
//     $(input_imgs).trigger('click');
// }

// function handleImgsFilesSelect(e){

//     //이미지 정보들을 초기화
//     sel_files = [];
//     $(".imgs_wrap").empty();

//     var files = e.target.files;
//     var filesArr = Array.prototype.slice.call(files);

//     var index = 0;
//     filesArr.forEach(function(f) {
//         if(!f.type.match("image.*")){
//             alert("확장자는 이미지 확장자만 가능합니다.");
//             return;
//         }

//         sel_files.push(f);

//         // 삭제
//         function deleteImageAction(index){
//             console.log("index:"+index);
//             sel_files.splice(index,1);

//             var img_id = "#img_id_"+index;
//             $(img_id).remove();

//             console.log(sel_files);
//         }

//         var reader = new FileReader();
//         reader.onload = function(e){
//            var html = "<a href=\"javascript:void(0);\" onclick=\"deleteImageAction("+index+")\" id =\"img_id_"+index+"\"><img src = \""+e.target.result +"\" data=file='"+f.name+"'class = 'selProductFile' title = 'Click to remove'></a>"
//            $(".imgs_wrap").append(img_html);
//             index++;
//         }
//         reader.readAsDataURL(f);
//     });
// }

var sel_files = [];

$(document).ready(function(){
    $("#input_imgs").on("change",handleImgsFilesSelect);
});

function handleImgsFilesSelect(e){
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
