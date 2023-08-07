<script setup>
import 'element-plus/dist/index.css'
</script>

<template>
    <div class="blog-stream" v-for="blog in blogs" :key="blog.id">
        <div class="blog-item" :style="{boxShadow: `var(--el-box-shadow-dark)`}">
            <div class="blog-title">
                <h2>{{ blog.title }}</h2>
            </div>
            <el-divider class="title-content-divider" border-style="dashed"/>
            <div class="blog-content">
                <p>{{ blog.content }}</p>
            </div>
            <el-divider class="title-content-divider" border-style="dashed"/>
            <div class="blog-tags">
                <el-tag v-for="tag in blog.tagNames" class="blog-tag"
                        :key="tag" size="large">
                    {{ tag }}
                </el-tag>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    created() {
        this.blogs.push({
            id: 1,
            title: '第一篇文章',
            content: '芝士第一篇文章的内容',
            tagNames: [
              '标签1',
              '标签2',
              '标签3'
            ]
        })
        this.blogs.push({
            id: 2,
            title: '第二篇文章',
            content: '芝士第二篇文章的内容',
            tagNames: [
                '标签114514'
            ]
        })
    },
    data() {
        return {
            requestParams: {
                startIndex: 0,
                num: 5
            },
            blogs: []
        }
    }
}
</script>

<style scoped>
.blog-item {
    border: 1px solid var(--el-border-color);
    padding: 20px;
    margin-bottom: 40px;
}

.blog-title {
    text-align: center;
}

.blog-content {
    margin-left: 20px;
}

.title-content-divider {
    scale: 90%;
}

.blog-tag {
    margin-right: 10px;
}
</style>