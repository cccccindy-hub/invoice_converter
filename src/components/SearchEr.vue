<template>
    <el-select class="er-select" v-model="selectValue" :multiple="multiple" filterable remote reserve-keyword placeholder="请输入公司名称或代码" :remote-method="remoteMethod" :loading="loading" collapse-tags collapse-tags-tooltip clearable :style="{width:width||'auto'}" :disabled="disabled">
        <el-option v-for="item in options" :key="item.employerid" :label="item.employername" :value="item.employerid" />
    </el-select>
</template>
<style scoped lang="scss">
.er-select{
  :deep(.el-select__tags){
    width: auto !important;
    max-width: none !important;
  }
}
</style>
<script lang="ts" setup>
import { UcService2Java } from '@/views/client/service';
import { onMounted, ref, watch } from 'vue';
import { delay } from '@/tools';
import { IErInfo } from '@/interface';

const props = withDefaults(defineProps<{
  width?:string;
  modelValue: number[]|number|null;
  multiple: boolean;
  disabled?:boolean;
}>(),{
  width:'auto',
  modelValue:null,
  multiple:false,
  disabled:false
});

const options = ref<IErInfo[]>([]);

onMounted(() => {
  
  console.log(555,typeof props.modelValue,props.modelValue)
})

watch(
  () => props.modelValue,
  (newV,oldV) => {
    if(newV && !oldV){
      if(typeof newV == 'number'){
        let exist = options.value.find(item=>item.employerid == newV);
        if(!exist){
          UcService2Java.queryEmployerByIds([newV as number]).then(ers=>{
            console.log("ers",ers)
            options.value.push(ers[0]);
            selectValue.value = props.modelValue as any;
          });
        }
      }
      // if(typeof newV == 'object'){
        
      // }
      console.log("newV",newV)
    }
  },
  {immediate: true}
);

const emit = defineEmits(["update:modelValue"]);
const selectValue = ref<number[]|number>();
watch(
  selectValue,
  (newV,oldV) => {
    // console.log(444,newV,oldV);
    emit("update:modelValue", selectValue.value);
  },
  {deep: true}
);


const loading = ref(false);
async function remoteMethod(query: string){console.log("remoteMethod",query);
  if (query) {
    loading.value = true;
    try{
      await delay(200)
      options.value = await UcService2Java.searchEmployer(query);
      loading.value = false;
    }catch(err){
      loading.value = false;
    }
  } else {
    options.value = []
  }
}
</script>