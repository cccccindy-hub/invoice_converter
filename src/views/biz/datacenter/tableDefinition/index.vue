<template>
  <div class="app-container">
    <!-- 搜索条件表单 -->
    <el-form :model="queryParams" ref="queryForm" size="default" :inline="true" v-show="showSearch">
      <el-form-item label="Table Name " prop="tableName">
        <el-input v-model="queryParams.tableName" placeholder="Enter Table Name" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item lable="Table type">
        <el-select v-model="queryParams.tableType" placeholder="Select Table Type" clearable>
          <el-option
              v-for="option in tableTypeOptions"
              :key="option.dictValue"
              :label="option.dictLabel"
              :value="option.dictValue"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">
          Search
        </el-button>
        <el-button @click="resetQuery">
          Reset
        </el-button>
      </el-form-item>
    </el-form>
    <!-- 操作按钮组 -->
    <el-row :gutter="10" class="mb8">
      <el-button v-if="hasPer('datacenter:TableDefinition:add')" type="primary" plain size="default" @click="handleAdd">
        Add
      </el-button>
      <el-button v-if="hasPer('datacenter:TableDefinition:edit')" type="success" plain size="default"
                 :disabled="ids.length !== 1" @click="handleUpdate(ids[0])">
        Edit
      </el-button>
    </el-row>
    <!-- 数据表格 -->
    <el-table
        v-loading="loading"
        :data="tableDefinitionList"
        @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="Table Name" align="center" prop="tableName" width="200" show-overflow-tooltip/>
      <el-table-column label="Table Name (EN)" align="center" prop="tableEnName" width="200" show-overflow-tooltip/>
      <el-table-column label="table Type" align="center" prop="tableType" :formatter="colFormat" width="180"
                       show-overflow-tooltip/>
      <el-table-column label="Generation status" align="center" prop="tableIsgen"
                       :formatter="row => row.tableIsgen === 1 ? 'Generated' : 'Not generated'" width="160"/>
      <el-table-column label="Sync status" align="center" prop="tableSyn"
                       :formatter="row => row.tableSyn === 1 ? 'Sync' : 'Not Sync'" width="120"/>
      <el-table-column label="Generation time" align="center" prop="tableLiveTime" width="130">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.tableLiveTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Status" align="center" prop="tableStatus" :formatter="formatStatus" width="80"/>
      <el-table-column label="Remark" align="center" prop="tableRemark" width="160" show-overflow-tooltip/>
      <el-table-column label="Operation" align="center" class-name="small-padding fixed-width" fixed="right"
                       width="200">
        <template #default="{ row }">
          <!-- Edit 按钮 -->
          <el-button
              v-if="hasPer('datacenter:TableDefinition:edit')"
              size="default" link type="primary" @click="handleUpdate(row.tableId)">
            Edit
          </el-button>
<!--          &lt;!&ndash; 编辑按钮 Temp &ndash;&gt;
          <el-button
              v-if="(row.tableIsgen === 1 || row.tableStatus === 1) && hasPer('datacenter:TableDefinition:tempedit')"
              size="default" link type="primary" @click="handleTempUpdate(row)">
            Edit
          </el-button>-->
          <!-- Detail 按钮 -->
          <el-button size="default" link type="primary" @click="handleTempDetail(row)"
                     v-if="hasRoleId(row.fieldMgtRoleId)">
            Detail
          </el-button>
          <!-- Generate 按钮 -->
          <el-button v-if="row.tableStatus !== 1 && hasPer('datacenter:TableDefinition:generate')" size="default" link
                     type="primary" @click="handleGenerate(row)">
            Run
          </el-button>
          <!-- More 按钮 -->
          <el-dropdown placement="top-start">
            <el-button link type="primary" style="margin-left: 10px;">More</el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <!-- Syn 按钮 -->
                <el-dropdown-item
                    v-if="row.tableSyn !== 1 && row.tableIsgen === 1 && hasPer('datacenter:TableDefinition:synTable')">
                  <el-button size="default" link type="primary" @click="handleSyn(row)">
                    Sync
                  </el-button>
                </el-dropdown-item>
                <!-- Delete 按钮 -->
                <el-dropdown-item
                    v-if="row.sysFlag === 0 && hasPer('datacenter:TableDefinition:remove') && row.tableStatus !== 1">
                  <el-button size="default" link type="primary" @click="handleDelete(row)">Delete</el-button>
                </el-dropdown-item>
                <!-- 回滚 按钮 -->
                <el-dropdown-item
                    v-if="row.tableSyn !== 1 && row.tableStatus === 1 && hasPer('datacenter:TableDefinition:rollback')">
                  <el-button size="default" link type="primary" @click="handleRollBack(row)">
                    RollBack
                  </el-button>
                </el-dropdown-item>
                <!-- Stop 按钮 -->
                <el-dropdown-item v-if="row.tableStatus === 1 && hasPer('datacenter:TableDefinition:stopTable')">
                  <el-button size="default" link type="primary" @click="handleStopTable(row)">
                    Stop
                  </el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
    <div class="footer" style="margin-top: 20px">
      <div>
        <el-pagination background layout="total, prev, pager, next, sizes" :total="total"
                       :page-sizes="[10, 20, 50, 100, 500]"
                       v-model:page-size="queryParams.pageSize"
                       v-model:current-page="queryParams.pageNum"
                       @current-change="getList"
                       @prev-click="getList"
                       @size-change="handleSizeChange"
                       @next-click="getList"/>
      </div>
    </div>

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px" append-to-body>
      <el-form :model="form" :rules="rules" label-width="150px" ref="formRef">
        <el-form-item label="Table Name" prop="tableName">
          <el-input v-model="form.tableName"/>
        </el-form-item>
        <el-form-item label="Table Name(EN)" prop="tableEnName">
          <el-input v-model="form.tableEnName" :disabled="form.tableId"/>
        </el-form-item>
        <el-form-item label="Table Type" prop="tableType">
          <el-select v-model="form.tableType" placeholder="Select Table Type">
            <el-option
                v-for="option in tableTypeOptions"
                :key="option.dictValue"
                :label="option.dictLabel"
                :value="option.dictValue"
            />
          </el-select>
        </el-form-item>
  
        <el-form-item label="Remark" prop="tableRemark">
          <el-input v-model="form.tableRemark" placeholder="Please enter a remark"/>
        </el-form-item>
      </el-form>
      <template v-slot:footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm(formRef)">Confirm</el-button>
          <el-button @click="dialogVisible=false">Cancel</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script lang="ts" setup>
import {onMounted, reactive, ref} from "vue";
import {datacenterService} from "@/service/tableDefinition";
import {useRouter} from "vue-router";
import {ElMessage, ElMessageBox, FormInstance} from "element-plus";
import {hasPer, hasRoleId} from "@/tools";
import {roleBasic} from "@/api/system/role";

defineOptions({name: 'TableDefinition'});

const router = useRouter();

/*const { proxy } = getCurrentInstance();
const { role_type } = proxy.useDict("role_type");*/

// 状态管理
const loading = ref(true);
const ids = ref([]); // 选中项 ID 数组
const showSearch = ref(true); // 是否显示搜索条件
const total = ref(0); // 总条数
const tableDefinitionList = ref([]); // 表数据
const tableTypeOptions = ref([]); // 表类型选项
const dialogVisible = ref(false); // 弹框显示
const dialogTitle = ref("");
const editIsTemp = ref(false) //是否是临时编辑
const formRef = ref<FormInstance>();
const assignFlag = ref(false);
const assignTableId = ref(null);
const allRoles = ref([]);

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 20,
  orderByColumn: "table_id",
  isAsc: "descending",
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
const form = ref({
  tableId: null,
  tableName: "",
  tableEnName: "",
  tableType: '',
  tableDataHasAuth: 1,
  fieldMgtRoleId: null,
  groupMgtRoleId: null,
  tableRemark: "",
});

// 表单校验规则
const rules = {
  tableName: [{required: true, message: "Table name cannot be empty", trigger: "blur"}],
  tableEnName: [{required: true, message: "English name of the table cannot be empty", trigger: "blur"}],
  tableType: [{required: true, message: "Table type of the table cannot be empty", trigger: "blur"}],
};

// 获取列表数据
const getList = async () => {
  loading.value = true;
  try {
    const response = await datacenterService.list(queryParams);
    tableDefinitionList.value = response.rows;
    total.value = response.total;
  } catch (error) {
    console.error("Error fetching table definition list:", error);
  } finally {
    loading.value = false;
  }
};

function handleSizeChange(size) {
  queryParams.pageSize = size;
  getList();
}

// 获取表类型选项
const init = async () => {
  tableTypeOptions.value = (await datacenterService.fetchTableTypeOptions()).data;
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
  dialogTitle.value = "Add Table";
  form.value = {tableDataHasAuth: 1}
  dialogVisible.value = true;
};

// 编辑
const handleUpdate = async (tableId) => {
  dialogTitle.value = "Edit Table";
  dialogVisible.value = true;
  form.value = (await datacenterService.get(tableId)).data;
};
// 编辑
/*const handleTempUpdate = async (row) => {
  dialogTitle.value = "Edit Table";
  dialogVisible.value = true;
  editIsTemp.value = true;
  form.value = (await datacenterService.getTemp(row.tableId)).data;
};*/

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
      params: {id: row.tableId},
      // query: { data: JSON.stringify(row) }, // 传递整个 row 对象作为 JSON 字符串
    });
  } catch (error) {
    console.error("Error during navigation:", error);
  }
};
// 提交表单
const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate(async (valid) => {
    if (valid) {
      try {
        if (form.value.tableId) {
          if (editIsTemp.value) {
            await datacenterService.editTemp(form.value);
          } else {
            await datacenterService.edit(form.value);
          }
        } else {
          await datacenterService.add(form.value);
        }
        dialogVisible.value = false;
        getList();
      } catch (error) {
        ElMessage.error("Error submitting table definition:" + error?.message);
      }
    }
  })
};

function assignTableRole(row) {
  assignTableId.value = row.tableId
  assignFlag.value = true;
}

// 同步表
const handleSyn = async (row) => {
  try {
    await datacenterService.synTable({tableId: row.tableId, tableSyn: 1});
    getList();
  } catch (error) {
    console.error("Error syncing table:", error);
  }
};
const handleRollBack = async (row) => {
  try {
    await datacenterService.rollbackTable({tableId: row.tableId});
    getList();
  } catch (error) {
    console.error("Error rollback table", error)
  }
}

// 停止表
const handleStopTable = async (row) => {
  try {
    await datacenterService.stop({tableId: row.tableId, tableStatus: 0});
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
    await datacenterService.createOrChange({tableId: row.tableId, tableStatus: 1});
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

function colFormat(row, column, cellVal, index) {
  if (!cellVal) {
    return '';
  }
  const property = column.property;
  let result;
  switch (property) {
    case "tableType":
      for (let type of tableTypeOptions.value) {
        if (type.dictValue === cellVal) {
          result = type.dictLabel;
          break;
        }
      }
      break;
    default:
      result = cellVal;
  }
  return result;
}

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
  const query = {...queryParams};
  const url = `/datacenter/TableDefinition/export`;
  const filename = `TableDefinition_${new Date().getTime()}.xlsx`;
  datacenterService.exportData(url, query, filename);
};

// 监听多选框
const handleSelectionChange = (selection) => {
  ids.value = selection.map((item) => item.tableId);
};

// 生命周期
onMounted(() => {
  init();

  getList();
});
</script>


