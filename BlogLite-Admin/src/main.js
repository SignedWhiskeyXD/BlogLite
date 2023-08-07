import { createApp } from 'vue'
import App from './App.vue'
import Element from 'element-plus'
import mavonEditor from 'mavon-editor'

import 'element-plus/dist/index.css'
import 'mavon-editor/dist/css/index.css'

import router from "./router";


createApp(App)
    .use(Element)
    .use(router)
    .use(mavonEditor)
    .mount('#app')