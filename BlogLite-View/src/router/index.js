import { createRouter, createWebHistory } from 'vue-router'

const HomePage = () => import('@/layout/HomePage.vue')

const BlogStream = () => import('@/components/BlogStream.vue')
const BlogDetail = () => import('@/components/BlogDetail.vue')
const BlogCollection = () => import('@/components/BlogCollection.vue')

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: HomePage,
      children: [
        {
          path: '',
          component: BlogStream,
        },
        {
          path: 'blog/:blog_id',
          component: BlogDetail
        }
      ]
    },
    {
      path: '/collection',
      component: BlogCollection
    }
  ]
})

export default router
