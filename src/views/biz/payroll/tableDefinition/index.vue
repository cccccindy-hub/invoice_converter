<template>
  <div class="app-container">
    <!-- 搜索条件表单 -->
    <el-form
        :model="queryParams"
        ref="queryForm"
        size="default"
        :inline="true"
        v-show="showSearch"
    >
      <el-form-item label="Table Name " prop="tableName">
        <el-input
            v-model="queryParams.tableName"
            placeholder="Enter Table Name"
            clearable
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button
            type="primary"
            @click="handleQuery"
        >
          Search
        </el-button>
        <el-button
            @click="resetQuery"
        >
          Reset
        </el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮组 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            size="default"
            @click="handleAdd"
        >
          Add
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            size="default"
            :disabled="single"
            @click="handleUpdate"
        >
          Edit
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            size="default"
            :disabled="multiple"
            @click="handleDelete"
        >
          Delete
        </el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table
        v-loading="loading"
        :data="tableDefinitionList"
        @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="Table Name" align="center" prop="tableName" width="100">
        <template #default="{ row }">
          <span @click="handleTempDetail(row)" class="link-style">{{ row.tableName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Table Name (EN)" align="center" prop="tableEnName" width="130"/>

      <el-table-column
          label="Generation status"
          align="center"
          width="140"
          prop="tableIsgen"
          :formatter="row => row.tableIsgen === 1 ? 'Generated' : 'Not generated'"
      />
      <el-table-column
          label="Sync status"
          align="center"
          prop="tableSyn"
          :formatter="row => row.tableSyn === 1 ? 'Synchronized' : 'Not Synchronized'"
      />
      <el-table-column
          label="Generation time"
          align="center"
          prop="tableLiveTime"
          width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.tableLiveTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column
          label="Status"
          align="center"
          prop="tableStatus"
          :formatter="formatStatus"
      />
      <el-table-column label="Remark" align="center" prop="tableRemark" />
      <el-table-column
          label="Operation"
          align="center"
          class-name="small-padding fixed-width"
      >
        <template #default="{ row }">
          <!-- Edit 按钮 -->
          <el-button
              v-if="row.tableIsgen !== 1 && row.tableStatus !== 1 && hasPer('datacenter:TableDefinition:edit')"
              size="default"
              type="text"
              @click="handleUpdate(row)"
          >
            Edit
          </el-button>

          <!-- 编辑按钮 Temp -->
          <el-button
              v-if="row.tableIsgen === 1 || row.tableStatus === 1 && hasPer('datacenter:TableDefinition:tempedit')"
              size="default"
              type="text"
              @click="handleTempUpdate(row)"
          >
            Edit
          </el-button>

          <!-- Detail 按钮 -->
          <el-button
              size="default"
              type="text"
              @click="handleTempDetail(row)"
              v-hasPermi="['datacenter:TableDefinition:view']"
          >
            Detail
          </el-button>

          <!-- Generate 按钮 -->
          <el-button
              v-if="row.tableStatus !== 1 && hasPer('datacenter:TableDefinition:generate')"
              size="default"
              type="text"
              @click="handleGenerate(row)"
          >
            Run
          </el-button>

          <!-- More 按钮 -->
          <el-dropdown placement="top-start">
            <el-button type="text" style="margin-left: 10px;">More</el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <!-- Syn 按钮 -->
                <el-dropdown-item v-if="row.tableSyn !== 1 && row.tableIsgen === 1 && hasPer('datacenter:TableDefinition:synTable')">
                  <el-button
                      size="default"
                      type="text"
                      @click="handleSyn(row)"
                  >
                    Syn
                  </el-button>
                </el-dropdown-item>

                <!-- Delete 按钮 -->
                <el-dropdown-item v-if="row.tableStatus !== 1 && hasPer('datacenter:TableDefinition:remove')">
                  <el-button
                      v-if="row.tableType!==99"
                      size="default"
                      type="text"
                      @click="handleDelete(row)"
                  >
                    Delete
                  </el-button>
                </el-dropdown-item>

                <!-- 回滚 按钮 -->
                <el-dropdown-item v-if="row.tableSyn !== 1 && row.tableStatus === 1 && hasPer('datacenter:TableDefinition:rollback')">
                  <el-button
                      size="default"
                      type="text"
                      @click="handleRollBack(row)"
                  >
                    RollBack
                  </el-button>
                </el-dropdown-item>

                <!-- Stop 按钮 -->
                <el-dropdown-item v-if="row.tableStatus === 1 && hasPer('datacenter:TableDefinition:stopTable')">
                  <el-button
                      size="default"
                      type="text"
                      @click="handleStopTable(row)"
                  >
                    Stop
                  </el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
      </el-table-column>


    </el-table>

    <!-- 分页 -->
    <el-pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
    />

    <el-dialog :title="title" v-model="dialogVisible" width="500px" append-to-body>
      <el-form :model="form" :rules="relus" label-width="150px">
        <el-form-item label="Table Name" prop="tableName">
          <el-input v-model="form.tableName" />
        </el-form-item>
        <el-form-item label="Table Name(EN)" prop="tableEnName">
          <el-input v-model="form.tableEnName" />
        </el-form-item>
        <!-- 添加 ignoreSheetName 开关 -->
        <el-form-item label="Ignore Sheet Name" prop="ignoreSheetName">
          <el-switch
              v-model="form.ignoreSheetName"
              active-text="Yes"
              inactive-text="No"
          />
        </el-form-item>
        <el-form-item v-if="!form.ignoreSheetName" label="Sheet Name" prop="sheetName">
          <el-input v-model="form.sheetName" placeholder="Enter Sheet Name" />
        </el-form-item>
        <!-- 新添加的 startLine 字段 -->
        <el-form-item label="Start Line" prop="startLine">
          <el-input-number
              v-model="form.importStart"
              placeholder="Enter Start Line"
              :min="0"
              :step="1"
          />
        </el-form-item>
<!--        <el-form-item label="Table Type" prop="tableType">-->
<!--          <el-select v-model="form.tableType" placeholder="Select Table Type">-->
<!--            <el-option-->
<!--                v-for="option in tableTypeOptions"-->
<!--                :key="option.dictValue"-->
<!--                :label="option.dictLabel"-->
<!--                :value="option.dictValue"-->
<!--            />-->
<!--          </el-select>-->
<!--        </el-form-item>-->

        <el-form-item label="Remark" prop="tableRemark">
          <el-input v-model="form.tableRemark" placeholder="Please enter a remark" />
        </el-form-item>
      </el-form>
      <template v-slot:footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">Confirm</el-button>
          <el-button @click="dialogVisible=false">Cancel</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from "vue";
import { datacenterService} from "@/service/tableDefinition";
import {useRouter} from "vue-router";
import { ElMessageBox, ElMessage } from "element-plus";
import {hasPer, hasRoleId} from "@/tools";
defineOptions({ name: 'TableDefinition' });

const router = useRouter();

// 状态管理
const loading = ref(true);
const ids = ref([]); // 选中项 ID 数组
const single = ref(true); // 单选禁用
const multiple = ref(true); // 多选禁用
const showSearch = ref(true); // 是否显示搜索条件
const total = ref(0); // 总条数
const tableDefinitionList = ref([]); // 表数据
const tableTypeOptions = ref([]); // 表类型选项
const dialogVisible = ref(false); // 弹框显示
const dialogTitle = ref("");
const editIsTemp=ref(false) //是否是临时编辑

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  tableName: null,
  tableEnName: null,
  tableDbName: null,
  dataSourceId: null,
  tableLiveTime: null,
  tableRemark: null,
  tableType: null,
  tableIsgen: null,
  tableStatus: null,
  tableSyn: null,
});

// 表单数据
const form = reactive({
  tableId: null,
  tableName: "",
  tableEnName: "",
  tableRemark: "",
  tableType:4,
  ignoreSheetName:true,
  importSheetName:"",
  importStart:""
});

// 表单校验规则
const rules = {
  tableName: [{ required: true, message: "Table name cannot be empty", trigger: "blur" }],
  tableEnName: [{ required: true, message: "The English name of the table cannot be empty", trigger: "blur" }],
};

// 获取列表数据
const getList = async () => {
  loading.value = true;
  try {
    const response = await datacenterService.billingList(queryParams);
    console.log("response:" + response)
    tableDefinitionList.value = response.rows;
    total.value = response.total;
  } catch (error) {
    console.error("Error fetching table definition list:", error);
  } finally {
    loading.value = false;
  }
};

// 获取表类型选项
const loadTableTypeOptions = async () => {
  try {
    const response = await datacenterService.fetchTableTypeOptions();
    tableTypeOptions.value = response.data
  } catch (error) {
    console.error("Error fetching table type options:", error);
  }
};

// 查询操作
const handleQuery = () => {
  queryParams.pageNum = 1;
  getList();
};

// 重置查询
const resetQuery = () => {
  Object.keys(queryParams).forEach((key) => {
    if (key !== "pageNum" && key !== "pageSize") queryParams[key] = null;
  });
  handleQuery();
};

// 新增
const handleAdd = () => {
  console.log("1111111111111111111")
  // Object.assign(form, { tableId: null, tableName: "", tableEnName: "", tableRemark: "" });
  dialogTitle.value = "Add Table";
  dialogVisible.value = true;
  console.log("dialogVisible:", dialogVisible.value); // 应打印 true
};

// 编辑
const handleUpdate = async (row) => {
  dialogTitle.value = "Edit Table";
  dialogVisible.value = true;
  try {
    const response = await datacenterService.get(row.tableId);
    Object.assign(form, response.data);
  } catch (error) {
    console.error("Error fetching table details:", error);
  }
};

// 编辑
const handleTempUpdate = async (row) => {
  dialogTitle.value = "Edit Table";
  dialogVisible.value = true;
  editIsTemp.value=true;
  try {
    const response = await datacenterService.getTemp(row.tableId);
    Object.assign(form, response.data);
  } catch (error) {
    console.error("Error fetching table details:", error);
  }
};

// 删除
const handleDelete = async (row) => {
  try {
    await datacenterService.del(row.tableId);
    getList();
  } catch (error) {
    console.error("Error deleting table definition:", error);
  }
};


const handleTempDetail = async (row) => {
  if (!row.tableId) {
    console.error("Table ID is missing in the row data");
    return;
  }

  try {
    // 跳转路由
    await router.push({
      name: 'columnSettingTemp',
      params: { id: row.tableId },
    });
  } catch (error) {
    console.error("Error during navigation:", error);
  }
};
// 提交表单
const submitForm = async () => {
  try {
    if (form.tableId) {
      if (editIsTemp.value){
        await datacenterService.editTemp(form);
      }else {
        await datacenterService.edit(form);
      }
    } else {
      await datacenterService.add(form);
    }
    dialogVisible.value = false;
    getList();
  } catch (error) {
    console.error("Error submitting table definition:", error);
  }
};

// 同步表
const handleSyn = async (row) => {
  try {
    await datacenterService.synTable({ tableId: row.tableId, tableSyn: 1 });
    getList();
  } catch (error) {
    console.error("Error syncing table:", error);
  }
};
const handleRollBack = async (row)=>{
  try {
    await datacenterService.rollbackTable({tableId:row.tableId});
    getList();
  }catch (error){
    console.error("Error rollback table",error )
  }
}

// 停止表
const handleStopTable = async (row) => {
  try {
    await datacenterService.stop({ tableId: row.tableId, tableStatus: 0 });
    getList();
  } catch (error) {
    console.error("Error stopping table:", error);
  }
};

const handleGenerate = async (row) => {
  try {
    // 弹出确认框
    await ElMessageBox.confirm(
        "Are you sure you want to use this config?",
        "Confirmation",
        {
          confirmButtonText: "Confirm",
          cancelButtonText: "Cancel",
          type: "warning",
        }
    );
    await datacenterService.createOrChange({ tableId: row.tableId, tableStatus: 1 });
    // 成功后刷新列表
    await getList();
    // 显示成功消息
    ElMessage.success("Synchronization successful");
  } catch (error) {
    if (error !== "cancel") {
      // 处理其他错误
      console.error("Error during table generation:", error);
      ElMessage.error("Error during table generation");
    } else {
      console.log("User cancelled the operation.");
    }
  }
};

const formatStatus = (row) => {
  if (row.tableStatus === 0) {
    return row.tableIsgen !== 1 ? " " : "deactivated";
  } else if (row.tableStatus === 1) {
    return "In use";
  } else {
    return row.tableIsgen !== 1 ? " " : "deactivated";
  }
};


// 导出数据
const handleExport = () => {
  const query = { ...queryParams };
  const url = `/datacenter/TableDefinition/export`;
  const filename = `TableDefinition_${new Date().getTime()}.xlsx`;
  datacenterService.exportData(url, query, filename);
};

// 监听多选框
const handleSelectionChange = (selection) => {
  ids.value = selection.map((item) => item.tableId);
  single.value = selection.length !== 1;
  multiple.value = selection.length === 0;
};

// 生命周期
onMounted(() => {
  getList();
  loadTableTypeOptions();
});
</script>

<style>
.link-style {
  color: #007bff; /* 设置为蓝色，类似链接颜色 */
  cursor: pointer; /* 鼠标悬停时显示为指针，表示可以点击 */
}
</style>



