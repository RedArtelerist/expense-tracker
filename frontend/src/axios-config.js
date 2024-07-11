import axios from 'axios';
import store from './store';
import router from './router';

// Create an instance of axios with a base URL
const axiosInstance = axios.create({
    baseURL: process.env.VUE_APP_API_GATEWAY
});

axiosInstance.interceptors.request.use(
    config => {
        const token = store.state.token;
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

// Add a response interceptor to handle 401 errors
axiosInstance.interceptors.response.use(
    response => response,
    error => {
        if (error.response && error.response.status === 401) {
            store.dispatch('logout');
            router.push('/login');
        }
        return Promise.reject(error);
    }
);


export default axiosInstance;