<template>
  <el-dialog :visible.sync="visible" :title="title">
    <slot />
  </el-dialog>
</template>
<script setup lang="ts">
import { ref } from 'vue';
const visible = ref(false);
const title = ref('');
</script>
<style scoped>
</style>
