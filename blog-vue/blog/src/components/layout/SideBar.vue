<template>
  <div id="sidebar" :style="isShow ? show : ''">
    <div class="inner" :class="$route.path == '/message' ? isMessage : ''">
      <ul class="tab" v-show="$route.meta.title == '文章详情'">
        <li class="contents item" :class="{ active: activeClass == 1 }" @click="open(1)">
          <span>文章目录</span>
        </li>
        <li class="overview item" :class="{ active: activeClass == 2 }" @click="open(2)">
          <span>站点概览</span>
        </li>
      </ul>
      <div class="panels" :style="styleObject">
        <div class="inner">
          <div
            id="toc"
            class="contents panel"
            :class="{
              active: activeClass == 1 && $route.meta.title == '文章详情',
            }"
          ></div>
          <div
            class="overview panel"
            :class="{
              active: $route.meta.title !== '文章详情' || activeClass == 2,
            }"
          >
            <!-- 作者 -->
            <div class="author">
              <img class="image lozaded" :src="blogInfo.websiteDTO.authorAvatar" />
              <p class="name">{{ blogInfo.websiteDTO.websiteAuthor }}</p>
              <div class="description">{{ blogInfo.websiteDTO.websiteIntro }}</div>
            </div>
            <!-- 文章详情 -->
            <nav class="state">
              <div class="item posts">
                <router-link to="/archives">
                  <span class="count">{{ blogInfo.articleCount }}</span>
                  <span class="name">文章</span>
                </router-link>
              </div>
              <div class="item categories">
                <router-link to="/categories">
                  <span class="count">{{ blogInfo.categoryCount }}</span>
                  <span class="name">分类</span>
                </router-link>
              </div>
              <div class="item tags">
                <router-link to="/tags">
                  <span class="count">{{ blogInfo.tagCount }}</span>
                  <span class="name">标签</span>
                </router-link>
              </div>
            </nav>
            <!-- 社交 -->
            <div class="social">
              <a
                v-if="isShowSocial('github')"
                target="_blank"
                :href="blogInfo.websiteDTO.github"
                class="item"
                style="color: #24292f"
                ><i class="iconfont icon-GitHub"></i
              ></a>
              <a
                v-if="isShowSocial('gitee')"
                target="_blank"
                :href="blogInfo.websiteDTO.gitee"
                class="item"
                style="color: #c71d23"
                ><i class="iconfont icon-gitee"></i
              ></a>
              <a
                v-if="isShowSocial('bilibili')"
                target="_blank"
                :href="blogInfo.websiteDTO.bilibili"
                class="item"
                style="color: #ff5ca1"
                ><i class="iconfont icon-bilibili"></i
              ></a>
              <a
                v-if="isShowSocial('qq')"
                target="_blank"
                :href="
                  'http://wpa.qq.com/msgrd?v=3&uin=' + blogInfo.websiteDTO.qq + '&site=qq&menu=yes'
                "
                class="item"
                style="color: #00a1d6"
                ><i class="iconfont icon-qq"></i
              ></a>
            </div>
            <!-- 菜单 -->
            <ul class="menu">
              <li class="item" :class="{ active: $route.meta.title === '首页' }">
                <router-link to="/"> <i class="iconfont icon-home-fill"></i> 首页 </router-link>
              </li>
              <li class="item dropdown" :class="{ expand: expand }">
                <a><i class="iconfont icon-icon_category"></i> 文章</a>
                <ul class="submenu">
                  <li class="item" :class="{ active: $route.meta.title === '归档' }">
                    <router-link to="/archives">
                      <i class="iconfont icon-guidang"></i> 归档
                    </router-link>
                  </li>
                  <li class="item" :class="{ active: $route.meta.title === '分类' }">
                    <router-link to="/categories">
                      <i class="iconfont icon-fenlei"></i> 分类
                    </router-link>
                  </li>
                  <li class="item" :class="{ active: $route.meta.title === '标签' }">
                    <router-link to="/tags"> <i class="iconfont icon-tags"></i> 标签 </router-link>
                  </li>
                </ul>
              </li>
              <li class="item" :class="{ active: $route.meta.title === '友情链接' }">
                <router-link to="/friends">
                  <i class="iconfont icon-lianjie"></i> 友链
                </router-link>
              </li>
              <li class="item" :class="{ active: $route.meta.title === '说说' }">
                <router-link to="/talks"> <i class="iconfont icon-pinglun2"></i> 说说 </router-link>
              </li>
              <li class="item" :class="{ active: $route.meta.title === '留言板' }">
                <router-link to="/message">
                  <i class="iconfont icon-pinglun1"></i> 留言板
                </router-link>
              </li>
              <li class="item" :class="{ active: $route.meta.title === '关于' }">
                <router-link to="/about"> <i class="iconfont icon-7"></i> 关于 </router-link>
              </li>
              <li class="item">
                <a v-if="!this.$store.state.avatar" @click="openLogin">
                  <i class="iconfont icon-dengluyonghuming"></i> 登录
                </a>
              </li>
              <li
                class="item"
                v-if="this.$store.state.userId"
                :class="{ active: $route.meta.title === '个人中心' }"
              >
                <router-link to="/user"> <i class="iconfont icon-user"></i> 个人中心 </router-link>
              </li>
              <li v-if="this.$store.state.userId" class="item">
                <a @click="logout"><i class="iconfont icon-tuichu"></i> 退出 </a>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <!-- 向上、向下 -->
      <ul id="quick">
        <li class="prev"></li>
        <li class="up" @click="backTop"><i class="iconfont icon-xiangshang"></i></li>
        <li class="down" @click="backBottom"><i class="iconfont icon-xiangxia"></i></li>
        <li class="next"></li>
        <li
          class="percent"
          :style="{
            width: percent + '%',
          }"
        ></li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: "SideBar",
  props: ["percent"],
  data() {
    return {
      scrollTop: null,
      on: "on",
      activeClass: 1,
      isMessage: "isMessage",
      styleObject: {
        height: "746px",
      },
      show: {
        transform: "translateX(0px)",
        opacity: "1",
        display: "block",
      },
      screenWidth: document.body.clientWidth,
    };
  },
  created() {
    this.handleHeight();
  },
  mounted() {
    window.addEventListener("resize", this.handleHeight);
    window.addEventListener("resize", this.handleWidth);
    this.$bus.$on("handleMessage", this.handleMessage);
  },
  methods: {
    handleMessage(data) {
      this.isMessage = data;
    },
    // 处理浏览器窗口变大侧边栏问题
    handleWidth() {
      this.screenWidth = document.body.clientWidth;
      if (this.screenWidth > 991 && this.isShow) {
        this.$bus.$emit("handleToggle");
      }
      if (this.screenWidth > 991 && this.$route.path == "/message") {
        this.isMessage = "isMessage";
      }
    },
    // 处理侧边栏高度变化
    handleHeight() {
      this.styleObject.height = document.documentElement.clientHeight + "px";
    },
    open(i) {
      this.activeClass = i;
    },
    openLogin() {
      this.$store.state.loginFlag = true;
    },
    backTop() {
      window.scrollTo({
        behavior: "smooth",
        top: 0,
      });
    },
    backBottom() {
      let t = document.body.clientHeight;
      window.scroll({ top: t, left: 0, behavior: "smooth" });
    },
    logout() {
      //如果在个人中心则跳回上一页
      if (this.$route.path == "/user") {
        this.$router.go(-1);
      }
      this.$store.dispatch("LogOut");
      this.$toast({ type: "success", message: "注销成功" });
    },
  },
  watch: {
    screenWidth(val) {
      this.screenWidth = val;
    },
    $route() {
      if (this.$route.meta.title == "文章详情") {
        this.$delete(this.styleObject, "paddingTop");
      } else {
        this.$set(this.styleObject, "paddingTop", "0.625rem");
      }
    },
  },
  computed: {
    blogInfo() {
      return this.$store.state.blogInfo;
    },
    isShow() {
      return this.$store.state.isCollapse;
    },
    isShowSocial() {
      return function (social) {
        return this.blogInfo.websiteDTO.socialUrlList.includes(social);
      };
    },
    expand() {
      if (
        this.$route.meta.title === "归档" ||
        this.$route.meta.title === "标签" ||
        this.$route.meta.title === "分类"
      ) {
        return true;
      }
      return false;
    },
  },
  destroyed() {
    window.removeEventListener("resize", this.handleHeight);
    window.removeEventListener("resize", this.handleWidth);
    this.$bus.$off("handleMessage");
  },
};
</script>

<style lang="scss" scoped>
.isMessage {
  display: none !important;
}
body.loaded #sidebar .panel.active {
  display: block;
}

#sidebar {
  position: static;
  top: 0;
  bottom: 0;
  width: 15rem;
  transition: all 0.2s ease-in-out 0s;
  & > .inner {
    position: relative;
    width: 15rem;
    color: var(--grey-6);
    text-align: center;
    display: flex;
    justify-content: space-around;
    align-items: flex-start;
    flex-wrap: wrap;
    z-index: 1;
  }
}
#sidebar .tab {
  position: absolute;
  top: 0;
  display: inline-flex;
  padding: 1.875rem 0 0.625rem;
  margin: 0;
  min-height: 1.875rem;
}
#sidebar .tab .item {
  cursor: pointer;
  display: inline-flex;
  font-size: 0.875em;
  padding: 0.3125rem 0.9375rem;
  border-radius: 0.625rem;
  text-align: center;
  text-decoration: none;
  background-color: rgba(0, 0, 0, 0.08);
  transition: all 0.2s ease-out 0s;
}
#sidebar .tab .item:nth-child(2) {
  margin: auto 0.625rem;
}
#sidebar .tab .item.active span {
  display: inherit;
}
#sidebar .tab .item span {
  display: none;
  word-break: keep-all;
}
#sidebar .tab .item.active::before {
  margin-right: 0.3125rem;
}
#sidebar .tab .item::before {
  font-family: iconfont;
  font-weight: 400;
}
#sidebar .tab .item.contents::before {
  content: "\e63a";
}
#sidebar .tab .item.overview::before {
  content: "\e867";
}
#sidebar .tab .item.active:hover {
  box-shadow: 0 0 0.75rem var(--color-pink);
}
.panels {
  padding: 4.6875rem 0 2rem;
  width: 100%;
  overflow: hidden;
  .inner {
    overflow-x: hidden;
    overflow-y: auto;
    width: auto;
    height: 100%;
    &::-webkit-scrollbar {
      display: none;
    }
  }
  .panel {
    display: none;
    padding: 0.875rem 0.9375rem 2rem;
  }
}

.overview {
  .author {
    .image {
      border: 0.0625rem solid var(--body-bg-shadow);
      display: block;
      margin: 0 auto;
      max-width: 10rem;
      padding: 0.125rem;
      box-shadow: 0 0 1rem 0.625rem var(--body-bg-shadow);
      border-radius: 50%;
    }
    .name {
      color: var(--grey-7);
      font-weight: 400;
      margin: 0.3125rem 0 0;
      text-align: center;
    }
    .description {
      color: var(--grey-5);
      font-size: 1em;
      margin-top: 0.3125rem;
      text-align: center;
    }
  }
  .menu {
    padding: 0.425rem 1.25rem 1.25rem;
    margin: 0;
    background-color: transparent;
    .item {
      border-radius: 0.9375rem;
      margin-bottom: 0.625rem;
      display: block;
      transition: all 0.2s ease-in-out 0s;
      &:hover {
        background-color: rgba(0, 0, 0, 0.1);
        .submenu {
          display: block;
        }
      }
      a {
        color: inherit;
        display: block;
        line-height: 3;
      }
      .iconfont {
        margin-right: 0.125rem;
      }
      .submenu {
        display: none;
        padding: 0;
      }
    }
  }
}
.overview .menu .item.expand {
  background-color: rgba(0, 0, 0, 0.05);
}
.overview .menu .item.expand .submenu {
  display: block;
}
.state {
  display: flex;
  justify-content: center;
  line-height: 1.4;
  margin-top: 0.625rem;
  overflow: hidden;
  text-align: center;
  white-space: nowrap;
  .item {
    padding: 0 0.9375rem;
    a {
      border-bottom: none;
    }
    .count {
      display: block;
      font-size: 1.25em;
      font-weight: 600;
      text-align: center;
    }
    .name {
      color: inherit;
      font-size: 0.875em;
    }
    &:not(:first-child) {
      border-left: 0.0625rem solid var(--grey-4);
    }
  }
}
.social {
  margin-top: 0.9375rem;
  text-align: center;
  .item {
    display: inline-block;
    width: 1.875rem;
    height: 1.875rem;
    margin: 0 0.125rem;
    line-height: 1.875rem;
    text-align: center;
    position: relative;
    overflow: hidden;
    border-radius: 38%;
    i {
      font-size: 1.4em;
      vertical-align: middle;
    }
  }
}
#quick {
  display: none;
  align-items: center;
  flex-wrap: wrap;
  width: 15rem;
  margin: 0;
  padding: 0;
  position: fixed;
  bottom: 0.125rem;
  li {
    width: 25%;
    min-height: 1.875rem;
    transition: all 0.2s ease-in-out 0s;
    i {
      cursor: pointer;
    }
    &.percent {
      display: block;
      background: var(--primary-color);
      width: 0;
      min-height: 0.125rem;
    }
  }
}

@media (max-width: 991px) {
  #sidebar {
    display: none;
    position: fixed;
    right: 0;
    background: var(--grey-1);
    box-shadow: 0 0.375rem 0.9375rem 0.3125rem rgb(0 0 0 / 20%);
    z-index: 99;
  }
}
/* 作者头像动画 */
.overview .author:hover .image {
  -webkit-animation: shake 1s;
  animation: 1000ms ease 0ms 1 normal none running shake;
}
.lozaded {
  -webkit-animation: blur 0.8s ease-in-out forwards;
  animation: 1000ms ease-in-out 0ms 1 normal forwards running blur;
}
@keyframes shake {
  0% {
    transform: scale(1);
  }
  10%,
  20% {
    transform: scale(0.9) rotate(3deg);
  }
  30%,
  50%,
  70%,
  90% {
    transform: scale(1.1) rotate(-3deg);
  }
  40%,
  60%,
  80% {
    transform: scale(1.1) rotate(3deg);
  }
  100% {
    transform: scale(1);
  }
}
</style>