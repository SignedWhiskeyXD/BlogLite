<script setup>
import AboutMe from "@/components/AboutMe.vue";
import MainPageTags from "@/components/MainPageTags.vue";
import BlogRanking from "@/components/BlogRanking.vue";
</script>

<template>
<!--子路由组件，文章流的无限滚动能够正常工作，依赖于父元素容器的高度限制，
    这样才能得知是否滚动到底部，而不能直接平铺在整个DOM树上
    另一个矛盾的问题是，Vue Router能在被缓存的组件之间保留滚动条的位置，但是似乎仅限于BOM的window的滚动条
    对于子元素产生的滚动条，则似乎无法保留滚动状态
    所以，这个组件作为一个路由组件，如果希望保留滚动条的状态，必须在这里自己整一个，这样才有办法在路由更新时，自定义恢复位置的方法-->
  <el-scrollbar ref="scrollbarRef" always @scroll="onScroll">
    <div class="blog-banner">
      <p class="banner-about w-mobile-no-display">我手上实在没什么像样的风景图，拿一张玩对马岛之魂的截图糊弄一下算了</p>
      <h1 class="banner-title">WhiskeyXD的个人博客</h1>
    </div>
    <div class="home-page-wrapper">
      <div class="home-page-wrapper-secondary">
        <main class="main-wrapper">
          <router-view v-slot="{ Component }">
            <keep-alive include="BlogStream">
              <component :is="Component"/>
            </keep-alive>
          </router-view>
        </main>
        <aside class="right-side-wrapper w-mobile-no-display">
          <AboutMe class="transition-shadow"/>
          <main-page-tags class="transition-shadow"/>
          <blog-ranking class="transition-shadow"/>
        </aside>
      </div>
    </div>
  </el-scrollbar>
</template>

<script>
export default {
    activated() {
        const scrollBar = this.$refs.scrollbarRef
        if(this.$route.path === '/')
            scrollBar.setScrollTop(this.scrollBarPos)
    },
    methods: {
        onScroll({ scrollTop }){
            this.scrollBarPos = scrollTop
        }
    },
    data(){
        return {
            scrollBarPos: 0
        }
    }
}
</script>

<style scoped>
.home-page-wrapper{
    display: flex;
    justify-content: center;
}

@media screen and (max-width: 768px) {
    .home-page-wrapper {
        justify-content: flex-start;
    }
}

.home-page-wrapper-secondary {
    display: flex;
    width: 1280px;
}

.transition-shadow {
    box-shadow: 0 1px 2px rgba(0,0,0,0.15);
    transition: box-shadow 0.3s ease-in-out;
}

.transition-shadow:hover {
    box-shadow: 0 5px 15px rgba(0,0,0,0.3);
}

.main-wrapper {
    width: 75%;
    flex-grow: 1;
    min-width: 500px;
}

.right-side-wrapper {
    width: 25%;
}

.blog-banner {
    background-image: url("https://raw.githubusercontent.com/SignedWhiskeyXD/PicGoRepo/master/img/banner.jpg");
    background-position: center center;
    height: 30vh;
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
}

.banner-about {
    position: absolute;
    bottom: 5px;
    right: 5px;
    color: rgba(255, 255, 255, 0.3);
    margin: 0;
}

.banner-title {
    margin: 0;
    color: rgba(255, 255, 255, 0.7);
}
</style>