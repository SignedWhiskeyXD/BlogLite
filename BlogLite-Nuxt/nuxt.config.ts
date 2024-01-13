// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  devtools: { enabled: true },
  modules: ['@element-plus/nuxt'],
  css: ['~/assets/my-typo.css'],
  vite: {
    server: {
      proxy: {
        '/api': 'http://localhost:52480'
      }
    }
  },
  app: {
    head: {
      charset: 'utf-8',
      title: 'WhiskeyXD BlogLite'
    }
  },
  routeRules: {
    '/blog/**': {
      swr: 60,
    },
    '/': {
      swr: 60
    }
  }
})
