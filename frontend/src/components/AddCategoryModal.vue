<template>
  <div class="modal-fade">
    <div class="modal-content">
      <span class="close" @click="$emit('close')">&times;</span>
      <form @submit.prevent="addCategory">
        <label for="name">Category Name:</label>
        <input type="text" v-model="categoryName" required />
        <label for="type">Category Type:</label>
        <select v-model="categoryType" required>
          <option value="EXPENSE">Expense</option>
          <option value="INCOME">Income</option>
        </select>
        <button type="submit">Add Category</button>
      </form>
    </div>
  </div>
</template>

<script>
import axios from '../axios-config';

export default {
  name: 'AddCategoryModal',
  data() {
    return {
      categoryName: '',
      categoryType: 'EXPENSE'
    };
  },
  methods: {
    addCategory() {
      const newCategory = {
        name: this.categoryName,
        type: this.categoryType
      };
      axios.post('/tracker/categories', newCategory)
          .then(() => {
            this.$emit('added');
            this.$emit('close');
          })
          .catch(() => {
            console.log("ERROR! Something wrong happened");
          });
    }
  }
};
</script>

<style scoped>
.modal-fade {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  width: 300px;
}

.close {
  position: absolute;
  top: 10px;
  right: 10px;
  cursor: pointer;
}

form {
  display: flex;
  flex-direction: column;
}

label {
  margin-top: 10px;
}

input, select {
  padding: 5px;
  margin-top: 5px;
}

button {
  margin-top: 10px;
  padding: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  cursor: pointer;
}
</style>