import {createRouter, createWebHistory} from "vue-router";
const Hello = () => import("@/components/BlogTagPanel.vue");
const Login = () => import("@/components/Login.vue");
const Failed = () => import("@/components/Failed.vue");
const BlogTagPanel = () => import("@/components/BlogTagPanel.vue");
const BlogListPanel = () => import("@/components/BlogListPanel.vue");
const BlogPanel = () => import("@/components/BlogPanel.vue");
const  Layout = () => import('@/layout/index.vue');
const BlogCollectionPanel = () => import("@/components/BlogCollectionPanel.vue");
const CommentPanel = () => import("@/components/CommentPanel.vue");
const DashBoard = () => import('@/components/DashBoard.vue')


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
                path: '/dashboard',
                component: DashBoard
            },
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
            },
            {
                path: '/collection',
                component: BlogCollectionPanel
            },
            {
                path: '/comment',
                component: CommentPanel
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