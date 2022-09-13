<template>
  <el-card class="main-card">
    <!-- 标题 -->
    <div class="title">{{ $route.name }}</div>
    <!-- 图片 -->
    <div class="picture-container">
      <div class="picture-info">
        <div class="upload-count">共上传了{{ count }}张</div>
        <el-button type="primary" size="small" icon="el-icon-picture" @click="isUpload = true">
          上传图片
        </el-button>
      </div>
      <!-- 图片操作 -->
      <div class="picture-operation">
        <el-input
          class="apiInfo"
          placeholder="请输入上传接口"
          v-model="uploadUrl"
          clearable
        ></el-input>
        <el-input class="apiInfo" placeholder="请输入Cookie" v-model="cookie" clearable></el-input>
        <el-button
          type="text"
          size="small"
          icon="el-icon-delete"
          style="margin-right: 1rem"
          @click="openDelete"
        >
          回收站
        </el-button>
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
          type="danger"
          @click="isDelete = true"
          :disabled="selectPictureIdList.length == 0"
          size="small"
          icon="el-icon-delete"
        >
          批量删除
        </el-button>
      </div>
    </div>
    <!-- 图片列表 -->
    <el-row class="picture-list" :gutter="10" v-loading="loading">
      <!-- 空状态 -->
      <el-empty v-if="pictureList.length == 0" description="暂无图片"></el-empty>
      <el-checkbox-group v-model="selectPictureIdList" @change="handleCheckedPictureChange">
        <!-- 图片信息 -->
        <el-col :md="4" v-for="item of pictureList" :key="item.id">
          <el-checkbox :label="item.id">
            <div class="picture-item">
              <div class="picture-edit">
                <el-dropdown @command="handleCommand">
                  <i class="el-icon-more" style="color: #fff"></i>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item :command="item">
                      <i class="el-icon-edit">编辑</i>
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </div>
              <el-image
                fit="cover"
                class="picture-img"
                :src="item.pictureUrl"
                :preview-src-list="[item.pictureUrl]"
              ></el-image>
              <div class="picture-name">{{ item.pictureName }}</div>
            </div>
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
    <!-- 编辑对话框 -->
    <el-dialog :visible.sync="isEdit" width="30%">
      <div class="dialog-title-container" slot="title">修改信息</div>
      <el-form label-width="80px" size="medium" :model="pictureForm">
        <el-form-item label="图片名称">
          <el-input style="width: 220px" v-model="pictureForm.pictureName" />
        </el-form-item>
        <el-form-item label="图片描述">
          <el-input style="width: 220px" v-model="pictureForm.pictureDesc" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="isEdit = false">取 消</el-button>
        <el-button type="primary" @click="updatePicture">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 批量删除对话框 -->
    <el-dialog :visible.sync="isDelete" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color: #ff9900">提示</i>
      </div>
      <div style="font-size: 1rem">是否删除选中图片？</div>
      <div slot="footer">
        <el-button @click="isDelete = false">取 消</el-button>
        <el-button type="primary" @click="updatePictureDelete(null)"> 确 定 </el-button>
      </div>
    </el-dialog>
    <!-- 上传对话框 -->
    <el-dialog :visible.sync="isUpload" width="70%" top="10vh">
      <div class="dialog-title-container" slot="title">上传图片</div>
      <!-- 上传 -->
      <div class="upload-container">
        <el-upload
          ref="upload"
          drag
          multiple
          action=""
          accept="image/*"
          list-type="picture-card"
          :limit="limitCount"
          :class="{ hide: hideUpload }"
          :headers="authorization"
          :http-request="uploadPicture"
          :on-preview="handlePreview"
          :before-upload="handleBefore"
          :on-change="handleChange"
          :on-success="handleSuccess"
          :on-exceed="handleExceed"
          :on-remove="handleRemove"
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        </el-upload>
      </div>
      <div slot="footer">
        <div class="upload-footer">
          <div class="upload-count">已上传{{ uploadList.length }}张图片</div>
          <div style="margin-left: auto">
            <el-button @click="isUpload = false">取 消</el-button>
            <el-button @click="savePictures" type="primary">保 存</el-button>
          </div>
        </div>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="dialogImageUrl" alt="" />
    </el-dialog>
  </el-card>
</template>

<script>
import {
  uploadPictures,
  savePictures,
  selectPictures,
  updatePictureDelete,
  updatePicture,
} from "@/api/picture";
import { getToken } from "@/utlis/token";
export default {
  name: "BiliBili",
  data() {
    return {
      uploadList: [],
      pictureIdList: [],
      selectPictureIdList: [],
      pictureList: [],
      pictureForm: {
        id: null,
        pictureName: "",
        pictureDesc: "",
      },
      dialogImageUrl: "",
      uploadUrl: "https://api.vc.bilibili.com/api/v1/drawImage/upload",
      cookie: "",
      isIndeterminate: false,
      dialogVisible: false,
      hideUpload: false,
      checkAll: false,
      loading: true,
      isUpload: false,
      isDelete: false,
      isEdit: false,
      addCount: 0,
      limitCount: 10,
      currentPage: 1,
      pageSize: 18,
      count: 0,
    };
  },
  created() {
    this.listPictures();
  },
  methods: {
    openDelete() {
      this.$router.push({ path: "/picture/delete" });
    },
    handleCurrentChange(currentPage) {
      this.currentPage = currentPage;
      this.listPictures();
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.listPictures();
    },
    updatePicture() {
      if (this.pictureForm.pictureName.trim() == "") {
        this.$message.error("图片名称不能为空");
        return false;
      }
      updatePicture(this.pictureForm).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.msg,
          });
          this.listPictures();
        }
        this.isEdit = false;
      });
    },
    listPictures() {
      selectPictures(this.currentPage, this.pageSize, 0).then(({ data }) => {
        this.pictureList = data.data.recordList;
        this.count = data.data.count;
        this.loading = false;
      });
    },
    handleCommand(command) {
      this.pictureForm = JSON.parse(JSON.stringify(command));
      this.isEdit = true;
    },
    handleCheckAllChange(val) {
      this.selectPictureIdList = val ? this.pictureIdList : [];
      this.isIndeterminate = false;
    },
    handleCheckedPictureChange(value) {
      let checkedCount = value.length;
      this.checkAll = checkedCount === this.pictureIdList.length;
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.pictureIdList.length;
    },
    handleBefore(file) {
      let result = true;
      this.uploadList.forEach((item) => {
        if (item.name == file.name) {
          result = false;
        }
      });
      return result;
    },
    handleSuccess(response, file) {
      this.uploadList.push({ name: file.name, url: response });
    },
    handleRemove(file, fileList) {
      this.uploadList.forEach((item, index) => {
        if (item.url == file.response) {
          this.uploadList.splice(index, 1);
        }
      });
      this.hideUpload = fileList.length >= this.limitCount;
    },
    updatePictureDelete(id) {
      let param = {};
      if (id == null) {
        param = { idList: this.selectPictureIdList, isDelete: 1 };
      } else {
        param = { idList: [id], isDelete: 1 };
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
    handleChange(file, fileList) {
      this.hideUpload = fileList.length >= this.limitCount;
    },
    handleExceed(files, fileList) {
      this.$message.warning(
        `当前限制上传 ${this.limitCount} 个文件，本次选择了 ${files.length} 个文件，共选择了 ${
          files.length + fileList.length
        } 个文件`
      );
    },
    savePictures() {
      var pictureUrlList = [];
      this.uploadList.forEach((item) => {
        pictureUrlList.push(item.url);
      });
      savePictures(pictureUrlList).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.msg,
          });
          this.$refs.upload.clearFiles();
          this.uploadList = [];
          this.listPictures();
        }
      });
      this.isUpload = false;
    },
    uploadPicture(param) {
      let formData = new FormData();
      formData.append("file", param.file);
      formData.append("api", this.uploadUrl);
      formData.append("cookie", this.cookie);
      uploadPictures(formData).then(({ data }) => {
        param.onSuccess(data.data);
      });
    },
    handlePreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
  },
  watch: {
    pictureList() {
      this.pictureIdList = this.pictureList.map((item) => item.id);
    },
  },
  computed: {
    authorization() {
      return {
        Authorization: getToken(),
      };
    },
  },
};
</script>

<style scoped>
.hide >>> .el-upload--picture-card {
  display: none !important;
}
.upload-container >>> .el-upload--picture-card {
  border: none !important;
  width: 0 !important;
  height: 0 !important;
  line-height: 0 !important;
}
.upload-container >>> .el-upload-dragger {
  height: 148px !important;
}
.apiInfo {
  margin-right: 2rem;
}
.apiInfo .el-input__inner {
  height: 37px !important;
}
.upload-container {
  height: 400px;
}
.upload {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.upload-footer {
  display: flex;
  align-items: center;
}
.picture-container {
  display: flex;
  justify-content: space-between;
  margin-top: 2.25rem;
  margin-bottom: 2rem;
}
.upload-count {
  margin-bottom: 0.625rem;
}
.picture-info {
  padding-top: 0.8rem;
  margin-left: 0.8rem;
}
.picture-operation {
  display: flex;
  justify-content: center;
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
