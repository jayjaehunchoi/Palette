
// var inputbox = Vue.component('inputbox',{
//   template : 
//   `<div>
//     <div class="line1">
//     <select class="category__input" v-model="category">
//       <option value="" selected>분류</option>
//       <option value="교통">교통</option>
//       <option value="식비">식비</option>
//       <option value="숙박">숙박</option>
//       <option value="기타">기타</option>
//     </select>
//     <input class="detail__input" type="text" v-model="detail" placeholder="항목" v-on:keyup.enter="add_item"/> <br>
//     </div>
//     <div class="line2">
//     <input class="price__input" type="number" v-model="price" placeholder="금액" v-on:keyup.enter="add_item">
//       <div class="addContainer" v-on:click="add_item">
//         <button class="addBt"> + </button>
//       </div>
//     </div>
//   </div>`,
//   props: ['propsdata'],

//   data() {    
//     return {
//       detail: '',
//       price: '',
//       category: '',
//       showModal: false
//     }
//   }, 
//   methods: {
//     add_item() {
//       if (this.detail !== "" && this.category !== "" && this.price !== "") {
//         var value = {category: this.category, detail: this.detail, price: this.price };
//         this.$emit('add_item', value);
//         this.clearInput();
//       } else {
//         this.showModal = !this.showModal;
//       }
//     },
//     clearInput() {
//       this.detail = '';
//       this.category ='';
//       this.price = '';
//     }
//   },
// });

// var budgetlist = Vue.component('budgetlist',{
//  template: 
//   `<section>
//     <transition-group name="list" tag="ul"> 
//       <li v-for="(budgetItem, index) in propsdata" :key="index" class="shadow"> 
//         <div class="itemContainer">
//           <div class="left">
//           <div class="category">
//             {{ budgetItem.category }}
//           </div> 
//           <div class="detail">
//             {{ budgetItem.detail }} 
//           </div>
//           <div class="price">
//             {{ budgetItem.price }} 
//           </div>
//           </div>
//           <div class="right">
//             <div class="edit">
//             <span class="editBt" type="button" @click="open_edit()">
//               <i class="far fa-edit fa-2x" ></i>
//             </span>
//             </div>
//             <div class="remove">
//             <span class="removeBt" type="button" @click="remove_item(budgetItem, index)">
//               <i class="far fa-trash-alt fa-2x" aria-hidden="true"></i>
//             </span>
//             </div>
//           </div>
//         </div>
//       </li>
//     </transition-group>
//   </section>`,
//   props: ['propsdata'], 
 
//   methods: {
//    remove_item(budgetItem, index) {
//      this.$emit('remove_item', budgetItem, index)
//    },
//    open_edit(budgetItem, index) {
//     console.log(budgetItem);
//     this.$emit('open_edit', budgetItem, index)
//    } 
//  }
// });

// var clear = Vue.component('clear', {
//   template: 
//     `<div class="clearAllContainer">  
//       <button class="clearAllBt" v-on:click="clear_item">초기화</button> 
//     </div>`,
//     methods: {
//       clear_item() {
//         this.$emit('remove_all');
//       }
//     } 
// });

// var modal = Vue.component('modal', {
//   template: 
//   `<transition name="modal">
//   <div class="modal-mask">
//     <div class="modal-wrapper">
//       <div class="modal-container">

//         <div class="modal-header">
//           <slot name="header">
//             default header
//           </slot>
//         </div>

//         <div class="modal-body">
//           <slot name="body">
//             default body
//           </slot>
//         </div>

//         <div class="modal-footer">
//           <slot name="footer">
//             default footer
//             <button class="modal-default-button" @click="$emit('close')">
//               OK
//             </button>
//           </slot>
//         </div>
//       </div>
//     </div>
//   </div>
// </transition>`
// }

// )


// var test = new Vue({
//   el: '.app',
//   data() {
//     return {
//       budgetItems: [],
//       showModal: false
//     }
//   }, 

//   components :{
//     modal
//   }, 
//   methods: {
//     add_item(budgetItem) {
//       this.budgetItems.push(budgetItem);
//       console.log(budgetItem);
      
//       // 전송
//     },
//     clearAll() {
//       this.budgetItems =[];
//     },
//     remove_item(budgetItem, index) {
//       this.budgetItems.splice(index, 1);
//     },

//     open_edit(budgetItem) {
//       this.showModal = !this.showModal;
//       console.log(budgetItem);
//       document.querySelector(".detail__input").setAttribute("value", budgetItem.detail);
//     }
//   }
// });

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

      if (data.totalBudget != 0) {
        document.querySelector(".setBt").style.display = "none";
        document.querySelector(".changeBt").style.display = "inline-block";
      }
      
      let expenseList = data.expenses;
      let expenseListLen = expenseList.length;
      let str = "";
      console.log(expenseList);
      console.log(expenseListLen);
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
          str += expenseList[i].price + '</div>'
          str += '<div class="right">'
          str += '<div class="edit">'
          str += '<span class="editBt" type="button">'
          str += '<i class="far fa-edit fa-2x" ></i>'
          str += '</span>'
          str += '</div>'
          str += '<div class="remove">'
          str += '<span class="removeBt" type="button">'
          str += '<i class="far fa-trash-alt fa-2x" aria-hidden="true"></i>'
          str += '</span>'
          str += '</div>'
          str += '</div>'
          str += '</div>'
          str += '</li>'
        }
      console.log(str);
      $('.budgetlist .list').html(str);
      } 
    }
  });
  
})

$('#setModal .modalOkBt').click(function(){
  let travelgroupid = sessionStorage.getItem("groupId");
  var token = sessionStorage.getItem("token");
  
  let totalBudget = $('.totalBudget__input').val();
  $.ajax({
    type : "POST",
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
      closeSetModal();
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
      console.log(data.expenses)
      
      let expenseList = data.expenses;
      let expenseListLen = expenseList.length;
      let str = "";
      console.log(expenseList);
      if (expenseListLen > 0) {
        for (let i = 0 ; i < expenseListLen ; i++) {
          str += '<li>'
          str += '<div class="itemContainer" id="' + +'">' 
          str += '<div class="left">'
          str += '<div class="category">'
          str += expenseList[i].category + '</div>'
          str += '<div class="detail">'
          str += expenseList[i].detail + '</div>'
          str += '<div class="price">'
          str += expenseList[i].price + '</div>'
          str += '<div class="right">'
          str += '<div class="edit">'
          str += '<span class="editBt" type="button">'
          str += '<i class="far fa-edit fa-2x" ></i>'
          str += '</span>'
          str += '</div>'
          str += '<div class="remove">'
          str += '<span class="removeBt" type="button">'
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
      console.log(data);
    }
  });
})

//모달 열기
function openSetModal() {
  document.getElementById("setModal").style.display="block";
}

function openChangeModal() {
  document.getElementById("changeModal").style.display="block";
}

//모달 닫기
function closeSetModal() {
  document.getElementById("setModal").style.display="none";
}   

function closeChangeModal() {
  document.getElementById("changeModal").style.display="none";
}   


