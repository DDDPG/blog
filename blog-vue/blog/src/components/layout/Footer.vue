<template>
  <!-- 底部 -->
  <footer id="footer">
    <div class="inner">
      <div class="widgets">
        <div class="widget">
          <h2>最新文章</h2>
          <ul class="content">
            <li class="item" v-for="item of articleList" :key="item.id">
              <span class="breadcrumb" style="font-size: 12px">{{ item.createTime }}</span>
              <span class="span-class" style="margin-top: 0.2rem"
                ><router-link :to="`/articles/${item.id}`">{{ item.articleTitle }}</router-link>
              </span>
            </li>
          </ul>
        </div>
        <div class="widget">
          <h2>最新评论</h2>
          <ul class="content">
            <li class="item" v-for="item of commentList" :key="item.id">
              <a @click="goToComment(item.typeId, item.commentType)">
                <span class="breadcrumb"
                  ><span class="span-class" style="margin-right: 0.5rem">{{ item.nickname }}</span>
                  <span class="span-class">{{ item.createTime }}</span></span
                >
                <span
                  class="span-class"
                  style="margin-top: 0.2rem"
                  v-html="item.commentContent"
                ></span
              ></a>
            </li>
          </ul>
        </div>
      </div>
      <div class="status">
        <div class="copyright">
          © {{ blogInfo.websiteDTO.websiteCreateTime | year }} –
          <span>{{ new Date().getFullYear() }}</span>
          <span class="author"> by {{ blogInfo.websiteDTO.websiteAuthor }}</span>
        </div>
        <div class="blog-info">
          <span><i class="iconfont icon-jurassic_broken-line"></i></span>
          <span> {{ time }}</span>
          <span style="margin: 0 4px">|</span>
          <span><i class="iconfont icon-yanjing"></i></span>
          <span> {{ blogInfo.viewCount }}</span>
        </div>
        <a href="https://beian.miit.gov.cn/" target="_blank">
          {{ blogInfo.websiteDTO.websiteRecordNumber }}
        </a>
      </div>
    </div>
  </footer>
</template>

<script>
import { getRecentComments } from "@/api/comment";
import { getRecentArticles } from "@/api/article";
export default {
  name: "Footer",
  created() {
    this.timer = setInterval(this.runTime, 1000);
    this.getComments();
    this.getArticles();
  },
  data() {
    return {
      time: "",
      articleList: [],
      commentList: [],
    };
  },
  methods: {
    getArticles() {
      getRecentArticles().then(({ data }) => {
        this.articleList = data.data;
      });
    },
    getComments() {
      getRecentComments().then(({ data }) => {
        if (data.flag) {
          this.commentList = data.data;
        }
      });
    },
    goToComment(typeId, commentType) {
      if (commentType == 1) {
        this.$router.push({ path: `/articles/${typeId}` });
      }
      if (commentType == 2) {
        this.$router.push({ path: `/friends` });
      }
      if (commentType == 3) {
        this.$router.push({ path: `/talks/${typeId}` });
      }
    },
    runTime() {
      var timeold =
        new Date().getTime() - new Date(this.blogInfo.websiteDTO.websiteCreateTime).getTime();
      var msPerDay = 24 * 60 * 60 * 1000;
      var daysold = Math.floor(timeold / msPerDay);
      var str = "";
      var day = new Date();
      str += daysold + "天";
      str += day.getHours() + "时";
      str += day.getMinutes() + "分";
      str += day.getSeconds() + "秒";
      this.time = str;
    },
  },
  computed: {
    blogInfo() {
      return this.$store.state.blogInfo;
    },
  },
};
</script>

<style lang="scss" scoped>
a:hover {
  border-bottom-color: var(--color-blue);
  color: var(--color-blue);
}
#footer {
  color: var(--grey-5);
  font-size: 0.875em;
  background: var(--body-bg-shadow);
}
#footer .inner {
  margin: 0 auto;
  width: calc(100% - 0.625rem);
  position: relative;
  padding-right: 16.25rem;
}

.widgets {
  display: flex;
  z-index: 1;
  background: var(--body-bg-shadow);
  justify-content: space-around;
}
.widgets .item {
  padding: 0.5rem 0 0.5rem 2rem;
  border-bottom: 0.0625rem dashed var(--grey-4);
  position: relative;
}
.widgets .item .breadcrumb,
.widgets .item .span-class {
  display: block;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}
.widgets .item .breadcrumb {
  margin: 0;
  display: flex;
  max-height: 1.2rem;
}

.breadcrumb {
  display: inline-flex;
  font-size: 0.8125em;
  align-items: center;
  margin: 1.25rem 0;
  flex-wrap: wrap;
}
.widgets > .widget {
  width: calc(50% - 2rem);
  padding: 1rem;
}

.widgets .content {
  counter-reset: counter;
}
.widgets .item::before {
  counter-increment: counter;
  content: counter(counter);
  position: absolute;
  left: 0;
  font-size: 1.5em;
  color: var(--grey-4);
  line-height: 1.2;
  text-align: right;
  width: 1em;
}
.status {
  width: 100%;
  text-align: center;
  margin-top: 2rem;
}
.with-love {
  color: pink;
  display: inline-block;
  margin: 0 0.3125rem 0 0.125rem;
}
@media (min-width: 1200px) {
  #footer .inner {
    width: 72.5rem;
  }
}
@media (max-width: 991px) {
  #footer .inner {
    padding-left: 0;
    padding-right: 0;
    width: auto;
  }
}
@media (max-width: 767px) {
  .widgets {
    flex-direction: column-reverse;
  }
  .widgets > .widget {
    width: calc(100% - 1rem) !important;
  }
}
@keyframes rotate {
  0% {
    transform: rotate(0);
  }

  100% {
    transform: rotate(360deg);
  }
}
</style>