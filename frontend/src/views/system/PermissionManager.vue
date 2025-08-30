<template>
  <div class="permission-manager">
    <BaseTable :data="permissions">
      <el-table-column prop="name" label="权限名称" />
      <el-table-column prop="code" label="权限编码" />
      <el-table-column prop="desc" label="描述" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button size="small" @click="onEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="onDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </BaseTable>
    <BaseModal v-model:visible="modalVisible" title="编辑权限">
      <!-- 权限编辑表单 -->
      <BaseForm :model="editPerm">
        <el-form-item label="权限名称">
          <el-input v-model="editPerm.name" />
        </el-form-item>
        <el-form-item label="权限编码">
          <el-input v-model="editPerm.code" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="editPerm.desc" />
        </el-form-item>
      </BaseForm>
      <template #footer>
        <el-button @click="modalVisible = false">取消</el-button>
        <el-button type="primary" @click="onSave">保存</el-button>
      </template>
    </BaseModal>
  </div>
</template>
<script setup lang="ts">
import { ref } from 'vue';
import BaseTable from '@/components/table/BaseTable.vue';
import BaseModal from '@/components/modal/BaseModal.vue';
import BaseForm from '@/components/form/BaseForm.vue';
const permissions = ref([
  { name: '文件管理', code: 'file_manage', desc: '管理文件的权限' },
  { name: '用户管理', code: 'user_manage', desc: '管理用户的权限' }
]);
const modalVisible = ref(false);
const editPerm = ref({ name: '', code: '', desc: '' });
const onEdit = (perm: any) => {
  editPerm.value = { ...perm };
  modalVisible.value = true;
};
import { ElMessage } from 'element-plus';
// TODO: 引入权限相关API
const onDelete = async (perm: any) => {
  try {
    // await deletePermission(perm.id);
    ElMessage.success('删除成功');
    // TODO: 刷新权限列表
  } catch (e) {
    ElMessage.error('删除失败');
  }
};
const onSave = async () => {
  try {
    // await addPermission(editPerm.value);
    ElMessage.success('保存成功');
    modalVisible.value = false;
    // TODO: 刷新权限列表
  } catch (e) {
    ElMessage.error('保存失败');
  }
};
</script>
<style scoped>
.permission-manager {
  padding: 24px;
}
</style>
