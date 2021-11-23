
// 모달 열기
function openMakeGroupModal() {
  document.getElementById("makeGroupModal").style.display="block";
}

function openJoinGroupModal() {
  document.getElementById("joinGroupModal").style.display="block";
}

//모달 닫기
function closeMakeGroupModal() {
  document.getElementById("makeGroupModal").style.display="none";
}   

function closeJoinGroupModal() {
  document.getElementById("joinGroupModal").style.display="none";
}   

//플랜 페이지 이동 시 그룹 리스트 출력
$(document).ready(function(){
  var token = sessionStorage.getItem("token");
  $.ajax({
    type : "GET",
    url : "http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/travelgroup",
    contentType: "application/json",
    beforeSend: function (xhr) {
      xhr.setRequestHeader("Authorization","Bearer " + token);//header추가
    },
    error: function (jqXHR, textStatus, errorThrown) {
      let errorMsg = jqXHR.responseText.split("\"")[3];
      alert(errorMsg);
    },
    success: function(data){
      getGroupList(data);
    }
  });
});

function getGroupList(data) {
  let groupList = data.responseDtoGroups;
  let groupListLen = groupList.length;
  let str = "";

  for(let i=0; i < groupListLen ; i++ ) { 
    str += '<li>'
    str += '<div class="toGroup">'
    str += groupList[i].groupName
    str += '</div> <button class="toGroupBt" onclick="moveGroupPage(this.id);" id="' + groupList[i].id + '">입장</button> </li>';
  }

  $('.list').html(str);
};

// 해당 그룹 페이지로 이동
function moveGroupPage(travelgroupid) {
  sessionStorage.setItem("groupId", travelgroupid);
  window.location.href = "./group.html";
};


// 그룹 생성하기
$('#makeGroupModal .modalBt').click(function(){
  let token = sessionStorage.getItem("token"); 
  let groupName = $('.groupName').val();
  let groupIntroduction = $('.groupIntroduction').val();
  let groupInfo = {
    "groupName":groupName,
    "groupIntroduction": groupIntroduction
  }
  $.ajax({
    type: "POST",
    url: "http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/travelgroup",
    data: JSON.stringify(groupInfo),
    contentType: 'application/json',
    beforeSend: function (xhr) {
      xhr.setRequestHeader("Authorization","Bearer " + token);//header추가
    },
    error: function (jqXHR, textStatus, errorThrown) {
      let errorMsg = jqXHR.responseText.split("\"")[3];
      alert(errorMsg);
    },
    success: function(data){
      console.log("그룹생성 완료");
      closeMakeGroupModal();
    }
  });
});

$('#joinGroupModal .modalOkBt').click(function(){
  let token = sessionStorage.getItem("token"); 
  let groupCode = $('#joinGroupModal .groupCode').val();
  let groupData = {
    "code" : groupCode
  }
  $.ajax({
    type: "POST",
    url: "http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/travelgroup/join",
    data: JSON.stringify(groupData),
    contentType: 'application/json',
    beforeSend: function (xhr) {
      xhr.setRequestHeader("Authorization","Bearer " + token);//header추가
    },
    error: function (jqXHR, textStatus, errorThrown) {
      let errorMsg = jqXHR.responseText.split("\"")[3];
      alert(errorMsg);
    },
    success: function(data){
      console.log("그룹가입 완료");
      closeJoinGroupModal();
    }
  });
});