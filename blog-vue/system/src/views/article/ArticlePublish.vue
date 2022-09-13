<template>
  <el-card class="main-card">
    <div class="title">{{ $route.name }}</div>
    <!-- 文章标题 -->
    <div class="article-container">
      <el-input
        v-model="article.articleTitle"
        size="medium"
        placeholder="请输入文章标题"
      ></el-input>
      <el-button
        type="info"
        size="medium"
        @click="saveArticleDraft"
        style="margin-left: 10px"
        v-if="article.id == null || article.status == 3"
        >保存草稿</el-button
      >
      <el-button type="danger" size="medium" @click="openModel" style="margin-left: 10px"
        >发布文章</el-button
      >
    </div>
    <!--文章内容-->
    <mavon-editor
      ref="md"
      @imgAdd="uploadImg"
      v-model="article.articleContent"
      style="height: calc(100vh - 260px)"
    />
    <!-- 发布对话框 -->
    <el-dialog :visible.sync="addOrEdit" width="40%" top="3vh">
      <div class="dialog-title-container" slot="title">发布文章</div>
      <el-form label-width="80px" size="medium" :model="article">
        <!-- 文章分类 -->
        <el-form-item label="文章分类">
          <el-tag
            type="success"
            v-show="article.categoryName"
            style="margin: 0 1rem 0 0"
            :closable="true"
            @close="removeCategory"
          >
            {{ article.categoryName }}
          </el-tag>
          <!-- 分类选项 -->
          <el-popover
            placement="bottom-start"
            width="460"
            trigger="click"
            v-if="!article.categoryName"
          >
            <div class="popover-title">分类</div>
            <!-- 搜索框 -->
            <el-autocomplete
              style="width: 100%"
              v-model="categoryName"
              :fetch-suggestions="searchCategory"
              placeholder="请输入分类名搜索,enter可添加自定义分类"
              :trigger-on-focus="false"
              @keyup.enter.native="saveCategory"
              @select="handleSelectCategory"
            >
              <template slot-scope="{ item }">
                <div>{{ item.categoryName }}</div>
              </template>
            </el-autocomplete>
            <!-- 分类 -->
            <div class="popover-container">
              <div
                v-for="item of categoryList"
                :key="item.id"
                class="category-item"
                @click="addCategory(item)"
              >
                {{ item.categoryName }}
              </div>
            </div>
            <el-button type="success" plain slot="reference" size="small"> 添加分类 </el-button>
          </el-popover>
        </el-form-item>
        <!-- 文章标签 -->
        <el-form-item label="文章标签">
          <el-tag
            v-for="(item, index) of article.tagNameList"
            :key="index"
            style="margin: 0 1rem 0 0"
            :closable="true"
            @close="removeTag(item)"
          >
            {{ item }}
          </el-tag>
          <!-- 标签选项 -->
          <el-popover
            placement="bottom-start"
            width="460"
            trigger="click"
            v-if="article.tagNameList.length < 3"
          >
            <div class="popover-title">标签</div>
            <!-- 搜索框 -->
            <el-autocomplete
              style="width: 100%"
              v-model="tagName"
              :fetch-suggestions="searchTag"
              placeholder="请输入标签名搜索,enter可添加自定义标签"
              :trigger-on-focus="false"
              @keyup.enter.native="saveTag"
              @select="handleSelectTag"
            >
              <template slot-scope="{ item }">
                <div>{{ item.tagName }}</div>
              </template>
            </el-autocomplete>
            <!-- 标签 -->
            <div class="popover-container">
              <div style="margin-bottom: 1rem">添加标签</div>
              <el-tag
                v-for="(item, index) of tagList"
                :key="index"
                :class="tagClass(item)"
                @click="addTag(item)"
              >
                {{ item.tagName }}
              </el-tag>
            </div>
            <el-button type="primary" plain slot="reference" size="small"> 添加标签 </el-button>
          </el-popover>
        </el-form-item>
        <!-- 文章类型 -->
        <el-form-item label="文章类型">
          <el-select v-model="article.articleType" placeholder="请选择类型">
            <el-option
              v-for="item in typeList"
              :key="item.type"
              :label="item.description"
              :value="item.type"
            ></el-option>
          </el-select>
        </el-form-item>
        <!-- 文章缩略图 -->
        <el-form-item label="缩略图">
          <el-upload
            class="upload-cover"
            drag
            :headers="authorization"
            action="/api/admin/articles/images"
            multiple
            :before-upload="beforeUpload"
            :on-success="uploadCover"
          >
            <i class="el-icon-upload" v-if="article.articleCover == ''" />
            <div class="el-upload__text" v-if="article.articleCover == ''">
              将文件拖到此处，或<em>点击上传</em>
            </div>
            <img v-else :src="article.articleCover" width="360px" height="180px" />
          </el-upload>
        </el-form-item>
        <!-- 置顶 -->
        <el-form-item label="置顶">
          <el-switch
            v-model="article.isTop"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            :active-value="1"
            :inactive-value="0"
          ></el-switch>
        </el-form-item>
        <!-- 发布形式 -->
        <el-form-item label="发布形式">
          <el-radio-group v-model="article.status">
            <el-radio :label="1">公开</el-radio>
            <el-radio :label="2">私密</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOrEdit = false">取 消</el-button>
        <el-button type="danger" @click="saveOrUpdateArticle">发布</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import { saveOrUpdateArticle, uploadArticleImg, getArticle } from "@/api/article";
import { searchCategory } from "@/api/category";
import { searchTag } from "@/api/tag";
import { getToken } from "@/utlis/token";
import * as imageConversion from "image-conversion";
export default {
  name: "ArticlePublish",
  data() {
    return {
      addOrEdit: false,
      autoSave: true,
      categoryList: [],
      tagList: [],
      typeList: [
        {
          type: 1,
          description: "原创",
        },
        {
          type: 2,
          description: "转载",
        },
        {
          type: 3,
          description: "翻译",
        },
      ],
      categoryName: "",
      tagName: "",
      article: {
        id: null,
        articleCover: "",
        articleTitle: this.$moment(new Date()).format("YYYY-MM-DD"),
        articleContent: "",
        categoryName: null,
        tagNameList: [],
        isTop: 0,
        articleType: 1,
        isComment: 1,
        status: 1,
      },
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      const articleId = this.$route.params.articleId;
      if (articleId) {
        getArticle(articleId).then(({ data }) => {
          this.article = data.data;
        });
      } else {
        this.article = {
          id: null,
          articleCover: "",
          articleTitle: this.$moment(new Date()).format("YYYY-MM-DD"),
          articleContent: "",
          categoryName: null,
          tagNameList: [],
          isTop: 0,
          articleType: 1,
          isComment: 1,
          status: 1,
        };
      }
    },
    // 保存草稿
    saveArticleDraft() {
      if (this.article.articleTitle.trim() == "") {
        this.$message.error("文章标题不能为空");
        return false;
      }
      if (this.article.articleContent.trim() == "") {
        this.$message.error("文章内容不能为空");
        return false;
      }
      this.article.status = 3;
      saveOrUpdateArticle(this.article).then(({data})=>{
        if (data.flag) {
          if (this.article.id === null) {
            this.$store.commit("tab/removeTab", "发布文章");
          } else {
            this.$store.commit("tab/removeTab", "修改文章");
          }
          this.$router.push({ path: "/article-list" });
          this.$notify.success({
            title: "成功",
            message: "保存草稿成功"
          });
        } 
      })
    },
    // 添加或修改文章
    saveOrUpdateArticle() {
      if (this.article.articleTitle.trim() == "") {
        this.$message.error("文章标题不能为空");
        return false;
      }
      if (this.article.articleContent.trim() == "") {
        this.$message.error("文章内容不能为空");
        return false;
      }
      if (this.article.categoryName == null) {
        this.$message.error("文章分类不能为空");
        return false;
      }
      if (this.article.tagNameList.length == 0) {
        this.$message.error("文章标签不能为空");
        return false;
      }
      saveOrUpdateArticle(this.article).then(({ data }) => {
        if (data.flag) {
          if (this.article.id === null) {
            this.$store.commit("tab/removeTab", "发布文章");
          } else {
            this.$store.commit("tab/removeTab", "修改文章");
          }
          this.$router.push({ path: "/article-list" });
          this.$notify.success({
            title: "发表成功",
            message: data.msg,
          });
        }
        this.addOrEdit = false;
      });
    },
    autoSaveArticle() {},
    // 上传图片成功处理
    uploadCover(response) {
      this.article.articleCover = response.data;
    },
    // 图片上传前压缩处理
    beforeUpload(file) {
      return new Promise((resolve) => {
        if (file.size / 1024 < this.config.UPLOAD_SIZE) {
          resolve(file);
        }
        // 压缩到200KB,这里的200就是要压缩的大小,可自定义
        imageConversion.compressAccurately(file, this.config.UPLOAD_SIZE).then((res) => {
          resolve(res);
        });
      });
    },
    // 上传图片
    uploadImg(pos, file) {
      var formdata = new FormData();
      if (file.size / 1024 < this.config.UPLOAD_SIZE) {
        formdata.append("file", file);
        uploadArticleImg(formdata).then(({ data }) => {
          this.$refs.md.$img2Url(pos, data.data);
        });
      } else {
        // 压缩到200KB,这里的200就是要压缩的大小,可自定义
        imageConversion.compressAccurately(file, this.config.UPLOAD_SIZE).then((res) => {
          formdata.append("file", new window.File([res], file.name, { type: file.type }));
          uploadArticleImg(formdata).then(({ data }) => {
            this.$refs.md.$img2Url(pos, data.data);
          });
        });
      }
    },
    // 分类列表
    listCategory() {
      searchCategory().then(({ data }) => {
        this.categoryList = data.data;
      });
    },
    // 标签列表
    listTag() {
      searchTag().then(({ data }) => {
        this.tagList = data.data;
      });
    },
    // 打开发布对话框
    openModel() {
      if (this.article.articleTitle.trim() == "") {
        this.$message.error("文章标题不能为空");
        return false;
      }
      if (this.article.articleContent.trim() == "") {
        this.$message.error("文章内容不能为空");
        return false;
      }
      this.listCategory();
      this.listTag();
      this.addOrEdit = true;
    },
    // 保存分类
    saveCategory() {
      // 分类不为空
      if (this.categoryName.trim() != "") {
        this.addCategory({
          categoryName: this.categoryName,
        });
        this.categoryName = "";
      }
    },
    // 添加分类到article
    addCategory(item) {
      this.article.categoryName = item.categoryName;
    },
    // 从article中移除分类
    removeCategory() {
      this.article.categoryName = "";
    },
    // 查询分类
    searchCategory(keyword, cb) {
      searchCategory(keyword).then(({ data }) => {
        // 返回提示信息
        cb(data.data);
      });
    },
    // 选择分类
    handleSelectCategory(item) {
      this.addCategory({
        categoryName: item.categoryName,
      });
    },
    // 选择标签
    handleSelectTag(item) {
      this.addTag({
        tagName: item.tagName,
      });
    },
    // 查询标签
    searchTag(keyword, cb) {
      searchTag(keyword).then(({ data }) => {
        cb(data.data);
      });
    },
    // 保存标签
    saveTag() {
      if (this.tagName.trim() != "") {
        this.addTag({
          tagName: this.tagName,
        });
        this.tagName = "";
      }
    },
    // 添加标签到article
    addTag(item) {
      // 如果是不重复的标签则添加
      if (this.article.tagNameList.indexOf(item.tagName) == -1) {
        this.article.tagNameList.push(item.tagName);
      }
    },
    // 从article中移除标签
    removeTag(item) {
      const index = this.article.tagNameList.indexOf(item);
      this.article.tagNameList.splice(index, 1);
    },
  },
  computed: {
    tagClass() {
      return function (item) {
        const index = this.article.tagNameList.indexOf(item.tagName);
        return index != -1 ? "tag-item-select" : "tag-item";
      };
    },
    authorization() {
      return {
        Authorization: getToken(),
      };
    },
  },
};
</script>

<style>
.article-container {
  display: flex;
  align-items: center;
  margin-bottom: 1.25rem;
  margin-top: 2.25rem;
}
.popover-title {
  margin-bottom: 1rem;
  text-align: center;
}
.popover-container {
  margin-top: 1rem;
  height: 260px;
  overflow-y: auto;
}
.category-item {
  cursor: pointer;
  padding: 0.6rem 0.5rem;
}
.category-item:hover {
  background-color: #f0f9eb;
  color: #67c23a;
}
.tag-item {
  margin-right: 1rem;
  margin-bottom: 1rem;
  cursor: pointer;
}
.tag-item-select {
  margin-right: 1rem;
  margin-bottom: 1rem;
  cursor: not-allowed;
  color: #ccccd8 !important;
}
</style>
