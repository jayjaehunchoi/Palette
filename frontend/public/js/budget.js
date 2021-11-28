// 지출내역 리스트 생성
function loadExpenseList(data) {
  let expenseList = data.expenses;
  let expenseListLen = expenseList.length;
  let str = "";
  if (expenseListLen > 0) {
    for (let i = 0 ; i < expenseListLen ; i++) {
      str += '<li>'
      str += '<div class="itemContainer">' 
      str += '<div class="left">'
      str += '<div class="category">'
      str += expenseList[i].category + '</div>'
      str += '<div class="detail">'
      str += expenseList[i].detail + '</div>'
      str += '<div class="price">'
      str += expenseList[i].price + '</div> </div>'
      str += '<div class="right">'
      str += '<div class="edit">'
      str += '<span class="editBt" type="button" onclick="openChangeExpenseModal(this.id);" id="' + expenseList[i].expenseId + '">'
      str += '<i class="far fa-edit fa-2x" ></i>'
      str += '</span>'
      str += '</div>'
      str += '<div class="remove">'
      str += '<span class="removeBt" type="button" onclick="deleteExpense(this.id);" id="' + expenseList[i].expenseId + '">'      
      str += '<i class="far fa-trash-alt fa-2x" aria-hidden="true"></i>'
      str += '</span>'
      str += '</div>'
      str += '</div>'
      str += '</div>'
      str += '</li>'
    }
  
    $('.budgetlist .list').html(str);
  } else {
    $('.budgetlist .list').empty();
  }
}

//예산 정보 및 지출 내역 리스트 받아오기
function readBudget() {
  let travelgroupid = sessionStorage.getItem("groupId");
  var token = sessionStorage.getItem("token");
  
  $.ajax({
    type : "GET",
    url : "http://www.palette-travel.com/api/travelgroup/" +travelgroupid + "/budget",
    contentType: "application/json",
    beforeSend: function (xhr) {
      xhr.setRequestHeader("Authorization","Bearer " + token);//header추가
    },
    error: function (jqXHR, textStatus, errorThrown) {
      let errorMsg = jqXHR.responseText.split("\"")[3];
      alert(errorMsg);
    },
    success: function(data){
      $('.totalBudget').html(data.totalBudget);
      $('.expense').html(data.totalExpense);
      $('.budgetLeft').html(data.remainingBudget);
      
      loadExpenseList(data);
    }
  });

}$(document).ready(function() {
  readBudget();
});


//총예산 변경
$('#changeModal .modalOkBt').click(function(){
  let travelgroupid = sessionStorage.getItem("groupId");
  var token = sessionStorage.getItem("token");
  
  let totalBudget = $('#changeModal .totalBudget__input').val();
  $.ajax({
    type : "PUT",
    url : "http://www.palette-travel.com/api/travelgroup/" +travelgroupid + "/budget",
    contentType: "application/json",
    data: JSON.stringify({"totalBudget":totalBudget}),
    beforeSend: function (xhr) {
      xhr.setRequestHeader("Authorization","Bearer " + token);//header추가
    },
    error: function (jqXHR, textStatus, errorThrown) {
      let errorMsg = jqXHR.responseText.split("\"")[3];
      alert(errorMsg);
    },
    success: function(data){
      readBudget();
      closeChangeModal();
    }
  });
});

// 지출내역 추가
$('.addBt').click(function() {
  let travelgroupid = sessionStorage.getItem("groupId");
  var token = sessionStorage.getItem("token");
  
  let category = $('#category-select').val();
  let detail = $('.detail__input').val();
  let price = $('.price__input').val();
  let expenseData = {
    "category":category,
    "detail":detail,
    "price":price
  }

  if (category == "" || detail == "" || price == "") {
    alert("지출계획을 모두 입력하세요!");
  } else if ( price < 0 ) {
    alert("금액에 음수는 입력할 수 없습니다");
  } else if ( price > 200000000 ) {
    alert("금액에 2억 이상은 입력할 수 없습니다");
  } else {
    $.ajax({
      type : "POST",
      url : "http://www.palette-travel.com/api/travelgroup/" +travelgroupid + "/expenses",
      contentType: "application/json",
      data: JSON.stringify(expenseData),
      beforeSend: function (xhr) {
        xhr.setRequestHeader("Authorization","Bearer " + token);//header추가
      },
      error: function (jqXHR, textStatus, errorThrown) {
        let errorMsg = jqXHR.responseText.split("\"")[3];
        alert(errorMsg);
      },
      success: function(data){
        readBudget();
        $('#category-select').val('');
        $('.detail__input').val('');
        $('.price__input').val('');
      }
    });
  }
});

$('.price__input').keydown(function(key) {
  if(key.keyCode == 13) {
      $('.addBt').click() ;
  }
});


// 지출내역 수정
$('#changeExpenseModal .modalOkBt').click(function() {
  let travelgroupid = sessionStorage.getItem("groupId");
  let expenseid = sessionStorage.getItem("expenseId");
  var token = sessionStorage.getItem("token");
  
  let category = $('#changeExpenseModal .category__input').val();
  let detail = $('#changeExpenseModal .detail__input').val();
  let price = $('#changeExpenseModal .price__input').val();
  let expenseData = {
    "category":category,
    "detail":detail,
    "price":price
  }

  $.ajax({
    type : "PUT",
    url : "http://www.palette-travel.com/api/travelgroup/" +travelgroupid + "/expenses/" + expenseid,
    contentType: "application/json",
    data: JSON.stringify(expenseData),
    beforeSend: function (xhr) {
      xhr.setRequestHeader("Authorization","Bearer " + token);//header추가
    },
    error: function (jqXHR, textStatus, errorThrown) {
      let errorMsg = jqXHR.responseText.split("\"")[3];
      alert(errorMsg);
    },
    success: function(data){
      readBudget();
      closeChangeExpenseModal();
    }
  });
});

// 지출내역 삭제
function deleteExpense(id) {
  sessionStorage.setItem("expenseId", id);

  let travelgroupid = sessionStorage.getItem("groupId");
  let expenseid = sessionStorage.getItem("expenseId");
  var token = sessionStorage.getItem("token");

  $.ajax({
    type : "DELETE",
    url : "http://www.palette-travel.com/api/travelgroup/" +travelgroupid + "/expenses/" + expenseid,
    contentType: "application/json",
    beforeSend: function (xhr) {
      xhr.setRequestHeader("Authorization","Bearer " + token);//header추가
    },
    error: function (jqXHR, textStatus, errorThrown) {
      let errorMsg = jqXHR.responseText.split("\"")[3];
      alert(errorMsg);
    },
    success: function(data){
      readBudget();
    }
  });
}

//모두 지우기
$('.clearAllBt').click(function() {
  let travelgroupid = sessionStorage.getItem("groupId");
  var token = sessionStorage.getItem("token");

  $.ajax({
    type : "DELETE",
    url : "http://www.palette-travel.com/api/travelgroup/" +travelgroupid + "/expenses",
    contentType: "application/json",
    beforeSend: function (xhr) {
      xhr.setRequestHeader("Authorization","Bearer " + token);//header추가
    },
    error: function (jqXHR, textStatus, errorThrown) {
      let errorMsg = jqXHR.responseText.split("\"")[3];
      alert(errorMsg);
    },
    success: function(data){
      readBudget();
      $('.budgetlist .list').empty();
    }
  });
});


//모달 열기

function openChangeModal() {
  document.getElementById("changeModal").style.display="block";
}

function openChangeExpenseModal(expenseid) {
  sessionStorage.setItem("expenseId", expenseid);
  document.getElementById("changeExpenseModal").style.display="block";

  let travelgroupid = sessionStorage.getItem("groupId");
  let id = sessionStorage.getItem("expenseId");
  var token = sessionStorage.getItem("token");
  
  let category = $('#changeExpenseModal .category__input');
  let detail = $('#changeExpenseModal .detail__input');
  let price = $('#changeExpenseModal .price__input');

  $.ajax({
    type : "GET",
    url : "http://www.palette-travel.com/api/travelgroup/" +travelgroupid + "/budget",
    contentType: "application/json",
    beforeSend: function (xhr) {
      xhr.setRequestHeader("Authorization","Bearer " + token);//header추가
    },
    error: function (jqXHR, textStatus, errorThrown) {
      let errorMsg = jqXHR.responseText.split("\"")[3];
      alert(errorMsg);
    },
    success: function(data){
      let expenseList = data.expenses;

      $.each(expenseList, function(idx, row) {
        if(expenseList[idx].expenseId == id ) {
          category.val(expenseList[idx].category).prop("selected", true);
          detail.val(expenseList[idx].detail);
          price.val(expenseList[idx].price);
        }
      })
    }
  });
}

//모달 닫기

function closeChangeModal() {
  $('.totalBudget__input').val('');
  document.getElementById("changeModal").style.display="none";
}   

function closeChangeExpenseModal() {
  document.getElementById("changeExpenseModal").style.display="none";
}

// 2초마다 readBudget(); 실행
setInterval(function() {readBudget();}, 2000);