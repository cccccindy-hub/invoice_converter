<!-- 公式 -->
<template>
    <el-form v-if="form" label-width="120px" ref="formRef" :rules="rules" :model="form" :disabled="props.isView">
        <el-form-item label="公式描述" prop="desc">
            <el-input v-model="form.desc" :rows="2" type="textarea" placeholder="请输入公式描述"/>
        </el-form-item>
        <el-form-item label="动态描述" prop="dyncDesc">
            <el-switch v-model="form.dyncDesc"  @change="onDyncDescChange"/>
        </el-form-item>
        <el-form-item v-if="form.dyncDesc==true" label="动态描述" prop="descScript">
            <DescScript v-model:script="form.descScript"></DescScript>
        </el-form-item>
        <el-form-item label="引用工资项代码" prop="refCodes">
            <el-input v-model="form.refCodes" type="text" placeholder="请输入引用工资项代码,请用半角逗号分隔">
                <!-- <template v-if="form.desc" #append>
                    <el-button :icon="Refresh" @click="syncRefcodes"/>
                </template> -->
            </el-input>
        </el-form-item>
        <el-form-item label-width="0"  class="script-content">
            <EditScript v-if="originItems.length>0" :originItems="originItems" v-model:script="(form.script as any)" v-model:item-ids="form.itemIds" />
        </el-form-item>
    </el-form>
</template>
<style scoped lang="scss">
.script-content{
    :deep(.el-form-item__content){
        display: block;
        min-height: 200px;
    }
}
</style>
<script lang="ts" setup>
import { IItem, IItemConfigByFormula } from '@/interface';
import { FormInstance, FormRules } from 'element-plus';
import { onBeforeUnmount, onMounted, reactive, ref, watch } from 'vue';
import { itemService } from '@/service';
// import {hasRole} from '@/tools';
import EditScript from './EditScript.vue'
import DescScript from './DescScript.vue'

const props = defineProps<{
    modelValue: IItemConfigByFormula;
    isView?:boolean;
    itemId:string;
    erId?:number;
}>();
const formRef = ref<FormInstance>();
// const hasFormulaEditRole = hasRole('sysItemFormulaEdit')
const form = ref<IItemConfigByFormula>({});
const rules = reactive<FormRules>({
    "desc":[
        { required: true, message: '请输入公式描述', trigger: 'blur' },
    ],
    "refCodes":[   
        { required: false, message: '必须以半角逗号分隔的大小写字母或数字组合', trigger: 'blur',pattern:/^[a-zA-Z0-9,]*$/ },
    ]
});
const originItems = ref<IItem[]>([]);
const emit = defineEmits(['update:modelValue']);

onMounted(() => {
    form.value = props.modelValue;
    initOriginItems().catch(console.error);
});
onBeforeUnmount(() => {
    // window.removeEventListener('keydown', handleKeydown);
})

function onDyncDescChange(ev:any){
    form.value.dyncDesc = ev;
    console.log("dyncDescChange",form.value.descScript)
    if(ev){
        
    }else{
        form.value.descScript=undefined;
    }
}

watch(
    form, 
    (newVal, oldVal) => {
        console.log("watch formula form",newVal, oldVal)
        emit("update:modelValue",newVal)
    }
)

watch<undefined|number>(
    () => props.erId,
    (newVal, oldVal) => {
        console.log("initOriginItems",newVal, oldVal)
        initOriginItems()
    }
)
async function initOriginItems(){
    let rst:IItem[]=[];
    if(props.erId){
        rst = await itemService.listByEr(props.erId) as IItem[];
    }else{
        rst = await itemService.list({}) as IItem[];
    }
    originItems.value=rst.filter(r=>r.id!=props.itemId);
}
async function validate(){
    await formRef.value?.validate();
    emit('update:modelValue', form.value);
}

defineExpose({
    validate
})
</script>