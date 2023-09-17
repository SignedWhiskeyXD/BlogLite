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
    <div class="home-page-wrapper">
      <div class="home-page-wrapper-secondary">
        <main class="main-wrapper">
          <router-view v-slot="{ Component }">
            <keep-alive include="BlogStream">
              <component :is="Component"/>
            </keep-alive>
          </router-view>
        </main>
        <aside class="right-side-wrapper">
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
}

.right-side-wrapper {
    width: 25%;
}


</style>