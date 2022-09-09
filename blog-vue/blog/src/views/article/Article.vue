<template>
  <div class="article wrap">
    <!-- 文章分类 -->
    <div class="article-header">
      <div class="breadcrumb">
        <i class="iconfont icon-home-fill"></i>
        <span><router-link to="/">首页</router-link></span>
        <i class="iconfont icon-jiantouyou"></i>
        <span class="current"
          ><router-link class="category" :to="`/categories/${article.category.id}`">
            {{ article.category.categoryName }}
          </router-link></span
        >
      </div>
      <div>
        <span class="article-type" v-if="article.articleType == 1">原创</span>
        <span class="article-type" v-if="article.articleType == 2">转载</span>
        <span class="article-type" v-if="article.articleType == 3">翻译</span>
      </div>
    </div>
    <!-- 文章内容 -->
    <article v-viewer>
      <div class="article-content md" v-html="article.articleContent" ref="article"></div>
    </article>
    <!-- 文章标签 -->
    <div class="tags">
      <router-link
        class="tag"
        v-for="item of article.tagVOList"
        :key="item.id"
        :to="'/tags/' + item.id"
      >
        <i class="iconfont icon-tags"></i>
        {{ item.tagName }}
      </router-link>
    </div>
    <footer class="article-info">
      <!-- 文章信息 -->
      <div class="meta">
        <span class="item" v-show="article.updateTime">
          <span class="icon"><i class="iconfont icon-rili"></i></span>
          <span>更新于 </span>
          <span>{{ article.updateTime | date }}</span>
        </span>
        <span class="item">
          <span class="icon"><i class="iconfont icon-ai-eye"></i></span>
          <span>阅读次数 </span>
          <span>{{ article.viewCount }} 次</span>
        </span>
      </div>
      <!-- 点赞、赞赏 -->
      <div class="reward">
        <button class="btn" :class="isLike" @click="like">
          <i class="iconfont icon-dianzan"></i> 点赞
          <span v-show="article.likeCount > 0">{{ article.likeCount }}</span>
        </button>
        <button class="btn reward-btn" v-if="blogInfo.websiteDTO.isReward == 1">
          <i class="iconfont icon-erweima"></i> 打赏
          <!-- 二维码 -->
          <div class="animated fadeInDown reward-main">
            <ul class="reward-all">
              <li class="reward-item">
                <img class="reward-img" :src="blogInfo.websiteDTO.weiXinQRCode" />
                <div class="reward-desc">微信</div>
              </li>
              <li class="reward-item">
                <img class="reward-img" :src="blogInfo.websiteDTO.alipayQRCode" />
                <div class="reward-desc">支付宝</div>
              </li>
            </ul>
          </div>
        </button>
        <p class="tea">请我喝[茶]~(￣▽￣)~*</p>
      </div>
      <!-- 版权声明 -->
      <div class="copyright">
        <ul>
          <li class="author"><strong>本文作者： </strong>阿冬</li>
          <li class="link">
            <strong>本文链接：</strong>
            <a :href="articleHref" target="_blank">{{ articleHref }} </a>
          </li>
          <li class="license">
            <strong>版权声明： </strong>本站所有文章除特别声明外，均采用
            <a
              href="https://creativecommons.org/licenses/by-nc-sa/4.0/deed.zh"
              target="_blank"
              class="exturl"
              ><i class="iconfont icon-banquan"></i>BY-NC-SA</a
            >
            许可协议。转载请注明出处！
          </li>
        </ul>
      </div>
    </footer>
    <!-- 上下文 -->
    <div class="post-nav">
      <div class="item left" v-if="article.lastArticle">
        <router-link
          class="post-cover"
          :to="`/articles/${article.lastArticle.id}`"
          :style="{
            backgroundImage:
              article.lastArticle.articleCover && `url(${article.lastArticle.articleCover})`,
          }"
        >
          <span class="post-last-next">上一篇</span>
          <h3 class="post-title">{{ article.lastArticle.articleTitle }}</h3>
        </router-link>
      </div>
      <div class="item right" v-if="article.nextArticle">
        <router-link
          class="post-cover"
          :to="`/articles/${article.nextArticle.id}`"
          :style="{
            backgroundImage:
              article.nextArticle.articleCover && `url(${article.nextArticle.articleCover})`,
          }"
        >
          <span class="post-last-next">下一篇</span>
          <h3 class="post-title">{{ article.nextArticle.articleTitle }}</h3>
        </router-link>
      </div>
    </div>
    <Comment :commentType="commentType"></Comment>
  </div>
</template>

<script>
import { getArticle, likeArticle } from "@/api/article";
import markdownToHtml from "@/utils/markdown";
import Comment from "../../components/Comment.vue";
import Clipboard from "clipboard";
import tocbot from "tocbot";
export default {
  name: "Article",
  components: {
    Comment,
  },
  created() {
    this.getArticle();
  },
  data() {
    return {
      article: {
        category: {},
        nextArticle: {},
        lastArticle: {},
      },
      brandInfo: {
        articleTitle: "",
        createTime: "",
        wordNum: "",
        readTime: "",
      },
      commentType: 1,
      articleHref: window.location.href,
      wordNum: "",
      readTime: "",
      clipboard: null,
    };
  },
  methods: {
    like() {
      // 判断登录
      if (!this.$store.state.userId) {
        this.$store.state.loginFlag = true;
        return false;
      }
      likeArticle(this.article.id).then(({ data }) => {
        if (data.flag) {
          //判断是否点赞
          if (this.$store.state.articleLikeSet.indexOf(this.article.id) != -1) {
            this.$set(this.article, "likeCount", this.article.likeCount - 1);
          } else {
            this.$set(this.article, "likeCount", this.article.likeCount + 1);
          }
          this.$store.commit("articleLike", this.article.id);
        }
      });
    },
    getArticle() {
      getArticle(this.$route.params.id).then(({ data }) => {
        document.title = data.data.articleTitle;
        this.brandInfo["articleTitle"] = data.data.articleTitle;
        this.brandInfo["createTime"] = data.data.createTime;
        //将markdown转换为Html
        this.article = data.data;
        this.article.articleContent = markdownToHtml(this.article.articleContent);
        this.$nextTick(() => {
          // 统计文章字数
          this.wordNum = this.deleteHTMLTag(this.article.articleContent).length;
          this.brandInfo["wordNum"] = this.wordNum;
          // 计算阅读时间
          this.readTime = Math.round(this.wordNum / 400);
          this.brandInfo["readTime"] = this.readTime;
          // 添加代码复制功能
          this.clipboard = new Clipboard(".copy-btn");
          this.clipboard.on("success", () => {
            this.$toast({ type: "success", message: "复制成功" });
          });
          // 添加文章生成目录功能
          let nodes = this.$refs.article.children;
          if (nodes.length) {
            for (let i = 0; i < nodes.length; i++) {
              let node = nodes[i];
              let reg = /^H[1-4]{1}$/;
              if (reg.exec(node.tagName)) {
                node.id = i;
                let titleHref = document.createElement("a");
                titleHref.setAttribute("class", "anchor");
                node.appendChild(titleHref);
              }
            }
          }
          tocbot.init({
            tocSelector: "#toc", //要把目录添加元素位置，支持选择器
            contentSelector: ".article-content", //获取html的元素
            headingSelector: "h1, h2, h3", //要显示的id的目录
            hasInnerContainers: true,
            onClick: function (e) {
              e.preventDefault();
            },
          });
        });
      });
    },

    deleteHTMLTag(content) {
      return content
        .replace(/<\/?[^>]*>/g, "")
        .replace(/[|]*\n/, "")
        .replace(/&npsp;/gi, "");
    },
  },
  computed: {
    blogInfo() {
      return this.$store.state.blogInfo;
    },
    isLike() {
      var articleLikeSet = this.$store.state.articleLikeSet;
      return articleLikeSet.indexOf(this.article.id) != -1 ? "like-btn-active" : "like-btn";
    },
  },
  mounted() {
    this.$bus.$emit("getArticleInfo", this.brandInfo);
  },
  destroyed() {
    this.clipboard.destroy();
    tocbot.destroy();
  },
};
</script>

<style lang="scss" scoped>
.reward-btn:hover .reward-main {
  display: block;
}
.reward-main {
  display: none;
  position: absolute;
  bottom: 35px;
  left: 0;
  margin: 0;
  padding: 0 0 15px;
  width: 100%;
}
.reward-all {
  display: inline-block;
  margin: 0 0 0 -110px;
  padding: 20px 10px 8px !important;
  width: 320px;
  border-radius: 4px;
  background: #f5f5f5;
}
.reward-all:before {
  position: absolute;
  bottom: -10px;
  left: 0;
  width: 100%;
  height: 20px;
  content: "";
}
.reward-all:after {
  content: "";
  position: absolute;
  right: 0;
  bottom: 2px;
  left: 0;
  margin: 0 auto;
  width: 0;
  height: 0;
  border-top: 13px solid #f5f5f5;
  border-right: 13px solid transparent;
  border-left: 13px solid transparent;
}
.reward-item {
  display: inline-block;
  padding: 0 8px;
  list-style-type: none;
}
.reward-img {
  width: 130px;
  height: 130px;
  display: block;
}
.reward-desc {
  margin: -5px 0;
  color: #858585;
  text-align: center;
}
.article-header {
  display: flex;
  justify-content: space-between;
}
.article-type {
  display: inline-flex;
  font-size: 0.8125em;
  margin: 1.25rem 1.25rem 0 0;
  background-color: var(--color-pink-a3);
  color: var(--color-pink);
  border-radius: 0.625rem;
  padding: 0 0.625rem;
}
.breadcrumb {
  display: inline-flex;
  font-size: 0.8125em;
  align-items: center;
  margin: 1.25rem 0;
  flex-wrap: wrap;
  .iconfont {
    margin: 0 0.125rem;
    color: var(--grey-4);
    &:nth-child(1) {
      margin-left: 0;
      margin-right: 0.3125rem;
    }
  }
  .current {
    background-color: var(--color-red-a1);
    border-radius: 0.625rem;
    padding: 0 0.625rem;
    transition: all 0.2s ease-in-out 0s;
    .category {
      color: var(--primary-color);
    }
    &:hover {
      background-color: var(--color-red-a3);
    }
  }
}

.article {
  .article-content {
    padding: 0 2rem;
  }
  .tags {
    text-align: left;
    margin-top: 0.625rem;
    padding: 0 2rem;
    font-size: 0.75em;
    .tag {
      display: inline-block;
      position: relative;
      padding: 0 0.3125rem;
      border-radius: 0.3125rem;
      background: var(--note-bg);
      color: #8a51c0;
      &:not(:last-child) {
        margin-right: 0.625rem;
      }
      &:hover {
        color: #e9546b;
        &:before {
          width: 104%;
          left: -2%;
        }
      }
      &:before {
        content: "";
        position: absolute;
        bottom: 0;
        height: 100%;
        width: 0;
        right: 0;
        background: var(--color-red-a1);
        border-radius: 0.25rem;
        transition: all 0.2s ease-in-out 0s;
      }
    }
  }
  .article-info {
    padding: 0 2rem;
    &::before {
      content: "";
      width: 100%;
      height: 0.0625rem;
      background: var(--grey-3);
      display: block;
      margin: 1.25rem auto 0;
    }
  }
  .meta {
    color: var(--grey-5);
    font-size: 0.75em;
    text-align: right;
    .item {
      display: inline-block;
      margin-right: 0.625rem;
    }
    .icon {
      margin-right: 0.1875rem;
    }
  }
  .reward {
    margin: 1.25rem auto;
    padding: 0.625rem 0;
    text-align: center;
    .btn {
      border: 0;
      border-radius: 0.3125rem;
      color: var(--grey-0);
      cursor: pointer;
      line-height: 2;
      outline: 0;
      padding: 0 0.9375rem;
      vertical-align: text-top;
      i {
        margin-right: 0.3125rem;
      }
    }
    .like-btn-active {
      background: var(--primary-color);
    }
    .like-btn {
      background: #999;
    }
    .reward-btn {
      position: relative;
      margin-left: 1rem;
      background: var(--primary-color);
    }
    .tea {
      font-size: 0.8125em;
      color: var(--grey-5);
      margin: 0;
    }
  }
}
.copyright {
  position: relative;
  font-size: 0.75em;
  padding: 1rem 2rem;
  margin-bottom: 2.5rem;
  border-radius: 0.625rem;
  background: var(--grey-2);
  color: var(--grey-6);
}
.copyright li::before {
  font-family: iconfont;
  font-weight: 400;
  color: var(--grey-5);
  margin-right: 0.3125rem;
  font-size: 0.9rem;
  line-height: 0.75rem;
  vertical-align: -0.0667rem;
}
.copyright .author::before {
  content: "\e65c";
}
.copyright .link::before {
  content: "\e8e1";
}
.copyright .license::before {
  content: "\e74f";
}
.post-nav {
  display: flex;
  margin-bottom: 2.5rem;
  .item {
    width: 50%;
    .post-cover {
      display: flex;
      flex-direction: column;
      height: 100%;
      color: var(--header-text-color);
      padding: 1.25rem 2.5rem;
      background-size: cover;
      position: relative;
      animation: blur 0.8s ease-in-out forwards;
      &:before {
        content: "";
        position: absolute;
        width: 100%;
        height: 100%;
        background: linear-gradient(135deg, #434343, #000);
        opacity: 0.5;
        transition: all 0.2s ease-in-out 0s;
        z-index: -1;
        top: 0;
        left: 0;
      }
    }
    .post-last-next {
      font-size: 0.8125rem;
    }
  }
}
.post-nav .item .post-cover:hover::before {
  opacity: 0.4;
}
@media (max-width: 767px) {
  .post-nav {
    flex-direction: column;
  }
  .post-nav .item {
    width: 100%;
  }
}
@media (max-width: 567px) {
  .article-content {
    padding: initial !important;
  }
  .tags,
  .article-info {
    padding: 0 !important;
  }
}
</style>

<style lang="scss">
pre.hljs {
  padding: 12px 2px 12px 40px !important;
  border-radius: 5px !important;
  position: relative;
  font-size: 14px !important;
  line-height: 22px !important;
  overflow: hidden !important;
  &:hover .copy-btn {
    display: flex;
    justify-content: center;
    align-items: center;
  }
  code {
    display: block !important;
    margin: 20px 10px 0 10px !important;
    overflow-x: auto !important;
    &::-webkit-scrollbar {
      z-index: 11;
      width: 6px;
    }
    &::-webkit-scrollbar:horizontal {
      height: 6px;
    }
    &::-webkit-scrollbar-thumb {
      border-radius: 5px;
      width: 6px;
      background: #666;
    }
    &::-webkit-scrollbar-corner,
    &::-webkit-scrollbar-track {
      background: #1e1e1e;
    }
    &::-webkit-scrollbar-track-piece {
      background: #1e1e1e;
      width: 6px;
    }
  }
  .line-numbers-rows {
    position: absolute;
    pointer-events: none;
    top: 32px;
    bottom: 12px;
    left: 0;
    font-size: 100%;
    width: 40px;
    text-align: center;
    letter-spacing: -1px;
    border-right: 1px solid rgba(0, 0, 0, 0.66);
    user-select: none;
    counter-reset: linenumber;
    span {
      pointer-events: none;
      display: block;
      counter-increment: linenumber;
      &:before {
        content: counter(linenumber);
        color: #999;
        display: block;
        text-align: center;
      }
    }
  }
  b.name {
    position: absolute;
    top: 7px;
    right: 45px;
    z-index: 1;
    color: #999;
    pointer-events: none;
  }
  .copy-btn {
    position: absolute;
    top: 6px;
    right: 6px;
    z-index: 1;
    color: #ccc;
    background-color: #525252;
    border-radius: 6px;
    display: none;
    font-size: 14px;
    width: 32px;
    height: 24px;
    outline: none;
  }
}
.hljs::before {
  position: absolute;
  left: 0.95rem;
  top: 0.8125rem;
  width: 0.75rem;
  height: 0.75rem;
  overflow: visible;
  font-weight: 700;
  font-size: 16px;
  line-height: 12px;
  white-space: nowrap;
  text-indent: 75px;
  background-color: #fc625d;
  border-radius: 16px;
  box-shadow: 20px 0 #fdbc40, 40px 0 #35cd4b;
  content: attr(codetype);
}
</style>