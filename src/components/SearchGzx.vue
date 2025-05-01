<template>
  <el-select class="er-select" v-model="selectValue" :multiple="multiple" filterable remote reserve-keyword
    placeholder="请输入工资项名称" :remote-method="remoteMethod" :loading="loading" collapse-tags collapse-tags-tooltip
    clearable :style="{ width: width || 'auto' }" :disabled="disabled">
    <el-option v-for="item in options" :key="item.id" :label="item.name" :value="item.id" />
  </el-select>
</template>
<style scoped lang="scss">
.er-select {
  :deep(.el-select__tags) {
    width: auto !important;
    max-width: none !important;
  }
}
</style>
<script lang="ts" setup>
import { UcService2Java } from '@/views/client/service';
import { onMounted, ref, watch } from 'vue';
import { delay } from '@/tools';
import { IErInfo, IItem } from '@/interface';
import { itemService } from '@/service';

const props = withDefaults(defineProps<{
  width?: string;
  // employerId: number | null;
  gzxId: string | null;
  multiple: boolean;
  disabled?: boolean;
}>(), {
  width: 'auto',
  // employerId: null,
  gzxId: null,
  multiple: false,
  disabled: false
});

const options = ref<any[]>([]);

// watch(
//   () => props.employerId,
//   (newV, oldV) => {
//     if (newV && !oldV) {
//       showItem()
//       console.log("newV", newV)
//     }
//   },
//   { immediate: true }
// );

onMounted(async() =>{
  await showItem()
})

const emit = defineEmits(["update"]);
const selectValue = ref(props.gzxId);
watch(
  selectValue,
  (newV, oldV) => {
    console.log(444,newV,oldV,selectValue.value); 
    emit("update", selectValue.value);
  },
  { deep: true }
);


const loading = ref(false);
async function remoteMethod(query: string) {
  console.log("remoteMethod", query);
  if (query) {
    loading.value = true;
    try {
      await delay(200)
      options.value = itemsData.value.filter((item:any) => item.name.includes(query))
      loading.value = false;
    } catch (err) {
      loading.value = false;
    }
  } else {
    options.value = []
  }
}

let itemsData = ref<IItem[]>([])
async function showItem() {
    let res = await itemService.list({}) as IItem[]
    itemsData.value = res.filter((item) => {
      if (item?.config?.useFormula == true) {
          return true
      }
    })
    console.log(itemsData.value, 888)
}
</script>