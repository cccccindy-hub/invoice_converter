<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            size="default"
            @click="handleExport"
        >Export
        </el-button>
      </el-col>
    </el-row>

    <!-- 表格部分 -->
    <el-table
        v-loading="loading"
        :data="fieldDataList"
        style="width: 100%"
        row-key="id"
        border
    >
      <!-- 选择框 -->
      <el-table-column type="selection" width="55" align="center" />

      <!-- 动态生成表头 -->
      <template v-for="(column, index) in fieldList" :key="index">
        <!-- 如果当前列是父级表头 -->
        <el-table-column
            v-if="column.childList && column.childList.length"
            :label="column.fieldName"
            align="center"
        >
          <!-- 递归渲染子表头 -->
          <el-table-column
              v-for="(child, childIndex) in column.childList"
              :key="childIndex"
              :label="child.fieldName"
              :prop="child.fieldName"
              align="center"
          />
        </el-table-column>

        <!-- 如果当前列是普通字段 -->
        <el-table-column
            v-else
            :label="column.fieldName"
            :prop="column.fieldName"
            align="center"
        />
      </template>
    </el-table>


    <el-pagination
        background
        layout="prev, pager, next, sizes, total"
        :total="total"
        :page-size="queryParams.pageSize"
        :current-page="queryParams.pageNum"
        @current-change="handlePageChange"
        @size-change="handlePageSizeChange"
    />

  </div>
</template>

<script lang="ts" setup>
import {onMounted, reactive, ref} from "vue";
import {useRoute} from "vue-router";
import * as XLSX from 'xlsx'; // 导入 xlsx 库
import {payrollConfigService} from "@/service/payroll/config";
import {payrollAttributeService} from "@/service/payroll/attribute";

const route = useRoute();
const otherTableId=route.params.otherTableId;
// 状态管理
const loading = ref(true);
const dialogFormVisible = ref(false);
const showSearch = ref(true);
const title = ref("");
const field = ref("");

const fieldList = ref([]);
const fieldDataList = ref([]);
const total = ref(0);
const queryParams = reactive({
  id:null,
  params:{
    pageNum: 1,
    pageSize: 10,
  }
});

const queryField = reactive({
  otherTableId:null
});

const getList = async () => {
  loading.value = true;
  try {
    console.log(otherTableId,"ooooooooooooooooooo")
    queryParams.id = otherTableId; // 确保 otherTableId 已经正确设置
    console.log("queryParams before request:", queryParams);  // 打印 queryParams 确保 id 被设置
    const response= await payrollConfigService.View(queryParams); // 调用请求方法
    fieldDataList.value = response.rows;
    total.value = response.total;
  } finally {
    loading.value = false;
  }
};

const getField = async () => {
  try {
    queryField.otherTableId = otherTableId;
    const response = await payrollAttributeService.list(queryField);
    // fieldList.value = rows; // 排序字段
    fieldList.value = response.data;
  } catch (error) {
    console.error("Failed to fetch Field config:", error);
  }
};

// 修改页码
const handlePageChange = (newPage: number) => {
  queryParams.params.pageNum = newPage;
  getList();
};

// 修改每页显示条数
const handlePageSizeChange = (newPageSize: number) => {
  queryParams.params.pageSize = newPageSize;
  queryParams.params.pageNum = 1; // 重置到第一页
  getList();
};

// 定义递归函数，按层级生成表头
const groupByLevel = (data, level = 0, result = []) => {
  data.forEach(item => {
    if (item.header === 1) {  // 表头字段
      const groupedItem = { ...item, level };  // 添加层级信息
      result.push(groupedItem);  // 将当前表头字段加入结果

      // 如果该字段有子项 (childList)，递归处理子项
      if (item.childList && item.childList.length > 0) {
        groupByLevel(item.childList, level + 1, result);  // 递归处理下一层
      }
    }
  });

  return result;
};

const handleExport = () => {
  // 收集所有叶子节点及其路径
  const leavesWithPaths = collectLeavesWithPaths(fieldList.value);
  const maxDepth = Math.max(...leavesWithPaths.map(l => l.path.length));
  console.log(leavesWithPaths,maxDepth,"77")
  // 生成表头标签数组
  const headerRows = Array.from({ length: maxDepth }, () => []);
  leavesWithPaths.forEach(leaf => {
    const path = leaf.path;
    for (let i = 0; i < maxDepth; i++) {
      headerRows[i].push(i < path.length ? path[i].fieldName : '');
    }
  });
  console.log(headerRows,"hhhhhhhhhhhhhh")



  // 生成合并区域
  const merges: XLSX.Range[] = [];
// 处理水平合并（同一层级连续的相同父节点）
  for (let r = 0; r < maxDepth; r++) {
    let startCol = 0; // 记录当前合并区域的开始列
    let currentHeader = headerRows[r][0]; // 当前表头字段值，初始为第一列

    for (let c = 1; c <= headerRows[r].length; c++) {
      // 如果当前列的表头与前一列相同，继续合并
      if (headerRows[r][c] === currentHeader && headerRows[r][c] !== "" && currentHeader !== "") {
        continue;
      } else {
        // 如果连续相同的表头字段有多个列，合并这些列
        if (c - startCol > 1) {
          merges.push({ s: { r, c: startCol }, e: { r, c: c - 1 } });
        }

        // 更新当前字段和开始列
        startCol = c;
        currentHeader = headerRows[r][c];
      }
    }

    // 处理最后一列的合并（防止遗漏最后一段连续的表头）
    if (headerRows[r].length - startCol > 1) {
      merges.push({ s: { r, c: startCol }, e: { r, c: headerRows[r].length - 1 } });
    }
  }

  console.log(leavesWithPaths,"leleleeeeeeeeeeeeee")
  // 处理垂直合并（普通字段跨行）
  leavesWithPaths.forEach((leaf, col) => {
    if (leaf.path.length === 1) {
      merges.push({ s: { r: 0, c: col }, e: { r: maxDepth - 1, c: col } });
    }
  });

  // 生成数据部分
  const data = fieldDataList.value.map(row =>
      leavesWithPaths.map(leaf => row[leaf.field.fieldName] || '')
  );



  // 创建工作表并设置合并
  const ws = XLSX.utils.aoa_to_sheet([...headerRows, ...data]);

  // 设置表头单元格样式（加粗并居中）
  for (let c = 0; c < headerRows[0].length; c++) {
    for (let r = 0; r < maxDepth; r++) {
      const cell = ws[XLSX.utils.encode_cell({ r, c })];
      if (!cell) {
        ws[XLSX.utils.encode_cell({ r, c })] = {};  // 如果单元格不存在，则创建一个空单元格
      }
      ws[XLSX.utils.encode_cell({ r, c })].s = {
        font: { bold: true },  // 加粗
        alignment: { horizontal: 'center', vertical: 'center' }  // 水平和垂直居中
      };
    }
  }

  ws['!merges'] = merges.map(merge => ({
    s: { r: merge.s.r, c: merge.s.c },
    e: { r: merge.e.r, c: merge.e.c }
  }));

  // 动态设置列宽
  ws['!cols'] = headerRows[0].map((_, colIndex) => {
    let maxLength = headerRows.reduce((max, row) => {
      return Math.max(max, (row[colIndex] || '').length);
    }, 0);
    // 计算数据部分的最大长度
    data.forEach(row => {
      maxLength = Math.max(maxLength, (row[colIndex] || '').toString().length);
    });
    return {wpx: maxLength * 10};  // 增加适当的宽度，单位为像素
  });

  // 导出文件
  const wb = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(wb, ws, 'Sheet1');
  XLSX.writeFile(wb, 'export_data.xlsx');
};

interface Field {
  fieldName: string;
  childList?: Field[];
}

const collectLeavesWithPaths = (fields: Field[]): { field: Field, path: Field[] }[] => {
  const leaves: { field: Field, path: Field[] }[] = [];
  const traverse = (field: Field, path: Field[]) => {
    const currentPath = [...path, field];
    if (field.childList?.length) {
      field.childList.forEach((child) => traverse(child, currentPath));
    } else {
      leaves.push({ field, path: currentPath });
    }
  };
  fields.forEach((field) => traverse(field, []));
  return leaves;
};


// 生命周期钩子
onMounted(() => {
  getField();
  getList();
});
</script>
