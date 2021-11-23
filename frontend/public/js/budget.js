
var inputbox = Vue.component('inputbox',{
  template : 
  `<div>
    <div class="line1">
    <select class="category__input" v-model="category">
      <option value="" selected>분류</option>
      <option value="교통">교통</option>
      <option value="식비">식비</option>
      <option value="숙박">숙박</option>
      <option value="기타">기타</option>
    </select>
    <input class="detail__input" type="text" v-model="detail" placeholder="항목" v-on:keyup.enter="add_item"/> <br>
    </div>
    <div class="line2">
    <input class="price__input" type="number" v-model="price" placeholder="금액" v-on:keyup.enter="add_item">
      <div class="addContainer" v-on:click="add_item">
        <button class="addBt"> + </button>
      </div>
    </div>
  </div>`,
  props: ['propsdata'],

  data() {    
    return {
      detail: '',
      price: '',
      category: '',
      showModal: false
    }
  }, 
  methods: {
    add_item() {
      if (this.detail !== "" && this.category !== "" && this.price !== "") {
        var value = {category: this.category, detail: this.detail, price: this.price };
        this.$emit('add_item', value);
        this.clearInput();
      } else {
        this.showModal = !this.showModal;
      }
    },
    clearInput() {
      this.detail = '';
      this.category ='';
      this.price = '';
    }
  },
});

var budgetlist = Vue.component('budgetlist',{
 template: 
  `<section>
    <transition-group name="list" tag="ul"> 
      <li v-for="(budgetItem, index) in propsdata" :key="index" class="shadow"> 
        <div class="itemContainer">
          <div class="left">
          <div class="category">
            {{ budgetItem.category }}
          </div> 
          <div class="detail">
            {{ budgetItem.detail }} 
          </div>
          <div class="price">
            {{ budgetItem.price }} 
          </div>
          </div>
          <div class="right">
            <div class="edit">
            <span class="editBt" type="button" @click="open_edit()">
              <i class="far fa-edit fa-2x" ></i>
            </span>
            </div>
            <div class="remove">
            <span class="removeBt" type="button" @click="remove_item(budgetItem, index)">
              <i class="far fa-trash-alt fa-2x" aria-hidden="true"></i>
            </span>
            </div>
          </div>
        </div>
      </li>
    </transition-group>
  </section>`,
  props: ['propsdata'], 
 
  methods: {
   remove_item(budgetItem, index) {
     this.$emit('remove_item', budgetItem, index)
   },
   open_edit(budgetItem, index) {
    console.log(budgetItem);
    this.$emit('open_edit', budgetItem, index)
   } 
 }
});

var clear = Vue.component('clear', {
  template: 
    `<div class="clearAllContainer">  
      <button class="clearAllBt" v-on:click="clear_item">초기화</button> 
    </div>`,
    methods: {
      clear_item() {
        this.$emit('remove_all');
      }
    } 
});

var modal = Vue.component('modal', {
  template: 
  `<transition name="modal">
  <div class="modal-mask">
    <div class="modal-wrapper">
      <div class="modal-container">

        <div class="modal-header">
          <slot name="header">
            default header
          </slot>
        </div>

        <div class="modal-body">
          <slot name="body">
            default body
          </slot>
        </div>

        <div class="modal-footer">
          <slot name="footer">
            default footer
            <button class="modal-default-button" @click="$emit('close')">
              OK
            </button>
          </slot>
        </div>
      </div>
    </div>
  </div>
</transition>`
}

)


var test = new Vue({
  el: '.app',
  data() {
    return {
      budgetItems: [],
      showModal: false
    }
  }, 

  components :{
    modal
  }, 
  methods: {
    add_item(budgetItem) {
      this.budgetItems.push(budgetItem);
      console.log(budgetItem);
      
      // 전송
    },
    clearAll() {
      this.budgetItems =[];
    },
    remove_item(budgetItem, index) {
      this.budgetItems.splice(index, 1);
    },

    open_edit(budgetItem) {
      this.showModal = !this.showModal;
      console.log(budgetItem);
      document.querySelector(".detail__input").setAttribute("value", budgetItem.detail);
    }
  }
});

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


// 예산설정
function setTotalBudget() {
  document.querySelector(".totalBudget").innerHTML
    = document.querySelector(".totalBudget__input").value;

  document.querySelector(".setBt").style.display = "none";
  document.querySelector(".changeBt").style.display = "inline-block";

  closeSetModal();
}

// 예산수정
function changeTotalBudget() {
  
  document.querySelector(".totalBudget").innerHTML
    = document.querySelector("#changeModal .totalBudget__input").value;

  closeChangeModal();
}
