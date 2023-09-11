import { createApp } from 'vue'
import App from './App.vue'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import router from "./router";

createApp(App)
    .use(router)
    .use(mavonEditor)
    .mount('#app')