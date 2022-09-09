<template>
  <div class="index wrap">
    <Swiper :contentArr="talkList" v-if="talkList.length > 0"></Swiper>
    <div class="segments">
      <div class="item" v-for="item in articleList" :key="item.id">
        <!-- 文章缩略图 -->
        <div class="cover">
          <router-link :to="`/articles/${item.id}`"
            ><img class="article-cover" :src="item.articleCover"
          /></router-link>
        </div>
        <!-- 文章信息 -->
        <div class="info">
          <div class="meta">
            <!-- 是否置顶 -->
            <span class="item" v-if="item.isTop == 1">
              <span class="icon" style="color: #ec8c69"
                ><i class="iconfont icon-zhiding"></i>置顶</span
              >
            </span>
            <!-- 发表时间 -->
            <span class="item">
              <span class="icon"><i class="iconfont icon-rili"></i></span
              >{{ item.createTime | date }}
            </span>
            <!-- 文章标签 -->
            <router-link
              class="item"
              :to="`/tags/${tag.id}`"
              v-for="tag of item.tagVOList"
              :key="tag.id"
            >
              <span class="icon"><i class="iconfont icon-tags"></i>{{ tag.tagName }}</span>
            </router-link>
          </div>
          <!-- 文章标题 -->
          <h3 class="article-title">
            <router-link :to="`/articles/${item.id}`">{{ item.articleTitle }}</router-link>
          </h3>
          <!-- 文章内容 -->
          <div class="excerpt">
            {{ item.articleContent }}
          </div>
          <!-- 文章分类 -->
          <div class="meta footer">
            <router-link :to="`/categories/${item.category.id}`">
              <span style="margin-right: 0.5rem"
                ><i class="iconfont icon-message_5"></i>{{ item.category.categoryName }}</span
              >
            </router-link>
          </div>
          <!-- 阅读按钮 -->
          <router-link class="btn" :to="`/articles/${item.id}`">more...</router-link>
        </div>
      </div>
    </div>
    <v-pagination
      v-if="count > 10"
      class="pagination"
      color="#ed6ea0"
      v-model="currentPage"
      :length="Math.ceil(count / 10)"
      total-visible="7"
    ></v-pagination>
  </div>
</template>

<script>
import { getArticles } from "@/api/article";
import { getHomeTalk } from "@/api/talk";
import Dimmer from "../../components/Dimmer.vue";
import Swiper from "../../components/Swiper.vue";
var md = require("markdown-it")();
export default {
  name: "Home",
  data() {
    return {
      articleList: [],
      talkList: [],
      currentPage: 1,
      count: 0,
    };
  },
  components: {
    Dimmer,
    Swiper,
  },
  created() {
    this.getArticleList();
    this.getHomeTalks();
  },
  methods: {
    getArticleList() {
      getArticles(this.currentPage).then(({ data }) => {
        this.count = data.data.count;
        if (this.count) {
          // 去除markdown标签
          data.data.recordList.forEach((item) => {
            item.articleContent = md
              .render(item.articleContent)
              .replace(/<\/?[^>]*>/g, "")
              .replace(/[|]*\n/, "")
              .replace(/&npsp;/gi, "");
          });
          this.articleList = data.data.recordList;
        }
      });
    },
    getHomeTalks() {
      getHomeTalk().then(({ data }) => {
        this.talkList = data.data;
      });
    },
  },
  computed: {
    blogInfo() {
      return this.$store.state.blogInfo;
    },
  },
  watch: {
    currentPage(value) {
      getArticles(value).then(({ data }) => {
        this.count = data.data.count;
        if (this.count) {
          // 去除markdown标签
          data.data.recordList.forEach((item) => {
            item.articleContent = md
              .render(item.articleContent)
              .replace(/<\/?[^>]*>/g, "")
              .replace(/[|]*\n/, "")
              .replace(/&npsp;/gi, "");
          });
          this.articleList = data.data.recordList;
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.pagination {
  padding: 30px 0 20px 0;
}
a:hover {
  border-bottom-color: var(--color-blue);
  color: var(--color-blue);
}

.segments {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  & > .item {
    position: relative;
    display: flex;
    width: calc(100% - 2rem);
    min-width: calc(100% - 2rem);
    height: 14rem;
    margin: 1rem;
    color: inherit;
    border-radius: 0.5rem;
    box-shadow: 0 0.625rem 1.875rem -0.9375rem var(--box-bg-shadow);
    transition: all 0.2s ease-in-out 0s;

    &:hover {
      box-shadow: 0 0 2rem var(--box-bg-shadow);

      .cover {
        .article-cover {
          transform: scale(1.05) rotate(1deg);
        }
      }
    }

    &:nth-child(even) {
      flex-direction: row-reverse;
      .cover {
        margin-right: auto;
        margin-left: 1.5rem;
        -webkit-clip-path: polygon(0 0, 100% 0, 100% 100%, 8% 100%);
        clip-path: polygon(0 0, 100% 0, 100% 100%, 8% 100%);
        border-radius: 0 0.625rem 0.625rem 0;
      }

      .info {
        padding: 1rem 0 3rem 1.5rem;

        .meta {
          justify-content: flex-start;
        }
      }

      .btn {
        left: 0;
        right: auto;
        border-radius: 0 1rem;
        background-image: linear-gradient(to right, var(--color-orange) 0, var(--color-pink) 100%);
      }

      .meta {
        &.footer {
          right: 0.5rem;
          justify-content: flex-start;
        }
      }
    }

    &:nth-child(even):hover {
      .cover {
        .article-cover {
          transform: scale(1.05) rotate(-1deg);
        }
      }
    }
  }

  .cover {
    width: 50%;
    margin-right: 1.5rem;
    clip-path: polygon(0 0, 92% 0, 100% 100%, 0 100%);
    border-radius: 0.625rem 0 0 0.625rem;
    overflow: hidden;

    .article-cover {
      object-fit: cover;
      width: 100%;
      height: 100%;
      transition: all 0.2s ease-in-out 0s;
    }
  }

  .info {
    position: relative;
    width: 50%;
    padding: 1rem 1.5rem 3rem 0;
    perspective: 62.5rem;

    .meta {
      display: flex;
      justify-content: flex-end;
      margin: 0;
    }

    .article-title {
      text-overflow: ellipsis;
      overflow: hidden;
      white-space: nowrap;
      margin: 0.625rem 0;
      color: var(--primary-color);
    }

    .excerpt {
      overflow: hidden;
      font-size: 0.875em;
      max-height: 5rem;
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 3;
      text-overflow: ellipsis;
    }
  }
}

.index {
  &.wrap {
    .meta {
      font-size: 0.8125em;
      color: var(--grey-5);
      font-size: 0.8125em;
      color: var(--grey-5);

      .iconfont {
        margin-right: 0.15rem;
      }

      &.footer {
        position: absolute;
        bottom: 0.5rem;
        max-width: calc(100% - 7rem);
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        justify-content: flex-start;
      }

      .item {
        & + .item {
          margin-left: 0.625rem;
        }
      }
    }

    .btn {
      position: absolute;
      bottom: 0;
      right: 0;
      padding: 0.3rem 1rem;
      border-radius: 1rem 0;
      color: var(--grey-0);
      background-image: linear-gradient(to right, var(--color-pink) 0, var(--color-orange) 100%);

      &:hover {
        transform: translateZ(2.5rem);
      }
    }
  }
}

@media (max-width: 767px) {
  .segments {
    & > .item {
      flex-direction: column;
      height: fit-content;
      max-height: fit-content;
      width: calc(100% - 1rem) !important;
      min-width: calc(100% - 1rem) !important;
      margin: 1rem 0.5rem !important;
    }

    .cover {
      width: 100%;
      height: 14rem;
      margin: auto;
      clip-path: polygon(0 0, 100% 0, 100% 92%, 0 100%);
      border-radius: 0.625rem 0.625rem 0 0;
    }

    .info {
      width: 100%;
      height: 14rem;
      padding: 0 1rem 3rem;
    }
  }
  .segments {
    & > .item {
      &:nth-child(even) {
        flex-direction: column;

        .cover {
          width: 100%;
          margin: auto;
          clip-path: polygon(0 0, 100% 0, 100% 100%, 0 92%);
          border-radius: 0.625rem 0.625rem 0 0;
        }

        .info {
          padding: 0 1rem 3rem;
        }
      }
    }
  }
}
</style>
