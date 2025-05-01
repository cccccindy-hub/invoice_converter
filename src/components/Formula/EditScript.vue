<template>
    <div class="set-formula-frame">
        <div class="items-pannel">
            <div class="search-ipt">
                <input type="text" v-model.trim="searchKey" placeholder="筛选"/>
                <div v-if="searchKey.length>0" class="clear-btn" @click="searchKey=''">&#10006;</div>
            </div>
            <div class="sitems">
                <template v-for="element in items">
                    <div class="sitem" @mouseenter="toggleCopyItemCodeButton(element.code as string)" @mouseleave="toggleCopyItemCodeButton(null)" style="position: relative;">
                        <div class="item-name" @dblclick="addItem(element)">{{ element.name }}({{ element.code }})</div>
                        <!-- <div class="previous" @dblclick="addItem(element,'pp')">上期</div> -->
                        <span class="formula-tip" v-if="element?.config?.useFormula">公式</span>
                        <el-button @click="copyItemCodeToClipboard(element.code as string)" v-if="currentHoverItemCode === element.code" style="position: absolute;right: 0;height: 100%;border-radius: 0px;">复制</el-button>
                        <!-- <el-icon v-if="element.config.useFormula">
                            <Connection />
                        </el-icon> -->
                    </div>
                </template>
            </div>
        </div>
        <div class="formula-pannel">
            <div class="formula-hedaer">
                <div>
                    <span style="color: #67C23A;" v-if="validResult">脚本验证通过！</span>
                    <span style="color: #F56C6C;" v-else>脚本验证失败！</span>
                </div>
                <div class="formula-hedaer-btns">
                    <el-icon @click="reset"><RefreshLeft /></el-icon>
                    <el-icon @click="clear"><Delete /></el-icon>
                </div>
            </div>
            <div class="script-content">
                <CodeMirror v-if="initialized" v-model="script" :placeholder="placeholder" :extensions="extensions" ref="codeMirror"/>
            </div>
        </div>
    </div>
</template>
<style scoped lang="scss">
@import './style.scss';
</style>
<script setup lang="ts">
import { IItem } from '@/interface';
import { onMounted, ref, watch } from 'vue';
import CodeMirror from './CodeMirror.vue'
import { covert2ItemExp, validScript } from './tools';
import { placeholders } from './placeholders';

/**
 * 默认脚本
 */
 const placeholder = `编写脚本必须return结果`;

const props = withDefaults(
    defineProps<{
        originItems: IItem[];
        itemIds: string[];
        script: string|null;
    }>(),
    {
        originItems:[] as any,
        script: null,
        itemIds:[] as any
    }
);

const emit = defineEmits(["update:script","update:itemIds"]);
const items = ref<IItem[]>([]);

const codeMirror = ref();
const script = ref<string|null>("")

const extensions = [
    placeholders(props.originItems),
];

watch(
    script,
    (newV, oldV) => {
        valid(newV as any)
    },
    { deep: true }
);

let originScript: string|null = "";

const initialized = ref(false);

onMounted(() => {
    items.value = props.originItems;
    originScript = props.script;
    script.value = props.script;
    initialized.value = true;
});

function addItem(element: IItem,type?:"pp") {
    let itemCode = element.code as string;
    if(type){
        itemCode=`${itemCode}.${type}`;
    }
    codeMirror.value.insert(covert2ItemExp(itemCode));
}

const searchKey = ref('');
watch(
    searchKey,
    (newV, oldV) => {
        search(searchKey.value);
    }
);
function search(key:string) {
    if (key.length > 0) {
        items.value = props.originItems.filter(r => {
            return (r.name && r.name.indexOf(key) > -1) || (r.code && r.code.indexOf(key) > -1) || (r.id && r.id.indexOf(key) > -1);
        });
    } else {
        items.value = props.originItems
    }
}
/**
 * 清空编辑器内代码
 */
function clear() {
    script.value=null;
    codeMirror.value.setContent(null);
    console.log("clearFormula", script.value)
}
/**
 * 重置编辑器内代码
 */
function reset() {
    script.value=originScript;
    codeMirror.value.setContent(originScript);
    console.log("reset", script.value)
}

/**
 * 验证计算结果
 */
const validResult = ref(false);
/**
 * 验证脚本并试算
 * @param script 
 */
function valid(script: string="") {
    const { isValid, itemIds } = validScript(script);
    validResult.value = isValid;
    if(isValid){
        emit("update:script", script);
        emit("update:itemIds", itemIds);
    }
}
async function copyItemCodeToClipboard(code:string) {//点击发生复制事件
    try {  
        await navigator.clipboard.writeText(code);  
    } catch (err) {  
        console.error('Failed to copy text: ', err);  
    }  
}

const currentHoverItemCode = ref(<string|null>null)

const toggleCopyItemCodeButton = (code:string|null) =>{
    currentHoverItemCode.value = code
}
</script>