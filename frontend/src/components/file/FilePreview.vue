<template>
  <div class="file-preview">
    <el-dialog :visible.sync="visible" title="文件预览">
      <div v-if="file.type === 'PDF'">
        <!-- PDF预览区域 -->
        <iframe :src="file.url" width="100%" height="600px"></iframe>
      </div>
      <div v-else-if="file.type === 'IMAGE'">
        <img :src="file.url" alt="图片预览" style="max-width:100%;max-height:600px;" />
      </div>
      <div v-else>
        <span>暂不支持该类型预览</span>
      </div>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref } from 'vue';
const visible = ref(false);
const file = ref({ type: '', url: '' });
// TODO: 预览逻辑
</script>
<style scoped>
.file-preview {
}
</style>
