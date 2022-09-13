<template>
  <el-card shadow="never" class="main-card">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="修改信息" name="first">
        <div class="user-container">
          <el-form
            ref="form"
            :model="userForm"
            label-width="70px"
            style="width: 320px; margin-left: 6.5rem; padding-top: 3rem"
          >
            <el-form-item label="用户名">
              <el-input v-model="userForm.username"></el-input>
            </el-form-item>
            <el-form-item label="昵称">
              <el-input v-model="userForm.nickname"></el-input>
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="userForm.email"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="updateAdminInfo">修改</el-button>
              <el-button>取消</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-tab-pane>
      <el-tab-pane label="修改密码" name="second">
        <el-form
          :model="passwordForm"
          status-icon
          :rules="rules"
          ref="passwordForm"
          label-width="100px"
          class="demo-ruleForm"
          style="width: 320px"
        >
          <el-form-item label="旧密码" prop="oldPassword">
            <el-input type="password" v-model="passwordForm.oldPassword" show-password></el-input>
          </el-form-item>
          <el-form-item label="新密码" prop="newPassword">
            <el-input type="password" v-model="passwordForm.newPassword" show-password></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="checkPassword">
            <el-input type="password" v-model="passwordForm.checkPassword" show-password></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm('passwordForm')">修改</el-button>
            <el-button @click="resetForm('passwordForm')">重置</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<script>
import { updatePassword, updateAdminInfo } from "@/api/user";
export default {
  name: "Person",
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (this.passwordForm.checkPassword !== "") {
          this.$refs.passwordForm.validateField("checkPassword");
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.passwordForm.newPassword) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {
      activeName: "first",
      userForm: {
        username: "",
        nickname: "",
        email: "",
      },
      passwordForm: {
        oldPassword: "",
        newPassword: "",
        checkPassword: "",
      },
      rules: {
        newPassword: [{ validator: validatePass, trigger: "blur" }],
        checkPassword: [{ validator: validatePass2, trigger: "blur" }],
      },
    };
  },
  methods: {
    handleClick(tab, event) {},
    updateAdminInfo() {
      if (this.userForm.username.trim() == "") {
        this.$message.error("用户名不能为空");
        return false;
      }
      if (this.userForm.nickname.trim() == "") {
        this.$message.error("昵称不能为空");
        return false;
      }
      if (this.userForm.email.trim() == "") {
        this.$message.error("邮箱不能为空");
        return false;
      }
      updateAdminInfo(this.userForm).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.msg,
          });
          this.$store.dispatch("user/updateUserInfo", this.userForm);
        }
      });
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.passwordForm.oldPassword.trim() == "") {
            this.$message.error("旧密码不能为空");
            return false;
          }
          updatePassword(this.passwordForm).then(({ data }) => {
            if (data.flag) {
              this.passwordForm.oldPassword = "";
              this.passwordForm.newPassword = "";
              this.passwordForm.checkPassword = "";
              this.$notify.success({
                title: "成功",
                message: data.msg,
              });
            }
          });
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
  },
};
</script>

<style>
.user-container {
  display: flex;
  align-items: center;
  margin-left: 20%;
  margin-top: 5rem;
}
.demo-ruleForm {
  margin-left: 26%;
  margin-top: 8rem;
}
</style>
