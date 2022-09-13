<template>
  <el-card class="main-card">
    <!-- 标题 -->
    <div class="title">{{ this.$route.name }}</div>
    <!-- 表格操作 -->
    <div class="operation-container">
      <el-button type="primary" size="small" icon="el-icon-plus" @click="openModel(null)"> 新增 </el-button>
      <el-button type="danger" size="small" icon="el-icon-delete" :disabled="friendIdList.length == 0" @click="isDelete = true"> 批量删除 </el-button>
      <!-- 条件筛选 -->
      <div style="margin-left: auto">
        <el-input
          v-model="keyword"
          prefix-icon="el-icon-search"
          size="small"
          placeholder="请输入友链名"
          style="width: 200px"
          @keyup.enter.native="searchFriend"
        />
        <el-button type="primary" size="small" icon="el-icon-search" style="margin-left: 1rem" @click="searchFriend"> 搜索 </el-button>
      </div>
    </div>
    <!-- 表格展示 -->
    <el-table border :data="friendList" @selection-change="selectionChange" v-loading="loading">
      <!-- 表格列 -->
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="avatar" label="链接头像" align="center" width="110">
        <template slot-scope="scope">
          <img :src="scope.row.avatar" width="40" height="40" />
        </template>
      </el-table-column>
      <el-table-column prop="color" label="链接颜色" align="center" width="150">
        <template slot-scope="scope">
          <span class="color-item" :style="{background: scope.row.color}"></span>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="链接名" align="center"></el-table-column>
      <el-table-column prop="url" label="链接地址" align="center"></el-table-column>
      <el-table-column prop="introduction" label="链接介绍" align="center"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="140" align="center">
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right: 5px" />
          {{ scope.row.createTime | date }}
        </template>
      </el-table-column>
      <!-- 列操作 -->
      <el-table-column label="操作" align="center" width="160">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="openModel(scope.row)"> 编辑 </el-button>
          <el-popconfirm title="确定删除吗？" style="margin-left: 1rem" @confirm="deleteFriend(scope.row.id)">
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
    ></el-pagination>
    <!-- 批量删除对话框 -->
    <el-dialog :visible.sync="isDelete" width="30%">
      <div class="dialog-title-container" slot="title"><i class="el-icon-warning" style="color: #ff9900" />提示</div>
      <div style="font-size: 1rem">是否删除选中项？</div>
      <div slot="footer">
        <el-button @click="isDelete = false">取 消</el-button>
        <el-button type="primary" @click="deleteFriend(null)"> 确 定 </el-button>
      </div>
    </el-dialog>
    <!-- 添加对话框 -->
    <el-dialog :visible.sync="addOrEdit" width="27%">
      <div class="dialog-title-container" slot="title" ref="friendTitle"></div>
      <el-form label-width="80px" size="medium" :model="friendForm">
        <el-form-item label="链接名称">
          <el-input style="width: 250px" v-model="friendForm.name"></el-input>
        </el-form-item>
        <el-form-item label="链接颜色">
          <!-- <el-input style="width: 250px" v-model="friendForm.color"></el-input> -->
          <el-color-picker v-model="friendForm.color"></el-color-picker>
        </el-form-item>
        <el-form-item label="链接头像">
          <el-input style="width: 250px" v-model="friendForm.avatar"></el-input>
        </el-form-item>
        <el-form-item label="链接地址">
          <el-input style="width: 250px" v-model="friendForm.url"></el-input>
        </el-form-item>
        <el-form-item label="链接介绍">
          <el-input style="width: 250px" v-model="friendForm.introduction"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOrEdit = false">取 消</el-button>
        <el-button type="primary" @click="addOrEditFriend">确 定</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import { getFriends, addOrEditFriend, deleteFriend } from "@/api/friend";
export default {
  name: "Friend",
  data: function () {
    return {
      isDelete: false,
      addOrEdit: false,
      loading: true,
      friendForm: {
        id: null,
        name: "",
        avatar: "",
        url: "",
        introduction: "",
      },
      friendIdList: [],
      friendList: [],
      keyword: null,
      currentPage: 1,
      pageSize: 10,
      count: 0,
    };
  },
  created() {
    this.getFriendList();
  },
  methods: {
    selectionChange(friendList) {
      this.friendIdList = friendList.map((item) => item.id);
    },
    openModel(friend) {
      if (friend != null) {
        this.friendForm = JSON.parse(JSON.stringify(friend));
        this.$refs.friendTitle.innerHTML = "修改友链";
      } else {
        this.friendForm.id = null;
        this.friendForm.name = "";
        this.friendForm.avatar = "";
        this.friendForm.url = "";
        this.friendForm.introduction = "";
        this.$refs.friendTitle.innerHTML = "添加分类";
      }
      this.addOrEdit = true;
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.getFriendList();
    },
    handleCurrentChange(currentPage) {
      this.currentPage = currentPage;
      this.getFriendList();
    },
    addOrEditFriend() {
      if (this.friendForm.name.trim() == "") {
        this.$message.error("友链名不能为空");
        return false;
      }
      if (this.friendForm.avatar.trim() == "") {
        this.$message.error("友链头像不能为空");
        return false;
      }
      if (this.friendForm.url.trim() == "") {
        this.$message.error("友链地址不能为空");
        return false;
      }
      if (this.friendForm.introduction.trim() == "") {
        this.$message.error("友链介绍不能为空");
        return false;
      }
      addOrEditFriend(this.friendForm).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.msg,
          });
          this.getFriendList();
        }
        this.addOrEdit = false;
      });
    },
    getFriendList() {
      getFriends(this.currentPage, this.pageSize, this.keyword).then(({ data }) => {
        this.friendList = data.data.recordList;
        this.count = data.data.count;
        this.loading = false;
      });
    },
    searchFriend() {
      this.currentPage = 1;
      this.getFriendList();
    },
    deleteFriend(id) {
      let param = [];
      if (id == null) {
        param = this.friendIdList;
      } else {
        param = [id];
      }
      deleteFriend(param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.msg,
          });
          this.getFriendList();
        }
        this.isDelete = false;
      });
    },
  },
};
</script>

<style scoped>
.color-item {
  position: absolute;
  top: 16px;
  left: 35px;
  width: 80px;
  height: 40px;
}
</style>
