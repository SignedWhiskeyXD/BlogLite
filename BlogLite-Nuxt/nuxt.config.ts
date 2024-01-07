// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  devtools: { enabled: true },
  modules: ['@element-plus/nuxt'],
  css: ['~/assets/my-typo.css'],
  routeRules: {
    '/blog/**': {
      swr: 60,
    },
    '/': {
      swr: 60
    }
  }
})
