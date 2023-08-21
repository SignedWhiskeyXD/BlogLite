<script setup>
import AboutMe from "@/components/AboutMe.vue";
import MainPageTags from "@/components/MainPageTags.vue";
</script>

<template>
  <div class="home-page-wrapper">
    <aside class="left-side-wrapper">
      <AboutMe/>
    </aside>
    <main class="main-wrapper">
      <router-view v-slot="{ Component }">
        <keep-alive include="BlogStream">
          <component :is="Component"/>
        </keep-alive>
      </router-view>
    </main>
    <aside class="right-side-wrapper">
      <main-page-tags/>
    </aside>
  </div>
</template>

<style scoped>
.home-page-wrapper{
    display: flex;
    justify-content: center;
    flex-direction: row;
    min-width: 1100px;
    min-height: 95vh;
}

@media screen and (max-width: 1100px) {
  .home-page-wrapper{
      justify-content: flex-start;

  }
}

.left-side-wrapper {
    width: 250px;
    position: relative;
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
    .left-side-wrapper {
        width: 250px;
    }

    .main-wrapper {
        width: 600px;
    }

    .right-side-wrapper {
        width: 250px;
    }
}

@media screen and (min-width: 1600px){
  .left-side-wrapper {
      width: 300px;
  }

  .main-wrapper {
      width: 1000px;
  }

  .right-side-wrapper {
      width: 300px;
  }
}



</style>