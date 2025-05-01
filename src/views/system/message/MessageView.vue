<template>
  <div>
    <el-form :model="msgDetail" label-width="120px" :disabled="msgDetail?.status === 1">
      <el-form-item label="标题：">
        <el-input v-model="msgDetail.title"/>
      </el-form-item>
      <el-form-item label="内容：">
        <el-input v-model="msgDetail.content" type="textarea"/>
      </el-form-item>
      <el-form-item label="消息类型：">
        <el-select v-model="msgDetail.type">
          <el-option :value="1" label="系统升级"/>
          <el-option :value="2" label="法律条款更新"/>
        </el-select>
      </el-form-item>
      <el-form-item label="群体：">
        <el-select v-model="msgDetail.targets">
          <el-option :value="1" label="所有用户"/>
        </el-select>
      </el-form-item>
      <el-form-item label="开始时间：" v-if="msgDetail.type === 1">
        <el-date-picker
            v-model="msgDetail.startTime"
            placeholder="可被用户查询到的开始时间"
            type="datetime"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            date-format="MMM DD, YYYY"
            time-format="HH:mm"
        />
      </el-form-item>
      <el-form-item label="结束时间：" v-if="msgDetail.type === 1">
        <el-date-picker
            v-model="msgDetail.endTime"
            placeholder="不可被用户查询到的结束时间"
            type="datetime"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            date-format="MMM DD, YYYY"
            time-format="HH:mm"
        />
      </el-form-item>
    </el-form>

    <el-button v-if="msgDetail.status === 0" @click="onSubmit" type="primary" style="margin-top: 30px; margin-left: 180px">提交</el-button>
  </div>
</template>

<script lang="ts" setup>

import {onMounted, ref} from "vue";
import {ElMessage} from "element-plus";
import {MessageApi} from "@/views/system/message/Message";

const props = defineProps({
  msgId: Number
})

const emits = defineEmits(["close"])

const msgDetail = ref({
  title: '',
  content: '',
  type: 1,
  targets: 1,
  status: 0,
  startTime: null,
  endTime: null,
});

onMounted(() => {
  initMsg();
})

async function initMsg() {
  if (props.msgId) {
    let msg = await MessageApi.viewMessage(props.msgId);
    if (!msg) {
      ElMessage.error("公告不存在！")
      emits("close");
    }
    msgDetail.value = msg;
  }
}

async function onSubmit() {
  console.log("msgDetail", msgDetail)
  if (props.msgId) {
    await MessageApi.updateMsg(msgDetail.value);
  } else {
    await MessageApi.createMsg(msgDetail.value);
  }

  ElMessage.success("提交成功")
  emits("close")
}

</script>

<style scoped lang="scss">
.el-form-item .el-input {
  width: 300px;
}
</style>