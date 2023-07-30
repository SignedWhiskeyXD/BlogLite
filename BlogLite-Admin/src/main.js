// import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import Element from 'element-plus'
import 'element-plus/dist/index.css'

import router from "./router";


createApp(App).use(Element).use(router).mount('#app')