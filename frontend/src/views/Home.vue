<template>
  <div class="content">
    <div class="row">
      <UndefinedTransactionList
          @highlight-category="highlightCategory"
          @unhighlight-category="unhighlightCategory"
          @fetch-transactions="fetchTransactions"
          ref="transactions"
      />
      <Categories
          :highlightedCategoryId="highlightedCategoryId"
      />
    </div>
  </div>
</template>

<script>

import UndefinedTransactionList from "@/components/UndefinedTransactions.vue";
import Categories from "@/components/Categories.vue";

export default {
  name: 'HomeView',
  components: {
    Categories,
    UndefinedTransactionList,
  },

  data() {
    return {
      highlightedCategoryId: null
    };
  },

  methods: {
    async fetchTransactions() {
      await this.$refs.transactions.getUndefinedTransactionsData(); // TODO fetch transactions
    },

    highlightCategory(categoryId) {
      console.log(categoryId);
      this.highlightedCategoryId = categoryId;
    },
    unhighlightCategory() {
      this.highlightedCategoryId = null;
    }
  }
}
</script>

<style scoped>
body, html {
  height: 100%;
}

.row {
  height: 100%;
  display: flex;
}

.content {
  margin-top: 90px;
}
</style>
