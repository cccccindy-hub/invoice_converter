<template>
  <el-dialog
      v-model="dialogFormVisible"
      destroy-on-close
      :before-close="beforeClose"
      :close-on-click-modal="false"
      class="auto-width-dialog">
    <template #header>
      {{ title }}
    </template>
    <el-form :model="dataForm" label-width="200px">
      <el-form-item label="ID" v-show="false">
        <el-input v-model="dataForm.id"/>
      </el-form-item>

      <el-form-item
          v-for="column in editConfig()"
          :label="column.columnName + '：'">
        <el-select
            clearable
            v-if="column.columnDbtype === 0 && column.columnInputMethod === 1"
            v-model="dataForm[column.columnDbname]">
          <el-option
              v-for="(item, index) in dictsMap[column.columnDictid]"
              :key="index"
              :label="item.dictLabel"
              :value="item.columnDictchosetype === 1 ? item.dictValue : item.dictLabel"
          />
        </el-select>
        <el-input-number v-else-if="column.columnDbtype === 1" v-model="dataForm[column.columnDbname]"
                         class="column-input-number"/>
        <el-date-picker
            v-model="dataForm[column.columnDbname]"
            v-else-if="column.columnDbtype === 2"
            placeholder="Pick a Date"
            :type="getDateType(column)"
            value-format="YYYY-MM-DD HH:mm:ss"
            :format="column.columnFormate"/>
        <el-input v-else v-model="dataForm[column.columnDbname]" clearable class="column-input"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="footer">
        <el-button @click="beforeClose(false)">取 消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">

import {TableResultService} from "@/service";
import {onMounted, ref} from "vue";
import {ElMessage} from "element-plus";

const props = defineProps({
  tableId: {
    required: true,
    type: Number
  },
  columnId: {
    required: false,
    type: Number
  },
  title: {
    required: true,
    type: String
  }
});
const emits = defineEmits(["close", "success"]);
const tableColumns = ref([]);
const dictsMap = ref();
const dataForm = ref({});
const dialogFormVisible = ref(true);

onMounted(async () => {
  try {
    //配置
    await tableConfig();
    if (props.columnId) {
      //数据
      await getResultDetail()
    }
  } catch (e) {
    ElMessage.error(e?.message);
    beforeClose();
  }
})

async function tableConfig() {
  const {configs, dictMap} = (await TableResultService.getTableConfigData(props.tableId)).data;
  tableColumns.value = configs;
  dictsMap.value = dictMap;
}

async function getResultDetail() {
  dataForm.value = (await TableResultService.getResultDetail(props.tableId, props.columnId)).data;
}

// 提交表单
async function submitForm() {
  let form = dataForm.value;
  try {
    if (form.id) {
      // 编辑
      await TableResultService.editTableData(form, props.tableId, form.id)
      ElMessage.success("Edit success")
    } else {
      // 新增
      await TableResultService.addTableData(form, props.tableId);
      ElMessage.success("Add success")
    }
    beforeClose(true);
  } catch (error) {
    ElMessage.error("Failed to submit form:" + error?.message);
  }
}

function editConfig() {
  return tableColumns.value.filter(item => item.columnType === 0);
}

function getDateType(config) {
  let dateFormater = config.columnFormate;
  let dateType;
  if (!dateFormater) {
    dateType = 'datetime';
  } else if (dateFormater.includes("HH")) {
    dateType = 'datetime';
  } else if (dateFormater.includes("DD")) {
    dateType = 'date';
  } else if (dateFormater.includes('MM')) {
    dateType = 'month';
  } else if (dateFormater.includes('YYYY')) {
    dateType = 'year';
  } else {
    dateType = 'datetime';
  }
  return dateType;
}

function beforeClose(success: false) {
  dialogFormVisible.value = false;
  emits(success ? "success" : "close")
}
</script>

<style scoped lang="scss">
.auto-width-dialog {
  display: flex;
  width: 600px;
  max-height: 90%;
  overflow: auto;
}

.footer {
  width: 100%;
  margin-top: auto; /* 将内容推到底部 */
  display: flex; /* 如果按钮需要水平排列 */
  justify-content: center; /* 如果按钮需要右对齐 */
}

.column-input {
  width: 400px;
}

.column-input-number {
  width: 200px;
}
</style>