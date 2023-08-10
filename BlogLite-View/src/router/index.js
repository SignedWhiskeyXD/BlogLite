import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/layout/index.vue'
import BlogStream from "@/components/BlogStream.vue";
import BlogDetail from "@/components/BlogDetail.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/stream',
      component: Layout,
      children: [
        {
          path: '/stream',
          component: BlogStream
        },
        {
          path: '/blog/:blog_id',
          component: BlogDetail
        }
      ]
    }
  ]
})

export default router
