<template>
    <div ref="editorRef" class="editor-main"></div>
</template>
<style lang="scss" scoped>
.editor-main {
    width: 100%;
    height: 100%;

    :deep(.cm-editor) {
        height: 100%;
    }
}
</style>
<script lang="ts" setup>
import { basicSetup } from "codemirror";
import { EditorView, placeholder,keymap } from "@codemirror/view";
import { EditorState } from "@codemirror/state";
import { javascript } from "@codemirror/lang-javascript";
import { oneDark } from '@codemirror/theme-one-dark'
import {indentWithTab} from "@codemirror/commands"
import { onMounted, ref } from "vue";

const props = defineProps<{
    modelValue?: string | null;
    placeholder?: string;
    extensions?: any[];
}>();

const emit = defineEmits(['update:modelValue']);
const editorRef = ref();
const editorView = ref<EditorView>();

/**
 * 初始化codemirror
 */
const initEditor = () => {
    if (typeof editorView.value !== "undefined") {
        editorView.value.destroy();
    }
    // 配置扩展
    const extensions = [
        basicSetup,
        javascript(),
        oneDark,
        keymap.of([indentWithTab]),
        EditorView.lineWrapping,// 自动换行
        EditorView.updateListener.of((update) => {
            if (update.docChanged) {
                emit("update:modelValue", editorView.value?.state.doc.toString());
            }
        })
    ];
    // 设置占位符
    if (props.placeholder) {
        extensions.push(placeholder(props.placeholder))
    }
    if (props.extensions) {
        extensions.push(...props.extensions);
    }
    const startState = EditorState.create({
        doc: props.modelValue as any,
        extensions: extensions
    });
    if (editorRef.value) {
        editorView.value = new EditorView({
            state: startState,
            parent: editorRef.value,
        });
    }
};
function setContent(content: string) {
    if (editorView.value) {
        editorView.value.dispatch({
            changes: {
                from: 0,
                to: editorView.value.state.doc.length,
                insert: content
            }
        })
    }
}
function insert(content: string) {
    if (editorView.value) {
        editorView.value.dispatch({
            changes: {
                from: editorView.value.state.selection.main.head,
                insert: content
            }
        })
    }
}
onMounted(() => {
    initEditor();
});
defineExpose({
    editorView,
    setContent,
    insert
})
</script>