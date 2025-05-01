<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="default" :inline="true">
      <el-form-item label="Field Name" prop="tableEnName">
        <el-input
            v-model="queryParams.fieldName"
            placeholder="Enter field Name"
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

    <el-table v-loading="loading"
              :data="fieldList"
              row-key="id"
              :tree-props="{ children: 'childList', hasChildren: 'childList.length' }"
              style="width: 100%">

      <!-- 选择框 -->
      <el-table-column type="selection" width="55" align="center" />

      <!-- Field Name 列 -->
      <el-table-column label="Field Name" align="center" prop="fieldName" />

      <!-- Formula 列 -->
      <el-table-column label="Formula" align="center" prop="fieldFormula" />

      <!-- Type 列 -->
      <el-table-column label="Type" align="center">
        <template #default="{ row }">
          <span>{{ row.header === 0 ? 'Basic' : 'Header' }}</span>
        </template>
      </el-table-column>

      <!-- Sort Order 列 -->
      <el-table-column label="Sort Order" align="center" prop="sortOrder" />

      <!-- Operation 列 -->
      <el-table-column label="Operation" align="center" width="250">
        <template #default="{ row }">
          <el-button size="default" type="text" @click="handleEdit(row)">Edit</el-button>
          <el-button size="default" type="text" @click="handleDelete(row)">Delete</el-button>
        </template>
      </el-table-column>

    </el-table>



    <!-- 弹窗表单 -->
    <el-dialog v-model="dialogVisible"
               :title="dialogTitle"
               width="80%"
               class="custom-dialog"
    >
      <el-form :model="form" label-width="150px">
        <!-- Type 单选框 -->
        <el-form-item label="Field Type" prop="type">
          <el-radio-group v-model="form.header">
            <el-radio :label="0">Basic Field</el-radio>
            <el-radio :label="1">Header Field</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- Parent Name 字段，仅在选择 Header Field 时显示，数据来源于 fieldList 中 header === 1 的字段 -->
        <el-form-item label="Parent Name" prop="parentName" >
          <el-select v-model="form.fieldParentId" placeholder="Select Parent Name">
            <!-- 根据 fieldList 中的 header === 1 的字段生成下拉框 -->
            <el-option
                v-for="field in fieldList.filter(item => item.header === 1)"
                :key="field.id"
                :label="field.fieldName"
                :value="field.id"
            />
          </el-select>
        </el-form-item>
        <!-- 字段名称 -->
        <el-form-item label="Field Name" prop="fieldName">
          <el-input v-model="form.fieldName" />
        </el-form-item>
        <el-form-item  label="Order" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" :min="1"  />
        </el-form-item>

        <!-- Formula 输入框 -->
        <el-form-item label="Formula" prop="formula" v-if="form.header === 0">
          <el-input
              v-model="form.fieldFormula"
              placeholder="Enter formula"
              class="formula-input"
              >
            <!-- 在输入框前添加加号按钮 -->
            <template #append>
              <el-button @click="addFormula" type="text" class="large-plus-button">+</el-button>
            </template>
          </el-input>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button type="primary" @click="submitForm">Submit</el-button>
        <el-button @click="dialogVisible = false">Cancel</el-button>
      </template>
    </el-dialog>

    <el-dialog
        v-model="secondDialogVisible"
        :title="'公式编辑器'"
        width="80%"
        class="formula-dialog"
    >
      <div class="formula-layout">
        <!-- 左侧：字段选择树 -->
        <div class="left-panel">
          <div class="panel-title">可用字段</div>
          <el-tree
              :data="columnConfigList"
              :props="treeProps"
              node-key="columnId"
              show-checkbox
              :check-strictly="true"
              @check-change="handleFieldSelect"
          >
            <template #default="{ node }">
          <span class="tree-node">
            <i class="el-icon-menu"></i>
            {{ node.label }}
          </span>
            </template>
          </el-tree>
        </div>

        <!-- 中间：函数/操作符 -->
        <div class="center-panel">
          <div class="panel-title">函数列表</div>
          <el-tabs type="border-card">
            <!-- 数学函数 -->
            <el-tab-pane label="数学函数">
              <div class="function-item"
                   v-for="func in mathFunctions"
                   :key="func.name"
                   @click="insertFunction(func)">
                <span class="func-name">{{ func.name }}</span>
                <span class="func-desc">{{ func.desc }}</span>
              </div>
            </el-tab-pane>

            <!-- 条件逻辑 -->
            <el-tab-pane label="条件逻辑">
              <div class="function-item"
                   v-for="func in conditionFunctions"
                   :key="func.name"
                   @click="insertFunction(func)">
                <span class="func-name">{{ func.name }}</span>
                <span class="func-desc">{{ func.desc }}</span>
              </div>
            </el-tab-pane>

            <el-tab-pane label="其他逻辑">
              <div class="function-item"
                   v-for="func in otherFunctions"
                   :key="func.name"
                   @click="insertFunction(func)">
                <span class="func-name">{{ func.name }}</span>
                <span class="func-desc">{{ func.desc }}</span>
              </div>

            </el-tab-pane>
          </el-tabs>
        </div>

        <!-- 右侧：公式编辑区 -->
        <div class="right-panel">
          <div class="panel-title">编辑公式</div>
          <div class="formula-editor-container">
            <el-input
                v-model="currentFormulaValue"
                type="textarea"
                :rows="8"
                placeholder="示例：IF([销售额] > 1000, [奖金] * 0.1, 0)"
                ref="formulaEditor"
                class="formula-editor"
                :class="{ 'formula-error': hasFormulaError }"
            />
          </div>
        </div>
      </div>

      <template #footer>
        <el-button @click="secondDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmFormula">确认</el-button>
      </template>
    </el-dialog>



  </div>
</template>

<script lang="ts" setup>
import {ComponentPublicInstance, nextTick, onMounted, reactive, ref} from "vue";
import {useRoute} from "vue-router";
import {tableConfigService} from "@/service/tableconfig";
import {payrollAttributeService} from "@/service/payroll/attribute";
import {ElMessage, ElMessageBox} from "element-plus";

const route = useRoute();
const otherTableId=route.params.otherTableId;
const TableId=route.params.tableId;
const isEdit = ref(false); // 用来确认是否是编辑操作

const columnConfigList = ref([]);
let ColumnList = ref([]);
let parentColumnList = ref([]);
const selectedColumns = ref([]);
const formulaEditor = ref<ComponentPublicInstance | null>(null);

// el-tree 配置
const treeProps = {
  children: 'children', // 子节点的字段名
  label: 'columnName',  // 节点标签的字段名
};// 用来存储用户选择的字段

const total = ref(0); // 总条数

// 控制弹窗的显示与隐藏
const dialogVisible = ref(false);
const dialogTitle = ref('Add Formula');
const secondDialogVisible = ref<boolean>(false);
const loading = ref(true);
const fieldList = ref([]);
const queryParams = reactive({
  fieldName:"",
  otherTableId:otherTableId,
  tableId:""
});


const form = reactive({
  id:"",
  baseTableId:TableId,
  otherTableId:otherTableId,
  fieldParentId:"",
  fieldName:"",
  fieldFormula:"",
  sortOrder:"",
  header:"",
  remark:""
});


// 查询操作
const handleQuery = () => {
  getList();
};

// 新增
const handleAdd = () => {
  console.log("1111111111111111111")
  dialogTitle.value = "Add Filed";
  isEdit.value = false; // 设置为新增操作
  resetForm();
  dialogVisible.value = true;
  console.log("dialogVisible:", dialogVisible.value); // 应打印 true
};


// 新增
const handleEdit = async (row) => {
  dialogTitle.value = "Edit Formula";
  isEdit.value = true; // 设置为编辑操作
  dialogVisible.value = true;
  try {
    // 异步请求获取数据
    const response = await payrollAttributeService.get(row.id);
    // 确保返回数据存在，并将数据复制到表单
    if (response && response.data) {
      Object.assign(form, response.data); // 将返回的数据赋值给 form
    } else {
      console.error("No data returned from the service.");
    }
  } catch (error) {
    console.error("Error fetching table details:", error);
  }
};

// 删除
const handleDelete = async (row) => {
  try {
    // 显示确认删除的提示框
    const isConfirmed = await ElMessageBox.confirm(
        `Are you sure you want to delete ${row.fieldName}?`,
        'Warning',
        {
          confirmButtonText: 'Yes',
          cancelButtonText: 'No',
          type: 'warning',
        }
    );

    // 如果用户点击了确认，执行删除操作
    if (isConfirmed === 'confirm') {
      await payrollAttributeService.del(row.id); // 调用删除接口
      getList(); // 刷新列表
      ElMessage.success(`${row.fieldName} deleted successfully!`); // 显示成功提示
    }
  } catch (error) {
    console.error("Error deleting table definition:", error);
    ElMessage.error("Failed to delete item."); // 显示错误提示
  }
};


// 提交表单
const submitForm = async () => {
  form.baseTableId=TableId;
  form.otherTableId=otherTableId;
  if (form.id) {
    await payrollAttributeService.edit(form);
  } else {
    await payrollAttributeService.add(form);
  }
  dialogVisible.value = false;
  getList();
};

// 查询数据列表
const getList = async () => {
  loading.value = true;
  try {
    queryParams.otherTableId=otherTableId;
    const response = await payrollAttributeService.list(queryParams);
    console.log(response,7777777777777777)
    fieldList.value = response.data;
    // fieldList.value = buildTree(rows);
  } finally {
    loading.value = false;
  }
};

const buildTree = (list) => {
  const map = {};
  list.forEach(item => map[item.id] = { ...item, children: [] });
  const tree = [];
  list.forEach(item => {
    if (item.fieldParentId) {
      map[item.fieldParentId].children.push(map[item.id]);
    } else {
      tree.push(map[item.id]);
    }
  });
  return tree;
};

// 获取表格数据
const getColumnList = async () =>{
  try {
    queryParams.tableId=TableId;
    console.log("1111111111111111111")
    const response = await tableConfigService.listTemp(queryParams);
    columnConfigList.value = formatToTree(response.rows);
    // console.log(columnConfigList.value,ppppppppppp)
    // ColumnList = columnConfigList.value.filter((item) => item.columnType === 0);
    // parentColumnList = columnConfigList.value.filter((item) => item.columnType === 1);
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
    map[item.columnId] = { ...item, children: [] };
  });

  data.forEach((item) => {
    if (item.columnParentDbname && map[item.columnParentDbname]) {
      map[item.columnParentDbname].children.push(map[item.columnId]);
    } else {
      treeData.push(map[item.columnId]);
    }
  });
  console.log(treeData,"TreeData")
  return treeData;
};


// 添加加号到公式中的方法
const addFormula = () => {
  selectedColumns.value = [];
  let matches = [];
  const regex = /\[([^\]]+)\]/g;
  currentFormulaValue.value = form.fieldFormula.replace(regex, (match, p1) => {
    matches.push(p1);
    return p1;
  })

  secondDialogVisible.value = true;
};



const resetForm = () => {
  form.id = "";
  form.baseTableId = TableId;  // 可能需要保留 TableId
  form.otherTableId = otherTableId;  // 可能需要保留 otherTableId
  form.fieldParentId = "";
  form.fieldName = "";
  form.fieldFormula = "";
  form.sortOrder = "";
  form.header = "";  // 可能默认为 0
  form.remark = "";
};

/// ====================== 数学运算 ======================
const mathFunctions = ref([
  {
    name: '+',
    desc: '加法：字段1 + 字段2',
    template: '{field1} + {field2}',
    params: [
      { name: 'field1', desc: '选择第一个字段', type: 'field' },
      { name: 'field2', desc: '选择第二个字段', type: 'field' }
    ],
    example: 'salary + bonus'
  },
  {
    name: '-',
    desc: '减法：字段1 - 字段2',
    template: '{field1} - {field2}',
    params: [
      { name: 'field1', desc: '选择第一个字段', type: 'field' },
      { name: 'field2', desc: '选择第二个字段', type: 'field' }
    ],
    example: 'total - discount'
  },
  {
    name: '*',
    desc: '乘法：字段1 × 字段2',
    template: '{field1} * {field2}',
    params: [
      { name: 'field1', desc: '选择第一个字段', type: 'field' },
      { name: 'field2', desc: '选择第二个字段', type: 'field' }
    ],
    example: 'price * quantity'
  },
  {
    name: '/',
    desc: '除法：字段1 ÷ 字段2',
    template: '{field1} / {field2}',
    params: [
      { name: 'field1', desc: '选择第一个字段', type: 'field' },
      { name: 'field2', desc: '选择第二个字段（非零）', type: 'field' }
    ],
    example: 'revenue / employees'
  },
  {
    name: 'ROUND()',
    desc: '四舍五入到指定位数',
    template: 'ROUND({value}, {decimal})',
    params: [
      { name: 'value', desc: '需要四舍五入的字段', type: 'field' },
      { name: 'decimal', desc: '小数位数（0-10）', type: 'number' }
    ],
    example: 'ROUND(total, 2)'
  }
]);

// ====================== 条件逻辑 ======================
const conditionFunctions = ref([
  {
    name: 'IF()',
    desc: '条件判断：IF(条件, 真值, 假值)',
    template: 'IF({condition} {operator} {value}, {true_value}, {false_value})',
    params: [
      { name: 'condition', desc: '条件字段', type: 'field' },
      { name: 'operator', desc: '比较运算符', type: 'operator', options: ['>', '<', '=', '>=', '<='] },
      { name: 'value', desc: '比较值', type: 'mixed' },
      { name: 'true_value', desc: '满足条件的值', type: 'mixed' },
      { name: 'false_value', desc: '不满足条件的值', type: 'mixed' }
    ],
    example: "IF(age > 18, '成年', '未成年')"
  },
  {
    name: 'CASE',
    desc: '多条件分支判断',
    template: `CASE
      WHEN {condition1} THEN {result1}
      WHEN {condition2} THEN {result2}
      ELSE {default_result}
    END`,
    params: [
      { name: 'condition1', desc: '第一个条件表达式', type: 'expression' },
      { name: 'result1', desc: '第一个结果值', type: 'mixed' },
      { name: 'condition2', desc: '第二个条件表达式', type: 'expression' },
      { name: 'result2', desc: '第二个结果值', type: 'mixed' },
      { name: 'default_result', desc: '默认结果值', type: 'mixed' }
    ],
    example: `CASE
      WHEN score >= 90 THEN 'A'
      WHEN score >= 80 THEN 'B'
      ELSE 'C'
    END`
  }
]);

// ====================== 其他常用函数 ======================
const otherFunctions = ref([
  {
    name: 'CONCAT()',
    desc: '字符串拼接',
    template: 'CONCAT({field1}, "{sep}", {field2})',
    params: [
      { name: 'field1', desc: '第一个字段或字符串', type: 'mixed' },
      { name: 'sep', desc: '分隔符', type: 'string', default: '-' },
      { name: 'field2', desc: '第二个字段或字符串', type: 'mixed' }
    ],
    example: "CONCAT(first_name, ' ', last_name)"
  },
  {
    name: 'DATE_FORMAT()',
    desc: '日期格式化',
    template: "DATE_FORMAT({date_field}, '%Y-%m-%d')",
    params: [
      { name: 'date_field', desc: '日期字段', type: 'field' },
      { name: 'format', desc: '格式字符串', type: 'string', options: ['%Y-%m', '%Y/%m/%d', '%H:%i'] }
    ],
    example: "DATE_FORMAT(create_time, '%Y-%m')"
  },
  {
    name: 'COALESCE()',
    desc: '空值替换',
    template: 'COALESCE({field}, {default_value})',
    params: [
      { name: 'field', desc: '需要检查的字段', type: 'field' },
      { name: 'default_value', desc: '默认值', type: 'mixed' }
    ],
    example: 'COALESCE(bonus, 0)'
  }
]);


// 公式编辑器状态
const currentFormula = ref('');
const currentFormulaValue = ref('');
const selectedFields = ref([]);
const hasFormulaError = ref(false);


const insertFunction = (func) => {
  nextTick(() => {
    // 1. 获取真实的 textarea 元素
    const textareaEl = formulaEditor.value?.$el?.querySelector('textarea');
    if (!textareaEl) {
      console.error('未找到公式编辑器');
      return;
    }

    // 2. 获取光标位置
    const startPos = textareaEl.selectionStart;
    const endPos = textareaEl.selectionEnd;

    // 3. 插入模板
    currentFormulaValue.value = currentFormulaValue.value.slice(0, startPos) +
        func.template +
        currentFormulaValue.value.slice(endPos);

    // 4. 自动聚焦到第一个占位符
    setTimeout(() => {
      const firstPlaceholder = currentFormulaValue.value.indexOf('{');
      if (firstPlaceholder > -1) {
        textareaEl.focus();
        textareaEl.setSelectionRange(firstPlaceholder, firstPlaceholder + 8);
      }
    }, 50);
  });
};


const handleConfirmFormula = () => {
  // 移除所有反引号
  let storedFormula = currentFormulaValue.value.replace(/`/g, '');

  // 精确替换字段映射（如果需要保留字段映射关系）
  selectedFields.value.forEach(field => {
    // 使用单词边界确保精确匹配
    const fieldReg = new RegExp(`\\b${field.field}\\b`, 'g');
    storedFormula = storedFormula.replace(fieldReg, field.field);
  });

  // 更新到表单字段
  form.fieldFormula = storedFormula;
  secondDialogVisible.value = false;
};



// 字段选择处理（显示Label，存储columnId）
const handleFieldSelect = (node, checked) => {
  const textareaEl = formulaEditor.value?.$el?.querySelector('textarea');
  if (!textareaEl) return;

  // 获取当前光标位置
  const startPos = textareaEl.selectionStart;
  const endPos = textareaEl.selectionEnd;
  if (checked) {
    // 在光标位置插入字段
    currentFormulaValue.value =
        currentFormulaValue.value.slice(0, startPos) +
        `${node.columnDbname}` +
        currentFormulaValue.value.slice(endPos);

    // 更新光标位置到插入内容之后
    setTimeout(() => {
      const newPos = startPos + node.columnDbname.length + 2; // +2 是反引号
      textareaEl.setSelectionRange(newPos, newPos);
      textareaEl.focus();
    }, 0);
  } else {
    // 取消选择时移除
    const index = selectedFields.value.findIndex(f => f.field === node.columnDbname);
    if (index > -1) {
      selectedFields.value.splice(index, 1);
      currentFormula.value = currentFormula.value.replace(new RegExp(node.columnName, 'g'), '');
      currentFormulaValue.value = currentFormulaValue.value.replace(new RegExp(node.columnDbname, 'g'), '');

    }
  }
};




// 生命周期
onMounted(() => {
  getList();
  getColumnList()
  if (!formulaEditor.value) {
    console.error('公式编辑器组件未正确挂载');
  }
});
</script>

<style scoped>
.custom-dialog {
  height: 80%;  /* 设置弹框的高度为页面高度的80% */
}

.formula-input {
  margin-top: 10px;
  width: 100%;  /* 使文本框宽度占满父容器 */
  min-height: 100px;  /* 设置文本框的最小高度 */
}

.large-plus-button {
  font-size: 36px; /* 设置更大的字体大小 */
  padding: 10px 20px; /* 增加按钮的内边距 */
  line-height: 1; /* 确保文本居中 */
  min-width: 40px; /* 防止按钮过小 */
}

.formula-dialog {
  .formula-layout {
    display: grid;
    grid-template-columns: 250px 200px 1fr;
    gap: 16px;
    height: 60vh;

    .panel-title {
      font-weight: 500;
      margin-bottom: 12px;
      color: var(--el-color-primary);
    }

    .left-panel {
      border-right: 1px solid var(--el-border-color);
      padding-right: 16px;

      .tree-node {
        display: flex;
        align-items: center;
        padding: 6px 0;

        i {
          margin-right: 8px;
          color: var(--el-color-info);
        }
      }
    }

    .center-panel {
      .function-item {
        padding: 8px;
        cursor: pointer;
        transition: background 0.2s;

        &:hover {
          background: var(--el-color-primary-light-9);
        }

        .func-name {
          color: var(--el-color-primary);
          font-family: Consolas, Monaco, monospace;
        }

        .func-desc {
          color: var(--el-text-color-secondary);
          font-size: 12px;
          margin-left: 8px;
        }
      }
    }

    .right-panel {
      .formula-editor-container {
        border: 1px solid var(--el-border-color);
        border-radius: 4px;

        .formula-editor {
          font-family: 'JetBrains Mono', monospace;
          font-size: 14px;
          border: none;

          &.formula-error {
            box-shadow: 0 0 3px var(--el-color-danger);
          }
          .formula-label {
            color: #409EFF;
            font-weight: 500;
            padding: 2px 4px;
            background: #ecf5ff;
            border-radius: 3px;
          }

        }

        .formula-preview {
          border-top: 1px solid var(--el-border-color);
          padding: 12px;

          .preview-title {
            color: var(--el-text-color-secondary);
            margin-bottom: 8px;
          }
        }
      }
    }
  }
}




</style>

