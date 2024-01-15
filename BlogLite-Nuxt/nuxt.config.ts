// https://nuxt.com/docs/api/configuration/nuxt-config
import {createResolver} from "@nuxt/kit";

const resolver = createResolver(import.meta.url);

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
      swr: 60 * 10      // 10 min
    },
    '/': {
      swr: 60           // 1 min
    }
  },
  serverHandlers: [
    {
      middleware: true,
      handler: resolver.resolve('./my-utils/server-middlewares/post-blog-views.ts')
    }
  ]
})
