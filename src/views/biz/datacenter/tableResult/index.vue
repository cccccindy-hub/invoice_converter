<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="Table Name" prop="tableEnName">
        <el-input
            v-model="queryParams.tableName"
            placeholder="Enter Client Name"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="default" @click="handleQuery">Search</el-button>
        <el-button icon="el-icon-refresh" size="default" @click="resetQuery">Reset</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="tableDefinitionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="Table Name" align="center" prop="tableName" />
      <el-table-column label="Sync Status" align="center" prop="tableSyn" :formatter="syncStatusFormatter" />
      <el-table-column label="Remark" align="center" prop="tableRemark" />
      <el-table-column label="Operation" align="center" width="250">
        <template #default="{ row }">
          <el-button size="default" type="text" @click="handleDetail(row)">Detail</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 弹窗表单 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" label-width="150px">
        <el-form-item label="Table Name" prop="tableName">
          <el-input v-model="form.tableName" />
        </el-form-item>
        <el-form-item label="Client Name" prop="tableEnName">
          <el-input v-model="form.tableEnName" />
        </el-form-item>
        <el-form-item label="Remark" prop="tableRemark">
          <el-input v-model="form.tableRemark" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="submitForm">Submit</el-button>
        <el-button @click="dialogVisible = false">Cancel</el-button>
      </template>
    </el-dialog>

  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from "vue";
import { datacenterService } from "@/service/tableDefinition";
import { useRouter } from "vue-router";

const router = useRouter();

const showSearch = ref(true); // 是否显示搜索条件
const loading = ref(false);
const dialogVisible = ref(false);
const dialogTitle = ref("Add Table");
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  tableName: "",
  tableIsgen:""
});
const tableDefinitionList = ref([]);
const total = ref(0);
const single = ref(true);
const multiple = ref(true);
const selectedIds = ref([]);
const form = reactive({
  tableId: null,
  tableName: "",
  tableEnName: "",
  tableRemark: "",
});
const rules = {
  tableName: [{ required: true, message: "Table Name is required", trigger: "blur" }],
  tableEnName: [{ required: true, message: "Client Name is required", trigger: "blur" }],
};

// 查询数据列表
const getList = async () => {
  loading.value = true;
  try {
    queryParams.tableIsgen=1;
    const { rows, total: count } = await datacenterService.list(queryParams);
    tableDefinitionList.value = rows;
    total.value = count;
  } finally {
    loading.value = false;
  }
};

// 重置查询条件
const resetQuery = () => {
  queryParams.tableName = "";
  queryParams.pageNum = 1;
  getList();
};

// 新增操作
const handleAdd = () => {
  dialogTitle.value = "Add Table";
  Object.assign(form, { tableId: null, tableName: "", tableEnName: "", tableRemark: "" });
  dialogVisible.value = true;
};

// 打开编辑弹窗
const handleEdit = async (row) => {
  dialogTitle.value = "Edit Table";
  dialogVisible.value = true;
  try {
    const data = await datacenterService.get(row.tableId);
    console.log("Fetched data:", data); // 检查数据是否正确
    Object.assign(form, data.data);
  } catch (error) {
    console.error("Failed to fetch table details:", error);
  }
};

// 查询操作
const handleQuery = () => {
  queryParams.pageNum = 1;
  getList();
};

// 提交表单
const submitForm = async () => {
  if (form.tableId) {
    await datacenterService.edit(form);
  } else {
    await datacenterService.add(form);
  }
  dialogVisible.value = false;
  getList();
};

// 删除操作
const handleDelete = async () => {
  await Promise.all(selectedIds.value.map((id) => datacenterService.del(id)));
  getList();
};

// 同步表
const handleSync = async (row) => {
  await datacenterService.synTable({ tableId: row.tableId, tableSyn: 1 });
  getList();
};

// 查看详情
const handleDetail = (row: { tableId: number; tableName: string }) => {
  if (row.tableId) {
    // 跳转到路由
    router.push({
      name: "showTable",
      params: { id: row.tableId, name: row.tableName },
      query: { data: JSON.stringify(row) }, // 将整个 row 对象作为 JSON 字符串传递
    });
  } else {
    console.error("Table ID is missing in the row data");
  }
};
// 处理表格选择变化
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map((item) => item.tableId);
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
};

// 同步状态格式化
const syncStatusFormatter = (row) => (row.tableSyn === 1 ? "Synchronized" : "Not Synchronized");

// 生命周期
onMounted(() => {
  getList();
});
</script>
