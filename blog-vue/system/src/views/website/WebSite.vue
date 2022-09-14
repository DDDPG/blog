<template>
  <el-card class="main-card">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="网站信息" name="info">
        <el-form label-width="100px" :model="websiteConfigForm" label-position="left">
          <el-form-item label="作者头像">
            <el-upload
              class="avatar-uploader"
              :headers="authorization"
              action="/api/admin/site/image"
              :show-file-list="false"
              :on-success="handleAuthorAvatarSuccess"
            >
              <img
                v-if="websiteConfigForm.authorAvatar"
                :src="websiteConfigForm.authorAvatar"
                class="avatar"
              />
              <i v-else class="el-icon-plus avatar-uploader-icon" />
            </el-upload>
          </el-form-item>
          <el-form-item label="网站名称">
            <el-input v-model="websiteConfigForm.websiteName" size="small" style="width: 400px" />
          </el-form-item>
          <el-form-item label="网站作者">
            <el-input
              v-model="websiteConfigForm.websiteAuthor"
              size="small"
              style="width: 400px"
            ></el-input>
          </el-form-item>
          <el-form-item label="网站简介">
            <el-input
              v-model="websiteConfigForm.websiteIntro"
              size="small"
              style="width: 400px"
            ></el-input>
          </el-form-item>
          <el-form-item label="网站创建日期">
            <el-date-picker
              style="width: 400px"
              value-format="yyyy-MM-dd"
              v-model="websiteConfigForm.websiteCreateTime"
              type="date"
              placeholder="选择日期"
            ></el-date-picker>
          </el-form-item>
          <el-form-item label="备案号">
            <el-input
              v-model="websiteConfigForm.websiteRecordNumber"
              size="small"
              style="width: 400px"
            ></el-input>
          </el-form-item>
          <el-form-item label="第三方登录">
            <el-checkbox-group v-model="websiteConfigForm.socialLoginList">
              <el-checkbox label="qq">QQ</el-checkbox>
              <el-checkbox label="weibo">微博</el-checkbox>
              <el-checkbox label="gitee">Gitee</el-checkbox>
              <el-checkbox label="github">Github</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-button
            type="primary"
            size="medium"
            style="margin-left: 6.3rem"
            @click="updateWebsiteConfig"
          >
            修改
          </el-button>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="社交信息" name="social">
        <el-form label-width="70px" :model="websiteConfigForm">
          <el-checkbox-group v-model="websiteConfigForm.socialUrlList">
            <el-form-item label="Github">
              <el-input
                v-model="websiteConfigForm.github"
                size="small"
                style="width: 400px; margin-right: 1rem"
              ></el-input>
              <el-checkbox label="github">是否展示</el-checkbox>
            </el-form-item>
            <el-form-item label="Gitee">
              <el-input
                v-model="websiteConfigForm.gitee"
                size="small"
                style="width: 400px; margin-right: 1rem"
              ></el-input>
              <el-checkbox label="gitee">是否展示</el-checkbox>
            </el-form-item>
            <el-form-item label="BiliBili">
              <el-input
                v-model="websiteConfigForm.bilibili"
                size="small"
                style="width: 400px; margin-right: 1rem"
              ></el-input>
              <el-checkbox label="bilibili">是否展示</el-checkbox>
            </el-form-item>
            <el-form-item label="QQ">
              <el-input
                v-model="websiteConfigForm.qq"
                size="small"
                style="width: 400px; margin-right: 1rem"
              ></el-input>
              <el-checkbox label="qq">是否展示</el-checkbox>
            </el-form-item>
            <el-button
              type="primary"
              size="medium"
              style="margin-left: 4.375rem"
              @click="updateWebsiteConfig"
            >
              修改
            </el-button>
          </el-checkbox-group>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="其他设置" name="other">
        <el-form label-width="120px" :model="websiteConfigForm" label-position="left">
          <el-row style="width: 600px">
            <el-col :md="12">
              <el-form-item label="用户头像">
                <el-upload
                  class="avatar-uploader"
                  :headers="authorization"
                  action="/api/admin/site/image"
                  :show-file-list="false"
                  :on-success="handleUserAvatarSuccess"
                >
                  <img
                    v-if="websiteConfigForm.userAvatar"
                    :src="websiteConfigForm.userAvatar"
                    class="avatar"
                  />
                  <i v-else class="el-icon-plus avatar-uploader-icon" />
                </el-upload>
              </el-form-item>
            </el-col>
            <el-col :md="12">
              <el-form-item label="游客头像">
                <el-upload
                  class="avatar-uploader"
                  :headers="authorization"
                  action="/api/admin/site/image"
                  :show-file-list="false"
                  :on-success="handleTouristAvatarSuccess"
                >
                  <img
                    v-if="websiteConfigForm.touristAvatar"
                    :src="websiteConfigForm.touristAvatar"
                    class="avatar"
                  />
                  <i v-else class="el-icon-plus avatar-uploader-icon" />
                </el-upload>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="邮箱通知">
            <el-radio-group v-model="websiteConfigForm.isEmailNotice">
              <el-radio :label="0">关闭</el-radio>
              <el-radio :label="1">开启</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="评论审核">
            <el-radio-group v-model="websiteConfigForm.isCommentCheck">
              <el-radio :label="0">关闭</el-radio>
              <el-radio :label="1">开启</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="留言审核">
            <el-radio-group v-model="websiteConfigForm.isMessageCheck">
              <el-radio :label="0">关闭</el-radio>
              <el-radio :label="1">开启</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="打赏状态">
            <el-radio-group v-model="websiteConfigForm.isReward">
              <el-radio :label="0">关闭</el-radio>
              <el-radio :label="1">开启</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-row style="width: 600px" v-show="websiteConfigForm.isReward == 1">
            <el-col :md="12">
              <el-form-item label="微信收款码">
                <el-upload
                  class="avatar-uploader"
                  :headers="authorization"
                  action="/api/admin/site/image"
                  :show-file-list="false"
                  :on-success="handleWeiXinSuccess"
                >
                  <img
                    v-if="websiteConfigForm.weiXinQRCode"
                    :src="websiteConfigForm.weiXinQRCode"
                    class="avatar"
                  />
                  <i v-else class="el-icon-plus avatar-uploader-icon" />
                </el-upload>
              </el-form-item>
            </el-col>
            <el-col :md="12">
              <el-form-item label="支付宝收款码">
                <el-upload
                  class="avatar-uploader"
                  :headers="authorization"
                  action="/api/admin/site/image"
                  :show-file-list="false"
                  :on-success="handleAlipaySuccess"
                >
                  <img
                    v-if="websiteConfigForm.alipayQRCode"
                    :src="websiteConfigForm.alipayQRCode"
                    class="avatar"
                  />
                  <i v-else class="el-icon-plus avatar-uploader-icon" />
                </el-upload>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="音乐播放器">
            <el-radio-group v-model="websiteConfigForm.isMusicPlayer">
              <el-radio :label="0">关闭</el-radio>
              <el-radio :label="1">开启</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="网易云歌单Id">
            <el-input
              v-model="websiteConfigForm.musicId"
              size="small"
              style="width: 400px; margin-right: 1rem"
            ></el-input>
          </el-form-item>
          <el-button
            type="primary"
            size="medium"
            style="margin-left: 6.3rem"
            @click="updateWebsiteConfig"
          >
            修改
          </el-button>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<script>
import { getSiteConfig, updateWebsiteConfig } from "@/api/sitesetting";
import { getToken } from "@/utlis/token";
export default {
  name: "WebSite",
  created() {
    this.getWebsiteConfig();
  },
  data() {
    return {
      activeName: "info",
      websiteConfigForm: {
        authorAvatar: "",
        userAvatar: "",
        touristAvatar: "",
        websiteName: "",
        websiteAuthor: "",
        websiteIntro: "",
        websiteCreateTime: null,
        websiteRecordNumber: "",
        socialLoginList: [],
        qq: "",
        github: "",
        gitee: "",
        socialUrlList: [],
      },
    };
  },
  methods: {
    getWebsiteConfig() {
      getSiteConfig().then(({ data }) => {
        this.websiteConfigForm = data.data;
      });
    },
    handleClick(tab, event) {},
    handleAuthorAvatarSuccess(response) {
      this.websiteConfigForm.authorAvatar = response.data;
    },
    handleUserAvatarSuccess(response) {
      this.websiteConfigForm.userAvatar = response.data;
    },
    handleTouristAvatarSuccess(response) {
      this.websiteConfigForm.touristAvatar = response.data;
    },
    handleWeiXinSuccess(response) {
      this.websiteConfigForm.weiXinQRCode = response.data;
    },
    handleAlipaySuccess(response) {
      this.websiteConfigForm.alipayQRCode = response.data;
    },
    updateWebsiteConfig() {
      updateWebsiteConfig(this.websiteConfigForm).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.msg,
          });
          this.getWebsiteConfig();
        }
      });
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

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
}
.avatar {
  width: 120px;
  height: 120px;
  display: block;
}
</style>