import { createRouter, createWebHistory } from 'vue-router'

const HomePage = () => import('@/layout/HomePage.vue')

const BlogStream = () => import('@/components/BlogStream.vue')
const BlogDetail = () => import('@/components/BlogDetail.vue')
const BlogCollection = () => import('@/components/BlogCollection.vue')
const Login = () => import('@/components/Login.vue')

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
          props: (route) => {
            let queryParams = {};
            queryParams.collectionID = parseInt(route.query.collectionID);
            queryParams.collectionName = route.query.collectionName;
            queryParams.searchKeyword = route.query.search;

            return {queryParams}
          }
        }
      ]
    },
    {
      path: '/blog/:blog_id',
      component: BlogDetail,
      props: true
    },
    {
      path: '/collection',
      component: BlogCollection
    },
    {
      path: '/login',
      component: Login
    }
  ]
})

export default router
