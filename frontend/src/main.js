import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import {library} from "@fortawesome/fontawesome-svg-core";
import {faCog} from "@fortawesome/free-solid-svg-icons";
import {faTelegram} from "@fortawesome/free-brands-svg-icons"
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";

library.add(faTelegram, faCog);

const app = createApp(App)
    .use(store)
    .use(router)
    .component("font-awesome-icon", FontAwesomeIcon)
    .mount('#app')