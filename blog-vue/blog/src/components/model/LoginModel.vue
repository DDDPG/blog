<template>
  <v-dialog v-model="loginFlag" max-width="460">
    <v-card class="login-container" style="border-radius: 4px">
      <v-icon class="float-right colorFlag" @click="loginFlag = false"> mdi-close </v-icon>
      <div class="login-wrapper">
        <!-- 用户名 -->
        <v-text-field
          v-model="loginForm.username"
          label="邮箱号"
          placeholder="请输入您的邮箱号"
          clearable
          @keyup.enter="login"
        />
        <!-- 密码 -->
        <v-text-field
          v-model="loginForm.password"
          class="mt-7"
          label="密码"
          placeholder="请输入您的密码"
          @keyup.enter="login"
          :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
          :type="show ? 'text' : 'password'"
          @click:append="show = !show"
        />
        <!-- 按钮 -->
        <v-btn class="mt-7" block style="background: #ed6ea0; color: #fff" @click="login">
          登录
        </v-btn>
        <!-- 注册和找回密码 -->
        <div class="mt-10 login-tip">
          <span class="colorFlag" @click="openRegister">立即注册</span>
          <span @click="openForget" class="float-right colorFlag">忘记密码?</span>
        </div>
        <div v-if="socialLoginList.length > 0">
          <div class="social-login-title">社交账号登录</div>
          <div class="social-login-wrapper">
            <a
              v-if="showLogin('weibo')"
              class="iconfont icon-weibo"
              style="color: #e05244"
              @click="weiboLogin"
            ></a>
            <a
              v-if="showLogin('qq')"
              class="iconfont icon-qq"
              style="color: #00aaee"
              @click="qqLogin"
            ></a>
            <a
              v-if="showLogin('gitee')"
              class="iconfont icon-gitee"
              style="color: #c71d23"
              @click="giteeLogin"
            ></a>
            <a
              v-if="showLogin('github')"
              class="iconfont icon-GitHub"
              style="color: #24292f"
              @click="githubLogin"
            ></a>
          </div>
        </div>
      </div>
    </v-card>
  </v-dialog>
</template>

<script>
import getUid from "@/utils/UUid";
export default {
  data() {
    return {
      loginForm: {
        username: "",
        password: "",
      },
      show: false,
    };
  },
  computed: {
    loginFlag: {
      set(value) {
        this.$store.state.loginFlag = value;
      },
      get() {
        return this.$store.state.loginFlag;
      },
    },
    socialLoginList() {
      return this.$store.state.blogInfo.websiteDTO.socialLoginList;
    },
    showLogin() {
      return function (type) {
        return this.socialLoginList.indexOf(type) != -1;
      };
    },
  },
  methods: {
    openRegister() {
      this.$store.state.loginFlag = false;
      this.$store.state.registerFlag = true;
    },
    openForget() {
      this.$store.state.loginFlag = false;
      this.$store.state.forgetFlag = true;
    },
    login() {
      var reg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
      if (!reg.test(this.loginForm.username)) {
        this.$toast({ type: "error", message: "邮箱格式不正确" });
        return false;
      }
      if (this.loginForm.password.trim().length == 0) {
        this.$toast({ type: "error", message: "密码不能为空" });
        return false;
      }
      this.$store.dispatch("LogIn", this.loginForm).then(() => {
        this.loginForm.username = "";
        this.loginForm.password = "";
        this.$store.dispatch("getUserInfo");
        this.$store.commit("closeModel");
        this.$toast({ type: "success", message: "登录成功" });
      });
    },
    weiboLogin() {
      //保留当前路径
      this.$store.commit("saveLoginUrl", this.$route.path);
      window.open(
        "https://api.weibo.com/oauth2/authorize?client_id=" +
          this.config.WEIBO_APP_ID +
          "&response_type=code&redirect_uri=" +
          this.config.WEIBO_REDIRECT_URI,
        "_self"
      );
    },
    giteeLogin() {
      //保留当前路径
      this.$store.commit("saveLoginUrl", this.$route.path);
      let UUid = getUid();
      window.open(
        "https://gitee.com/oauth/authorize?client_id=" +
          this.config.GITEE_APP_ID +
          "&response_type=code&redirect_uri=" +
          this.config.GITEE_REDIRECT_URI +
          "&state=" +
          UUid,
        "_self"
      );
    },
    githubLogin() {
      this.$store.commit("saveLoginUrl", this.$route.path);
      let UUid = getUid();
      window.open(
        "https://github.com/login/oauth/authorize?client_id=" +
          this.config.GITHUB_APP_ID +
          "&redirect_uri=" +
          this.config.GITHUB_REDIRECT_URL +
          "&scope=user&state=" +
          UUid,
        "_self"
      );
    },
    qqLogin() {
      //保留当前路径
      this.$store.commit("saveLoginUrl", this.$route.path);
      if (
        navigator.userAgent.match(
          /(iPhone|iPod|Android|ios|iOS|iPad|Backerry|WebOS|Symbian|Windows Phone|Phone)/i
        )
      ) {
        // eslint-disable-next-line no-undef
        QC.Login.showPopup({
          appId: this.config.QQ_APP_ID,
          redirectURI: this.config.QQ_REDIRECT_URI,
        });
      } else {
        window.open(
          "https://graph.qq.com/oauth2.0/show?which=Login&display=pc&client_id=" +
            +this.config.QQ_APP_ID +
            "&response_type=token&scope=all&redirect_uri=" +
            this.config.QQ_REDIRECT_URI,
          "_self"
        );
      }
    },
  },
};
</script>

<style scoped>
.social-login-title {
  margin-top: 1rem;
  color: #b5b5b5;
  font-size: 0.75rem;
  text-align: center;
}
.social-login-title::before {
  content: "";
  display: inline-block;
  background-color: #d8d8d8;
  width: 60px;
  height: 1px;
  margin: 0 12px;
  vertical-align: middle;
}
.social-login-title::after {
  content: "";
  display: inline-block;
  background-color: #d8d8d8;
  width: 60px;
  height: 1px;
  margin: 0 12px;
  vertical-align: middle;
}
.social-login-wrapper {
  font-size: 2rem;
  text-align: center;
}
.social-login-wrapper a {
  text-decoration: none;
}
</style>
