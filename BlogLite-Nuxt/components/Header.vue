<script setup lang="ts">
const hideMobileMenu = true;
const menuDefaultActive = '/';
const menuInfo = [
    {
        title: '主页',
        path: '/'
    },
    {
        title: '专栏',
        path: '/collection'
    }
];

const searchInput = ref('');

const routeToSearchResult = () => {};

</script>

<template>
  <div class="blog-header" :class="{'blog-header-mobile': !hideMobileMenu}">
    <div class="blog-title">
      <a class="blog-title-text" href="https://github.com/SignedWhiskeyXD/BlogLite" target="_blank">
        BlogLite
      </a>
    </div>
    <el-menu :mode="hideMobileMenu ? 'horizontal' : 'vertical'" background-color="#545c64" text-color="#fff" active-text-color="#ffd04b"
             :default-active="menuDefaultActive" :router="true" class="header-menu"
             :class="{'w-mobile-no-display': hideMobileMenu}">
      <el-menu-item class="menu-item" v-for="menuItem in menuInfo" :index="menuItem.path">
        {{ menuItem.title }}
      </el-menu-item>
    </el-menu>
    <div class="search-bar" :class="{'w-mobile-no-display': hideMobileMenu}">
      <el-input class="search-bar-input" v-model="searchInput" :placeholder="'搜索本站内容'"/>
      <el-button class="search-bar-button" @click="routeToSearchResult">搜索</el-button>
<!--      <el-dropdown class="search-bar-dropdown">
        <el-icon style="color: white">
          <setting/>
        </el-icon>
        <template #dropdown>
          <el-dropdown-item v-if="loginUser" @click="handleLogout">登出</el-dropdown-item>
          <el-dropdown-item v-else @click="() => showLoginDialog = true">管理员登录</el-dropdown-item>
        </template>
      </el-dropdown>-->
    </div>
<!--    <div class="w-mobile-show">
      <el-icon class="mobile-menu" @click="handleMobileMenuClicked">
        <Fold v-if="hideMobileMenu"/>
        <Expand v-else/>
      </el-icon>
    </div>-->
  </div>
</template>

<style scoped>
.blog-title {
    padding: 0 3% 0;
    width: fit-content;
}

.blog-title-text {
    align-items: center;
    font-size: 26px;
    color: #eee;
    margin: 10px;
    display: flex;
    justify-content: flex-start;
    text-decoration: none;
}

.blog-header {
    background-color: #545c64;
    display: flex;
    justify-content: space-between;
}

.blog-header-mobile {
    flex-direction: column;
}

.header-menu {
    width: 800px;
}

.menu-item {
    min-width: 80px;
}

.search-bar {
    display: flex;
    padding-right: 1%;
    width: 400px;
}

.search-bar-input {
    align-self: center;
}

.search-bar-button {
    align-self: center;
}

.mobile-menu {
    color: white;
    scale: 200%;
    position: fixed;
    right: 20px;
    top: 20px;
}

.search-bar-dropdown {
    margin-left: 3px;
    display: flex;
    align-items: center;
}
</style>