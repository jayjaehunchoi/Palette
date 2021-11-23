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
  }
}
$(document).ready(function() {
  let travelgroupid = sessionStorage.getItem("groupId");
  var token = sessionStorage.getItem("token");
  
  $.ajax({
    type : "GET",
    url : "http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/travelgroup/" +travelgroupid + "/budget",
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
});



$('#changeModal .modalOkBt').click(function(){
  let travelgroupid = sessionStorage.getItem("groupId");
  var token = sessionStorage.getItem("token");
  
  let totalBudget = $('#changeModal .totalBudget__input').val();
  $.ajax({
    type : "PUT",
    url : "http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/travelgroup/" +travelgroupid + "/budget",
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

function readBudget() {
  let travelgroupid = sessionStorage.getItem("groupId");
  var token = sessionStorage.getItem("token");
  
  $.ajax({
    type : "GET",
    url : "http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/travelgroup/" +travelgroupid + "/budget",
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
}



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
  } else {
    $.ajax({
      type : "POST",
      url : "http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/travelgroup/" +travelgroupid + "/expenses",
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
    url : "http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/travelgroup/" +travelgroupid + "/expenses/" + expenseid,
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

function deleteExpense(id) {
  sessionStorage.setItem("expenseId", id);

  let travelgroupid = sessionStorage.getItem("groupId");
  let expenseid = sessionStorage.getItem("expenseId");
  var token = sessionStorage.getItem("token");

  $.ajax({
    type : "DELETE",
    url : "http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/travelgroup/" +travelgroupid + "/expenses/" + expenseid,
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

$('.clearAllBt').click(function() {
  let travelgroupid = sessionStorage.getItem("groupId");
  var token = sessionStorage.getItem("token");

  $.ajax({
    type : "DELETE",
    url : "http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/travelgroup/" +travelgroupid + "/expenses",
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
    url : "http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/travelgroup/" +travelgroupid + "/budget",
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
  document.getElementById("changeModal").style.display="none";
}   

function closeChangeExpenseModal() {
  document.getElementById("changeExpenseModal").style.display="none";
}