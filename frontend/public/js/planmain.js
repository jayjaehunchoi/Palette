
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
  document.querySelector(".groupName").value = '';
  document.querySelector(".groupIntroduction").value = '';
}   

function closeJoinGroupModal() {
  document.getElementById("joinGroupModal").style.display="none";
  document.querySelector(".groupCode").value = '';
}   

//플랜 페이지 이동 시 그룹 리스트 출력
$(document).ready(function(){
  getGroupList();
});

function getGroupList() {
 var token = sessionStorage.getItem("token");
  $.ajax({
    type : "GET",
    url : "http://www.palette-travel.com/api/travelgroup",
    contentType: "application/json",
    beforeSend: function (xhr) {
      xhr.setRequestHeader("Authorization","Bearer " + token);//header추가
    },
    error: function (jqXHR, textStatus, errorThrown) {
      let errorMsg = jqXHR.responseText.split("\"")[3];
      alert(errorMsg);
      window.location.href="/index.html";
    },
    success: function(data){
      loadGroupList(data);
    }
  });
}

function loadGroupList(data) {
  let groupList = data.responseDtoGroups;
  let groupListLen = groupList.length;
  let str = "";
  
  if (groupListLen == 0) {
    $('.list').html("<li>가입한 그룹이 없습니다</li>");
  } else {
    for(let i=0; i < groupListLen ; i++ ) { 
      str += '<li>'
      str += '<div class="toGroup">'
      str += groupList[i].groupName
      str += '</div> <button class="toGroupBt" onclick="moveGroupPage(this.id);" id="' + groupList[i].id + '">입장</button> </li>';
    }

    $('.list').html(str);
  }
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
  if (groupName == '' || groupIntroduction == '') {
    alert("그룹 정보를 모두 입력해주세요");
  } else {
    $.ajax({
      type: "POST",
      url: "http://www.palette-travel.com/api/travelgroup",
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
        closeMakeGroupModal();
        getGroupList(data);
      }
  });
}
});

//그룹 가입하기
$('#joinGroupModal .modalOkBt').click(function(){
  let token = sessionStorage.getItem("token"); 
  let groupCode = $('#joinGroupModal .groupCode').val();
  let groupData = {
    "code" : groupCode
  }

  if (groupCode == '' ) {
    alert("그룹 코드를 입력해주세요");
  } else {
    $.ajax({
      type: "POST",
      url: "http://www.palette-travel.com/api/travelgroup/join",
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
        closeJoinGroupModal();
        getGroupList(data);
      }
    });
  }
});