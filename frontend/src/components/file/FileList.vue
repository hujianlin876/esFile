<template>
  <el-table :data="files" style="width: 100%">
    <el-table-column prop="name" label="文件名" />
    <el-table-column prop="size" label="大小" />
    <el-table-column prop="type" label="类型" />
    <el-table-column prop="createdAt" label="上传时间" />
    <el-table-column label="操作">
      <template #default="scope">
        <el-button size="small" @click="onPreview(scope.row)">预览</el-button>
        <el-button size="small" type="danger" @click="onDelete(scope.row)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>
<script setup lang="ts">
import { ref } from 'vue';
const files = ref([]);
const onPreview = (file: any) => {
  // TODO: 文件预览逻辑
};
const onDelete = (file: any) => {
  // TODO: 删除文件逻辑
};
</script>
<style scoped>
</style>
