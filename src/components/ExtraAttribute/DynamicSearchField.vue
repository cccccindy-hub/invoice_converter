<template>
    <span v-for="attribute in extraAttributeForm" :key="attribute.id">
        <el-form-item v-if="attribute.searchable === true" :label="`${attribute.name}`">
            <el-select
                v-if="attribute.searchable === true && attribute.type.toLowerCase() === 'string' && attribute.options && attribute.options.length > 0"
                v-model="queryParams.extraData[attribute.id]"
                placeholder="Select"
                style="width: 240px"
                clearable
            >
                <el-option
                    v-for="item in attribute.options"
                    :key="item"
                    :label="item"
                    :value="item"
                />
            </el-select>
            <!-- String without options -->
            <el-input
                v-else-if="attribute.searchable === true && attribute.type.toLowerCase() === 'string'"
                v-model="queryParams.extraData[attribute.id]"
                :placeholder="`Please enter ${attribute.name}`"
                clearable
            />

            <!-- Text with options -->
            <el-select
                v-if="attribute.searchable === true && attribute.type.toLowerCase() === 'text' && attribute.options && attribute.options.length > 0"
                v-model="queryParams.extraData[attribute.id]"
                placeholder="Select"
                style="width: 240px"
                clearable
            >
            <el-option
                v-for="item in attribute.options"
                :key="item"
                :label="item"
                :value="item"
                />
            </el-select>

            <!-- Text without options -->
            <el-input
                v-else-if="attribute.searchable === true && attribute.type.toLowerCase() === 'text'"
                v-model="queryParams.extraData[attribute.id]"
                type="textarea"
                :placeholder="`Please enter ${attribute.name}`"
                clearable
            />

            <!-- Number input -->
            <el-input-number
                v-else-if="attribute.searchable === true && attribute.type.toLowerCase() === 'number'"
                v-model="queryParams.extraData[attribute.id]"
                :placeholder="`Please enter ${attribute.name}`"
                clearable
            />

            <!-- Date picker -->
            <el-date-picker
                v-else-if="attribute.searchable === true && attribute.type.toLowerCase() === 'date'"
                v-model="queryParams.extraData[attribute.id]"
                type="date"
                :placeholder="`Please select ${attribute.name}`"
                clearable
            />

            <!-- Boolean radio buttons -->
            <el-radio-group
                v-else-if="attribute.searchable === true && attribute.type.toLowerCase() === 'boolean'"
                v-model="queryParams.extraData[attribute.id]"
                clearable
            >
                <el-radio :label="true">Yes</el-radio>
                <el-radio :label="false">No</el-radio>
            </el-radio-group>

            <span v-for="child of attribute.children" :key="child.id">
                <el-form-item :label="child.name" :prop="child.name">
            <!-- String field for children -->
                    <el-select
                        v-if="child.searchable === true && child.type.toLowerCase() === 'string' && child.options && child.options.length > 0"
                        v-model="queryParams.extraData[attribute.id][child.id]"
                        placeholder="Select"
                        style="width: 240px"
                        clearable
                    >
                        <el-option
                            v-for="item in child.options"
                            :key="item"
                            :label="item"
                            :value="item"
                        />
                        </el-select>
                        <el-input
                            v-else-if="child.searchable === true && child.type.toLowerCase() === 'string'"
                            v-model="queryParams.extraData[attribute.id][child.id]"
                            :placeholder="`Please enter ${child.name}`"
                            clearable
                            />
                        <el-select
                            v-else-if="child.searchable === true && child.type.toLowerCase() === 'text' && child.options && child.options.length > 0"
                            v-model="queryParams.extraData[attribute.id][child.id]"
                            placeholder="Select"
                            clearable
                            style="width: 240px"
                        >
                            <el-option
                            v-for="item in child.options"
                            :key="item"
                            :label="item"
                            :value="item"
                            />
                        </el-select>
                        <!-- Text field for children -->
                        <el-input
                        v-else-if="child.searchable === true && child.type.toLowerCase() === 'text'"
                        v-model="queryParams.extraData[attribute.id][child.id]"
                        type="textarea"
                        clearable
                        :placeholder="`Please enter ${child.name}`"
                        />
                        <!-- Number field for children -->
                        <el-input-number
                        v-else-if="child.searchable === true && child.type.toLowerCase() === 'number'"
                        v-model="queryParams.extraData[attribute.id][child.id]"
                        :placeholder="`Please enter ${child.name}`"
                        clearable
                        />

                        <!-- Date picker for children -->
                        <el-date-picker
                        v-else-if="child.searchable === true && child.type.toLowerCase() === 'date'"
                        v-model="queryParams.extraData[attribute.id][child.id]"
                        type="date"
                        :placeholder="`Please select ${child.name}`"
                        clearable
                        />

                        <!-- Boolean radio buttons for children -->
                        <el-radio-group
                        v-else-if="child.searchable === true && child.type.toLowerCase() === 'boolean'"
                        v-model="queryParams.extraData[attribute.id][child.id]"
                        clearable
                        >
                        <el-radio :label="true">Yes</el-radio>
                        <el-radio :label="false">No</el-radio>
                        </el-radio-group>
            </el-form-item>
            </span>
      </el-form-item>
      </span>
</template>

<script>
export default {
  props: {
    // Parent attribute object
    extraAttributeForm: {
      type: Array,
      required: true
    },
    // Query params object (for binding)
    queryParams: {
      type: Object,
      required: true
    }
  },
  methods: {
    // Method to handle the Enter key press (for submitting or querying)
    handleQuery() {
      this.$emit('query');
    }
  }
};
</script>

<style scoped>
/* Add any custom styles if needed */
</style>
