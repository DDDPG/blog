<template>
  <el-card class="main-card">
    <!-- 标题 -->
    <div class="title">{{ this.$route.name }}</div>
    <!-- 评论状态 -->
    <div class="status-menu">
      <span>状态</span>
      <span @click="changeStatus(null)" :class="isActive(null)"> 全部 </span>
      <span @click="changeStatus(1)" :class="isActive(1)"> 正常 </span>
      <span @click="changeStatus(0)" :class="isActive(0)"> 审核中 </span>
    </div>
    <!-- 表格操作 -->
    <div class="operation-container">
      <el-button
        type="success"
        size="small"
        icon="el-icon-success"
        :disabled="commentIdList.length == 0"
        @click="updateCommentCheck(null)"
      >
        批量通过
      </el-button>
      <div style="margin-left: auto">
        <el-select
          clearable
          v-model="commentType"
          placeholder="请选择来源"
          size="small"
          style="margin-right: 1rem"
        >
          <el-option
            v-for="item in commentOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <el-input
          v-model="keyword"
          prefix-icon="el-icon-search"
          size="small"
          placeholder="请输入用户昵称"
          style="width: 200px"
          @keyup.enter.native="searchComments"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left: 1rem"
          @click="searchComments"
        >
          搜索
        </el-button>
      </div>
    </div>
    <el-table border v-loading="loading" :data="commentList" @selection-change="selectionChange">
      <!-- 表格列 -->
      <el-table-column type="selection" align="center" width="55"></el-table-column>
      <!-- 用户头像 -->
      <el-table-column prop="avatar" label="头像" align="center" width="90">
        <template slot-scope="scope">
          <img :src="scope.row.avatar" width="40" height="40" />
        </template>
      </el-table-column>
      <!-- 评论人 -->
      <el-table-column
        prop="fromNickname"
        label="评论人"
        align="center"
        width="130"
      ></el-table-column>
      <!-- 回复人 -->
      <el-table-column prop="toNickname" label="回复人" align="center" width="130">
        <template slot-scope="scope">
          <span v-if="scope.row.toNickname">
            {{ scope.row.toNickname }}
          </span>
          <span v-else>无</span>
        </template>
      </el-table-column>
      <!-- 文章标题 -->
      <el-table-column prop="articleTitle" label="文章标题" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.articleTitle">
            {{ scope.row.articleTitle }}
          </span>
          <span v-else>无</span>
        </template>
      </el-table-column>
      <!-- 评论内容 -->
      <el-table-column prop="commentContent" label="评论内容" align="center">
        <template slot-scope="scope">
          <span v-html="scope.row.commentContent" class="comment-content" />
        </template>
      </el-table-column>
      <!-- 评论状态 -->
      <el-table-column prop="isCheck" label="状态" width="80" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isCheck == 0" type="warning">审核中</el-tag>
          <el-tag v-if="scope.row.isCheck == 1" type="success">正常</el-tag>
        </template>
      </el-table-column>
      <!-- 评论来源 -->
      <el-table-column prop="commentType" label="来源" align="center" width="80">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.commentType == 1">文章</el-tag>
          <el-tag v-if="scope.row.commentType == 2" type="warning">友链</el-tag>
          <el-tag v-if="scope.row.commentType == 3" type="danger">说说</el-tag>
        </template>
      </el-table-column>
      <!-- 评论类型 -->
      <el-table-column prop="isParent" label="类型" width="80" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isParent == 0" type="warning">子评论</el-tag>
          <el-tag v-if="scope.row.isParent == 1" type="success">父评论</el-tag>
        </template>
      </el-table-column>
      <!-- 评论时间 -->
      <el-table-column prop="createTime" label="评论时间" width="150" align="center">
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right: 5px" />
          {{ scope.row.createTime | date }}
        </template>
      </el-table-column>
      <!-- 列操作 -->
      <el-table-column label="操作" width="150" align="center">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.isCheck == 0"
            size="mini"
            type="success"
            @click="updateCommentCheck(scope.row.id)"
          >
            通过
          </el-button>
          <el-popconfirm
            style="margin-left: 10px"
            :title="scope.row.isParent ? '删除评论后，评论下所有回复都会被删除，是否继续?' : '是否删除?'"
            @confirm="deleteComment(scope.row.id, scope.row.isParent)"
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
  </el-card>
</template>

<script>
import { getComments, deleteComment, updateCommentCheck } from "@/api/comment";
export default {
  name: "Comment",
  data() {
    return {
      isCheck: null,
      loading: true,
      isDelete: false,
      commentOptions: [
        {
          value: 1,
          label: "文章",
        },
        {
          value: 2,
          label: "友链",
        },
        {
          value: 3,
          label: "说说",
        },
      ],
      commentIdList: [],
      commentList: [],
      commentType: null,
      keyword: null,
      currentPage: 1,
      pageSize: 10,
      count: 0,
    };
  },
  created() {
    this.getCommentList();
  },
  methods: {
    selectionChange(commentList) {
      this.commentIdList = commentList.map((item) => item.id);
    },
    changeStatus(check) {
      this.isCheck = check;
    },
    getCommentList() {
      getComments(
        this.currentPage,
        this.pageSize,
        this.keyword,
        this.commentType,
        this.isCheck
      ).then(({ data }) => {
        this.commentList = data.data.recordList;
        this.count = data.data.count;
        this.loading = false;
      });
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.getCommentList();
    },
    handleCurrentChange(currentPage) {
      this.currentPage = currentPage;
      this.getCommentList();
    },
    searchComments() {
      this.currentPage = 1;
      this.getCommentList();
    },
    updateCommentCheck(id) {
      let param = {};
      if (id != null) {
        param.idList = [id];
      } else {
        param.idList = this.commentIdList;
      }
      param.isCheck = 1;
      updateCommentCheck(param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.msg,
          });
          this.getCommentList();
        }
      });
    },
    deleteComment(id, isParent) {
      deleteComment({ id, isParent }).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.msg,
          });
          this.getCommentList();
        }
        this.isDelete = false;
      });
    },
  },
  watch: {
    isCheck() {
      this.currentPage = 1;
      this.getCommentList();
    },
    commentType() {
      this.currentPage = 1;
      this.getCommentList();
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

<style>
.comment-content {
  display: inline-block;
}
</style>
