import {createRouter, createWebHistory} from 'vue-router';
import store from '../store';
import Login from '../views/Login.vue';
import Home from '../views/Home.vue'; // Assuming you have a Home component
import NotFound from '../views/PageNotFound.vue'; // Assuming you have a Home component

const routes = [
    {
        path: '/',
        name: 'home',
        component: Home,
        meta: {requiresAuth: true}
    },
    {
        path: '/login',
        name: 'login',
        component: Login,
        meta: {guest: true}
    },
    {
        path: '/:pathMatch(.*)*',
        component: NotFound
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
});


router.beforeEach((to, from, next) => {
    if (to.matched.some(record => record.meta.requiresAuth)) {
        if (!store.getters.isAuthenticated) {
            next('/login');
        } else {
            next();
        }
    } else if (to.matched.some(record => record.meta.guest)) {
        if (store.getters.isAuthenticated) {
            next('/');
        } else {
            next();
        }
    } else {
        next();
    }
});

export default router;
