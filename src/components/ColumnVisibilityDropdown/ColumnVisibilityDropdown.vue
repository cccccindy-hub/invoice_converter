<template>
  <el-dropdown :hide-on-click="true">
    <span class="el-dropdown-link">
      Visible Columns
      <el-icon class="el-icon--right"><arrow-down /></el-icon>
    </span>
    <template #dropdown>
      <el-dropdown-menu>
        <el-scrollbar style="max-height: 300px; overflow-y: auto; width: auto">
          <el-checkbox-group v-loading="loading" v-model="visibleColumns" @change="onVisibleColumnsChange">
            <div style="display: flex; flex-direction: column; padding: 5px">
              <el-checkbox v-for="column in allColumns" :key="column.label" :label="column.label">
                {{ column.label }}
              </el-checkbox>
            </div>
          </el-checkbox-group>
        </el-scrollbar>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<script>
export default {
  name: "ColumnVisibilityDropdown",
  props: {
    defaultColumnVisibilityList: {
      type: Array,
      default: () => [],
    },
    extraAttributeForm: {
      type: Array,
      default: () => [],
    },
    modelValue: {
      type: Array,
      default: () => [],
    },
    localStorageKey: {
      type: String,
      default: "", // Default key
    },
  },
  emits: ["update:modelValue"],
  data() {
    return {
      allColumns: [],
      visibleColumns: this.modelValue,
      loading: true,
    };
  },
  watch: {
    extraAttributeForm: {
      handler: "initializeColumnVisibility",
      deep: true,
    },
    modelValue(newValue) {
      this.visibleColumns = newValue;
    },
  },
  methods: {
    initializeColumnVisibility() {

      const savedVisibility = JSON.parse(
        localStorage.getItem(this.localStorageKey + "_visibility")
      );


      let newVisibility = [];
      this.defaultColumnVisibilityList.forEach((item) => {
        newVisibility.push(
          {
            "label": `${item}`,
          }
        );
      });
      const hiddenIds = [65, 78, 73]
      // Add extra attributes to visibility list
      this.extraAttributeForm.forEach((item) => {
        newVisibility.push(
          {
            "label": `${item.name}`,
            "id": item.id,
          }
        );
        if (item.children) {
          item.children.forEach((childItem) => {
            if (!hiddenIds.includes(childItem.id)) {
              newVisibility.push(
                {
                  "label": `${item.name}-${childItem.name}`,
                  "id": childItem.id,
                }
              );
            }
          });
        }
      });

      this.allColumns = newVisibility

      // Use saved visibility if available, otherwise default to new visibility
      if (savedVisibility != null) {
        //change the name if the name is different
        savedVisibility.forEach(item =>{
          if(item.id !== undefined && !hiddenIds.includes(item.id)){
            const newItem = newVisibility.find(newEntry=>newEntry.id === item.id);
            if(newItem && newItem.label !==item.label)
              item.label=newItem.label;
          }
        })

        this.visibleColumns = savedVisibility.map(item => item.label);
        console.log("22", savedVisibility);
        console.log("23", newVisibility);
      } else {
        this.visibleColumns = this.allColumns.map(item => item.label);
      }


      // Emit event to update parent
      this.$emit("update:modelValue", this.visibleColumns);

      this.loading = false; // Data has been initialized
    },

    saveColumnVisibility() {
      let tempVisibility = this.allColumns.filter(item => this.visibleColumns.includes(item.label));
      console.log("25", tempVisibility);
      localStorage.setItem(
        this.localStorageKey + "_visibility",
        JSON.stringify(tempVisibility)
      );

    },
    onVisibleColumnsChange() {
      this.saveColumnVisibility();

      this.$emit("update:modelValue", this.visibleColumns);
    },
  },

};
</script>