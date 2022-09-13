<template>
  <el-card class="main-card">
    <!-- 标题 -->
    <div class="title">{{ $route.name }}</div>
    <!-- 图片操作 -->
    <div class="picture-operation">
      <div class="all-check">
        <el-checkbox
          :indeterminate="isIndeterminate"
          v-model="checkAll"
          @change="handleCheckAllChange"
        >
          全选
        </el-checkbox>
        <div class="check-count">已选择{{ selectPictureIdList.length }}张</div>
      </div>
      <el-button
        type="success"
        @click="updatePictureDelete(null)"
        :disabled="selectPictureIdList.length == 0"
        size="small"
        icon="el-icon-deleteItem"
      >
        批量恢复
      </el-button>
      <el-button
        type="danger"
        @click="isDelete = true"
        :disabled="selectPictureIdList.length == 0"
        size="small"
        icon="el-icon-delete"
      >
        批量删除
      </el-button>
    </div>
    <!-- 图片列表 -->
    <el-row class="picture-list" :gutter="10">
      <!-- 空状态 -->
      <el-empty v-if="pictureList.length == 0" description="暂无图片"></el-empty>
      <el-checkbox-group v-model="selectPictureIdList" @change="handleCheckedPictureChange">
        <!-- 图片信息 -->
        <el-col :md="4" v-for="item of pictureList" :key="item.id">
          <el-checkbox :label="item.id">
            <el-image
              fit="cover"
              class="picture-img"
              :src="item.pictureUrl"
              :preview-src-list="[item.pictureUrl]"
            ></el-image>
            <div class="picture-name">{{ item.pictureName }}</div>
          </el-checkbox>
        </el-col>
      </el-checkbox-group>
    </el-row>
    <!-- 分页 -->
    <el-pagination
      :hide-on-single-page="true"
      class="pagination-container"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-size="pageSize"
      :total="count"
      layout="prev, pager, next"
    ></el-pagination>
    <!-- 批量删除对话框 -->
    <el-dialog :visible.sync="isDelete" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color: #ff9900" />提示
      </div>
      <div style="font-size: 1rem">是否删除选中图片？</div>
      <div slot="footer">
        <el-button @click="isDelete = false">取 消</el-button>
        <el-button type="primary" @click="deletePictures(null)"> 确 定 </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import { selectPictures, updatePictureDelete, deletePictures } from "@/api/picture";
export default {
  name: "Delete",
  data() {
    return {
      loading: true,
      isDelete: false,
      isIndeterminate: false,
      checkAll: false,
      pictureIdList: [],
      selectPictureIdList: [],
      pictureList: [],
      currentPage: 1,
      pageSize: 18,
      count: 0,
    };
  },
  created() {
    this.listPictures();
  },
  methods: {
    handleCheckAllChange(val) {
      this.selectPictureIdList = val ? this.pictureIdList : [];
      this.isIndeterminate = false;
    },
    handleCheckedPictureChange(value) {
      let checkedCount = value.length;
      this.checkAll = checkedCount === this.pictureIdList.length;
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.pictureIdList.length;
    },
    handleCurrentChange(currentPage) {
      this.currentPage = currentPage;
      this.listPictures();
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.listPictures();
    },
    listPictures() {
      selectPictures(this.currentPage, this.pageSize, 1).then(({ data }) => {
        this.pictureList = data.data.recordList;
        this.count = data.data.count;
        this.loading = false;
      });
    },
    updatePictureDelete(id) {
      let param = {};
      if (id == null) {
        param = { idList: this.selectPictureIdList, isDelete: 0 };
      } else {
        param = { idList: [id], isDelete: 0 };
      }
      updatePictureDelete(param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.msg,
          });
          this.listPictures();
        }
      });
      this.isDelete = false;
    },
    deletePictures() {
      deletePictures(this.selectPictureIdList).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.msg,
          });
          this.listPictures();
        }
      });
      this.isDelete = false;
    },
  },
  watch: {
    pictureList() {
      this.pictureIdList = this.pictureList.map((item) => item.id);
    },
  },
};
</script>

<style>
.picture-operation {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding-top: 2.25rem;
  margin-right: 0.625rem;
}
.picture-name {
  font-size: 14px;
  margin-top: 0.3rem;
  text-align: center;
}
.all-check {
  display: inline-flex;
  align-items: center;
  margin-right: 1rem;
}
.check-count {
  width: 60px;
  margin-left: 1rem;
  font-size: 12px;
}
.picture-item {
  width: 100%;
  position: relative;
  cursor: pointer;
  margin-bottom: 1rem;
}
.picture-edit {
  position: absolute;
  z-index: 1000;
  top: 0.3rem;
  right: 0.5rem;
}
.picture-img {
  width: 100%;
  height: 7rem;
  border-radius: 4px;
}
</style>
