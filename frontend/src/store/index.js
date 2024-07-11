import Vuex from 'vuex';
import axios from '../axios-config';

export default new Vuex.Store({
  state: {
    token: localStorage.getItem('token') || '',
  },
  mutations: {
    setToken(state, token) {
      state.token = token;
    },
    clearToken(state) {
      state.token = '';
    }
  },
  actions: {
    login({ commit }, authData) {
      return axios.post('/auth/login', authData)
          .then(response => {
            const token = response.data.accessToken;
            localStorage.setItem('token', token);
            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
            commit('setToken', token);
          });
    },
    logout({ commit }) {
      commit('clearToken');
      localStorage.removeItem('token');
      delete axios.defaults.headers.common['Authorization'];
    }
  },
  getters: {
    isAuthenticated: state => !!state.token,
  }
});