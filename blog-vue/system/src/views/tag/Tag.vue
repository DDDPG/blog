<template>
  <el-card class="main-card">
    <!-- 标题 -->
    <div class="title">{{ $route.name }}</div>
    <!-- 表格操作 -->
    <div class="operation-container">
      <el-button
        type="primary"
        plain
        size="small"
        icon="el-icon-plus"
        @click="openModel(null)"
      >
        新增
      </el-button>
      <el-button
        type="danger"
        plain
        size="small"
        icon="el-icon-delete"
        :disabled="this.tagIdList.length == 0"
        @click="isDelete = true"
      >
        批量删除
      </el-button>
      <div style="margin-left: auto">
        <el-input
          v-model="keyword"
          prefix-icon="el-icon-search"
          size="small"
          placeholder="请输入标签名"
          style="width: 200px"
          @keyup.enter.native="searchTag"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left: 1rem"
          @click="searchTag"
        >
          搜索
        </el-button>
      </div>
    </div>
    <!-- 表格展示 -->
    <el-table
      :data="tagList"
      @selection-change="selectionChange"
      v-loading="loading"
    >
      <!-- 表格列 -->
      <el-table-column type="selection" width="55" align="center"></el-table-column>
      <!-- 标签名 -->
      <el-table-column prop="tagName" label="标签名" align="center">
        <template slot-scope="scope">
          <el-tag>
            {{ scope.row.tagName }}
          </el-tag>
        </template>
      </el-table-column>
      <!-- 文章量 -->
      <el-table-column
        prop="articleCount"
        label="文章量"
        align="center"
      ></el-table-column>
      <!-- 分类创建时间 -->
      <el-table-column prop="createTime" label="创建时间" align="center">
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right: 5px"></i>
          {{ scope.row.createTime | date }}
        </template>
      </el-table-column>
      <!-- 列操作 -->
      <el-table-column label="操作" width="160" align="center">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="openModel(scope.row)">
            编辑
          </el-button>
          <el-popconfirm
            title="确定删除吗？"
            style="margin-left: 1rem"
            @confirm="deleteTag(scope.row.id)"
          >
            <el-button size="mini" type="danger" slot="reference">
              删除
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
    <!--编辑对话框-->
    <el-dialog :visible.sync="addOrEdit" width="26%">
      <div class="dialog-title-container" slot="title" ref="tagTitle"></div>
      <el-form label-width="80px" size="medium" :model="tagForm">
        <el-form-item label="标签名">
          <el-input v-model="tagForm.tagName" style="width: 220px"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOrEdit = false">取 消</el-button>
        <el-button type="primary" @click="addOrEditTag">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 批量删除对话框 -->
    <el-dialog :visible.sync="isDelete" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color: #ff9900">提示</i>
      </div>
      <div style="font-size: 1rem">是否删除选中项?</div>
      <div slot="footer">
        <el-button @click="isDelete = false">取 消</el-button>
        <el-button type="primary" @click="deleteTag(null)">确 定</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import { getTags, addOrEditTag, deleteTag } from "@/api/tag";
export default {
  name: "Tag",
  data() {
    return {
      isDelete: false,
      addOrEdit: false,
      loading: true,
      tagForm: {
        id: null,
        tagName: "",
      },
      tagIdList: [],
      tagList: [],
      keyword: null,
      currentPage: 1,
      pageSize: 10,
      count: 0,
    };
  },
  created() {
    this.getTagList();
  },
  methods: {
    selectionChange(tagList) {
      this.tagIdList = tagList.map((item) => item.id);
    },
    openModel(tag) {
      if (tag != null) {
        this.tagForm = JSON.parse(JSON.stringify(tag));
        this.$refs.tagTitle.innerHTML = "修改标签";
      } else {
        this.tagForm.id = null;
        this.tagForm.tagName = "";
        this.$refs.tagTitle.innerHTML = "添加标签";
      }
      this.addOrEdit = true;
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.getTagList();
    },
    handleCurrentChange(currentPage) {
      this.currentPage = currentPage;
      this.getTagList();
    },
    addOrEditTag() {
      if (this.tagForm.tagName.trim() == "") {
        this.$message.error("标签名不能为空");
        return false;
      }
      addOrEditTag(this.tagForm).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.msg,
          });
          this.getTagList();
        }
        this.addOrEdit = false;
      });
    },
    getTagList() {
      getTags(this.currentPage, this.pageSize, this.keyword).then(
        ({ data }) => {
          this.tagList = data.data.recordList;
          this.count = data.data.count;
          this.loading = false;
        }
      );
    },
    searchTag() {
      this.currentPage = 1;
      this.getTagList();
    },
    deleteTag(id) {
      let param = [];
      if (id == null) {
        param = this.tagIdList;
      } else {
        param = [id];
      }
      deleteTag(param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.msg,
          });
          this.getTagList();
        }
        this.isDelete = false;
      });
    },
  },
};
</script>
