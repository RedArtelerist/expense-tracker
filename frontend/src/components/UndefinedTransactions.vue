<template>
  <div class="undefined-space">
    <div v-for="transaction in transactions"
         :key="transaction.id"
         :id="transaction.id"
         :data-comment="transaction.comment"
         :data-amount="transaction.amount"
         :data-id="transaction.id"
         :data-suggested-category-id="transaction.suggestedCategoryId"
         :class="['undefined-circle']"
         :style="getCircleStyle(transaction.amount)"
         draggable="true"
         @dragstart="handleDragStart($event, transaction)"
         @dragend="handleDragEnd">
      <div class="transaction-info">
        <span class="text">{{ transaction.comment }}</span>
        <span class="amount">{{ transaction.amount }}</span>
      </div>
    </div>
  </div>
</template>

<script>
import axios from '../axios-config';

export default {
  name: 'Transactions',

  data() {
    return {
      transactions: [],
      maxSingleTransactionAmount: 0,
      timer: null,
      intervalId: null,
      getNewUndefinedTransactions: false,
      fibonacciCounter: this.fibonacci(),
      timeout: 5000, // Default value, replace with the value from your server
      minUndefinedCircleSize: 100,
      maxUndefinedCircleSize: 150
    };
  },
  created() {
    this.getUndefinedTransactionsData();
    this.intervalId = setInterval(this.checkUserIsActive, this.timeout / 2);
    this.idleLongPolling();
  },
  beforeUnmount() { // For Vue 3
    this.clearTimers();
  },
  methods: {
    fibonacci() {
      let fibonacciInner = [1, 1];
      return {
        getNext() {
          let lastNumber = fibonacciInner[1];
          fibonacciInner[1] = fibonacciInner[0] + fibonacciInner[1];
          fibonacciInner[0] = lastNumber;
          return fibonacciInner[1];
        },
        reset() {
          fibonacciInner = [1, 1];
        }
      };
    },

    async getUndefinedTransactionsData() {
      try {
        console.log("call transactions");
        const response = await axios.get('/tracker/transactions');
        const data = response.data;

        data.forEach(transaction => {
          if (transaction.amount > this.maxSingleTransactionAmount) {
            this.maxSingleTransactionAmount = transaction.amount;
          }
        });

        this.transactions = data.map(transaction => ({
          id: transaction.id,
          comment: transaction.message,
          amount: transaction.amount,
          suggestedCategoryId: transaction.suggestedCategoryId
        }));

        this.getNewUndefinedTransactions = false;
      } catch (error) {
        await this.registerAccount();
      }
    },

    async registerAccount() {
      try {
        await axios.get('/tracker/accounts/register/single');

        alert('Account was successfully registered');
        window.location.reload();
      } catch (error) {
        console.error(error);
      }
    },

    getCircleStyle(amount) {
      let size = (amount / this.maxSingleTransactionAmount) * this.maxUndefinedCircleSize;
      let width = 0;
      let lineHeight = 0;

      if (size < this.minUndefinedCircleSize) {
        width = this.minUndefinedCircleSize * 1.5;
        lineHeight = this.minUndefinedCircleSize / 3;
      } else {
        width = size * 1.5;
        lineHeight = size / 3;
      }

      return {
        width: `${width}px`,
        lineHeight: `${lineHeight}px`
      };
    },

    handleDragStart(e, transaction) {
      e.target.style.opacity = 0.4;

      e.dataTransfer.setData("elementId", transaction.id);
      e.dataTransfer.setData("amount", transaction.amount);
      e.dataTransfer.setData("comment", transaction.comment);
      e.dataTransfer.setData("suggestedCategoryId", transaction.suggestedCategoryId);

      const suggestedCategoryId = transaction.suggestedCategoryId;
      if (suggestedCategoryId !== null) {
        console.log("Transaction drag enter " + suggestedCategoryId);
        this.$emit('highlight-category', suggestedCategoryId);
      }
    },
    handleDragEnd(e) {
      e.target.style.opacity = 1.0;

      const suggestedCategoryId = e.target.dataset.suggestedCategoryId;
      if (suggestedCategoryId !== null) {
        console.log("Transaction drag end " + suggestedCategoryId);
        this.$emit('unhighlight-category', suggestedCategoryId);
      }
    },

    idleLongPolling() {
      if (!this.getNewUndefinedTransactions) {
        clearTimeout(this.timer);
        const timeDelay = this.fibonacciCounter.getNext() * 1000;
        this.timer = setTimeout(this.getUndefinedTransactionsData, timeDelay);
        this.getNewUndefinedTransactions = true;
      }
    },

    checkUserIsActive() {
      // Logic to check if the user is active
      this.idleLongPolling();
    },

    clearTimers() {
      if (this.timer) {
        clearTimeout(this.timer);
      }
      if (this.intervalId) {
        clearInterval(this.intervalId);
      }
      this.getNewUndefinedTransactions = false;
    }
  }
}
</script>

<style scoped>
.undefined-space {
  display: inline-block;
  width: 70%;
  height: 100%;
}

.undefined-circle {
  display: inline-block;
  text-align: center;
  border-radius: 50%;
  background: #f5f2f2;
  cursor: move;
  border: 2px solid #868686;
  margin: 10px;
  padding: 10px;
}

.transaction-info {
  display: flex;
  flex-direction: column;
}

.undefined-circle .amount {
  font-weight: bold;
  margin-top: 5px;
}

.undefined-circle .text {
  font-weight: 500;
}
</style>