<template>
  <div>
    <el-form-item
      v-for="attribute in extraAttributeForm"
      :key="attribute.id"
      :label="attribute.name"
      :prop="attribute.name"
    >
      <!-- String with options -->
      <el-select
        v-if="attribute.type.toLowerCase() === 'string' && attribute.options && attribute.options.length > 0"
        v-model="form.extraData[attribute.id]"
        placeholder="Select"
        style="width: 240px"
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
        v-else-if="attribute.type.toLowerCase() === 'string'"
        v-model="form.extraData[attribute.id]"
        :placeholder="`Please enter ${attribute.name}`"
      />

      <!-- Text with options -->
      <el-select
        v-if="attribute.type.toLowerCase() === 'text' && attribute.options && attribute.options.length > 0"
        v-model="form.extraData[attribute.id]"
        placeholder="Select"
        style="width: 240px"
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
        v-else-if="attribute.type.toLowerCase() === 'text'"
        v-model="form.extraData[attribute.id]"
        type="textarea"
        :placeholder="`Please enter ${attribute.name}`"
      />

      <!-- Number input -->
      <el-input-number
        v-else-if="attribute.type.toLowerCase() === 'number'"
        v-model="form.extraData[attribute.id]"
        :placeholder="`Please enter ${attribute.name}`"
      />

      <!-- Date picker -->
      <el-date-picker
        v-else-if="attribute.type.toLowerCase() === 'date'"
        v-model="form.extraData[attribute.id]"
        type="date"
        :placeholder="`Please select ${attribute.name}`"
      />

      <!-- Boolean radio buttons -->
      <el-radio-group
        v-else-if="attribute.type.toLowerCase() === 'boolean'"
        v-model="form.extraData[attribute.id]"
        default="false"
      >
        <el-radio :label="true">Yes</el-radio>
        <el-radio :label="false">No</el-radio>
      </el-radio-group>

      <!-- JSON attributes (nested fields) -->
      <div v-else-if="attribute.type.toLowerCase() === 'json'">
        <div v-for="child in attribute.children" :key="child.id">
          <el-form-item :label="child.name" :prop="child.name">
            <!-- String field for children -->
            <el-select
                v-if="child.type.toLowerCase() === 'string' && child.options && child.options.length > 0"
                v-model="form.extraData[attribute.id][child.id]"
                placeholder="Select"
                style="width: 240px"
            >
                <el-option
                    v-for="item in child.options"
                    :key="item"
                    :label="item"
                    :value="item"
                />
            </el-select>
            <el-input
              v-else-if="child.type.toLowerCase() === 'string'"
              v-model="form.extraData[attribute.id][child.id]"
              :placeholder="`Please enter ${child.name}`"
            />
            <el-select
                v-else-if="child.type.toLowerCase() === 'text' && child.options && child.options.length > 0"
                v-model="form.extraData[attribute.id][child.id]"
                placeholder="Select"
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
              v-else-if="child.type.toLowerCase() === 'text'"
              v-model="form.extraData[attribute.id][child.id]"
              type="textarea"
              :placeholder="`Please enter ${child.name}`"
            />
            <!-- Number field for children -->
            <el-input-number
              v-else-if="child.type.toLowerCase() === 'number'"
              v-model="form.extraData[attribute.id][child.id]"
              :placeholder="`Please enter ${child.name}`"
            />

            <!-- Date picker for children -->
            <el-date-picker
              v-else-if="child.type.toLowerCase() === 'date'"
              v-model="form.extraData[attribute.id][child.id]"
              type="date"
              :placeholder="`Please select ${child.name}`"
            />

            <!-- Boolean radio buttons for children -->
            <el-radio-group
              v-else-if="child.type.toLowerCase() === 'boolean'"
              v-model="form.extraData[attribute.id][child.id]"
              default="false"
            >
              <el-radio :label="true">Yes</el-radio>
              <el-radio :label="false">No</el-radio>
            </el-radio-group>
          </el-form-item>
        </div>
      </div>
    </el-form-item>
  </div>
</template>


<script>
export default {
  name: 'DynamicFormField',
  props: {
    // The extra attributes form definition (array)
    extraAttributeForm: {
      type: Array,
      required: true
    },
    // The form data that will hold the values
    form: {
      type: Object,
      required: true
    }
  }
};
</script>

<style scoped>
</style>