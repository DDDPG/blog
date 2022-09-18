<template>
  <el-card class="main-card">
    <!-- 标题 -->
    <div class="title">{{ this.$route.name }}</div>
    <!-- 表格操作 -->
    <div class="operation-container">
      <el-button
        type="danger"
        plain
        icon="el-icon-delete"
        size="small"
        :disabled="this.logIdList.length == 0"
        @click="isDelete = true"
        >批量删除</el-button
      >
      <el-button type="danger" plain icon="el-icon-delete" size="small" @click="clearTask"
        >清空</el-button
      >
      <!-- 条件筛选 -->
      <div style="margin-left: auto">
        <!-- 任务状态 -->
        <el-select
          v-model="status"
          placeholder="请选择任务状态"
          clearable
          size="small"
          style="margin-right: 1rem"
        >
          <el-option label="运行" :value="1"></el-option>
          <el-option label="暂停" :value="0"></el-option>
        </el-select>
        <!-- 任务组名 -->
        <el-input
          size="small"
          prefix-icon="el-icon-search"
          v-model="taskGroup"
          placeholder="请输入任务组名"
          clearable
          style="width: 200px; margin-right: 1rem"
          @keyup.enter.native="searchTask"
        ></el-input>
        <!-- 任务名称 -->
        <el-input
          size="small"
          prefix-icon="el-icon-search"
          v-model="keyword"
          placeholder="请输入任务名称"
          clearable
          style="width: 200px"
          @keyup.enter.native="searchTask"
        ></el-input>
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left: 1rem"
          @click="searchTask"
        >
          搜索
        </el-button>
      </div>
    </div>
    <!-- 表格展示 -->
    <el-table border v-loading="loading" :data="logList" @selection-change="selectionChange">
      <!-- 表格列 -->
      <el-table-column type="selection" width="55" align="center"></el-table-column>
      <!--任务名称 -->
      <el-table-column
        label="任务名称"
        width="160"
        align="center"
        prop="taskName"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <!--任务组名 -->
      <el-table-column
        label="任务组名"
        align="center"
        width="150"
        prop="taskGroup"
      ></el-table-column>
      <!-- 调用目标 -->
      <el-table-column
        label="调用目标"
        align="center"
        prop="invokeTarget"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <!-- 日志信息 -->
      <el-table-column
        label="日志信息"
        align="center"
        prop="taskMessage"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <!-- 执行状态 -->
      <el-table-column label="执行状态" align="center" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status == 0" type="danger">失败</el-tag>
          <el-tag v-if="scope.row.status == 1" type="success">成功</el-tag>
        </template>
      </el-table-column>
      <!-- 执行时间 -->
      <el-table-column label="执行时间" align="center" width="190">
        <template slot-scope="scope">
          {{ scope.row.createTime | dateTime }}
        </template>
      </el-table-column>
      <!-- 操作 -->
      <el-table-column
        label="操作"
        align="center"
        width="120"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)"
            >详细</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination
      class="pagination-container"
      background
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[10, 20]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="count"
    ></el-pagination>
    <!-- 任务日志详细 -->
    <el-dialog title="任务日志详细" :visible.sync="taskView" width="700px" append-to-body>
      <el-form ref="taskForm" :model="taskForm" label-width="100px" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item label="任务名称：">{{ taskForm.taskName }}</el-form-item>
            <el-form-item label="任务分组：">{{ taskForm.taskGroup }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行时间：">{{ taskForm.createTime }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="调用方法：">{{ taskForm.invokeTarget }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="日志信息：">{{ taskForm.taskMessage }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="执行状态：">
              <div v-if="taskForm.status == 1">成功</div>
              <div v-else-if="taskForm.status == 0">失败</div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="错误信息：" v-if="taskForm.status == 0">{{
              taskForm.errorInfo
            }}</el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="taskView = false">关 闭</el-button>
      </div>
    </el-dialog>
    <!-- 批量删除对话框 -->
    <el-dialog :visible.sync="isDelete" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color: #ff9900"></i>提示
      </div>
      <div style="font-size: 1rem">是否删除选中项?</div>
      <div slot="footer">
        <el-button @click="isDelete = false">取 消</el-button>
        <el-button type="primary" @click="deleteTaskLog">确 定</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import { getTaskLogs, deleteTaskLogs, cleanTask } from "@/api/log";
export default {
  name: "TaskLog",
  data() {
    return {
      logIdList: [],
      logList: [],
      dateRange: [],
      isDelete: false,
      taskView: false,
      taskForm: {},
      status: null,
      keyword: null,
      taskId: null,
      taskGroup: null,
      loading: true,
      currentPage: 1,
      pageSize: 10,
      count: 0,
    };
  },
  created() {
    this.getTaskList();
  },
  methods: {
    selectionChange(logList) {
      this.logIdList = logList.map((item) => item.id);
    },
    handleCurrentChange(currentPage) {
      this.currentPage = currentPage;
      this.getTaskList();
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.getTaskList();
    },
    searchTask() {
      this.currentPage = 1;
      this.getTaskList();
    },
    getTaskList() {
      let params = {
        current: this.currentPage,
        size: this.pageSize,
        keyword: this.keyword,
        status: this.status,
        taskGroup: this.taskGroup,
      };
      getTaskLogs(params).then(({ data }) => {
        this.logList = data.data.recordList;
        this.count = data.data.count;
        this.loading = false;
      });
    },
    deleteTaskLog() {
      deleteTaskLogs(this.logIdList).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.msg,
          });
          this.getTaskList();
        }
        this.isDelete = false;
      });
    },
    clearTask() {
      this.$confirm("是否确认清空所有任务日志？", "系统提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          cleanTask().then(({ data }) => {
            if (data.flag) {
              this.$notify.success({
                title: "成功",
                message: data.msg,
              });
              this.getTaskList();
            }
          });
        })
        .catch(() => {});
    },
    handleView(task) {
      this.taskView = true;
      this.taskForm = task;
    },
  },
  watch: {
    status() {
      this.currentPage = 1;
      this.getTaskList();
    },
  },
};
</script>

<style></style>