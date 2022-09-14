<template>
  <div>
    <!-- 导航栏 -->
    <div class="nav-bar">
      <!-- 折叠按钮 -->
      <div class="hambuger-container" @click="trigger">
        <i :class="isFold" />
      </div>
      <!--面包屑-->
      <el-breadcrumb separator="/">
        <el-breadcrumb-item v-for="item in breadcrumbList" :key="item.path" :to="item.path"
          >{{ item.name }}
        </el-breadcrumb-item>
      </el-breadcrumb>
      <!-- 右侧菜单 -->
      <div class="right-menu">
        <div class="screen-full" @click="fullScreen">
          <i class="el-icon-full-screen" />
        </div>
        <el-dropdown size="mini" @command="handleCommand">
          <el-avatar :size="40" :src="$store.getters.avatar" />
          <el-dropdown-menu>
            <el-dropdown-item command="center">个人中心</el-dropdown-item>
            <el-dropdown-item command="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
    <!--历史标签-->
    <div class="tabs-container">
      <div class="tabs-wrapper">
        <el-tag
        v-for="tag in $store.getters.tabList"
        :key="tag.path"
        :closable="tag.name !== '首页'"
        :effect="$route.path === tag.path ? 'dark' : 'plain'"
        @click="changeMenu(tag)"
        @close="handleClose(tag)"
        disable-transitions
      >
        {{ tag.name }}
      </el-tag>
      </div>
      <el-button class="close-item" style="float:right" size="small" @click="closeAllTab">全部关闭</el-button>
    </div>
  </div>
</template>

<script>
import store from "@/store";
export default {
  name: "NavBar",
  data() {
    return {
      breadcrumbList: [],
    };
  },
  created() {
    this.getBreadCrumb();
  },
  // 监听路由
  watch: {
    $route() {
      this.getBreadCrumb();
      // 保存当前标签
      this.$store.commit("tab/saveTab", this.$route);
    },
  },
  methods: {
    trigger() {
      this.$store.commit("tab/trigger");
    },
    getBreadCrumb() {
      // filter把符合条件的过滤出来，item.name==true
      let matched = this.$route.matched.filter((item) => item.name);
      const first = matched[0];
      if (first && first.name !== "首页") {
        // 添加首页进去
        matched = [{ path: "/", name: "首页" }].concat(matched);
      }
      this.breadcrumbList = matched;
    },
    changeMenu(item) {
      this.$router.push({
        path: item.path,
      });
    },
    handleClose(tag) {
      //删除标签
      this.$store.commit("tab/removeTab", tag);
      //如果删除的是当前页则返回上一标签页
      if (tag.name == this.$route.name) {
        // const length = this.$store.state.tab.tabList.length;
        const length = store.getters.tabList.length;
        this.$router.push({
          // path: this.$store.state.tab.tabList[length - 1].path,
          path: store.getters.tabList[length - 1].path,
        });
      }
    },
    // 关闭所有标签
    closeAllTab() {
      this.$store.commit("tab/resetTab");
      this.$router.push({ path: "/" });
    },
    handleCommand(command) {
      if (command == "center") {
        this.$router.push({ path: "/person"});
      }
      if (command == "logout") {
        this.$confirm("确定退出吗？", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }).then(() => {
          this.$store
            .dispatch("user/LogOut")
            .then(() => {
              this.$store.commit("tab/resetTab");
              this.$message.success("注销成功");
              this.$router.push({ path: "/login" });
            })
            .catch(() => {});
        });
      }
    },
    fullScreen() {
      let element = document.documentElement;
      if (this.fullscreen) {
        if (document.exitFullscreen) {
          document.exitFullscreen();
        } else if (document.webkitCancelFullScreen) {
          document.webkitCancelFullScreen();
        } else if (document.mozCancelFullScreen) {
          document.mozCancelFullScreen();
        } else if (document.msExitFullscreen) {
          document.msExitFullscreen();
        }
      } else {
        if (element.requestFullscreen) {
          element.requestFullscreen();
        } else if (element.webkitRequestFullScreen) {
          element.webkitRequestFullScreen();
        } else if (element.mozRequestFullScreen) {
          element.mozRequestFullScreen();
        } else if (element.msRequestFullscreen) {
          element.msRequestFullscreen();
        }
      }
      this.fullscreen = !this.fullscreen;
    },
  },
  computed: {
    isFold() {
      return store.getters.isCollapse ? "el-icon-s-unfold" : "el-icon-s-fold";
    },
  },
};
</script>

<style scoped>
.nav-bar {
  display: flex;
  align-items: center;
  padding-left: 15px;
  padding-right: 30px;
  height: 50px;
}
.hambuger-container {
  font-size: 1.25rem;
  cursor: pointer;
  margin-right: 24px;
}

.right-menu {
  margin-left: auto;
  display: flex;
  align-items: center;
}
.tabs-container {
  display: flex;
  position: relative;
  padding: 3px 0 3px 10px;
  border-bottom: 1px solid #d8dce5;
  box-shadow: 0 1px 3px 0 rgb(0 0 0 / 12%), 0 0 3px 0 rgb(0 0 0 / 4%);
}
.tabs-wrapper {
  overflow-x: auto;
  overflow-y: hidden;
  white-space: nowrap;
  width: 95%;
}
.tabs-container .el-tag {
  font-size: 13px;
  margin-right: 5px;
  cursor: pointer;
}
.screen-full {
  cursor: pointer;
  margin-right: 1rem;
  font-size: 1.25rem;
}
.close-item {
  position: absolute;
  height: 32px;
  font-size: 13px;
  right: 10px;
}
</style>
