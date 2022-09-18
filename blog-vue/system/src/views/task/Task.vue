<template>
  <el-card class="main-card">
    <!-- 标题 -->
    <div class="title">{{ this.$route.name }}</div>
    <!-- 表格操作 -->
    <div class="operation-container">
      <el-button type="primary" plain icon="el-icon-plus" size="small" @click="openModel(null)"
        >新增</el-button
      >
      <el-button
        type="danger"
        plain
        icon="el-icon-delete"
        size="small"
        :disabled="this.taskIdList.length == 0"
        @click="isDelete = true"
        >批量删除</el-button
      >
      <el-button type="info" plain icon="el-icon-s-operation" size="small" @click="handleTaskLog"
        >日志</el-button
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
    <el-table border v-loading="loading" :data="taskList" @selection-change="selectionChange">
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
      <el-table-column label="任务组名" align="center" prop="taskGroup"></el-table-column>
      <!-- 调用目标 -->
      <el-table-column
        label="调用目标"
        align="center"
        prop="invokeTarget"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <!-- cron执行表达式 -->
      <el-table-column
        label="cron执行表达式"
        align="center"
        prop="cronExpression"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <!-- 状态 -->
      <el-table-column label="状态" align="center" width="100">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            :active-value="1"
            :inactive-value="0"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <!-- 备注 -->
      <el-table-column label="备注" align="center" width="160">
        <template slot-scope="scope">
          {{ scope.row.remark }}
        </template>
      </el-table-column>
      <!-- 创建时间 -->
      <el-table-column label="创建时间" align="center" width="160">
        <template slot-scope="scope">
          {{ scope.row.createTime | dateTime }}
        </template>
      </el-table-column>
      <!-- 操作 -->
      <el-table-column
        label="操作"
        align="center"
        width="180"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="openModel(scope.row)"
            >修改</el-button
          >
          <el-popconfirm
            title="确定删除吗？"
            style="margin-left: 10px"
            @confirm="deleteTask(scope.row.id)"
          >
            <el-button size="mini" type="text" slot="reference">
              <i class="el-icon-delete" /> 删除
            </el-button>
          </el-popconfirm>
          <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)">
            <el-button size="mini" type="text" style="margin-left: 9px"
              ><i class="el-icon-d-arrow-right el-icon--right"></i>更多</el-button
            >
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="handleRun" icon="el-icon-caret-right"
                >执行一次</el-dropdown-item
              >
              <el-dropdown-item command="handleView" icon="el-icon-view">任务详细</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
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
    <!-- 添加或修改定时任务对话框 -->
    <el-dialog :title="title" :visible.sync="addOrEdit" width="800px" top="10vh" append-to-body>
      <el-form ref="taskForm" :model="taskForm" :rules="rules" label-width="120px">
        <el-row>
          <!-- 任务名称 -->
          <el-col :span="12">
            <el-form-item label="任务名称" prop="taskName">
              <el-input v-model="taskForm.taskName" placeholder="请输入任务名称"></el-input>
            </el-form-item>
          </el-col>
          <!-- 任务分组 -->
          <el-col :span="12">
            <el-form-item label="任务分组" prop="taskGroup">
              <template v-if="this.title == '修改任务'">
                <span slot="label">
                  任务分组
                  <el-tooltip placement="top">
                    <div slot="content">不能手动修改数据库ID和任务组名，否则会导致脏数据</div>
                    <i class="el-icon-warning"></i>
                  </el-tooltip>
                </span>
              </template>
              <el-input v-model="taskForm.taskGroup" placeholder="请输入任务分组"></el-input>
            </el-form-item>
          </el-col>
          <!-- 调用方法 -->
          <el-col :span="24">
            <el-form-item prop="invokeTarget">
              <span slot="label">
                调用方法
                <el-tooltip placement="top">
                  <div slot="content">
                    Bean调用示例：ryTask.ryParams('ry')
                    <br />Class类调用示例：com.ruoyi.quartz.task.RyTask.ryParams('ry')
                    <br />参数说明：支持字符串，布尔类型，长整型，浮点型，整型
                  </div>
                  <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
              <el-input v-model="taskForm.invokeTarget" placeholder="请输入调用目标"></el-input>
            </el-form-item>
          </el-col>
          <!-- cron表达式 -->
          <el-col :span="24">
            <el-form-item label="cron表达式" prop="cronExpression">
              <el-input v-model="taskForm.cronExpression" placeholder="请输入cron执行表达式">
                <template slot="append">
                  <el-button type="primary" @click="handleShowCron">
                    生成表达式
                    <i class="el-icon-time el-icon--right"></i>
                  </el-button>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <!-- 错误执行策略 -->
          <el-col :span="24">
            <el-form-item label="错误执行策略" prop="misfirePolicy">
              <el-radio-group v-model="taskForm.misfirePolicy" size="small">
                <el-radio-button label="1">立即执行</el-radio-button>
                <el-radio-button label="2">执行一次</el-radio-button>
                <el-radio-button label="3">放弃执行</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <!-- 是否并发 -->
          <el-col :span="12">
            <el-form-item label="是否并发" prop="concurrent">
              <el-radio-group v-model="taskForm.concurrent" size="small">
                <el-radio-button label="1">允许</el-radio-button>
                <el-radio-button label="0">禁止</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <!-- 状态 -->
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="taskForm.status">
                <el-radio :label="1">正常</el-radio>
                <el-radio :label="0">暂停</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <!-- 备注 -->
          <el-col :span="12">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="taskForm.remark" placeholder="备注信息" type="textarea"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addOrEdit = false"> 取 消 </el-button>
        <el-button type="primary" @click="saveOrUpdateTask"> 确 定 </el-button>
      </div>
    </el-dialog>
    <!-- Cron表达式生成器 -->
    <el-dialog
      title="Cron表达式生成器"
      :visible.sync="openCron"
      append-to-body
      destroy-on-close
      top="10vh"
      class="scrollbar"
    >
      <crontab @hide="openCron = false" @fill="crontabFill" :expression="expression"></crontab>
    </el-dialog>
    <!-- 任务日志详细 -->
    <el-dialog title="任务详细" :visible.sync="taskView" width="700px" append-to-body>
      <el-form ref="form" :model="taskForm" label-width="120px" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item label="任务编号：">{{ taskForm.id }}</el-form-item>
            <el-form-item label="任务名称：">{{ taskForm.taskName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务分组：">{{ taskForm.taskGroup }}</el-form-item>
            <el-form-item label="创建时间：">{{ taskForm.createTime }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="cron表达式：">{{ taskForm.cronExpression }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="下次执行时间：">{{ taskForm.nextValidTime }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="调用目标方法：">{{ taskForm.invokeTarget }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务状态：">
              <div v-if="taskForm.status == 0">正常</div>
              <div v-else-if="taskForm.status == 1">失败</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否并发：">
              <div v-if="taskForm.concurrent == 1">允许</div>
              <div v-else-if="taskForm.concurrent == 0">禁止</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="错误执行策略：">
              <div v-if="taskForm.misfirePolicy == 0">默认策略</div>
              <div v-else-if="taskForm.misfirePolicy == 1">立即执行</div>
              <div v-else-if="taskForm.misfirePolicy == 2">执行一次</div>
              <div v-else-if="taskForm.misfirePolicy == 3">放弃执行</div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注">{{ taskForm.remark }}</el-form-item>
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
        <i class="el-icon-warning" style="color: #ff9900" />提示
      </div>
      <div style="font-size: 1rem">是否删除选中项？</div>
      <div slot="footer">
        <el-button @click="isDelete = false"> 取 消 </el-button>
        <el-button type="primary" @click="deleteTask(null)"> 确 定 </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import { getTasks, addTask, updateTask, deleteTask, changeTask, runTask } from "@/api/task";
import Crontab from "@/components/Crontab";
export default {
  name: "Task",
  components: { Crontab },
  data() {
    return {
      loading: true,
      taskIdList: [],
      taskList: [],
      isDelete: false,
      addOrEdit: false,
      title: "",
      taskView: false,
      openCron: false,
      expression: "",
      keyword: null,
      taskGroup: null,
      status: null,
      taskForm: {
        id: null,
        taskName: "",
        taskGroup: "",
        invokeTarget: "",
        cronExpression: "",
        misfirePolicy: 3,
        concurrent: 0,
        status: 0,
        remark: "",
      },
      rules: {
        taskName: [{ required: true, message: "任务名称不能为空", trigger: "blur" }],
        taskGroup: [{ required: true, message: "任务组名不能为空", trigger: "blur" }],
        invokeTarget: [{ required: true, message: "调用目标字符串不能为空", trigger: "blur" }],
        cronExpression: [{ required: true, message: "cron执行表达式不能为空", trigger: "blur" }],
      },
      currentPage: 1,
      pageSize: 10,
      count: 0,
    };
  },
  created() {
    this.getTaskList();
  },
  methods: {
    selectionChange(taskList) {
      this.taskIdList = taskList.map((item) => item.id);
    },
    handleCurrentChange(currentPage) {
      this.currentPage = currentPage;
      this.getTaskList();
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
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
      getTasks(params).then(({ data }) => {
        this.taskList = data.data.recordList;
        this.count = data.data.count;
        this.loading = false;
      });
    },
    openModel(task) {
      if (task != null) {
        this.taskForm = JSON.parse(JSON.stringify(task));
        this.title = "修改任务";
      } else {
        this.taskForm = {
          id: null,
          taskName: "",
          taskGroup: "",
          invokeTarget: "",
          cronExpression: "",
          misfirePolicy: 3,
          concurrent: 0,
          status: 1,
          remark: "",
        };
        this.title = "添加任务";
      }
      this.addOrEdit = true;
    },
    saveOrUpdateTask() {
      this.$refs["taskForm"].validate((valid) => {
        if (valid) {
          if (this.taskForm.id != null) {
            updateTask(this.taskForm).then(({ data }) => {
              if (data.flag) {
                this.$notify.success({
                  title: "成功",
                  message: data.msg,
                });
                this.getTaskList();
              }
              this.addOrEdit = false;
            });
          } else {
            addTask(this.taskForm).then(({ data }) => {
              if (data.flag) {
                this.$notify.success({
                  title: "成功",
                  message: data.msg,
                });
                this.getTaskList();
              }
              this.addOrEdit = false;
            });
          }
        }
      });
    },
    deleteTask(id) {
      let param = [];
      if (id == null) {
        param = this.taskIdList;
      } else {
        param = [id];
      }
      deleteTask(param).then(({ data }) => {
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
    searchTask() {
      this.currentPage = 1;
      this.getTaskList();
    },
    handleStatusChange(task) {
      changeTask({ id: task.id, status: task.status }).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.msg,
          });
        }
      });
    },
    handleShowCron() {
      this.expression = this.taskForm.cronExpression;
      this.openCron = true;
    },
    crontabFill(value) {
      this.taskForm.cronExpression = value;
    },
    handleCommand(command, row) {
      switch (command) {
        case "handleRun":
          this.handleRun(row);
          break;
        case "handleView":
          this.handleView(row);
          break;
        default:
          break;
      }
    },
    handleRun(task) {
      runTask({
        id: task.id,
        taskGroup: task.taskGroup,
      }).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.msg,
          });
        } else {
          this.$notify.error({
            title: "失败",
            message: data.msg,
          });
        }
      });
    },
    handleTaskLog() {
      this.$router.push({ path: "/task-log" });
    },
    handleView(task) {
      this.taskForm = task;
      this.taskView = true;
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

<style scoped></style>
