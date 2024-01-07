// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  devtools: { enabled: true },
  modules: ['@element-plus/nuxt'],
  routeRules: {
    '/blog/**': {
      swr: 60
    },
    '/': {
      prerender: true
    }
  }
})
