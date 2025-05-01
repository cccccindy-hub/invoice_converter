<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :model="queryParams" ref="queryForm" size="default" :inline="true" v-show="showSearch">
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

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="tableDefinitionList" @selection-change="handleSelectionChange">
<!--      <el-table-column type="selection" width="55" align="center"/>-->
      <el-table-column label="Table Name" align="center" prop="tableName" width="300" show-overflow-tooltip/>
<!--      <el-table-column label="Sync Status" align="center" prop="tableSyn" :formatter="syncStatusFormatter"/>-->
      <el-table-column label="Remark" align="center" prop="tableRemark" width="200" show-overflow-tooltip/>
      <el-table-column label="Operation" align="center" width="250">
        <template #default="{ row }">
          <el-button size="default" link @click="handleDetail(row)">Detail</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination v-show="count > 0" :total="count" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 弹窗表单 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" label-width="150px">
        <el-form-item label="Table Name" prop="tableName">
          <el-input v-model="form.tableName"/>
        </el-form-item>
        <el-form-item label="Table enName" prop="tableEnName">
          <el-input v-model="form.tableEnName"/>
        </el-form-item>
        <!-- <el-form-item label="Role Type" prop="roleType">
          <el-select v-model="form.roleType" placeholder="Select Role Type">
            <el-option
                v-for="option in tableTypeOptions"
                :key="option.dictValue"
                :label="option.dictLabel"
                :value="option.dictValue"
            />
          </el-select>
        </el-form-item> -->
        <el-form-item label="Remark" prop="tableRemark">
          <el-input v-model="form.tableRemark"/>
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
import {onMounted, reactive, ref} from "vue";
import {datacenterService} from "@/service/tableDefinition";
import {useRouter} from "vue-router";
import {financeService} from "@/service/financeService";
import {ElMessageBox} from "element-plus";

const router = useRouter();
const props = defineProps({
  dcType: {
    type: Number,
    required: true
  }
})

const showSearch = ref(true); // 是否显示搜索条件
const loading = ref(false);
const dialogVisible = ref(false);
const dialogTitle = ref("Add Table");
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  tableType: null,
  tableName: "",
});
const tableDefinitionList = ref([]);
const count = ref(0);
const single = ref(true);
const multiple = ref(true);
const selectedIds = ref([]);
const form = reactive({
  tableId: null,
  tableName: "",
  tableType: null,
  tableEnName: "",
  tableRemark: "",
});
const rules = {
  tableName: [{required: true, message: "Table Name is required", trigger: "blur"}],
  tableEnName: [{required: true, message: "Client Name is required", trigger: "blur"}],
};

// 查询数据列表
const getList = async () => {
  loading.value = true;
  queryParams.tableType = props.dcType;
  try {
    const {rows, total} = await datacenterService.list(queryParams);
    tableDefinitionList.value = rows;
    count.value = total;
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
  Object.assign(form, {tableId: null, tableName: "", tableEnName: "", tableRemark: ""});
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
    form.tableType = props.dcType;
    await datacenterService.add(form);
  }
  dialogVisible.value = false;
  getList();
};

// 删除操作
const handleDelete = async () => {
  ElMessageBox.confirm(
      'Are you sure you want to delete it?',
      'Warning',
      {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  )
      .then(async () => {
        await Promise.all(selectedIds.value.map((id) => datacenterService.del(id)));
        ElMessage({
          type: 'success',
          message: 'Delete completed',
        })
        getList();
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: 'Delete canceled',
        })
      })
};

// 查看详情
const handleDetail = (row: { tableId: number; tableName: string }) => {
  if (row.tableId) {
    // 跳转到路由
    router.push({
      name: "showTable",
      params: {id: row.tableId, name: row.tableName}
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
