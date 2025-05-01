<template>
  <div class="app-container">
    <!-- 搜索表单 -->
<!--    <el-form :model="queryParams" ref="queryForm" size="default" :inline="true" v-show="showSearch">-->
<!--      <el-form-item label="Import File Name" prop="tableEnName">-->
<!--        <el-input-->
<!--            v-model="queryParams.tableName"-->
<!--            placeholder="Enter Table Name"-->
<!--            clearable-->
<!--            @keyup.enter="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item>-->
<!--        <el-button type="primary" size="default" @click="handleQuery">Search</el-button>-->
<!--        <el-button size="default" @click="resetQuery">Reset</el-button>-->
<!--      </el-form-item>-->
<!--    </el-form>-->

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-button type="primary" size="default" @click="openUploadDialog">
        <i class="fa fa-upload"></i> Import File
      </el-button>
<!--      <el-col :span="1.5">-->
<!--        <el-button type="success" plain size="default" :disabled="single" @click="handleEdit">Edit</el-button>-->
<!--      </el-col>-->
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="tableImportLogList">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="input File Name" align="center" prop="inputFileName" />
      <el-table-column label="Operation Time" align="center" prop="createTime"/>
      <el-table-column label="Remark" align="center" prop="remark" />
    </el-table>

<!--    <pagination-->
<!--        v-show="queryParams.total > 0"-->
<!--        :total="queryParams.total"-->
<!--        :page.sync="queryParams.pageNum"-->
<!--        :limit.sync="queryParams.pageSize"-->
<!--        @pagination="getImportLogList"-->
<!--    />-->

    <el-pagination
        background
        layout="total, prev, pager, next, sizes"
        :total="queryParams.total"
        :page-size.sync="queryParams.pageSize"
        :current-page.sync="queryParams.pageNum"
        @current-change="handlePageChange"
        @size-change="handlePageSizeChange"
    />


    <!-- 弹窗表单 -->
    <el-dialog
        title="Import File"
        v-model="dialogVisible"
        width="400px"
        @close="resetForm"
    >
      <el-upload
          ref="uploadRef"
          accept=".xls,.xlsx,.xlsm"
          :show-file-list="false"
          :auto-upload="false"
          @change="handleFileChange"
      >
        <el-button type="primary" size="default">Select File</el-button>
      </el-upload>

      <!-- 显示文件名 -->
      <div v-if="fileName" style="margin-top: 10px;">Selected File: {{ fileName }}</div>

      <div class="upload-tips">
        <span class="tip-text">
          Tip: Only files in "xls", "xlsx", or "xlsm" format are allowed!
        </span>
      </div>

      <!-- 下拉选择框 -->
      <div style="margin-top: 15px;">
        <el-select v-model="selectedOption"
                   placeholder="Select an option"
                   filterable
        >
          <el-option
              v-for="item in tableDefinitionList"
              :key="item.tableId"
              :label="item.tableName"
              :value="item.tableId"
          ></el-option>
        </el-select>
      </div>


      <!-- 弹窗底部 -->
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary"
                   @click="handleImport"
                   :disabled="!fileSelected">Import</el-button>
      </template>
    </el-dialog>

  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from "vue";
import {ElMessage, ElTable,ElUpload, ElButton,ElMessageBox} from 'element-plus'
import { InvoiceService } from "@/service/invoice";
import { datacenterService } from "@/service/tableDefinition";
import { useRouter } from "vue-router";
import Tab from "@/plugins/tab";
import {TableImportLogService} from "@/service/tableImportLog";
import {UploadInstance} from "element-plus/lib/components";

const router = useRouter();

const showSearch = ref(true); // 是否显示搜索条件
const loading = ref(false);

// 弹窗控制变量
const dialogVisible = ref(false);
// 控制文件是否选择的状态
const fileSelected = ref(false);
// 选择的文件名
const fileName = ref<string | null>(null);
const selectedOption = ref('');

// 上传引用
const uploadRef = ref<UploadInstance>()

// 当前选择的文件
const selectedFile = ref<File | null>(null);

// 选中导入的TableId
const selectedTableId = ref<string | null>(null);

const queryParams = reactive({
  total:0,
  pageNum: 1,
  pageSize: 10
});



const tableDefinitionList = ref([]);
const total = ref(0);
const single = ref(true);
const multiple = ref(true);
const tableImportLogList = ref([]);

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

// 存储选中的表格ID

// 上传参数
const uploadParams = reactive({
  tableId: 1, // 替换为实际的表ID
  updateSupport: true, // 是否支持更新
  leaderId: "1", // 管理者ID
  ignoreSheetName: false, // 是否忽略Sheet名称
  validSheetPattern: "-L$", // 只匹配以 '-L' 结尾的 Sheet 名称
  selectedOption: '',    // 下拉选择框的值
});

// 打开弹窗
const openUploadDialog = () => {
  dialogVisible.value = true;
  fileSelected.value = false;
  fileName.value = null;
  selectedFile.value = null;
};

// 文件选择变化时触发
const handleFileChange = (file: File) => {
  console.log("beforeUpload triggered:", file);  // 打
  fileName.value = file.name; //
  selectedFile.value=file.raw;
  fileSelected.value = true; // 标记已选文件
  return true;
};



const handleImport = async () => {
  // 检查文件是否已选择
  if (!fileSelected.value || !fileName.value) {
    ElMessage.error("Please select a file before importing!");
    return;
  }

  if (!selectedOption.value) {
    ElMessage.error("Please select a table before importing!");
    return;
  }

  // 创建 FormData 对象并添加数据
  const formData = new FormData();
  formData.append("file", selectedFile.value);
  formData.append("tableId", selectedOption.value);  // 添加下拉框选择的值
  try {
    console.log(formData.values(),"开始导入");
    const response = await InvoiceService.importTableData(
        formData
    );
    if (response.code === 200) {
      ElMessage.success("File imported successfully!");
      dialogVisible.value = false;
      getTableList();
      // getImportLogList();// 假设这是更新列表的函数
    } else {
      ElMessage.error(response.msg || "Import failed. Please try again.");
    }
  } catch (error) {
    ElMessage.error("File upload failed. Please try again.");
    console.error("Import error:", error);
  } finally {
    loading.value = false; // 假设这是控制加载状态的变量
  }
};
// 查询数据列表
const getTableList = async () => {
  loading.value = true;
  try {
    const { rows, total: count } = await datacenterService.billingList(queryParams);
    tableDefinitionList.value = rows || [];
    total.value = count;
  } finally {
    loading.value = false;
  }
};

// 查询数据列表
const getImportLogList = async () => {
  loading.value = true;
  try {
    console.log('Requesting with params:', queryParams);  // 输出分页参数，查看传递的内容
    const response= await TableImportLogService.list(queryParams);
    console.log(response, "55555666");
    tableImportLogList.value = response.rows;
    queryParams.total = response.total; // 更新总记录数// 更新数据
    // console.log(rows,"Tabel Import Log")
    total.value = response.total;
    console.log(total.value,"99")
  } finally {
    loading.value = false;
  }
};

const handlePageChange = (newPage: number) => {
  queryParams.pageNum = newPage;
  getImportLogList(); // 根据新页码重新获取数据
};

const handlePageSizeChange = (newPageSize: number) => {
  queryParams.pageSize = newPageSize;
  queryParams.pageNum = 1; // 重置到第一页
  getImportLogList(); // 根据新页码和每页大小重新获取数据
};

// 重置表单
const resetForm = () => {
  Object.keys(form).forEach((key) => {
    form[key] = null;
  });
};


// 生命周期
onMounted(() => {
  getTableList();
  getImportLogList();
});
</script>

<style scoped>
.upload-tips {
  margin-bottom: 15px;
  font-size: 14px;
}

.tip-text {
  color: red;
}
</style>
