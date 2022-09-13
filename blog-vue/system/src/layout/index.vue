<template>
  <el-container style="height: 100%">
    <el-aside class="side-bar" width="auto">
      <SideBar></SideBar>
    </el-aside>
    <el-container>
      <el-header height="90px" style="padding: 0">
        <NavBar></NavBar>
      </el-header>
      <el-main class="main-container" style="background: #f7f9fb; padding: 13px 15px">
        <div class="fade-transform-box">
          <transition name="fade-transform" mode="out-in">
            <router-view :key="$route.fullPath"></router-view>
          </transition>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import SideBar from "./components/SideBar";
import NavBar from "./components/NavBar";

export default {
  components: {
    SideBar,
    NavBar,
  },
  computed: {
    isHide() {
      return this.$store.state.isCollapse;
    },
  },
};
</script>

<style scoped>
/* 整个滚动条 */
.main-container::-webkit-scrollbar {
  width: 8px;
  height: 10px;
}

/* 滚动条上的滚动滑块 */
.main-container::-webkit-scrollbar-thumb {
  background-color: #49b1f5;
  /* 关键代码 */
  background-image: -webkit-linear-gradient(
    45deg,
    rgba(255, 255, 255, 0.4) 25%,
    transparent 25%,
    transparent 50%,
    rgba(255, 255, 255, 0.4) 50%,
    rgba(255, 255, 255, 0.4) 75%,
    transparent 50%,
    transparent
  );
  border-radius: 32px;
}

/* 滚动条轨道 */
.main-container::-webkit-scrollbar-track {
  background-color: #dbeffd;
  border-radius: 32px;
}

.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.5s ease 0s;
}
.fade-transform-enter {
  opacity: 0;
  transform: translateX(-30px);
}
.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
.fade-transform-box {
  position: relative;
  top: 0px;
  bottom: 0px;
  width: 100%;
  overflow: hidden;
}
</style>
