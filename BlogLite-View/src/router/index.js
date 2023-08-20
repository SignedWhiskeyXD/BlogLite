import { createRouter, createWebHistory } from 'vue-router'

const Layout = () => import('@/layout/index.vue')
const BlogStream = () => import('@/components/BlogStream.vue')
const BlogDetail = () => import('@/components/BlogDetail.vue')

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: Layout,
      children: [
        {
          path: '',
          component: BlogStream
        },
        {
          path: 'blog/:blog_id',
          component: BlogDetail
        }
      ]
    }
  ]
})

export default router
