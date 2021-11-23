$(document).ready(function(){
  let travelgroupid = sessionStorage.getItem("groupId");
  var token = sessionStorage.getItem("token");
  $.ajax({
    type : "GET",
    url : "http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/travelgroup/" +travelgroupid,
    contentType: "application/json",
    beforeSend: function (xhr) {
      xhr.setRequestHeader("Authorization","Bearer " + token);//header추가
    },
    error: function (jqXHR, textStatus, errorThrown) {
      let errorMsg = jqXHR.responseText.split("\"")[3];
      alert(errorMsg);
    },
    success: function(data){
      console.log(data);
      getGroupInfo(data); 
    }
  });
});

function getGroupInfo(data) {
  $('.groupName').val(data.groupName);
  $('.groupCode').val(data.groupCode);
  $('.groupIntroduction').val(data.groupsIntroduction);
}

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
    url : "http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/travelgroup/" +travelgroupid,
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

$('.withdrawalBt').click(function() {
  let travelgroupid = sessionStorage.getItem("groupId");
  var token = sessionStorage.getItem("token");
  $.ajax({
    type : "DELETE",
    url : "http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/travelgroup/" +travelgroupid,
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