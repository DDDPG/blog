<template>
  <header id="header" ref="header">
    <div class="inner">
      <!-- 公用说明 -->
      <div id="brand" :class="blogLogo">
        <div class="pjax">
          <router-link to="/" v-if="this.$route.meta.title == 'Hello World'">
            <p class="artboard" v-show="this.$route.meta.title == 'Hello World'">Hello World</p>
            <h1 class="title">優萌初華</h1>
          </router-link>
          <h1 class="title" v-else-if="this.$route.meta.title == '文章详情'">
            {{ articleInfo.articleTitle }}
          </h1>
          <h1 class="title" v-else-if="this.$route.meta.title == '分类'">
            分类<span v-if="this.$route.params.categoryId">-{{ name }}</span>
          </h1>
          <h1 class="title" v-else-if="this.$route.meta.title == '标签'">
            标签<span v-if="this.$route.params.tagId">-{{ name }}</span>
          </h1>
          <h1 class="title" v-else>{{ this.$route.meta.title }}</h1>
          <p class="meta" v-if="$route.meta.title == 'Hello World'">= 有夢書架 =</p>
          <div class="meta" v-else-if="$route.meta.title == '文章详情'">
            <span class="item"
              ><span class="icon"><i class="iconfont icon-rili"></i></span
              ><span class="text">发表于 </span>
              <time>{{ articleInfo.createTime | date }}</time> </span
            ><span class="item"
              ><span class="icon"><i class="iconfont icon-gangbigongju"></i></span
              ><span class="text">本文字数</span> <span>{{ articleInfo.wordNum | num }}</span>
              <span class="text"> 字</span></span
            ><span class="item"
              ><span class="icon"><i class="iconfont icon-24gf-clockCircle"></i></span
              ><span class="text">阅读时长 </span><span>{{ articleInfo.readTime }} 分钟</span></span
            >
          </div>
        </div>
      </div>
      <!-- 导航 -->
      <Nav></Nav>
    </div>
    <!-- 背景轮播 -->
    <Images v-show="!isMessage"></Images>
  </header>
</template>

<script>
import Nav from "../Nav.vue";
import Images from "../Images.vue";
export default {
  name: "Header",
  components: {
    Nav,
    Images,
  },
  props: ["blogLogo"],
  data() {
    return {
      scrollTop: null,
      height: null,
      articleInfo: {},
      name: "",
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.$erd.listenTo(this.$refs.header, (element) => {
        this.height = element.offsetHeight;
        this.$bus.$emit("headerDetector", this.height);
      });
    });
    this.$bus.$on("getArticleInfo", this.getArticleInfo);
    this.$bus.$on("getName", this.getName);
  },
  computed: {
    isMessage() {
      return this.$route.path == "/message";
    },
  },
  methods: {
    getArticleInfo(articleInfo) {
      this.articleInfo = articleInfo;
    },
    getName(name) {
      this.name = name;
    },
    searchModel() {
      this.$store.commit("searchOn");
    },
  },
  beforeDestroy() {
    this.$bus.$off("getArticleInfo");
    this.$bus.$off("getName");
  },
};
</script>

<style lang="scss" scoped>
/* 头部 */
#header {
  margin: 0 auto;
  position: relative;
  width: 100%;
  height: 50vh;
  text-shadow: 0 0.2rem 0.3rem rgb(0 0 0 / 50%);
  color: var(--header-text-color);
}
#header a:hover {
  color: currentColor;
}
/* 公用说明 */
#brand,
#brand .pjax {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

#brand {
  position: fixed;
  text-align: center;
  padding: 3rem 5rem 0;
  width: 100%;
  height: 50vh;
  min-height: 10rem;
  .artboard + h1 {
    margin: 0.625rem 0;
  }
  h1 {
    font-size: 2.5rem;
    letter-spacing: 0.125rem;
  }
  .artboard {
    font-family: "Fredericka the Great", Mulish, -apple-system, "PingFang SC", "Microsoft YaHei",
      sans-serif;
    font-size: 3.5em;
    line-height: 1.2;
  }
  .meta {
    display: flex;
    font-size: 0.875rem;
    margin: 0;
    .icon {
      margin-right: 0.125rem;
    }
  }
}
#brand .meta .item + .item {
  margin-left: 0.625rem;
}
@media (max-width: 767px) {
  #brand {
    padding: 3rem 0.5rem 0;
    .meta {
      font-size: 0.75rem;
    }
    h1 {
      font-size: 1.5rem;
    }
  }
}
@media (max-width: 413px) {
  #brand .artboard {
    font-size: 2.5em;
  }
}
</style>
