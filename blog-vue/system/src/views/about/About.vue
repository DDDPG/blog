<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <mavon-editor
      ref="md"
      @imgAdd="uploadImg"
      v-model="aboutContent"
      style="height: calc(100vh - 250px); margin-top: 2.25rem"
    />
    <el-button type="danger" size="medium" class="edit-btn" @click="updateAbout"> 修改 </el-button>
  </el-card>
</template>

<script>
import { getAbout, updateAbout } from "@/api/about";
import * as imageConversion from "image-conversion";
export default {
  name: "About",
  data() {
    return {
      aboutContent: "",
    };
  },
  created() {
    this.getAbout();
  },
  methods: {
    getAbout() {
      getAbout().then(({ data }) => {
        this.aboutContent = data.data;
      });
    },
    uploadImg(pos, file) {
      var formdata = new FormData();
      if (file.size / 1024 < this.config.UPLOAD_SIZE) {
        formdata.append("file", file);
        this.axios.post("/api/admin/articles/images", formdata).then(({ data }) => {
          this.$refs.md.$img2Url(pos, data.data);
        });
      } else {
        // 压缩到200KB,这里的200就是要压缩的大小,可自定义
        imageConversion.compressAccurately(file, this.config.UPLOAD_SIZE).then((res) => {
          formdata.append("file", new window.File([res], file.name, { type: file.type }));
          this.axios.post("/api/admin/articles/images", formdata).then(({ data }) => {
            this.$refs.md.$img2Url(pos, data.data);
          });
        });
      }
    },
    updateAbout() {
      updateAbout({ aboutContent: this.aboutContent }).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.msg,
          });
        }
      });
    },
  },
};
</script>

<style scoped>
.edit-btn {
  float: right;
  margin: 1rem 0;
}
</style>
