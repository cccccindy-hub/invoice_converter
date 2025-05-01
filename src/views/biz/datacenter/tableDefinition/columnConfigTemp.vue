<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="default" :inline="true" v-show="showSearch">

      <el-form-item label="Column Name" prop="columnName">
        <el-input
            v-model="queryParams.columnName"
            placeholder="Column Name"
            clearable
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">Search</el-button>
        <el-button @click="resetQuery">Reset</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            size="default"
            @click="handleAdd"
        >Add
<!--            v-hasPermi="['columnConfigTemp:columnConfig:add']"-->
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>


    <el-table
        ref="columnTable"
        v-loading="loading"
        :data="columnConfigList"
        border
        @selection-change="handleSelectionChange"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        row-key="columnId"
    >
      <el-table-column type="selection" width="55" align="center" />
      <!-- 树形列 -->
      <el-table-column label="Column Name" align="center" prop="columnName"/>

      <!-- 自定义列类型显示 -->
      <el-table-column label="Column Type" prop="columnType" align="center">
        <template #default="scope">
          <span v-if="scope.row.columnType === 0" class="label label-primary">Basic Item</span>
          <span v-else-if="scope.row.columnType === 1" class="label label-warning">Header item</span>
        </template>
      </el-table-column>

      <!-- 数据类型显示 -->
      <el-table-column label="Default Data Type" prop="columnDbtype" align="center">
        <template #default="scope">
          <span v-if="scope.row.columnDbtype === 0 && scope.row.columnType===0" title="Character">Character</span>
          <span v-else-if="scope.row.columnDbtype === 1 && scope.row.columnType===0" title="Number">Number</span>
          <span v-else-if="scope.row.columnDbtype === 2 && scope.row.columnType===0" title="Date">Date</span>
        </template>
      </el-table-column>

      <el-table-column label="Sort" align="center" prop="columnSort"/>

      <!-- 操作列 -->
      <el-table-column label="Operation" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button size="default" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">Edit</el-button>
          <el-button size="default" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">Delete
          </el-button>
        </template>
      </el-table-column>
    </el-table>


    <el-dialog v-model="open" style="max-height: 80%;  width:600px; overflow: auto">
      <template #header>
        {{ title }}
      </template>
      <el-form :model="form" relus="relus" label-width="150px">
        <!-- 上级菜单名 -->
        <el-form-item label="Parent Name" prop="columnParentDbname">
          <el-select v-model="form.columnParentDbname" placeholder="Select">
            <el-option
                v-for="column in parentColumnList"
                :key="column.columnId"
                :label="column.columnName"
                :value="column.columnId"
            />
          </el-select>
        </el-form-item>
        <!-- 类型选择 -->
        <el-form-item label="Column Type" prop="columnType">
          <el-radio-group v-model="form.columnType">
            <el-radio :label="1">header</el-radio>
            <el-radio :label="0">basic</el-radio>
          </el-radio-group>
        </el-form-item>
        <!-- 显示名称 -->
        <el-form-item label="Column Name" prop="columnName">
          <el-input v-model="form.columnName"></el-input>
        </el-form-item>
        <!-- 数据库名称 -->
        <el-form-item label="Column Name(EN)" prop="columnEnName">
          <el-input v-model="form.columnEnName" @input="changeEnName"></el-input>
        </el-form-item>
        <!-- 排序 -->
        <el-form-item label="Sort" prop="columnSort">
          <el-input-number v-model="form.columnSort"></el-input-number>
        </el-form-item>
        <template v-if="form.columnType === 0">
          <!-- 数据库类型 -->
          <el-form-item label="Default Data type" prop="columnDbtype">
            <el-radio-group v-model="form.columnDbtype">
              <el-radio :label="0">Character</el-radio>
              <el-radio :label="1">Number</el-radio>
              <el-radio :label="2">Date</el-radio>
            </el-radio-group>
          </el-form-item>
          <!-- 数据库长度 -->
          <template v-if="form.columnDbtype === 2">
            <el-form-item label="Date format：">
              <el-select v-model="form.columnFormate" clearable>
                <el-option v-for="item in dateTypes" :value="item.dictLabel" :label="item.dictValue"/>
              </el-select>
            </el-form-item>
          </template>
          <template v-else>
            <el-form-item label="Data Length" prop="columnDblength">
              <el-input-number v-model="form.columnDblength"/>
            </el-form-item>
            <el-form-item label="Default Value">
              <el-input v-model="form.columnDefault"/>
            </el-form-item>
          </template>
          <template v-if="form.columnDbtype === 0">
            <el-form-item label="Input Method">
              <el-radio-group v-model="form.columnInputMethod">
                <el-radio :label="0">Input</el-radio>
                <el-radio :label="1">Choice</el-radio>
              </el-radio-group>
            </el-form-item>
            <template v-if="form.columnInputMethod === 1">
              <el-form-item label="Dict choice">
                <el-select v-model="form.columnDictid" clearable>
                  <el-option v-for="item in dictTypes" :value="item.dictType" :label="item.dictName"/>
                </el-select>
              </el-form-item>
              <el-form-item label="Dict choice type：">
                <el-radio-group v-model="form.columnDictchosetype">
                  <el-radio :label="0">Dict label</el-radio>
                  <el-radio :label="1">Dict value</el-radio>
                </el-radio-group>
              </el-form-item>
            </template>

          </template>
          <!--          <el-form-item label="Column width">-->
          <!--            <el-input-number/>-->
          <!--          </el-form-item>-->
          <!--是否必填 -->
          <el-form-item label="Is Requeired" prop="isRequired">
            <el-radio-group v-model="form.columnIsrequired">
              <el-radio :label="1">Y</el-radio>
              <el-radio :label="0">N</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="是否固定">
            <el-radio-group v-model="form.fixed" class="ml-4">
              <el-radio :label="1">Y</el-radio>
              <el-radio :label="0">N</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="是否查询:">
            <el-radio-group v-model="form.columnIsquery" class="ml-4">
              <el-radio :label="0">N</el-radio>
              <el-radio :label="1">Y</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="是否索引:">
            <el-radio-group v-model="form.columnIsindex" class="ml-4">
              <el-radio :label="0">N</el-radio>
              <el-radio :label="1">Y</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="是否唯一:">
            <el-radio-group v-model="form.columnIsonly" class="ml-4">
              <el-radio :label="0">N</el-radio>
              <el-radio :label="1">Y</el-radio>
            </el-radio-group>
          </el-form-item>
        </template>
      </el-form>

      <template v-slot:footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">Confirm</el-button>
          <el-button @click="open=false">Cancel</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script lang="ts" setup>
import {onMounted, reactive, ref} from "vue";
import {useRoute} from "vue-router";
import {tableConfigService} from "@/service/tableconfig";
import { ElMessageBox, ElMessage } from "element-plus";
import MultiLevelHeader from "@/components/DCTable/data/MultiHeader.vue";

// 路由参数
const route = useRoute();

// 状态管理
const loading = ref(true);
const ids = ref<number[]>([]); // 选中列的 ID 数组
const single = ref(true); // 单选禁用
const multiple = ref(true); // 多选禁用
const showSearch = ref(true); // 是否显示搜索条件
const open = ref(false); // 弹窗是否打开
const title = ref(""); // 弹窗标题
const columnConfigList = ref([]);
const columnRoleList = ref([])
let parentColumnList = ref([]);
const showAssignRoleDialog = ref(false);

const dateTypes = ref([]);
const dictTypes = ref([]);
const tableId = ref();
// 查询参数
const queryParams = reactive({
  tableId: null,
  columnName: "",
  columnIsquery: null,

});

// 表单数据
const form = reactive({
  columnId: null,
  tableId: null,
  columnParentDbname: null,
  columnType: null,
  columnName: "",
  columnEnName: "",
  columnSort: null,
  columnDbname: null,
  columnDbtype: 0,
  columnDblength: 50,
  columnInputMethod: null,
  columnDefault: null,
  columnFormate: null,
  columnDictid: null,
  columnDictchosetype: null,
  columnFormula: null,
  columnChooseType: null,
  columnIsrequired: null,
  columnIsindex: null,
  columnIsquery: null,
  fixed: 0,
  columnQueryaction: null,
  columnIsonly: null,
  columnHidefromtempl: null,
});

// 表单数据
const assignRoleForm = reactive({
  columnIds: [],
  roleIds:[]
});

// 生命周期钩子
onMounted(() => {
  init();
  getList();
});

async function init() {
  tableId.value = route.params.id;
  let {data} = await tableConfigService.configBasic();
  let dateTypeList = data.dateType;
  let dictTypeList = data.dictTypes;
  if (dateTypeList) {
    dateTypes.value = dateTypeList;
  }
  if(dictTypeList) {
    dictTypes.value = dictTypeList;
  }
}

// 校验规则
const rules = {
  columnType: [{required: true, message: " Category cannot be empty", trigger: "change"}],
  columnName: [{required: true, message: "Display name cannot be empty", trigger: "blur"}],
  columnEnName: [
    {required: true, message: "Display English name cannot be empty", trigger: "blur"},
  ],
};

// 获取表格数据
const getList = async () => {
  loading.value = true;
  queryParams.tableId = tableId.value;
  try {

    const {rows} = await tableConfigService.listTemp(queryParams);
    columnConfigList.value = formatToTree(rows);
    parentColumnList = rows.filter((item) => item.columnType === 1);
  } catch (error) {
    console.error("Error fetching columnConfig list:", error);
  } finally {
    loading.value = false;
  }
};


// 格式化为树形结构
const formatToTree = (data: any[]) => {
  const map: Record<string, any> = {};
  const treeData: any[] = [];

  data.forEach((item) => {
    map[item.columnId] = {...item, children: []};
  });

  data.forEach((item) => {
    if (item.columnParentDbname && map[item.columnParentDbname]) {
      map[item.columnParentDbname].children.push(map[item.columnId]);
    } else {
      treeData.push(map[item.columnId]);
    }
  });
  console.log(treeData, "TreeData")

  return treeData;
};

// 打开新增弹窗
const handleAdd = () => {
  resetForm();
  form.columnDbtype = 0;
  form.columnDblength = 50;
  form.columnInputMethod = 0;
  form.columnType = 0;
  form.columnIsrequired = 0;
  form.tableId = queryParams.tableId;
  open.value = true;
  title.value = "Add Column";
};

const handleUpdate = async (row: any) => {
  resetForm(); // 重置表单
  try {
    const response = await tableConfigService.get(row.columnId); // 获取详情
    Object.assign(form, response.data); // 将返回数据绑定到表单
    open.value = true; // 打开弹框
    title.value = "Update Column"; // 设置弹框标题
  } catch (error) {
    console.error("Error fetching columnConfig:", error);
  }
};

const changeEnName = (value) => {
  const reg = /[^A-Za-z0-9 ]/g; // 仅允许字母、数字和空格
  if (reg.test(value)) {
    // 弹出提示框，提示输入错误
    ElMessage.warning({
      message: "Only letters, numbers, and spaces are allowed",
      duration: 1000,
    });

    // 替换非法字符
    form.columnEnName = value.replace(reg, "");
  }
};


// 提交表单
const submitForm = async () => {
  console.log(form, "form表单")

  try {
    if (form.columnId) {
      await tableConfigService.update(form);
    } else {
      await tableConfigService.add(form);
    }
    open.value = false;
    await getList();
  } catch (error) {
    console.error("Error submitting form:", error);
  }
};

const submitRoleForm = async () => {
  const columnIds = ids.value;
  assignRoleForm.columnIds = columnIds;
  try {
    if (assignRoleForm.columnIds.length > 0 && assignRoleForm.columnIds.length > 0) {
      await tableConfigService.assignRoles(assignRoleForm);
    } else {
      ElMessage.error({
      message: "Please select at least one role and one column",
      duration: 3000,
    });
    }
    showAssignRoleDialog.value = false;
    ElMessage.success({
      message: "Assign Roles Successfully",
      duration: 3000,
    });
    await getList();
  } catch (error) {
    console.error("Error submitting form:", error);
  }
};

// 删除列
const handleDelete = async (row: any) => {
  const columnId = row.columnId || ids.value;
  const tableId = row.tableId;
  // 显示确认弹框
  await ElMessageBox.confirm(
      ` Are you sure you want to delete the data item with column Name"${row.columnName}"?`,
      "Confirm",
      {
        confirmButtonText: "Confirm",
        cancelButtonText: "Cancel",
        type: "warning",
      }
  );

  // 调用删除服务
  await tableConfigService.del(columnId, tableId);

  // 刷新列表
  await getList();

};

// show assign role dialog
const handleAssignRole = async ()  => {
  await getRolesByTableId();
  assignRoleForm.roleIds = []
  showAssignRoleDialog.value = true
}

// 获取表格数据
const getRolesByTableId = async () => {
  loading.value = true;
  const tableId = Number(route.params.id);
  try {
    const {rows} = await tableConfigService.getRoles(tableId);
    columnRoleList.value = rows
  } catch (error) {
    console.error("Error fetching Role list:", error);
  } finally {
    loading.value = false;
  }
};


// 重置表单
const resetForm = () => {
  Object.keys(form).forEach((key) => {
    form[key] = null;
  });
};

// 查询
const handleQuery = async () => {
  await getList();
};

// 重置查询
const resetQuery = () => {
  Object.keys(queryParams).forEach((key) => {
    if (key !== "tableId") queryParams[key] = null;
  });
  handleQuery();
};

// 选择多行
const handleSelectionChange = (selection: any[]) => {
  ids.value = selection.map((item) => item.columnId);
  single.value = selection.length !== 1;
  multiple.value = selection.length === 0;
};
</script>
