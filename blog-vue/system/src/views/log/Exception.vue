<template>
  <el-card class="main-card">
    <!-- 标题 -->
    <div class="title">{{ this.$route.name }}</div>
    <!-- 表格操作 -->
    <div class="operation-container">
      <el-button
        type="danger"
        plain
        size="small"
        icon="el-icon-delete"
        :disabled="this.logIdList.length == 0"
        @click="isDelete = true"
      >
        批量删除
      </el-button>
      <div style="margin-left: auto">
        <el-input
          v-model="optModule"
          prefix-icon="el-icon-search"
          size="small"
          placeholder="请输入操作模块"
          style="width: 200px"
          @keyup.enter.native="searchException"
        ></el-input>
        <el-input
          v-model="keyword"
          prefix-icon="el-icon-search"
          size="small"
          placeholder="请输入操作描述"
          style="width: 200px; margin-left: 1rem"
          @keyup.enter.native="searchException"
        ></el-input>
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left: 1rem"
          @click="searchException"
        >
          搜索
        </el-button>
      </div>
    </div>
    <!-- 表格展示 -->
    <el-table border :data="logList" @selection-change="selectionChange" v-loading="loading">
      <!-- 表格列 -->
      <el-table-column type="selection" align="center" width="55"></el-table-column>
      <!-- 异常模块 -->
      <el-table-column prop="module" label="异常模块" align="center" width="80"></el-table-column>
      <!-- 操作描述 -->
      <el-table-column
        prop="description"
        label="操作描述"
        align="center"
        width="140"
      ></el-table-column>
      <!-- 请求方式 -->
      <el-table-column prop="requestMethod" label="请求方式" align="center" width="90"
        ><template slot-scope="scope">
          <el-tag :type="tagType(scope.row.requestMethod)">
            {{ scope.row.requestMethod }}
          </el-tag>
        </template>
      </el-table-column>
      <!-- 操作ip -->
      <el-table-column prop="ipAddress" label="操作ip" align="center" width="140"></el-table-column>
      <!-- 操作地点 -->
      <el-table-column prop="ipSource" label="操作地点" align="center"></el-table-column>
      <!-- 操作系统 -->
      <el-table-column prop="os" label="操作系统" align="center"></el-table-column>
      <!-- 浏览器 -->
      <el-table-column prop="browser" label="浏览器" align="center"></el-table-column>
      <!-- 操作日期 -->
      <el-table-column prop="createTime" label="操作日期" align="center" width="170">
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right: 5px" />
          {{ scope.row.createTime }}
        </template>
      </el-table-column>
      <!-- 操作 -->
      <el-table-column label="操作" align="center" width="115">
        <template slot-scope="scope">
          <el-button size="mini" type="text" slot="reference" @click="check(scope.row)">
            <i class="el-icon-view" /> 查看
          </el-button>
          <el-popconfirm
            title="确定删除吗？"
            style="margin-left: 10px"
            @confirm="deleteException(scope.row.id)"
          >
            <el-button size="mini" type="text" slot="reference">
              <i class="el-icon-delete" /> 删除
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
    >
    </el-pagination>
    <!-- 查看模态框 -->
    <el-dialog :visible.sync="isCheck" width="80%">
      <div class="dialog-title-container" slot="title"><i class="el-icon-more"></i>详细信息</div>
      <el-form ref="form" :model="expLog" label-width="100px" size="mini">
        <el-form-item label="异常名称：">
          {{ expLog.name }}
        </el-form-item>
        <el-form-item label="请求地址：">
          {{ expLog.uri }}
        </el-form-item>
        <el-form-item label="异常方法：">
          {{ expLog.errorMethod }}
        </el-form-item>
        <el-form-item label="异常信息：">
          <code v-html="expLog.message"></code>
        </el-form-item>
      </el-form>
    </el-dialog>
    <!-- 批量删除对话框 -->
    <el-dialog :visible.sync="isDelete" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color: #ff9900"></i>提示
      </div>
      <div style="font-size: 1rem">是否删除选中项?</div>
      <div slot="footer">
        <el-button @click="isDelete = false">取 消</el-button>
        <el-button type="primary" @click="deleteException(null)">确 定</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import { getExceptions, deleteException } from "@/api/log";
export default {
  name: "Exception",
  data() {
    return {
      logList: [],
      logIdList: [],
      expLog: {},
      isCheck: false,
      loading: true,
      isDelete: false,
      optModule: null,
      keyword: null,
      currentPage: 1,
      pageSize: 10,
      count: 0,
    };
  },
  created() {
    this.getExceptionList();
  },
  methods: {
    selectionChange(logList) {
      this.logIdList = logList.map((item) => item.id);
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.getExceptionList();
    },
    handleCurrentChange(currentPage) {
      this.currentPage = currentPage;
      this.getExceptionList();
    },
    getExceptionList() {
      getExceptions(this.currentPage, this.pageSize, this.optModule, this.keyword).then(
        ({ data }) => {
          this.logList = data.data.recordList;
          this.count = data.data.count;
          this.loading = false;
        }
      );
    },
    searchException() {
      this.currentPage = 1;
      this.getExceptionList();
    },
    deleteException(id) {
      let param = [];
      if (id == null) {
        param = this.logIdList;
      } else {
        param = [id];
      }
      deleteException(param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.msg,
          });
          this.getExceptionList();
        }
        this.isDelete = false;
      });
    },
    check(expLog) {
      this.expLog = expLog;
      this.isCheck = true;
    },
  },
  computed: {
    tagType() {
      return function (type) {
        switch (type) {
          case "GET":
            return "";
          case "POST":
            return "success";
          case "PUT":
            return "warning";
          case "DELETE":
            return "danger";
        }
      };
    },
  },
};
</script>

<style></style>