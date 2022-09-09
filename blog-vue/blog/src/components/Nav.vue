<template>
  <!-- 导航 -->
  <nav id="nav" :class="isMessage ? '' : fixedClass">
    <div class="inner">
      <!-- 切换按钮 -->
      <div :class="toggleClass" @click="handleToggle">
        <div class="lines">
          <span class="line"></span>
          <span class="line"></span>
          <span class="line"></span>
        </div>
      </div>
      <!-- 菜单 -->
      <ul class="menu" :class="isAvatar ? 'np' : 'mp'">
        <li class="item title">
          <router-link to="/">Hello World</router-link>
        </li>
        <li class="item" :class="{ active: $route.meta.title === '首页' }">
          <router-link to="/"> <i class="iconfont icon-home-fill"></i> 首页 </router-link>
        </li>
        <li class="item dropdown">
          <a class="drop"><i class="iconfont icon-icon_category"></i> 文章</a>
          <ul class="submenu">
            <li class="subitem" :class="{ active: $route.meta.title === '归档' }">
              <router-link to="/archives"> <i class="iconfont icon-guidang"></i> 归档 </router-link>
            </li>
            <li class="subitem" :class="{ active: $route.meta.title === '分类' }">
              <router-link to="/categories">
                <i class="iconfont icon-fenlei"></i> 分类
              </router-link>
            </li>
            <li class="subitem" :class="{ active: $route.meta.title === '标签' }">
              <router-link to="/tags"> <i class="iconfont icon-tags"></i> 标签 </router-link>
            </li>
          </ul>
        </li>
        <li class="item" :class="{ active: $route.meta.title === '友链' }">
          <router-link to="/friends"> <i class="iconfont icon-lianjie"></i> 友链 </router-link>
        </li>
        <li class="item" :class="{ active: $route.meta.title === '说说' }">
          <router-link to="/talks"> <i class="iconfont icon-pinglun2"></i> 说说 </router-link>
        </li>
        <li class="item" :class="{ active: $route.meta.title === '留言板' }">
          <router-link to="/message"> <i class="iconfont icon-pinglun1"></i> 留言板 </router-link>
        </li>
        <li class="item" :class="{ active: $route.meta.title === '关于' }">
          <router-link to="/about"> <i class="iconfont icon-7"></i> 关于 </router-link>
        </li>
        <li class="item dropdown">
          <a v-if="!this.$store.state.avatar" @click="openLogin">
            <i class="iconfont icon-dengluyonghuming"></i> 登录
          </a>
          <template v-else>
            <img class="user-avatar" :src="this.$store.state.avatar" height="30" width="30" />
            <ul class="submenu">
              <li class="subitem" :class="{ active: $route.meta.title === '个人中心' }">
                <router-link to="/user"> <i class="iconfont icon-user"></i> 个人中心 </router-link>
              </li>
              <li class="subitem">
                <a @click="goToAdmin"> <i class="iconfont icon-houtaiguanli"></i> 前往后台 </a>
              </li>
              <li class="subitem">
                <a @click="logout"><i class="iconfont icon-tuichu"></i> 退出 </a>
              </li>
            </ul></template
          >
        </li>
      </ul>
      <!-- 右侧按钮 -->
      <ul class="right">
        <li class="item theme" @click="changeTheme">
          <i class="iconfont icon-weibiaoti-"></i>
        </li>
        <li class="item search" @click="openSearch">
          <i class="iconfont icon-search"></i>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script>
export default {
  name: "Nav",
  data() {
    return {
      scrollTop: null,
      height: null,
      fixedClass: "",
      themeFlag: true,
      toggleClass: "toggle",
    };
  },
  computed: {
    isMessage() {
      return this.$route.path == "/message";
    },
    isAvatar() {
      return this.$store.state.avatar;
    },
  },
  mounted() {
    window.addEventListener("scroll", this.handleScroll);
    this.$bus.$on("handleToggle", this.handleToggle);
    this.$bus.$on("headerDetector", this.headerDetector);
  },
  methods: {
    goToAdmin() {
      window.open(this.config.ADMIN, "_blank");
    },
    logout() {
      //如果在个人中心则跳回上一页
      if (this.$route.path == "/user") {
        this.$router.go(-1);
      }
      this.$store.dispatch("LogOut");
      this.$toast({ type: "success", message: "注销成功" });
    },
    handleScroll() {
      this.scrollTop =
        window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
    },
    openLogin() {
      this.$store.state.loginFlag = true;
    },
    openSearch() {
      this.$store.state.searchFlag = true;
    },
    changeTheme() {
      if (this.themeFlag) {
        this.themeFlag = false;
        document.documentElement.setAttribute("theme", "dark");
        sessionStorage.setItem("theme", "dark");
      } else {
        this.themeFlag = true;
        document.documentElement.removeAttribute("theme");
        sessionStorage.removeItem("theme");
      }
    },
    handleToggle() {
      this.$store.commit("toggle");
      if (this.$store.state.isCollapse) {
        this.toggleClass = "toggle close";
        if (this.$route.path == "/message") {
          this.$bus.$emit("handleMessage", "");
        }
      } else {
        this.toggleClass = "toggle";
      }
    },
    headerDetector(data) {
      this.height = data;
    },
  },
  watch: {
    scrollTop(oldValue, newValue) {
      if (newValue > this.height) {
        if (newValue > oldValue) {
          this.fixedClass = "show up";
        } else {
          this.fixedClass = "show down";
        }
      } else {
        this.fixedClass = "";
      }
    },
  },
  beforeDestroy() {
    this.$bus.$off("handleToggle");
    this.$bus.$off("headerDetector");
  },
  destroyed() {
    window.removeEventListener("scroll", this.handleScroll);
  },
};
</script>

<style lang="scss" scoped>
.user-avatar {
  width: 30px;
  height: 30px;
  display: inline-block;
  position: relative;
  top: 9px;
  cursor: pointer;
  border-radius: 50%;
}
.show {
  background: var(--nav-bg);
  box-shadow: 0.1rem 0.1rem 0.2rem var(--grey-9-a1);
  text-shadow: 0 0 0.625rem var(--grey-9-a1);
  color: var(--text-color);
}

.up {
  transform: translateY(0);
}

.down {
  transform: translateY(-100%);
}
#nav a:hover {
  color: currentColor;
}
.mp {
  padding: 0.625rem 0;
}
.np {
  padding: 0.11rem 0;
}
#nav {
  position: fixed;
  z-index: 9;
  width: 100%;
  height: 3.125rem;
  transition: all 0.2s ease-in-out 0s;
  .inner {
    display: flex;
    height: 100%;
    width: calc(100% - 0.625rem);
    flex-wrap: nowrap;
  }
  .toggle {
    display: none;
    .lines {
      padding: 1.25rem;
      width: 1.375rem;
      box-sizing: unset;
    }
  }
  .menu {
    margin: 0;
    width: 100%;
    .item {
      display: inline-block;
      position: relative;
      padding: 0 0.625rem;
      letter-spacing: 0.0625rem;
      text-align: center;
      &:not(.title) a::before {
        content: "";
        position: absolute;
        width: 0;
        height: 0.1875rem;
        bottom: 0;
        border-radius: 0.125rem;
        left: 50%;
        transform: translateX(-50%);
        background-color: currentColor;
      }
    }
    .submenu {
      display: none;
      position: absolute;
      margin-top: 0.5rem;
      padding: 0;
      left: 7px;
      width: max-content;
      background: var(--grey-9-a5);
      box-shadow: 0 0.3125rem 1.25rem -0.25rem var(--grey-9-a1);
      border-radius: 0.625rem 0;
      animation: slideUpIn 0.3s;
      .subitem {
        display: block;
        &:first-child {
          border-radius: 0.625rem 0 0 0;
        }
        &:last-child {
          border-radius: 0 0 0.625rem 0;
        }
        a {
          display: inline-block;
          padding: 0.3rem 0.7rem;
          width: 100%;
          text-shadow: none;
        }
        &:hover a {
          transform: translateX(0.3rem);
        }
      }
      &::before {
        position: absolute;
        top: -1.25rem;
        left: 0;
        width: 100%;
        height: 2.5rem;
        content: "";
      }
    }
  }
  .right {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    .item {
      padding: 0.625rem 0.5rem;
      cursor: pointer;
    }
  }
}
#nav.show .menu .submenu {
  background-color: var(--grey-1);
}
#nav.show .line {
  background: var(--text-color);
  box-shadow: 0 0 0.0625rem var(--grey-9-a1);
}

#nav .menu .item.dropdown:hover .submenu {
  display: block;
}

#nav .menu .item:not(.title) a {
  display: block;
  font-size: 1em;
}
#nav .menu .item.dropdown > .drop::after {
  content: "";
  display: inline-block;
  margin-left: 0.3rem;
  vertical-align: middle;
  border: 0.3rem solid transparent;
  border-top-color: currentColor;
  border-bottom: 0;
}
#nav.show .item.active a {
  color: var(--color-aqua);
  opacity: 1;
}
#nav .menu .item.active:not(.dropdown) a::before,
#nav .menu .item:not(.dropdown):hover a::before {
  width: 70%;
}
a::after,
a::before {
  transition: all 0.4s ease-in-out 0s;
}
/* 切换按钮 */
.toggle {
  line-height: 0;
  cursor: pointer;
  .line {
    position: relative;
    top: 0;
    left: 0;
    display: inline-block;
    height: 0.125rem;
    width: 100%;
    transition: all 0.4s;
    border-radius: 0.0625rem;
    background: var(--header-text-color);
    vertical-align: top;
    box-shadow: 0 0 0.5rem rgb(0 0 0 / 50%);
    &:not(:first-child) {
      margin-top: 0.1875rem;
    }
  }
}
.toggle.close {
  .line:nth-child(1) {
    transform: rotate(-45deg);
    top: 0.3125rem;
  }
  .line:nth-child(2) {
    opacity: 0;
  }
  .line:nth-child(3) {
    transform: rotate(45deg);
    top: -0.3125rem;
  }
}
.toggle .line:not(:first-child) {
  margin-top: 0.1875rem;
}

@media (min-width: 1200px) {
  #nav .inner {
    width: 72.5rem;
  }
}
@media (max-width: 991px) {
  #nav .toggle {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }
}
@media (max-width: 767px) {
  #nav .menu {
    padding: 0.625rem 0;
  }
  #nav .menu .item {
    display: none;
  }
  #nav .menu .item.title {
    display: block;
  }
}
@keyframes slideUpIn {
  0% {
    opacity: 0;
    transform: translateY(10px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
