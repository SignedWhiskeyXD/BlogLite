<script setup lang="ts">
const route = useRoute();

useHead({
    title: () =>
        route.query.searchKeyword
            ? `${route.query.searchKeyword} 的搜索结果 - WhiskeyXD BlogLite`
            : '首页 - WhiskeyXD BlogLite'
})
</script>

<template>
  <div class="blog-banner">
    <p class="banner-about w-mobile-no-display">我手上实在没什么像样的风景图，拿一张玩对马岛之魂的截图糊弄一下算了</p>
    <h1 class="banner-title">WhiskeyXD的个人博客</h1>
  </div>
  <div class="home-page-wrapper">
    <div class="home-page-wrapper-secondary">
      <main class="main-wrapper">
        <BlogStream/>
      </main>
      <aside class="right-side-wrapper w-mobile-no-display">
        <AboutMe class="transition-shadow"/>
        <MainPageTags class="transition-shadow"/>
        <BlogRanking class="transition-shadow"/>
      </aside>
    </div>
  </div>
</template>

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