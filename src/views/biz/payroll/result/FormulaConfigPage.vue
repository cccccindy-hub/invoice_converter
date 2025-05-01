<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="default" :inline="true">
      <el-form-item label="Table Name" prop="tableEnName">
        <el-input
            v-model="queryParams.tableName"
            placeholder="Enter Table Name"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="default" @click="handleQuery">Search</el-button>
        <el-button size="default" @click="resetQuery">Reset</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain size="default" @click="handleAdd">Add</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="otherTableList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="Table Name" align="center" prop="formulaTableName" >
        <template #default="{ row }">
          <span @click="handleView(row)" class="link-style">{{ row.formulaTableName}}</span>
        </template>
      </el-table-column>
      <el-table-column label="Remark" align="center" prop="remark" />
      <el-table-column label="Operation" align="center" width="250">
        <template #default="{ row }">
          <el-button size="default" type="text" @click="handleEdit(row)">Edit</el-button>
          <el-button size="default" type="text" @click="handleConfig(row)">Config</el-button>
<!--          <el-button size="default" type="text" @click="handleView(row)">View</el-button>-->
          <el-button size="default" type="text" @click="handleDelete(row)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />


    <!-- 弹窗表单 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" label-width="150px">
        <el-form-item label="Table Name" prop="tableName">
          <el-input v-model="form.formulaTableName" />
        </el-form-item>
        <el-form-item label="Remark" prop="tableRemark">
          <el-input v-model="form.remark" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="submitForm">Submit</el-button>
        <el-button @click="dialogVisible = false">Cancel</el-button>
      </template>
    </el-dialog>

<!--    <el-form :model="form" ref="formRef" label-width="150px">-->
<!--      <el-form-item label="Field Name" prop="fieldName">-->
<!--        <el-input v-model="form.fieldName" placeholder="Enter Field Name" />-->
<!--      </el-form-item>-->

<!--      <el-form-item label="Formula" prop="formula">-->
<!--        <el-input v-model="form.formula" placeholder="Enter Formula" />-->
<!--      </el-form-item>-->

<!--      <el-form-item>-->
<!--        <el-button type="primary" @click="submitFormula">Submit</el-button>-->
<!--        <el-button @click="goBack">Cancel</el-button>-->
<!--      </el-form-item>-->
<!--    </el-form>-->
  </div>
</template>

<script lang="ts" setup>
import {onMounted, reactive, ref} from "vue";
import { useRoute,useRouter } from "vue-router";
import { payrollConfigService } from "@/service/payroll/config";
import {ElMessage, ElMessageBox} from "element-plus";

const route = useRoute();
const router= useRouter();
const tableId=route.params.id;

const total = ref(0);

const dialogVisible = ref(false);
const dialogTitle = ref("Add Table");
const loading = ref(true);
const otherTableList = ref([]);
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  formulaTableName:"",
  tableId:""
});

const form = reactive({
  id:"",
  tableId:"",
  tableName:"",
  formulaTableName:"",
  remark:""
});

// 查询操作
const handleQuery = () => {
  queryParams.pageNum = 1;
  getList();
};

// 新增
const handleAdd = () => {
  console.log("1111111111111111111")
  dialogTitle.value = "Add Table";
  dialogVisible.value = true;
  console.log("dialogVisible:", dialogVisible.value); // 应打印 true
};

// 打开编辑弹窗
const handleEdit = async (row) => {
  dialogTitle.value = "Edit Table";
  dialogVisible.value = true;
  try {
    const data = await payrollConfigService.get(row.id);
    console.log("Fetched data:", data); // 检查数据是否正确
    Object.assign(form, data.data);
  } catch (error) {
    console.error("Failed to fetch table details:", error);
  }
};

// 提交表单
const submitForm = async () => {
  form.tableId=tableId;
  if (form.id) {
    await payrollConfigService.edit(form);
  } else {
    console.log(form,8888888888888888888888)
    await payrollConfigService.add(form);
  }
  dialogVisible.value = false;
  getList();
};

// 查询数据列表
const getList = async () => {
  loading.value = true;
  try {
    queryParams.tableId=tableId;
    const { rows, total: count } = await payrollConfigService.list(queryParams);
    otherTableList.value = rows;
    total.value = count;
  } finally {
    loading.value = false;
  }
};

// 查看详情
const handleConfig = (row: { tableId:number,id: number}) => {
  if (row.tableId) {
    // 跳转到路由
    router.push({
      name: "formulaConfigDetail",
      params: {tableId :row.tableId, otherTableId: row.id},
    });
  } else {
    console.error("ID is missing in the row data");
  }
};

// 查看详情
const handleView = (row: { id: number}) => {
  if (row.id) {
    // 跳转到路由
    router.push({
      name: "otherTableView",
      params: {otherTableId: row.id},
    });
  } else {
    console.error("ID is missing in the row data");
  }
};


// 删除
const handleDelete = async (row) => {
  try {
    // 显示确认删除的提示框
    const isConfirmed = await ElMessageBox.confirm(
        `Are you sure you want to delete ${row.formulaTableName}?`,
        'Warning',
        {
          confirmButtonText: 'Yes',
          cancelButtonText: 'No',
          type: 'warning',
        }
    );

    // 如果用户点击了确认，执行删除操作
    if (isConfirmed === 'confirm') {
      await payrollConfigService.del(row.id); // 调用删除接口
      getList(); // 刷新列表
      ElMessage.success(`${row.formulaTableName} deleted successfully!`); // 显示成功提示
    }
  } catch (error) {
    console.error("Error deleting table definition:", error);
    ElMessage.error("Failed to delete item."); // 显示错误提示
  }
};


// 生命周期
onMounted(() => {
  getList();
});
</script>
