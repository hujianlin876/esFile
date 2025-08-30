<template>
  <aside class="app-sidebar">
    <nav>
      <ul>
        <li><router-link to="/files">文件管理</router-link></li>
        <li><router-link to="/users">用户管理</router-link></li>
        <li><router-link to="/permissions">权限管理</router-link></li>
      </ul>
    </nav>
  </aside>
</template>
<script setup lang="ts">
</script>
<style scoped>
.app-sidebar {
  background: #f5f5f5;
  width: 200px;
  height: 100vh;
  padding: 16px 0;
}
.app-sidebar ul {
  list-style: none;
  padding: 0;
}
.app-sidebar li {
  margin: 16px 0;
}
</style>
