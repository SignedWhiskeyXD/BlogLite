<script setup>

</script>

<template>
  <h2>已统计的UV: {{ totalUV }}</h2>
</template>

<script>
import {makeRequest} from "@/fetch/FetchTemplate";

export default {
    created() {
        makeRequest('/api/admin/dashboard')
            .then(({body}) => {
                this.totalUV = body.totalUV;
            })
    },
    data() {
        return {
            totalUV: 0
        }
    }
}
</script>

<style scoped>

</style>