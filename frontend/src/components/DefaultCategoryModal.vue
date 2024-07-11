<template>
  <div class="modal-fade">
    <div class="modal-content">
      <span class="close" @click="$emit('close')">&times;</span>
      <form>
        <p>Ми виявили, що у вас немає жодної категорії.</p>
        <p>Додати стандартний набір категорій?</p>
        <button type="button" @click="$emit('add')">Так</button>
        <button type="button" @click="$emit('close')">Ні</button>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DefaultCategoryModal'
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

button {
  margin-top: 10px;
  padding: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  cursor: pointer;
}

button[type="button"]:last-child {
  background-color: #ccc;
  color: black;
}
</style>