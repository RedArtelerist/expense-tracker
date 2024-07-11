<template>
  <div class="categories-space">
    <div v-if="expenses.length > 0" class="category-section">
      <div class="category-title" style="color: red">Expenses</div>
      <div v-for="category in expenses"
           :key="category.id"
           :style="getCategoryStyle(categories.length)"
           :class="['category-tile', 'expense', { over: highlightedCategoryId === category.id }]"
           @dragover.prevent
           @dragenter="handleDragEnter(category.id)"
           @dragleave="handleDragLeave"
           @drop="handleDrop($event, category)">
        {{ category.name }}
      </div>
    </div>
    <div v-if="incomes.length > 0" class="category-section">
      <div class="category-title" style="color: green">Incomes</div>
      <div v-for="category in incomes"
           :key="category.id"
           :style="getCategoryStyle(categories.length)"
           :class="['category-tile', 'income', { over: highlightedCategoryId === category.id }]"
           @dragover.prevent
           @dragenter="handleDragEnter(category.id)"
           @dragleave="handleDragLeave(category.id)"
           @drop="handleDrop($event, category)">
        {{ category.name }}
      </div>
    </div>
    <div class="space-button-add-category">
      <button class="button-add-category" @click="showAddCategoryModal">+</button>
    </div>
  </div>

  <div v-if="showToast" class="toast">
    {{ toastMessage }}
    <button @click="undefineTransaction">Undefine</button>
  </div>

  <add-category-modal
      v-if="showCategoryModal"
      @close="hideAddCategoryModal"
      @added="fetchCategories"
  />

  <default-category-modal
      v-if="showDefaultCategoryModal"
      @close="hideDefaultCategoryModal"
      @add="addDefaultCategories"
  />
</template>

<script>
import axios from "../axios-config";
import AddCategoryModal from './AddCategoryModal.vue';
import DefaultCategoryModal from './DefaultCategoryModal.vue';

export default {
  name: 'Categories',
  components: {
    AddCategoryModal,
    DefaultCategoryModal
  },

  props: {
    highlightedCategoryId: {
      type: String,
      default: null
    }
  },

  data() {
    return {
      categories: [],
      showCategoryModal: false,
      showDefaultCategoryModal: false,
      showToast: false,
      toastMessage: '',
      lastDefinedTransaction: null
    };
  },

  created() {
    this.fetchCategories();
  },

  computed: {
    expenses() {
      return this.categories.filter(category => category.type === 'EXPENSE');
    },
    incomes() {
      return this.categories.filter(category => category.type === 'INCOME');
    }
  },

  methods: {
    fetchCategories() {
      axios.get('/tracker/categories')
          .then(response => {
            this.categories = response.data;
            const noDefaultCategory = localStorage.getItem("noDefaultCategory");

            if (this.categories.length === 0 && noDefaultCategory == null) {
              this.showDefaultCategoryModal = true;
            }
          })
          .catch(() => {
            console.log("ERROR! Something wrong happened");
          });
    },

    showAddCategoryModal() {
      this.showCategoryModal = true;
    },
    hideAddCategoryModal() {
      this.showCategoryModal = false
    },
    showDefaultCategoryModal() {
      this.showDefaultCategoryModal = true;
    },
    hideDefaultCategoryModal() {
      this.showDefaultCategoryModal = false;
    },

    addDefaultCategories() {
      axios.post('/tracker/categories/add-default-categories')
          .then(() => {
            console.log("Successfully added default categories");
            window.location.reload();
          })
          .catch(() => {
            console.log("ERROR! Something wrong happened");
          });
    },

    handleDragEnter(categoryId) {
      console.log("category-drag-enter " + categoryId)
      this.$emit('highlight-category', categoryId);
    },

    handleDragLeave(categoryId) {
      console.log("category-drag-Leave " + categoryId)
      this.$emit('unhighlight-category');
    },

    async handleDrop(e, category) {
      e.preventDefault();

      const transactionId = e.dataTransfer.getData("elementId");
      const transactionComment = e.dataTransfer.getData("comment");
      const suggestedCategoryId = e.dataTransfer.getData("suggestedCategoryId");

      const transactionDefined = {
        transactionId: transactionId,
        categoryId: category.id
      };

      await axios.post('/tracker/transactions/define', transactionDefined)
          .then(() => {
            this.showToastMessage(`Transaction "${transactionComment}" defined to category "${category.name}"`);

            this.lastDefinedTransaction = {
              id: transactionId,
              suggestedCategoryId: suggestedCategoryId
            };

            this.$emit('fetch-transaction');
          })
          .catch((error) => {
            console.log(error);
          });
      this.$emit('unhighlight-category');
    },

    showToastMessage(message) {
      this.toastMessage = message;
      this.showToast = true;

      setTimeout(() => {
        this.showToast = false;
      }, 5000);
    },

    async undefineTransaction() {
      try {
        await axios.post('/tracker/transactions/undefine', {
          transactionId: this.lastDefinedTransaction.id,
          categoryId: this.lastDefinedTransaction.categoryId
        }).then(() => {
          this.$emit('fetch-transaction');
        })
        .catch((error) => {
          console.log(error);
        });

        this.showToast = false;
      } catch (error) {
        console.log(error);
      }
    },

    getCategoryStyle(totalCount) {
      const availableHeight = window.innerHeight;
      const height = Math.max(20, Math.min(20, availableHeight / totalCount));
      return {
        height: `${height}px`,
        lineHeight: `${height}px`
      };
    }
  }
}
</script>

<style scoped>
.categories-space {
  width: 30%;
  flex-direction: column;
  padding: 10px;
  border-left: 1px solid #ccc;
  height: 100vh;
  overflow-y: auto;
}

.space-button-add-category {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 10px;
}

.button-add-category {
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  font-size: 24px;
  cursor: pointer;
}

.category-title {
  font-weight: bold;
  text-align: center;
}

.category-tile {
  justify-content: center;
  align-items: center;
  border: 1px solid #ccc;
  border-radius: 8px;
  text-align: center;
  font-size: 14px;
  margin: 5px auto;
  background-color: #f5f1f1;
}

.category-tile.expense {
  border-color: #ff0017;
}

.category-tile.income {
  border-color: #035216;
}

.category-tile.over {
  background-color: #559696; /* Light blue */
}

.toast {
  position: fixed;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  background-color: #333;
  color: white;
  padding: 10px 20px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}
</style>