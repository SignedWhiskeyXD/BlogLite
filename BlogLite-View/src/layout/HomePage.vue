<script setup>
import AboutMe from "@/components/AboutMe.vue";
import MainPageTags from "@/components/MainPageTags.vue";
import BlogRanking from "@/components/BlogRanking.vue";
</script>

<template>
  <el-scrollbar ref="scrollbarRef" always @scroll="onScroll" max-height="95vh">
    <div class="home-page-wrapper">
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
    flex-direction: row;
    min-width: 1100px;
    min-height: 95vh;
}

.transition-shadow {
    box-shadow: 0 1px 2px rgba(0,0,0,0.15);
    transition: box-shadow 0.3s ease-in-out;
}

.transition-shadow:hover {
    box-shadow: 0 5px 15px rgba(0,0,0,0.3);
}

@media screen and (max-width: 1100px) {
  .home-page-wrapper{
      justify-content: flex-start;

  }
}

.main-wrapper {
    width: 600px;
    position: relative;
}

.right-side-wrapper {
    width: 250px;
    position: relative;
}

@media screen and (min-width: 1100px) {
    .main-wrapper {
        width: 750px;
    }

    .right-side-wrapper {
        width: 350px;
    }
}

@media screen and (min-width: 1500px){
  .main-wrapper {
      width: 1100px;
  }

  .right-side-wrapper {
      width: 400px;
  }
}
</style>