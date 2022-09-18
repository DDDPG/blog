<template>
  <el-card class="main-card">
    <div class="title">{{ $route.name }}</div>
    <!-- 表格操作 -->
    <div class="operation-container">
      <div style="margin-left: auto">
        <el-select
          clearable
          v-model="loginType"
          size="small"
          placeholder="请选择登录方式"
          style="margin-right: 1rem"
        >
          <el-option
            v-for="item in typeList"
            :key="item.type"
            :label="item.desc"
            :value="item.type"
          ></el-option>
        </el-select>
        <el-input
          v-model="keyword"
          prefix-icon="el-icon-search"
          size="small"
          placeholder="请输入用户昵称"
          style="width: 200px"
          @keyup.enter.native="searchUser"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left: 1rem"
          @click="searchUser"
        >
          搜索
        </el-button>
      </div>
    </div>
    <!-- 表格展示 -->
    <el-table border :data="userList" v-loading="loading">
      <!--头像 -->
      <el-table-column prop="avatar" label="头像" align="center" width="100">
        <template slot-scope="scope">
          <img :src="scope.row.avatar" width="40" height="40" />
        </template>
      </el-table-column>
      <!-- 昵称-->
      <el-table-column prop="nickname" label="昵称" align="center" width="160"></el-table-column>
      <!-- 登录方式 -->
      <el-table-column prop="loginType" label="登录方式" align="center" width="120">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.loginType == 1">邮箱</el-tag>
          <el-tag v-if="scope.row.loginType == 2">QQ</el-tag>
          <el-tag type="warning" v-if="scope.row.loginType == 3">微博</el-tag>
          <el-tag type="danger" v-if="scope.row.loginType == 4">Gitee</el-tag>
          <el-tag type="info" v-if="scope.row.loginType == 5">Github</el-tag>
        </template>
      </el-table-column>
      <!-- 状态 -->
      <el-table-column prop="status" label="状态" align="center" width="100">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value="1"
            :inactive-value="0"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <!-- 登录ip -->
      <el-table-column prop="ipAddress" label="登录ip" align="center" width="140"></el-table-column>
      <!-- 登录地址 -->
      <el-table-column
        prop="ipSource"
        label="登录地址"
        align="center"
        width="160"
      ></el-table-column>
      <!--创建时间-->
      <el-table-column prop="createTime" label="创建时间" align="center" sortable>
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right: 5px"></i>
          {{ scope.row.createTime }}
        </template>
      </el-table-column>
      <!-- 最近登录时间 -->
      <el-table-column prop="oginTime" label="最近登录时间" align="center" sortable>
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right: 5px"></i>
          {{ scope.row.loginTime }}
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
  </el-card>
</template>

<script>
import { getUsers, changeUserStatus } from "@/api/user";
export default {
  name: "User",
  data() {
    return {
      typeList: [
        {
          type: 1,
          desc: "邮箱",
        },
        {
          type: 2,
          desc: "QQ",
        },
        {
          type: 3,
          desc: "微博",
        },
        {
          type: 4,
          desc: "Gitee",
        },
        {
          type: 5,
          desc: "Github",
        },
      ],
      userList: [],
      loading: true,
      loginType: null,
      keyword: null,
      currentPage: 1,
      pageSize: 10,
      count: 0,
    };
  },
  created() {
    this.getUserList();
  },
  methods: {
    getUserList() {
      let params = {
        current: this.currentPage,
        size: this.pageSize,
        loginType: this.loginType,
        keyword: this.keyword,
      };
      getUsers(params).then(({ data }) => {
        this.userList = data.data.recordList;
        this.count = data.data.count;
        this.loading = false;
      });
    },
    handleStatusChange(user) {
      changeUserStatus({ id: user.id, status: user.status }).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.msg,
          });
        }
      });
    },
    searchUser() {
      this.currentPage = 1;
      this.getUserList();
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.getUserList();
    },
    handleCurrentChange(currentPage) {
      this.currentPage = currentPage;
      this.getUserList();
    },
  },
  watch: {
    loginType() {
      this.current = 1;
      this.getUserList();
    },
  },
};
</script>

<style></style>
