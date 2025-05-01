<template>
  <div class="app-container">
    <el-form v-if="searchColumns.length > 0" inline>
      <el-form-item :label="item.columnName + '：'" v-for="item in searchColumns">
        <el-select
            clearable
            v-if="item.columnDbtype === 0 && item.columnInputMethod === 1"
            v-model="queryParams[item.columnDbname]">
          <el-option
              v-for="(item, index) in dictsMap[item.columnDictid]"
              :key="index"
              :label="item.dictLabel"
              :value="item.columnDictchosetype === 1 ? item.dictValue : item.dictLabel"
          />
        </el-select>
        <el-input-number v-else-if="item.columnDbtype === 1" v-model="queryParams[item.columnDbname]"/>
        <el-date-picker
            v-else-if="item.columnDbtype === 2"
            v-model="queryParams[item.columnDbname]"
            :type="dateRangeType(item)"
            range-separator="~"
            value-format="YYYY-MM-DD HH:mm:ss"
            :format="item.columnFormate"
            start-placeholder="Start Date"
            end-placeholder="End Date"
        />
        <el-input v-else v-model="queryParams[item.columnDbname]" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchTableData">Search</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            size="default"
            @click="handleAdd"
        >Add
        </el-button>
      </el-col>
    </el-row>

    <el-table :data="tableData" style="width: 100%" border>
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column
          v-for="column in columnsTree"
          :key="column.columnDbname"
          :label="column.columnName"
          :prop="column.columnDbname"
          :fixed="column.fixed === 1"
          :align="column.align || 'center'"
      >
        <template v-if="column.childList && column.childList.length > 0">
          <el-table-column
              v-for="child in column.childList"
              :key="child.columnDbname"
              :label="child.columnName"
              :prop="child.columnDbname"
              :align="child.align || 'center'">
            <template v-if="child.childList && child.childList.length > 0">
              <el-table-column
                  v-for="child2 in child.childList"
                  ::key="child2.columnDbname"
                  :label="child2.columnName"
                  :prop="child2.columnDbname"
                  :align="child2.align || 'center'">
                <template v-if="child2.childList && child2.childList.length > 0">
                  <el-table-column
                      v-for="child3 in child2.childList"
                      ::key="child3.columnDbname"
                      :label="child3.columnName"
                      :prop="child3.columnDbname"
                      :align="child3.align || 'center'">
                    <template v-if="child3.childList && child3.childList.length > 0">
                      <el-table-column
                          v-for="child4 in child3.childList"
                          ::key="child4.columnDbname"
                          :label="child4.columnName"
                          :prop="child4.columnDbname"
                          :align="child4.align || 'center'">
                      </el-table-column>
                    </template>
                  </el-table-column>
                </template>
              </el-table-column>
            </template>
          </el-table-column>
        </template>
      </el-table-column>

      <el-table-column label="Operation" align="center" width="150" fixed="right">
        <template #default="scope">
          <el-button size="small" type="primary" @click="handleEdit(scope.row)">Edit</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row)">Delete
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        style="margin-top: 10px"
        background
        layout="prev, pager, next, sizes, total"
        :total="total"
        :page-size="queryParams.pageSize"
        :current-page="queryParams.pageNum"
        @current-change="handlePageChange"
        @size-change="handlePageSizeChange"
    />

    <result-detail v-if="showEdit" :table-id="tableId" :column-id="columnId" title="Detail" @close="showEdit=false" @success="editSuccess"/>
  </div>
</template>

<script lang="ts" setup>
import {onMounted, reactive, ref} from "vue";
import {useRoute} from "vue-router";
import {TableResultService} from "@/service/tableResult";
import {ElMessage, ElMessageBox} from "element-plus";
import ResultDetail from "@/views/biz/payroll/result/ResultDetail.vue";

const showEdit = ref(false);
const showSearch = ref(true);
const field = ref("")
const tableColumns = ref([]);
const columnsTree = ref([]);
const searchColumns = ref([]);
const tableId = ref();
const columnId = ref();

const dictsMap = ref();
const tableData = ref([]);
const total = ref(0);
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  tableId: Number(useRoute().params.id),
});

// 生命周期钩子
onMounted(() => {
  tableId.value = queryParams.tableId;
  fetchTableConfig(tableId.value);
  fetchTableData();
});
// 获取表格配置
const fetchTableConfig = async (tableId: number) => {
  try {
    const {configs, dictMap} = (await TableResultService.getTableConfigData(tableId)).data;
    console.log(configs,"1111111111");
    console.log(dictMap,"2222222222222");
    tableColumns.value = configs;
    dictsMap.value = dictMap
    initConfigTree(configs);
  } catch (error) {
    ElMessage.error("Failed to fetch table config:" + error?.message)
  }
}

//表头转化成Tree
function initConfigTree(configs) {
  if(configs.length == 0) {
    return;
  }
  const configMap = new Map();
  let parentId;
  let nodeConfigs
  for (let config of configs) {
    if (config.columnIsquery) {
      searchColumns.value.push(config)
    }
    parentId = config.columnParentDbname || 0;
    nodeConfigs = configMap.get(parentId);
    if (!nodeConfigs) {
      nodeConfigs = []
      configMap.set(parentId, nodeConfigs);
    }
    nodeConfigs.push(config);
  }
  columnsTree.value = buildTree(configMap.get(0), configMap);
  console.log(columnsTree.value,"cccccccccccccccccccc")
}

function buildTree(rootConfig: [], nodeMap: Map) {
  for (let column of rootConfig) {
    let childList = nodeMap.get(column.columnId);
    if (childList) {
      column.childList = buildTree(childList, nodeMap);
    }
  }
  return rootConfig;
}

// 获取表格数据
const fetchTableData = async () => {
  try {
    const response = await TableResultService.getTableData(tableId.value, paramsFormat(queryParams));
    tableData.value = response.rows;
    total.value = response.total;
  } catch (error) {
    ElMessage.error("Failed to fetch table data:" + error?.message)
  }
};

function paramsFormat() {
  let params = {};
  for (const key in queryParams) {
    if (queryParams.hasOwnProperty(key)) { // 检查属性是否是对象自身的属性，而不是继承的
      let val = queryParams[key];
      if(Array.isArray(val)) {
        val = val.join("~")
      }
      params[key] = val
    }
  }
  return params;
}

const handleAdd = async () => {
  columnId.value = null;
  // 清空表单
  showEdit.value = true;
};

// 编辑操作
const handleEdit = (row) => {
  columnId.value = row.id;
  showEdit.value = true;
};

function editSuccess() {
  showEdit.value = false;
  fetchTableData();
}

// 删除操作
const handleDelete = async (row: Record<string, any>) => {
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
        await TableResultService.deleteTableData(row.id, queryParams.tableId);
        ElMessage({
          type: 'success',
          message: 'Delete completed',
        })
        fetchTableData();
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: 'Delete canceled',
        })
      })
};

function dateRangeType(config) {
  let dateFormater = config.columnFormate;
  let dateType;
  if (!dateFormater) {
    dateType = 'datetimerange';
  } else if (dateFormater.includes("HH")) {
    dateType = 'datetimerange';
  } else if (dateFormater.includes("DD")) {
    dateType = 'daterange';
  } else if (dateFormater.includes('MM')) {
    dateType = 'monthrange';
  } else if (dateFormater.includes('YYYY')) {
    dateType = 'yearrange';
  } else {
    dateType = 'datetimerange';
  }
  return dateType;
}


// 修改页码
const handlePageChange = (newPage: number) => {
  queryParams.pageNum = newPage;
  fetchTableData(); // 根据新页码重新获取数据
};

// 修改每页显示条数
const handlePageSizeChange = (newPageSize: number) => {
  queryParams.pageSize = newPageSize;
  queryParams.pageNum = 1; // 重置到第一页
  fetchTableData(); // 根据新页码和每页大小重新获取数据
};
</script>

<style scoped>

</style>
