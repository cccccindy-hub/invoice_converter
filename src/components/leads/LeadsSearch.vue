<template>
  <el-select
      style="width: 210px;"
      v-model="selectedClientCode"
      remote
      @change="clientCodeChange"
      filterable
      clearable
      :remote-method="searchClientName"
      :loading="loading">
    <el-option
        v-for="client in clients"
        :key="client.leadCode"
        :value="client.companyName"
        :label="client.companyName + ' - ' + client.leadCode"
    />
  </el-select>
</template>

<script setup lang="ts">
import {ElMessage} from "element-plus";
import {ref} from "vue";
import {leadService} from "@/service/lead";

const loading = ref(false);
const clients = ref([]);
const selectedClientCode = ref('');
const emits = defineEmits(["leadsChange"]);

const queryForm = ref({
  pageNum: 1,
  pageSize: 20,
  companyName: '',
  orderByColumn: "id",
  isAsc: "descending",
});

async function searchClientName(clientName: string) {
  if (!clientName) {
    clients.value = [];
  }
  let form = queryForm.value;
  if (form.companyName === clientName) {
    return;
  }
  form.companyName = clientName;
  try {
    loading.value = true;
    let clientList = (await leadService.list(form)).rows;
    clients.value = clientList ? clientList : [];
    loading.value = false;
  } catch (e) {
    loading.value = false;
    ElMessage.error("search fail:", e?.message)
  }
}

function clientCodeChange() {
  let searchClient = {leadCode: '', companyName: ''};
  if (selectedClientCode.value) {
    for (let client of clients.value) {
      if (client.companyName === selectedClientCode.value) {
        searchClient = client;
        break;
      }
    }
  }
  emits("leadsChange", searchClient)
}
</script>

<style scoped lang="scss">

</style>