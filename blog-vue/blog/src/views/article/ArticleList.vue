<template>
  <div class="article-list wrap">
    <div class="article-item" v-for="item of articleList" :key="item.id">
      <!-- 文章缩略图 -->
      <div class="article-cover">
        <router-link :to="`/articles/${item.id}`"
          ><img class="cover" :src="item.articleCover"
        /></router-link>
      </div>
      <div class="card">
        <div class="article-info">
          <!-- 文章标题 -->
          <div class="title">
            <router-link :to="`/articles/${item.id}`">{{ item.articleTitle }}</router-link>
          </div>
          <div class="meta">
            <div class="info">
              <!-- 发表时间 -->
              <span class="info-item">{{ item.createTime | date }}</span>
              <!-- 浏览量 -->
              <span class="info-item"
                ><i class="iconfont icon-yanjing" style="font-size: 15px; margin-right: 2px"></i>
                {{ item.viewCount }}</span
              >
              <!-- 评论量 -->
              <span class="info-item"
                ><i class="iconfont icon-pinglun" style="margin-right: 2px"></i>
                {{ item.commentCount }}</span
              >
              <!-- 点赞量 -->
              <span class="info-item"
                ><i class="iconfont icon-dianzan" style="font-size: 13px; margin-right: 2px"></i>
                {{ item.likeCount == null ? 0 : item.likeCount }}</span
              >
            </div>
            <!-- 分类 -->
            <div class="category">
              <router-link class="category-item" :to="`/categories/${item.category.id}`"
                ><i class="iconfont icon-message_5" style="font-size: 13px; margin-right: 3px"></i>
                {{ item.category.categoryName }}</router-link
              >
            </div>
          </div>
        </div>
        <!-- 标签 -->
        <div class="article-tags">
          <router-link
            class="tag"
            v-for="tag of item.tagVOList"
            :to="`/tags/${tag.id}`"
            :key="tag.id"
            ><i class="iconfont icon-tags"></i>{{ tag.tagName }}</router-link
          >
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getArticleCondition } from "@/api/article";
export default {
  name: "ArticleList",
  data() {
    return {
      currentPage: 1,
      articleList: [],
      name: null,
    };
  },
  created() {
    this.getArticleList();
  },

  methods: {
    getArticleList() {
      getArticleCondition(
        this.$route.params.categoryId,
        this.$route.params.tagId,
        this.currentPage
      ).then(({ data }) => {
        if (data.flag) {
          this.articleList = data.data.articleConditionVOList;
          this.$bus.$emit("getName", data.data.name);
        }
      });
    },
  },
  mounted() {
    this.$bus.$emit("getName", this.name);
  },
};
</script>

<style lang="scss" scoped>
.article-list {
  padding: 2rem;
}
.card {
  padding: 0.6rem 1rem 1rem;
}
.category-item,
.info-item {
  display: flex;
  align-items: center;
}
.article-item:hover .cover {
  transform: scale(1.1);
  filter: brightness(0.9);
}
.article-item {
  position: relative;
  display: flex;
  flex-direction: column;
  height: fit-content;
  max-height: fit-content;
  margin: 1rem;
  border-radius: 0.5rem;
  box-shadow: 0 0.625rem 1.875rem -0.9375rem var(--box-bg-shadow);
  transition: all 0.2s ease-in-out 0s;
  .article-cover {
    width: 100%;
    height: 12rem;
    margin: auto;
    overflow: hidden;
    border-radius: 0.625rem 0.625rem 0 0;
    .cover {
      object-fit: cover;
      width: 100%;
      height: 100%;
      transition: all 0.5s;
    }
  }
  .article-info {
    display: flex;
    flex-direction: column;
    .title {
      font-size: 23px;
      font-weight: 400;
      line-height: 1.25;
      &:hover {
        color: var(--color-blue);
      }
    }
    .meta {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-top: 0.625rem;
      font-size: 14px;
      color: var(--grey-5);
    }
    .info {
      display: flex;
      justify-content: flex-start;
      align-items: center;
    }
    .category {
      .category-item:hover {
        color: var(--color-blue);
      }
    }
  }
  &:hover {
    box-shadow: 0 0 2rem var(--box-bg-shadow);
  }
}
.article-tags {
  text-align: left;
  margin-top: 0.625rem;
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
.info-item:not(:first-child)::before {
  content: "/";
  padding: 0 5px;
}
@media (max-width: 767px) {
  .article-item {
    width: calc(100% - 1rem) !important;
    min-width: calc(100% - 1rem) !important;
    margin: 1rem 0.5rem !important;
  }
}
</style>
