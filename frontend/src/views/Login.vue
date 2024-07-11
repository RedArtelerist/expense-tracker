<template>
  <div id="login">
    <div class="centered-form">
      <div class="form-title">Login with Telegram account</div>
      <div id="login-form"/>
    </div>
  </div>
</template>

<script>
import {mapActions} from 'vuex';
import axios from "../axios-config";

export default {
  data() {
    return {
      telegramBotName: ''
    };
  },
  created() {
    this.fetchTelegramBotName();
  },
  methods: {
    ...mapActions(['login']),
    fetchTelegramBotName() {
      axios.get('/bot-login')
          .then(response => {
            this.telegramBotName = response.data;
            this.loadTelegramWidget();
          })
          .catch(error => {
            console.error('Error fetching token:', error);
          });
    },
    loadTelegramWidget() {
      window.TelegramLoginWidget = {
        dataOnauth: (user) => {
          this.onTelegramAuth(user);
        }
      };
      const script = document.createElement('script');
      script.src = `https://telegram.org/js/telegram-widget.js?2`;
      script.setAttribute('data-telegram-login', this.telegramBotName);
      script.setAttribute('data-size', 'large');
      script.setAttribute('data-onauth', 'TelegramLoginWidget.dataOnauth(user)');
      script.setAttribute('data-request-access', 'write');
      script.setAttribute('data-request-access', 'write');
      script.async = true;
      document.getElementById('login-form').appendChild(script);
    },
    onTelegramAuth(user) {
      this.login(user)
          .then(() => {
            this.$router.push('/');
          })
          .catch(error => {
            console.error('Login failed:', error);
          });
    }
  }
};
</script>

<style scoped>
#login {
  height: 100%;
  margin: 0;
  padding-top: 20%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: Arial, sans-serif;
}

.centered-form {
  width: 300px;
  padding: 20px;
  border-radius: 50px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background-color: white;
  text-align: center;
}

.form-title {
  font-size: 18px;
  color: #0088cc;
  margin-bottom: 20px;
  border-bottom: 2px solid #0088cc;
  padding-bottom: 10px;
  font-weight: 600;
}
</style>
