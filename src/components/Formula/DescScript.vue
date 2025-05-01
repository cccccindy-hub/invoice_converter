<!-- 脚本动态描述 -->
<template>
    <div class="set-formula-frame">
        <div class="formula-pannel">
            <div class="formula-hedaer">
                <div class="formula-hedaer-btns">
                    <el-icon @click="reset"><RefreshLeft /></el-icon>
                    <el-icon @click="clear"><Delete /></el-icon>
                </div>
            </div>
            <div class="script-content">
                <CodeMirror v-if="initialized" v-model="script" :placeholder="placeholder" ref="codeMirror"/>
            </div>
        </div>
    </div>
</template>
<style scoped lang="scss">
@import './style.scss';
</style>
<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import CodeMirror from './CodeMirror.vue'

/**
 * 默认脚本
 */
 const placeholder = `编写脚本必须return结果`;

const props = withDefaults(
    defineProps<{
        script: string|null;
    }>(),
    {
        script: null
    }
);

const emit = defineEmits(["update:script"]);

const codeMirror = ref();
const script = ref<string|null>("")

watch(
    script,
    (newV, oldV) => {
        emit("update:script", script);
    },
    { deep: true }
);

let originScript: string|null = "";

const initialized = ref(false);

onMounted(() => {
    originScript = props.script;
    script.value = props.script;
    initialized.value = true;
});

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
</script>