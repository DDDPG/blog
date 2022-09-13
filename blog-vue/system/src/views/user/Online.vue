<template>
  <el-card class="main-card">
    <div class="title">{{ $route.name }}</div>
    <!-- 表格操作 -->
    <div class="operation-container">
      <div style="margin-left: auto">
        <el-input
          v-model="keyword"
          prefix-icon="el-icon-search"
          size="small"
          placeholder="请输入用户昵称"
          style="width: 200px"
          @keyup.enter.native="searchOnlineUser"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left: 1rem"
          @click="searchOnlineUser"
        >
          搜索
        </el-button>
      </div>
    </div>
    <!-- 表格展示 -->
    <el-table
      v-loading="loading"
      :data="userList.slice((currentPage - 1) * pageSize, currentPage * pageSize)"
    >
      <!--头像 -->
      <el-table-column prop="avatar" label="头像" align="center" width="100">
        <template slot-scope="scope">
          <img :src="scope.row.avatar" width="40" height="40" />
        </template>
      </el-table-column>
      <!-- 昵称-->
      <el-table-column prop="nickname" label="昵称" align="center" width="160"></el-table-column>
      <!-- 登录ip -->
      <el-table-column prop="ipAddress" label="登录ip" align="center" width="140"></el-table-column>
      <!-- 登录地址 -->
      <el-table-column
        prop="ipSource"
        label="登录地址"
        align="center"
        width="160"
      ></el-table-column>
      <!-- 浏览器 -->
      <el-table-column prop="browser" label="浏览器" align="center" width="160"></el-table-column>
      <!-- 操作系统 -->
      <el-table-column prop="os" label="操作系统" align="center" width="170"></el-table-column>
      <!-- 最近登录时间 -->
      <el-table-column prop="oginTime" label="最近登录时间" align="center" sortable>
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right: 5px" />
          {{ scope.row.loginTime }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="120">
        <template slot-scope="scope">
          <el-popconfirm
            title="确定下线吗？"
            style="margin-left: 10px"
            @confirm="handleForceLogout(scope.row.userId)"
          >
            <el-button size="mini" type="text" slot="reference">
              <i class="el-icon-delete" /> 下线
            </el-button>
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
    ></el-pagination>
  </el-card>
</template>

<script>
import { getOnlineUser, forceLogout } from "@/api/user";
export default {
  name: "Online",
  data() {
    return {
      loading: true,
      userList: [],
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
      getOnlineUser(this.keyword).then(({ data }) => {
        this.userList = data.data.recordList;
        this.count = data.data.count;
        this.loading = false;
      });
    },
    searchOnlineUser() {
      this.currentPage = 1;
      this.getUserList();
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
    },
    handleCurrentChange(currentPage) {
      this.currentPage = currentPage;
    },
    handleForceLogout(userId) {
      forceLogout(userId).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.msg,
          });
          this.getUserList();
        }
      });
    },
  },
};
</script>

<style></style>
