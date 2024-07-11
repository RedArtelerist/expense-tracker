<template>
  <Navbar v-if="!isLoginPage" />
  <router-view/>
</template>

<script>
  import Navbar from './components/general/Navbar.vue';

  export default {
    components: {
      Navbar
    },
    computed: {
      isLoginPage() {
        return this.$route.path === '/login';
      }
    }
  };
</script>
