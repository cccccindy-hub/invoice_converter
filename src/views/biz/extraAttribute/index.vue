<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="Name" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="Please enter"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="search" size="small" @click="handleQuery">Search</el-button>
        <el-button icon="refresh" size="small" @click="resetQuery">Reset</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="plus"
          size="small"
          @click="handleAdd"
          v-hasPermi="['attribute:add']"
        >Add</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="edit"
          size="small"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['attribute:edit']"
        >Edit</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="delete"
          size="small"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['attribute:remove']"
        >Delete</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="download"
          size="small"
          @click="handleExport"
          v-hasPermi="['attribute:export']"
        >Export</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="attributeList" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="Name" align="center" prop="name" />
      <el-table-column label="Type" align="center" prop="type" >
        <template v-slot:default="scope">
          <span v-if="scope.row.type === 'json'">Parent Attribute</span>
          <span v-else>{{ scope.row.type }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="Length" align="center" prop="length" /> -->
      <el-table-column label="Parent Attribute" align="center" prop="parentName">
        <template v-slot:default="scope">
          <span v-if="scope.row.parentName === null ">N/A</span>
          <span v-else>{{ scope.row.parentName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Operation" align="center" class-name="small-padding fixed-width">
        <template v-slot:default="scope">
          <el-button
            size="small"
            link
            type="primary"
            icon="edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['attribute:edit']"
          >Edit</el-button>
          <el-button
            size="small"
            link
            type="primary"
            icon="delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['attribute:remove']"
          >Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />

    <!-- Add and Edit Form -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
                <!-- Field Name -->
                <el-form-item label="Attribute Name" prop="name">
                    <el-input v-model="form.name" placeholder="" />
                </el-form-item>

                <!-- Field Type -->
                <el-form-item label="Attribute Type" prop="type">
                    <el-select v-model="form.type" placeholder="" @change="updateLength(form.type)">
                        <el-option label= "Text" value="text"></el-option>
                        <el-option label="Number" value="number"></el-option>
                        <el-option label="String" value="string"></el-option>
                        <el-option label="Date" value="date"></el-option>
                        <el-option label= "Boolean" value="boolean"></el-option>
                        <el-option label= "Parent Attribute" value="json"></el-option>
                    </el-select>
                </el-form-item>

                <!-- Field Type (dynamically generated options) -->
                <el-form-item  label="Parent Attribute" prop="parentId">
                    <el-select :disabled="isDisabled" v-model="form.parentId" placeholder="">
                        <el-option v-for="(option, index) in parentAttributeList" :key="index" :label="option.name"
                            :value="option.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="Searchable" prop="searchable" value="1">
                   <!-- <el-checkbox v-model ="form.searchable" label="Searchable" /> -->
                   <el-checkbox v-model="form.searchable"/>
                </el-form-item>
                <el-form-item  label="Order" prop="sortOrder">
                  <el-input-number v-model="form.sortOrder" :min="1"  />
                </el-form-item>
                <el-form-item  label="Default Options" prop="options">
                <el-select
                    v-model="form.options"
                    multiple
                    filterable
                    allow-create
                    default-first-option
                    :reserve-keyword="false"
                    placeholder="Create Options for your new attribute"
                    style="width: 240px"
                  >
                    <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                </el-select>
                </el-form-item>

            </el-form>
            <template #footer>
                <div class="dialog-footer">
                <el-button type="primary" @click="submitForm">Confirm</el-button>
                <el-button @click="cancel">Cancel</el-button>
                </div>
            </template>
    </el-dialog>
  </div>
</template>

<script>
import { listAttribute, getAttribute, delAttribute, addAttribute, updateAttribute, listParentAttribute} from "@/service/attribute";

export default {
  name: "Attribute",
  data() {
    return {
      isTypeDisabled: false,
      // 遮罩层
      loading: true,
      // Selected array
      ids: [],
      // Disabled if not a single selection
      single: true,
      // Disabled if not multiple selections
      multiple: true,
      // Display search criteria
      showSearch: true,
      // Total count
      total: 0,
      // [Please fill in the feature name] table data
      attributeList: [],
      parentAttributeList: [],
      // Popup title
      title: "",
      // Whether to display the popup
      open: false,
      tableName: "",
// Query parameters
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        type: null,
        length:null,
        parentId: null,
        createdAt: null,
        updatedAt: null,
        tableName: this.tableName
      },

      lengthMapping: {
        "text": 60000,
        "string": 256,
        "number": null, // No fixed length for numbers
        "date": null,   // No fixed length for dates
        "boolean": 1,   // Boolean values are often represented as 1
        "json": null    // JSON type has no specific length
      },

      // form params
      form: {},
      // form validation
      rules: {
        name: [
          { required: true, message: "Not Empty", trigger: "blur" }
        ],
        type: [
          { required: true, message: "Not Empty", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getTableNameFromQuery();
    this.getList();
  },
  computed: {
    isDisabled: function () {
        return this.form.type === "json"
    }
},     
  methods: {
    /** Query extra attribute list */
    getList() {
      this.loading = true;
      listAttribute(this.queryParams).then(response => {
        this.attributeList = response.rows;
        console.log(JSON.stringify(this.attributeList, null, 2));

        this.total = response.total;
        this.loading = false;
      });
    },
    // handle cancle button
    cancel() {
      this.open = false;
      this.isTypeDisabled = false
      this.reset();
    },
    // reset form params
    reset() {
      this.form = {
        id: null,
        name: null,
        type: null,
        length: null,
        parentId: null,
        searchable: null,
        sortOrder:null,
        options:[],
        tableName: this.tableName,
        createdAt: null,
        updatedAt: null,
      };
      this.resetForm("form");
    },
    /** handle query button */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** reset query params */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    getTableNameFromQuery() {
      const queryString = window.location.search;
      const urlParams = new URLSearchParams(queryString);
      const tableName = urlParams.get('tableName');
      this.tableName = tableName
      this.queryParams.tableName = tableName;
    },
    getParentAttribute() {
      listParentAttribute(this.tableName).then(response => {
          this.parentAttributeList = response.rows;
          this.total = response.total;
          this.loading = false;
      });
    },
    // Data selected via multi-select checkbox
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    // Disable "single" actions if the selection is not exactly one item
    handleAdd() {
      this.getParentAttribute();
      this.reset();
      this.open = true;
      this.title = "Add Attribute";
    },
    // Disable "multiple" actions if no items are selected
    handleUpdate(row) {
      this.getParentAttribute();
      this.isTypeDisabled = true
      this.reset();
      const id = row.id || this.ids
      getAttribute(id, this.tableName).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = 'Edit Attribute';
      });
    },
    updateLength(selectedType) {
      this.form.length = this.lengthMapping[selectedType] || null;
    },

    /** handle submit button */
   submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.id != null) {
                        updateAttribute(this.form).then(response => {
                            this.$modal.msgSuccess("Update successful");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addAttribute(this.form).then(response => {
                            this.$modal.msgSuccess("Addition successful");
                            this.open = false;
                            this.getList();
                        });
                    }
                }
            });
            this.isTypeDisabled = false
            
        },
    /** handle delete button */
    handleDelete(row) {
      const id = row.id || this.ids;
      const tableName = this.tableName;
      this.$modal.confirm('Are you sure you want to delete the attribute with name : "' + row.name + '"?').then(function() {
        return delAttribute(id, tableName);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("Deletion successful");
      }).catch(() => {});
    },
    /** handle export button */
    handleExport() {
      this.download('system/attribute/export', {
        ...this.queryParams
      }, `attribute_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
