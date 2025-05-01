<template>
  <!-- 外层表格列 -->
  <el-table-column
      v-for="column in columns"
      :key="column.columnDbname"
      :label="column.columnName"
      :prop="column.columnDbname">
    <!-- 如果有子列，递归渲染 multi-header 组件 -->
    <template v-if="column.childList && column.childList.length > 0">
      <multi-header :columns="column.childList" :level="level + 1"/>
    </template>
  </el-table-column>

</template>

<script setup lang="ts">
import {onMounted} from "vue";

const props = defineProps({
  columns: {
    type: Array,
    required: true
  },
  level: {
    type: Number,
    required: true
  }
})

function getLevel() {
  return props.level + 1;
}

onMounted(() => {
  console.log("columns:", props.columns, props.level)
})
</script>
<style scoped lang="scss">

</style>