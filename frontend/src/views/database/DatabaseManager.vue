<template>
  <div class="database-manager">
    <h2>数据库操作</h2>
    <el-form :model="form" class="sql-form">
      <el-form-item label="SQL语句">
        <el-input v-model="form.sql" type="textarea" rows="4" placeholder="请输入SQL语句" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onExecute">执行</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="result" style="width: 100%" v-if="result.length">
      <el-table-column v-for="col in columns" :key="col" :prop="col" :label="col" />
    </el-table>
  </div>
</template>
<script setup lang="ts">
import { ref } from 'vue';
const form = ref({ sql: '' });
const result = ref([]);
const columns = ref([]);
const onExecute = () => {
  // TODO: 调用后端数据库执行接口
  // 示例：result.value = [{id:1,name:'test'}]; columns.value = ['id','name'];
};
</script>
<style scoped>
.database-manager {
  padding: 24px;
}
.sql-form {
  margin-bottom: 24px;
}
</style>
