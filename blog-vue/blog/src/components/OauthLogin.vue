<template>
  <div class="oauth-background">
    <div id="preloader_6">
      <span></span>
      <span></span>
      <span></span>
      <span></span>
    </div>
  </div>
</template>

<script>
/* eslint-disable no-undef */
import { weiBoLogin, giteeLogin, githubLogin, qqLogin } from "@/api/login";
import { setToken } from "@/utils/token";
export default {
  created() {
    const that = this;
    //关闭登录框
    that.$store.state.loginFlag = false;
    //通过路径判断是微博登录还是qq登录
    if (that.$route.path == "/oauth/login/qq") {
      // 拿到openId，accessToken传入后台
      if (QC.Login.check()) {
        QC.Login.getMe(function (openId, accessToken) {
          qqLogin({
            openId: openId,
            accessToken: accessToken,
          }).then(({ data }) => {
            if (data.flag) {
              // 设置Token
              setToken(data.data);
              // 获取用户信息
              that.$store.dispatch("getUserInfo");
              if (data.data.email == null) {
                that.$toast({
                  type: "warning",
                  message: "请绑定邮箱以便及时收到回复",
                });
              } else {
                that.$toast({ type: "success", message: data.msg });
              }
            } else {
              that.$toast({ type: "error", message: data.msg });
            }
          });
        });
      } else {
        that.$toast({ type: "error", message: data.msg });
      }
    } else if (this.$route.path == "/oauth/login/weibo") {
      weiBoLogin({ code: this.$route.query.code }).then(({ data }) => {
        if (data.flag) {
          // 设置Token
          setToken(data.data);
          // 获取用户信息
          this.$store.dispatch("getUserInfo");
          if (data.data.email == null) {
            this.$toast({
              type: "warning",
              message: "请绑定邮箱以便及时收到回复",
            });
          } else {
            this.$toast({ type: "success", message: data.msg });
          }
        } else {
          this.$toast({ type: "error", message: data.msg });
        }
      });
    } else if (this.$route.path == "/oauth/login/gitee") {
      giteeLogin({ code: this.$route.query.code, state: this.$route.query.state }).then(
        ({ data }) => {
          if (data.flag) {
            // 设置Token
            setToken(data.data);
            // 获取用户信息
            this.$store.dispatch("getUserInfo");
            if (data.data.email == null) {
              this.$toast({
                type: "warning",
                message: "请绑定邮箱以便及时收到回复",
              });
            } else {
              this.$toast({ type: "success", message: data.msg });
            }
          } else {
            this.$toast({ type: "error", message: data.msg });
          }
        }
      );
    } else if (this.$route.path == "/oauth/login/github") {
      githubLogin({ code: this.$route.query.code, state: this.$route.query.state }).then(
        ({ data }) => {
          if (data.flag) {
            // 设置Token
            setToken(data.data);
            // 获取用户信息
            this.$store.dispatch("getUserInfo");
            if (data.data.email == null) {
              this.$toast({
                type: "warning",
                message: "请绑定邮箱以便及时收到回复",
              });
            } else {
              this.$toast({ type: "success", message: data.msg });
            }
          } else {
            this.$toast({ type: "error", message: data.msg });
          }
        }
      );
    }
    // 跳转回原页面
    const loginUrl = this.$store.state.loginUrl;
    if (loginUrl != null && loginUrl != "") {
      this.$router.push({ path: loginUrl });
    } else {
      this.$router.push({ path: "/" });
    }
  },
};
</script>

<style scoped>
.oauth-background {
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  z-index: 1000;
}
#preloader_6 {
  position: relative;
  top: 45vh;
  left: 47vw;
  animation: preloader_6 5s infinite linear;
}

#preloader_6 span {
  width: 20px;
  height: 20px;
  position: absolute;
  background: red;
  display: block;
  animation: preloader_6_span 1s infinite linear;
}

#preloader_6 span:nth-child(1) {
  background: #2ecc71;
}

#preloader_6 span:nth-child(2) {
  left: 22px;
  background: #9b59b6;
  animation-delay: 0.2s;
}

#preloader_6 span:nth-child(3) {
  top: 22px;
  background: #3498db;
  animation-delay: 0.4s;
}

#preloader_6 span:nth-child(4) {
  top: 22px;
  left: 22px;
  background: #f1c40f;
  animation-delay: 0.6s;
}

@keyframes preloader_6 {
  from {
    -ms-transform: rotate(0deg);
  }

  to {
    -ms-transform: rotate(360deg);
  }
}

@keyframes preloader_6_span {
  0% {
    transform: scale(1);
  }

  50% {
    transform: scale(0.5);
  }

  100% {
    transform: scale(1);
  }
}
</style>