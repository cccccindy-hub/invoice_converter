<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryFormRef" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="应用名称" prop="appName">
        <el-input v-model="queryParams.appName" placeholder="请输入应用名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" size="small" @click="handleQuery">搜索</el-button>
        <el-button :icon="Refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain :icon="Plus" size="small" @click="handleAdd"
          v-hasPermi="['system:app:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain :icon="Edit" size="small" :disabled="single" @click="handleUpdate"
          v-hasPermi="['system:app:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain :icon="Delete" size="small" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['system:app:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="appList" @selection-change="handleSelectionChange" style="width:100%;flex:1;">
      <el-table-column type="selection" width="55" />
      <el-table-column label="应用名称" prop="appName" width="100" fixed="left" />
      <el-table-column label="appId" prop="appId" width="260" />
      <el-table-column label="appKey" prop="appKey" width="260" />
      <el-table-column label="secretKey" prop="secretKey" width="260" />
      <el-table-column label="备注" prop="fdesc" width="200" />
      <el-table-column label="操作" width="300" fixed="right">
        <template #default="scope">
          <el-button type="primary" size="small" :icon="Edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['system:app:edit']">修改</el-button>
          <el-button type="primary" size="small" :icon="Delete" @click="handleDelete(scope.row)"
            v-hasPermi="['system:app:remove']">删除</el-button>
          <el-button type="primary" size="small" :icon="Edit" @click="genToekn(scope.row)">生成测试token</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改应用管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" destroy-on-close>
      <el-form ref="editFormRef" :model="editForm" :rules="rules" label-width="80px">
        <el-form-item label="应用名称" prop="appName">
          <el-input v-model="editForm.appName" placeholder="请输入应用名称" />
        </el-form-item>
        <el-form-item label="备注" prop="fdesc">
          <el-input v-model="editForm.fdesc" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<style scoped lang="scss"></style>
<script lang="ts" setup>
import { listApp, getApp, delApp, addApp, updateApp, genTestToken } from "@/api/system/app";
import { IAdminApiResponse } from "@/interface";
import { Delete, Edit, Plus, Refresh, Search } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox, FormInstance } from "element-plus";
import { onMounted, reactive, ref } from "vue";

// 遮罩层
const loading = ref(true);
// 选中数组
const ids = ref([]);
// 非单个禁用
const single = ref(true);
// 非多个禁用
const multiple = ref(true);
// 显示搜索条件
const showSearch = ref(true);
// 总条数
const total = ref(0);
// 应用管理表格数据
const appList = ref([]);
// 弹出层标题
const title = ref("");
// 是否显示弹出层
const open = ref(false);
// 查询参数
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  appName: null,
  appKey: null,
  fdesc: null,
});
// 表单参数
const editForm = ref<{ appId?: string; appName?: string; appKey?: string; fdesc?: string; }>({});
// 表单校验
const rules = reactive({
  appName: [
    { required: true, message: "应用名称不能为空", trigger: "blur" }
  ],
});
const queryFormRef = ref<FormInstance>();
const editFormRef = ref<FormInstance>();
onMounted(() => {
  getList();
});

/** 查询应用管理列表 */
function getList() {
  loading.value = true;
  listApp(queryParams.value).then((response: any) => {
    appList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
};
// 取消按钮
function cancel() {
  open.value = false;
  reset();
};
function resetEditForm() {
  editFormRef.value?.resetFields();
}
// 表单重置
function reset() {
  editForm.value = {
    appId: null,
    appName: null,
    appKey: null,
    fdesc: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null
  } as any;
  resetEditForm();
};
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
};
/** 重置按钮操作 */
function resetQuery() {
  queryFormRef.value?.resetFields();
  handleQuery();
};
// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.appId)
  single.value = selection.length !== 1
  multiple.value = !selection.length
};
/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加应用管理";
};
/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const appId = row.appId || ids
  getApp(appId).then(response => {
    editForm.value = response.data as any;
    open.value = true;
    title.value = "修改应用管理";
  });
};
/** 提交按钮 */
function submitForm() {
  editFormRef.value?.validate(valid => {
    if (valid) {
      if (editForm.value.appId != null) {
        updateApp(editForm.value).then(response => {
          ElMessage.success("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addApp(editForm.value).then(response => {
          ElMessage.success("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
};
/** 删除按钮操作 */
function handleDelete(row) {
  const appIds = row.appId || ids.value;
  ElMessageBox.confirm('是否确认删除应用管理编号为"' + appIds + '"的数据项？').then(function () {
    return delApp(appIds);
  }).then(() => {
    getList();
    ElMessage.success("删除成功");
  }).catch(() => { });
};

function genToekn(row) {
  loading.value = true;
  genTestToken(row.appId).then((response: any) => {
    ElMessageBox.alert(response.token, '仅用于测试,有效期10分钟', {
      // if you want to disable its autofocus
      // autofocus: false,
      customStyle: {
        'word-break': 'break-all',
        'white-space': 'pre-wrap'
      },
      confirmButtonText: 'OK'
    })
    loading.value = false;
  });
}
</script>
