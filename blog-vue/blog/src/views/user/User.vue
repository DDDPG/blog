<template>
  <div class="collapse wrap">
    <div class="info-wrapper">
      <div class="box-avatar">
        <button id="pick-avatar">
          <img class="user-avatar" :src="this.$store.state.avatar" />
        </button>
        <avatar-cropper
          @uploaded="uploadAvatar"
          trigger="#pick-avatar"
          :uploadHeaders="authorization"
          upload-url="/api/users/avatar"
        ></avatar-cropper>
      </div>
      <div class="box-info">
        <v-text-field
          class="mt-7 colorFlag"
          v-model="userInfo.nickname"
          label="昵称"
          placeholder="请输入您的昵称"
        ></v-text-field>
        <div v-if="loginType != 0" class="mt-7 binding">
          <v-text-field disabled v-model="email" label="邮箱号" placeholder="请绑定邮箱" />
          <v-btn v-if="email" text small @click="openEmailModel"> 修改绑定 </v-btn>
          <v-btn v-else text small @click="openEmailModel"> 绑定邮箱 </v-btn>
        </div>
        <v-btn @click="updataUserInfo" outlined style="margin-top: 22px">修改</v-btn>
      </div>
    </div>
  </div>
</template>

<script>
import AvatarCropper from "vue-avatar-cropper";
import { updateUserInfo } from "@/api/user";
import { getToken } from "@/utils/token";
export default {
  name: "User",
  components: { AvatarCropper },
  data() {
    return {
      userInfo: {
        nickname: this.$store.state.nickname,
        loginType: this.$store.state.loginType,
      },
    };
  },
  methods: {
    openEmailModel() {
      this.$store.state.emailFlag = true;
    },
    updataUserInfo() {
      updateUserInfo(this.userInfo).then(({ data }) => {
        if (data.flag) {
          this.$store.commit("updateUserInfo", this.userInfo);
          this.$toast({ type: "success", message: "修改成功" });
        }
      });
    },
    uploadAvatar(data) {
      if (data.flag) {
        this.$store.commit("updateAvatar", data.data);
        this.$toast({ type: "success", message: "上传成功" });
      } else {
        this.$toast({ type: "error", message: data.msg });
      }
    },
  },
  computed: {
    email() {
      return this.$store.state.email;
    },
    loginType() {
      return this.$store.state.loginType;
    },
    authorization() {
      return {
        Authorization: getToken(),
      };
    },
  },
};
</script>

<style scoped>
#pick-avatar {
  outline: none;
}
.info-wrapper {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: center;
  margin-top: 7rem;
}
.box-avatar {
  position: relative;
  display: inline-flex;
  justify-content: center;
  width: 240px;
  height: 140px;
}
.user-avatar {
  width: 140px;
  height: 140px;
  border-radius: 50%;
}
.box-info {
  width: 500px;
}
.binding {
  display: flex;
  align-items: center;
}
</style>
