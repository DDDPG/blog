<template>
  <div id="app" data-app="true">
    <Header ref="header" :blogLogo="blogLogo"></Header>
    <Waves ref="waves" v-show="!isMessage"></Waves>
    <main>
      <div class="inner">
        <div id="main">
          <router-view :key="$route.fullPath"></router-view>
        </div>
        <SideBar :class="fixedClass" :percent="percent"></SideBar>
        <Dimmer></Dimmer>
      </div>
    </main>
    <Footer v-show="!isMessage"></Footer>
    <LoginModel></LoginModel>
    <RegisterModel></RegisterModel>
    <ForgetModel></ForgetModel>
    <EmailModel></EmailModel>
    <SearchModel></SearchModel>
    <MusicPlayer v-if="isMusicPlayer"></MusicPlayer>
  </div>
</template>
<script>
import Header from "./components/layout/Header.vue";
import SideBar from "./components/layout/SideBar.vue";
import Footer from "./components/layout/Footer.vue";
import Dimmer from "./components/Dimmer.vue";
import Message from "./views/message/Message.vue";
import Waves from "./components/Waves.vue";
import LoginModel from "./components/model/LoginModel.vue";
import RegisterModel from "./components/model/RegisterModel.vue";
import ForgetModel from "./components/model/ForgetModel.vue";
import EmailModel from "./components/model/EmailModel.vue";
import SearchModel from "./components/model/SearchModel.vue";
import MusicPlayer from "./components/MusicPlayer.vue";
import { getBlogInfo, report } from "@/api/blog";
export default {
  name: "App",
  components: {
    Header,
    Waves,
    SideBar,
    Message,
    Dimmer,
    Footer,
    LoginModel,
    RegisterModel,
    ForgetModel,
    EmailModel,
    SearchModel,
    MusicPlayer,
  },
  data() {
    return {
      scrollTop: null,
      fixedClass: "",
      blogLogo: "",
      percent: 0,
    };
  },
  created() {
    console.log(
      "%c Hello World %c By 阿冬 %c",
      "background:#e9546b ; padding: 1px; border-radius: 3px 0 0 3px;  color: #fff; padding:5px 0;",
      "background:#ec8c69 ; padding: 1px; border-radius: 0 3px 3px 0;  color: #000; padding:5px 0;",
      "background:transparent"
    );
    this.getSiteInfo();
    this.init();
  },
  mounted() {
    window.addEventListener("scroll", this.handleScroll);
  },
  methods: {
    handleScroll() {
      // 页面总高
      let totalH = document.body.scrollHeight || document.documentElement.scrollHeight;
      // 可视高
      let clientH = window.innerHeight || document.documentElement.clientHeight;
      // 计算有效高
      let validH = totalH - clientH;
      // 滚动条卷去高度
      let scrollH = document.body.scrollTop || document.documentElement.scrollTop;
      // 百分比
      this.percent = ((scrollH / validH) * 100).toFixed(0);
      this.scrollTop =
        window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
      if (this.scrollTop > this.$refs.header.$el.offsetHeight + this.$refs.waves.$el.offsetHeight) {
        this.fixedClass = "affix";
      } else {
        this.fixedClass = "";
      }
      if (this.scrollTop < 484 && this.$refs.header.$el.offsetHeight == null) {
        this.fixedClass = "";
      }
    },
    getSiteInfo() {
      getBlogInfo().then(({ data }) => {
        this.$store.commit("setBlogInfo", data.data);
      });
    },
    init() {
      report();
    },
  },
  watch: {
    // 监听屏幕滚动控制导航显示隐藏以及brand的z-index
    scrollTop(oldValue, newValue) {
      if (oldValue > 0) {
        this.blogLogo = "blogLogo";
      } else {
        this.blogLogo = "";
      }
    },
  },
  computed: {
    isMessage() {
      return this.$route.path == "/message";
    },
    isMusicPlayer() {
      return this.$store.state.blogInfo.websiteDTO.isMusicPlayer;
    },
  },
  beforeDestroy() {
    window.removeEventListener("scroll", this.handleScroll);
  },
};
</script>

<style>
#app {
  min-height: 100%;
  min-width: 100%;
  position: relative;
  display: flex;
  flex-direction: column;
}
main {
  display: block;
  background: linear-gradient(to top, var(--body-bg-shadow) 0, var(--grey-1) 20%) no-repeat bottom;
}
main > .inner {
  display: flex;
  width: calc(100% - 0.625rem);
  align-items: flex-start;
  justify-content: space-between;
}
.inner {
  margin: 0 auto;
}
.blogLogo {
  z-index: -1;
}
#main {
  background: linear-gradient(to top, var(--grey-0) 0, var(--grey-1) 20%) no-repeat top;
  box-shadow: 0 1.25rem 1rem 0.3125rem var(--body-bg-shadow);
  width: calc(100% - 15.75rem);
  min-height: 47rem;
}
#main .wrap {
  position: relative;
  padding: 1.25rem;
}

#sidebar.affix > .inner {
  position: fixed;
  top: 0;
}
#sidebar.affix #quick,
#sidebar.on #quick {
  display: flex;
}
@media (max-width: 991px) {
  #main {
    width: 100%;
  }
  #main .wrap {
    padding: 0.625rem;
  }
}
@media (min-width: 1600px) {
  main > .inner {
    width: 73%;
  }
}
@media (min-width: 1200px) {
  main > .inner {
    width: 72.5rem;
  }
}

@media (max-width: 767px) {
  #main .wrap {
    padding: 0.5rem;
  }
}
</style>