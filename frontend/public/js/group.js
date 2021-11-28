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

$('.withdrawGroupBt').click(function() {
  let travelgroupid = sessionStorage.getItem("groupId");
  var token = sessionStorage.getItem("token");
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
      if (confirm("정말 이 그룹에서 나가시겠습니까?")) {
        alert("탈퇴되었습니다");
        window.location.href = "./main.html"
      } 
    }
  });
});