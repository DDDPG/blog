<template>
  <div class="container">
    <div class="login_form">
      <div class="login_title">登录</div>
      <el-form :model="loginForm" :rules="rules" ref="loginForm">
        <el-form-item prop="username">
          <el-input
            prefix-icon="el-icon-user"
            v-model="loginForm.username"
            placeholder="请输入用户名"
            @keyup.enter.native="login"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            prefix-icon="iconfont el-icon-mima"
            v-model="loginForm.password"
            show-password
            placeholder="请输入密码"
            @keyup.enter.native="login"
          ></el-input>
        </el-form-item>
      </el-form>
      <el-button :loading="loading" type="primary" @click.native.prevent="login"
        ><span v-if="!loading">登 录</span> <span v-else>登 录中...</span></el-button
      >
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      loading: false,
      loginForm: {
        username: "",
        password: "",
      },
      rules: {
        username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
      },
    };
  },
  methods: {
    login() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.loading = true;
          this.$store
            .dispatch("user/LogIn", this.loginForm)
            .then(() => {
              this.$router.push({ path: "/" });
              this.$message.success("登录成功");
            })
            .catch((error) => {
              this.loading = false;
            });
        } else {
          return false;
        }
      });
    },
  },
};
</script>
<style scoped>
.container {
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  background: url("https://static.ttkwsd.top/config/0d7d8d691e644989b72ddda5f695aca2.jpg") no-repeat
    center center;
  background-size: cover;
}
.login_form {
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  padding: 200px 130px 20px;
  background-color: #fff;
  width: 300px;
}
.login_title {
  color: #303133;
  font-size: 17px;
  font-weight: 700;
  padding-bottom: 10px;
}
.login_form button {
  width: 100%;
}
</style>
