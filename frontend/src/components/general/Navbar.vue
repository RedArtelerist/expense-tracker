<template>
  <header id="header">
    <nav class="links" style="--items: 6;">
      <a id="telegram-bot-link" target="_blank">
        <font-awesome-icon :icon="['fab', 'telegram']" />
      </a>
      <router-link to="/">Головна</router-link>
      <router-link to="/history">Історія</router-link>
      <router-link to="/analytics">Аналітика</router-link>
      <router-link to="/micromanagement">Мікроменеджмент</router-link>
      <router-link to="/settings"><font-awesome-icon icon="cog"/></router-link>
      <span class="line"></span>
    </nav>
  </header>
</template>

<script>
import axios from "../../axios-config";

export default {
  mounted() {
    this.fetchTelegramBotName();
  },
  methods: {
    fetchTelegramBotName() {
      axios.get('/bot-login')
          .then(response => {
            let telegramBotName = response.data;
            let telegramLink = `https://t.me/${telegramBotName}`;
            let linkElement = document.getElementById("telegram-bot-link");
            linkElement.setAttribute("href", telegramLink);
            linkElement.addEventListener("click", function (event) {
              event.preventDefault();
              window.open(telegramLink, "_blank");
            });
          })
          .catch(error => {
            console.error('Error fetching Telegram bot name:', error);
          });
    }
  }
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css?family=Inconsolata|Lato:300,400,700');

.content {
  margin-top: 90px;
}

/* Appearance */
.links {
  background-color: #123;
  background-image: linear-gradient(to bottom, #0003, transparent);
  border-bottom: 1px solid #0003;
  box-shadow: 0 0 32px #0003;
  font-size: 2em;
  font-weight: 300;
}

.links > a {
  color: #9ab;
  padding: .75em;
  text-align: center;
  text-decoration: none;
  transition: all .5s;
}

.links > a:hover {
  background: #ffffff06;
  color: #adf;
}

.links > .line {
  background: #68a;
  height: 1px;
  pointer-events: none;
}

/* The Magic */
#header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  margin-bottom: 30px;
  padding-bottom: 30px;
  display: inline-block;
}

.links {
  display: grid;
  grid-template-columns: repeat(var(--items), 1fr);
  position: relative;
}

.links > .line {
  opacity: 0;
  transition: all .5s;
  position: absolute;
  bottom: 0;
  left: var(--left, calc(100% / var(--items) * (var(--index) - 1)));
  width: var(--width, calc(100% / var(--items)));
  --index: 0;
}

.links > a:hover ~ .line {
  opacity: 1;
}

.links > a:nth-of-type(1):hover ~ .line {
  --index: 1;
}

.links > a:nth-of-type(2):hover ~ .line {
  --index: 2;
}

.links > a:nth-of-type(3):hover ~ .line {
  --index: 3;
}

.links > a:nth-of-type(4):hover ~ .line {
  --index: 4;
}

.links > a:nth-of-type(5):hover ~ .line {
  --index: 5;
}

.links > a:nth-of-type(6):hover ~ .line {
  --index: 6;
}

.links > a:nth-of-type(7):hover ~ .line {
  --index: 7;
}

.links > a:nth-of-type(8):hover ~ .line {
  --index: 8;
}

.links > a:nth-of-type(9):hover ~ .line {
  --index: 9;
}

.links > a:nth-of-type(10):hover ~ .line {
  --index: 10;
}

.links > a:last-of-type:hover ~ .line {
  --index: var(--items);
}
</style>