import {createRouter, createWebHistory} from "vue-router";
import Hello from "@/components/BlogTagPanel.vue";
import Login from "@/components/Login.vue";
import Failed from "@/components/Failed.vue";
import BlogTagPanel from "@/components/BlogTagPanel.vue";
import BlogListPanel from "@/components/BlogListPanel.vue";
import BlogPanel from "@/components/BlogPanel.vue";
import Layout from '@/layout/index.vue';

const routes = [
    {path: '/hello', component: Hello},
    {path: '/failed', component: Failed},
    {path: '/login', component: Login},

    {
        path: '/admin',
        redirect: '/blog/list',
        component: Layout,
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
            },
            {
                path: '/blog/edit/:blog_id',
                component: BlogPanel
            }
        ]
    },

]

const router = createRouter(
    {
        history: createWebHistory(),
        routes: routes
    }
)

export default router