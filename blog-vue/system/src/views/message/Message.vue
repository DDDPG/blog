<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <!-- 留言状态 -->
    <div class="status-menu">
      <span>状态</span>
      <span @click="changeStatus(null)" :class="isActive(null)"> 全部 </span>
      <span @click="changeStatus(1)" :class="isActive(1)"> 正常 </span>
      <span @click="changeStatus(0)" :class="isActive(0)"> 审核中 </span>
    </div>
    <!-- 表格操作 -->
    <div class="operation-container">
      <el-button
        type="danger"
        size="small"
        icon="el-icon-delete"
        :disabled="messageIdList.length == 0"
        @click="isDelete = true"
      >
        批量删除
      </el-button>
      <el-button
        type="success"
        size="small"
        icon="el-icon-success"
        :disabled="messageIdList.length == 0"
        @click="updateMessageCheck(null)"
      >
        批量通过
      </el-button>
      <div style="margin-left: auto">
        <el-input
          v-model="keyword"
          prefix-icon="el-icon-search"
          size="small"
          placeholder="请输入用户昵称"
          style="width: 200px"
          @keyup.enter.native="searchMessages"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left: 1rem"
          @click="searchMessages"
        >
          搜索
        </el-button>
      </div>
    </div>
    <!-- 表格展示 -->
    <el-table border v-loading="loading" :data="messageList" @selection-change="selectionChange">
      <!-- 表格列 -->
      <el-table-column type="selection" width="55"></el-table-column>
      <!-- 用户头像 -->
      <el-table-column prop="avatar" label="头像" align="center" width="150">
        <template slot-scope="scope">
          <img :src="scope.row.avatar" width="40" height="40" />
        </template>
      </el-table-column>
      <!-- 留言人 -->
      <el-table-column prop="nickname" label="留言人" align="center" width="150"></el-table-column>
      <!-- 留言内容 -->
      <el-table-column prop="messageContent" label="留言内容" align="center"></el-table-column>
      <!-- IP地址 -->
      <el-table-column prop="ipAddress" label="ip地址" align="center" width="150"></el-table-column>
      <!-- IP来源 -->
      <el-table-column prop="ipSource" label="ip来源" align="center" width="170"></el-table-column>
      <!-- 留言状态 -->
      <el-table-column prop="isCheck" label="状态" width="80" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isCheck == 0" type="warning">审核中</el-tag>
          <el-tag v-if="scope.row.isCheck == 1" type="success">正常</el-tag>
        </template>
      </el-table-column>
      <!-- 留言时间 -->
      <el-table-column prop="createTime" label="留言时间" width="140" align="center">
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right: 5px"></i>
          {{ scope.row.createTime | date }}
        </template>
      </el-table-column>
      <!-- 列操作 -->
      <el-table-column label="操作" width="160" align="center">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.isCheck == 0"
            size="mini"
            type="success"
            @click="updateMessageCheck(scope.row.id)"
          >
            通过
          </el-button>
          <el-popconfirm
            style="margin-left: 10px"
            title="确定删除吗？"
            @confirm="deleteMessage(scope.row.id)"
          >
            <el-button size="mini" type="danger" slot="reference"> 删除 </el-button>
          </el-popconfirm>
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
    >
    </el-pagination>
    <!-- 批量删除对话框 -->
    <el-dialog :visible.sync="isDelete" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color: #ff9900" />提示
      </div>
      <div style="font-size: 1rem">是否删除选中项？</div>
      <div slot="footer">
        <el-button @click="isDelete = false">取 消</el-button>
        <el-button type="primary" @click="deleteMessage(null)"> 确 定 </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import { getMessages, updateMessageCheck, deleteMessage } from "@/api/message";
export default {
  name: "Message",
  data() {
    return {
      isCheck: null,
      loading: true,
      isDelete: false,
      messageIdList: [],
      messageList: [],
      keyword: null,
      currentPage: 1,
      pageSize: 10,
      count: 0,
    };
  },
  created() {
    this.getMessageList();
  },
  methods: {
    selectionChange(messageList) {
      this.messageIdList = messageList.map((item) => item.id);
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.getMessageList();
    },
    handleCurrentChange(currentPage) {
      this.currentPage = currentPage;
      this.getMessageList();
    },
    getMessageList() {
      let params = {
        current: this.currentPage,
        size: this.pageSize,
        keyword: this.keyword,
        isCheck: this.isCheck,
      };
      getMessages(params).then(({ data }) => {
        this.messageList = data.data.recordList;
        this.count = data.data.count;
        this.loading = false;
      });
    },
    changeStatus(check) {
      this.isCheck = check;
    },
    updateMessageCheck(id) {
      let param = {};
      if (id != null) {
        param.idList = [id];
      } else {
        param.idList = this.messageIdList;
      }
      param.isCheck = 1;
      updateMessageCheck(param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.msg,
          });
          this.getMessageList();
        }
      });
    },
    searchMessages() {
      this.currentPage = 1;
      this.getMessageList();
    },
    deleteMessage(id) {
      let param = [];
      if (id == null) {
        param = this.messageIdList;
      } else {
        param = [id];
      }
      deleteMessage(param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.msg,
          });
          this.getMessageList();
        }
        this.isDelete = false;
      });
    },
  },
  watch: {
    isCheck() {
      this.current = 1;
      this.getMessageList();
    },
  },
  computed: {
    isActive() {
      return function (status) {
        return this.isCheck == status ? "active-status" : "status";
      };
    },
  },
};
</script>

<style></style>