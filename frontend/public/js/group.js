//그룹 페이지 이동 시 그룹 정보 출력
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
  $('.groupName').html(data.groupName);
  $('.groupCode').html("CODE | " + data.groupCode);
  $('.groupIntroduction').html(data.groupsIntroduction);

  let memberList = data.responseDtoMembers;
  let memberListLen = memberList.length;
  let str = "";

  for(let i=0; i < memberListLen ; i++ ) { 
    str += '<li>'
    str += memberList[i].name
    str += '</li>';
  }

  $('.memberList').html(str);

};

// 그룹 탈퇴
$('.withdrawGroupBt').click(function() {
  let travelgroupid = sessionStorage.getItem("groupId");
  var token = sessionStorage.getItem("token");
  
  if (confirm("정말 이 그룹에서 나가시겠습니까?")) {
    $.ajax({
      type : "DELETE",
      url : "http://www.palette-travel.com/api/travelgroup/" +travelgroupid +"/member",
      contentType: "application/json",
      beforeSend: function (xhr) {
        xhr.setRequestHeader("Authorization","Bearer " + token);//header추가
      },
      error: function (jqXHR, textStatus, errorThrown) {
        let errorMsg = jqXHR.responseText.split("\"")[3];
        alert(errorMsg);
      },
      success: function(data){
        alert("탈퇴되었습니다");
        window.location.href = "./main.html"
      }
    });
  }

});