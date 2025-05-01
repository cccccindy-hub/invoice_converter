<template>
  <fc-designer ref="designerRef" />
</template>

<script setup>
import { ref } from 'vue';
import FcDesigner from '@form-create/designer'

const designerRef = ref(null);
</script>
