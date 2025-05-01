<template>
  <div class="home">
    <el-button @click="show(null)" type="primary" style="margin-top: 30px">新增公告</el-button>
    <el-table :data="messageList" style="height: 80%">
      <el-table-column label="标题" prop="title" width="120px" :show-overflow-tooltip="true"/>
      <el-table-column label="内容" prop="content" width="280px" :show-overflow-tooltip="true"/>
      <el-table-column label="类型" width="120px">
        <template v-slot="scope">
          <div v-if="scope.row.type === 1">系统升级</div>
          <div v-if="scope.row.type === 2">法律条款更新</div>
        </template>
      </el-table-column>
      <el-table-column label="群体" width="120px">
        <template v-slot="scope">
          <div v-if="scope.row.targets === 1">全体用户</div>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="120px">
        <template v-slot="scope">
          <div v-if="scope.row.status === 0">未发布</div>
          <div v-else>已发布</div>
        </template>
      </el-table-column>
      <el-table-column label="开始时间" prop="startTime" width="240px"/>
      <el-table-column label="结束时间" prop="endTime" width="240px"/>
      <el-table-column label="创建人" prop="createBy" width="120px"/>
      <el-table-column label="创建时间" prop="createTime" width="180px"/>
      <el-table-column label="修改人" prop="updateBy" width="120px"/>
      <el-table-column label="修改时间" prop="updateTime" width="180px"/>
      <el-table-column label="操作" width="200px" fixed="right">
        <template #default="scope">
          <el-button @click="show(scope.row.id)" type="primary" size="small">
            {{ scope.row.status === 0 ? '修改' : '查看' }}
          </el-button>
          <el-button v-if="scope.row.status === 0" @click="publish(scope.row.id)" type="primary" size="small">发布</el-button>
          <el-button v-if="showFinishMsg(scope.row)" @click="finishMsg(scope.row)" type="primary" size="small">结束公告</el-button>
          <el-button v-if="scope.row.status === 0" @click="del(scope.row.id)" type="danger" size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination background layout="total, sizes, pager"
                   style="margin-top: 30px"
                   :total="total"
                   :page-size="pageForm.pageSize"
                   :page-sizes="[10, 20, 30, 50, 100]"
                   @current-change="handleCurrentChange"
                   @size-change="handleSizeChange"/>

    <el-dialog
        v-model="showDetail"
        :title="msgId ? '修改表单' : '新增表单'"
        width="600px"
        :destroy-on-close="true"
        center>
      <MessageView :msg-id="msgId"
                   @close="msgDialogClose"/>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>

import {onMounted, ref} from "vue";
import {MessageApi} from "./Message";
import MessageView from "@/views/system/message/MessageView.vue";
import {ElMessage, ElMessageBox} from "element-plus";

onMounted(() => {
  queryPage();
})

const pageForm = ref({
  pageNum: 1,
  pageSize: 20
});

const messageList = ref([]);
const total = ref(0);
const msgId = ref(null);
const showDetail = ref(false);

async function queryPage() {
  //消息
  let messagePage = await MessageApi.messageList(pageForm.value);
  total.value = messagePage.total;
  messageList.value = messagePage.rows;
}

function showFinishMsg(msg) {
  try {
    return msg.finish === 0 && msg.status === 1 && msg.type === 1 && new Date(msg.endTime) > new Date();
  } catch (e) {
    return false;
  }
}

function handleCurrentChange(pageNum) {
  pageForm.value.pageNum = pageNum;
  queryPage();
}

function handleSizeChange(pageSize) {
  pageForm.value.pageSize = pageSize;
  queryPage();
}

function show(messageId) {
  msgId.value = messageId;
  showDetail.value = true;
}

async function publish(msgId) {
  await MessageApi.publish(msgId);
  ElMessage.success("发布成功！")

  queryPage();
}

/**
 * 系统公告结束走马灯
 * @param msgId
 */
async function finishMsg(msg) {
  if(!showFinishMsg(msg)) {
    ElMessage.error("公告已结束！")
    return;
  }
  await MessageApi.finish(msg.id)
  ElMessage.success("已结束！")

  queryPage();
}

async function del(msgId) {
  try {
    ElMessageBox.confirm("是否确认删除该消息？", "删除确认",
        {
          confirmButtonText: "确认",
          cancelButtonText: "取消"
        })
        .then(async () => {
          await MessageApi.del(msgId);
          ElMessage.success("删除成功！")

          queryPage();
        })

  } catch (e) {
    ElMessage.error(e?.message)
  }
}

function msgDialogClose() {
  showDetail.value = false;
  queryPage();
}
</script>

<style lang="scss" scoped>

.home {
  width: 100%;
  height: 100%;
  overflow: hidden
}
</style>