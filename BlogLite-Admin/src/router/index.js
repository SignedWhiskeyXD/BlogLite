import {createRouter, createWebHistory} from "vue-router";
import Hello from "@/components/BlogTagPanel.vue";
import Login from "@/components/Login.vue";
import Failed from "@/components/Failed.vue";
import AdminPanel from "@/components/AdminPanel.vue";
import BlogTagPanel from "@/components/BlogTagPanel.vue";
import BlogListPanel from "@/components/BlogListPanel.vue";
import BlogPanel from "@/components/BlogPanel.vue";

const routes = [
    {path: '/hello', component: Hello},
    {path: '/failed', component: Failed},
    {path: '/', component: Login},

    {
        path: '/admin',
        redirect: '/tag',
        component: AdminPanel,
        children:[
            {
                path: '/tag',
                component: BlogTagPanel
            },
            {
                path: '/blog/list',
                component: BlogListPanel
            },
            {
                path: '/blog/write',
                component: BlogPanel
            }
        ]
    }
]

const router = createRouter(
    {
        history: createWebHistory(),
        routes: routes
    }
)

export default router