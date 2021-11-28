//그룹 정보 수정페이지 이동 시 기존 정보 출력
$(document).ready(function(){
  let travelgroupid = sessionStorage.getItem("groupId");
  var token = sessionStorage.getItem("token");
  $.ajax({
    type : "GET",
    url : "http://www.palette-travel.com/api/travelgroup/" +travelgroupid,
    contentType: "application/json",
    beforeSend: function (xhr) {
      xhr.setRequestHeader("Authorization","Bearer " + token);//header추가
    },
    error: function (jqXHR, textStatus, errorThrown) {
      let errorMsg = jqXHR.responseText.split("\"")[3];
      alert(errorMsg);
    },
    success: function(data){
      getGroupInfo(data); 
    }
  });
});

function getGroupInfo(data) {
  $('.groupName').val(data.groupName);
  $('.groupCode').val(data.groupCode);
  $('.groupIntroduction').val(data.groupsIntroduction);
}

//수정 버튼 클릭
$('.modifyBt').click(function() {
  let travelgroupid = sessionStorage.getItem("groupId");
  var token = sessionStorage.getItem("token");
  let groupName = $('.groupName').val();
  let groupIntroduction =$('.groupIntroduction').val();
  let groupData = {
    "groupName" : groupName,
    "groupIntroduction" : groupIntroduction
  }

  $.ajax({
    type : "PUT",
    url : "http://www.palette-travel.com/api/travelgroup/" +travelgroupid,
    contentType: "application/json",
    data: JSON.stringify(groupData),
    dataType: 'text',
    beforeSend: function (xhr) {
      xhr.setRequestHeader("Authorization","Bearer " + token);//header추가
    },
    error: function (jqXHR, textStatus, errorThrown) {
      let errorMsg = jqXHR.responseText.split("\"")[3];
      alert(errorMsg);
    },
    success: function(data){
      alert("그룹정보가 변경되었습니다!");
      window.location.href="./group.html";
    }
  });
})

// 그룹 삭제버튼
$('.deleteBt').click(function() {
  let travelgroupid = sessionStorage.getItem("groupId");
  var token = sessionStorage.getItem("token");
  $.ajax({
    type : "DELETE",
    url : "http://www.palette-travel.com/api/travelgroup/" +travelgroupid,
    contentType: "application/json",
    beforeSend: function (xhr) {
      xhr.setRequestHeader("Authorization","Bearer " + token);//header추가
    },
    error: function (jqXHR, textStatus, errorThrown) {
      let errorMsg = jqXHR.responseText.split("\"")[3];
      alert(errorMsg);
    },
    success: function(data){
      alert("그룹이 삭제되었습니다");
      window.location.href="./main.html";
    }
  });
})