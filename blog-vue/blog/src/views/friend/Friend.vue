<template>
  <div class="friend wrap">
    <div class="inner">
      <h1><i class="iconfont icon-yinghua flower"></i> 本站信息</h1>
      <blockquote>
        <div>名称：{{ blogInfo.websiteDTO.websiteName }}</div>
        <div>简介：{{ blogInfo.websiteDTO.websiteIntro }}</div>
        <div>头像：{{ blogInfo.websiteDTO.authorAvatar }}</div>
      </blockquote>
      <h1><i class="iconfont icon-yinghua flower"></i> 申请方法</h1>
      <ul class="welcome">
        <li>需要交换友链的可在本页留言 (｡･∀･)ﾉﾞ嗨</li>
      </ul>
      <blockquote>
        <div>
          出于信息需要,大佬你的信息格式要包含：网站名称、网站链接、头像链接、网站介绍、方块颜色
        </div>
      </blockquote>
      <h1><i class="iconfont icon-yinghua flower"></i> 小伙伴们</h1>
      <div class="links">
        <div
          class="item"
          v-for="(item, index) in friendList"
          :key="index"
          @mouseenter="mouseenter(item)"
          @mouseleave="mouseleave(item)"
          :style="item.mouseFlag ? active : ''"
        >
          <a :href="item.url" target="_blank">
            <img class="image" :src="item.avatar" />
          </a>
          <div class="info">
            <a
              :href="item.url"
              target="_blank"
              :style="{
                color: item.mouseFlag ? '#fff' : item.color,
                margin: '0.5rem 0',
                fontWeight: 700,
              }"
              >{{ item.name }}</a
            >
            <p class="desc">{{ item.introduction }}</p>
          </div>
        </div>
      </div>
      <Comment :commentType="commentType"></Comment>
    </div>
  </div>
</template>

<script>
POWERMODE.colorful = true;
POWERMODE.shake = false;
document.body.addEventListener("input", POWERMODE);
</script>
<script>
import { getFriends } from "@/api/friend";
import Comment from "../../components/Comment";
export default {
  name: "Friend",
  components: {
    Comment,
  },
  data() {
    return {
      friendList: [],
      active: {},
      commentType: 2,
    };
  },
  created() {
    this.getFriends();
  },
  methods: {
    getFriends() {
      getFriends().then(({ data }) => {
        this.friendList = data.data;
      });
    },
    mouseenter(item) {
      this.$set(item, "mouseFlag", true);
      this.active = {
        backgroundColor: item.color,
        color: "#fff",
        borderColor: item.color,
        boxShadow: `0 0.125rem 1.25rem ${item.color}`,
      };
    },
    mouseleave(item) {
      this.$set(item, "mouseFlag", false);
    },
  },
  computed: {
    blogInfo() {
      return this.$store.state.blogInfo;
    },
  },
};
</script>

<style scoped>
.inner .welcome {
  padding: 0 2.5rem;
}
h1 {
  font-size: 1.5rem;
}
.flower {
  font-size: 0.875em;
  margin-top: 0.15rem;
  color: pink;
  left: -1.875rem;
  -webkit-animation: rotate 6s linear infinite;
  animation: rotate 6s linear infinite;
}
blockquote {
  line-height: 2;
  margin: 0 1.5rem;
  font-size: 15px;
  border-left: 0.2rem solid #cf8fdc;
  padding: 10px 1rem !important;
  color: #666;
  background: #fff1fb;
  border-radius: 4px;
  word-wrap: break-word;
}
.welcome li {
  position: relative;
  margin: 0.5em 0 0.5em;
  padding: 0.1em 0.2em 0.3em 0;
}
.welcome > li::before {
  content: "";
  position: absolute;
  width: 0.4em;
  height: 0.4em;
  background: var(--primary-color);
  border-radius: 50%;
  top: 0.85em;
  left: -1em;
}
.links {
  display: flex;
  flex-wrap: wrap;
  font-size: 0.9rem;
}
.links .item {
  position: relative;
  display: inline-flex;
  justify-self: center;
  align-items: center;
  line-height: 1.5;
  width: calc(50% - 2rem);
  padding: 0.5rem 1rem;
  margin: 1rem;
  transition: all 0.2s ease-in-out 0s;
  border-radius: 0.5rem;
  border: 0.0625rem solid var(--grey-2);
  box-shadow: 0 0.625rem 1.875rem -0.9375rem var(--box-bg-shadow);
}
.links .item .image {
  display: block;
  width: 4rem;
  height: 4rem;
  border-radius: 0.9375rem;
  background: var(--grey-1) center no-repeat;
  background-size: contain;
  flex-shrink: 0;
  border: none;
}
.links .item .info {
  padding-left: 1rem;
}
.links .item .desc {
  font-size: 0.75em;
  margin: 0.5rem 0;
}
@media (max-width: 767px) {
  .links .item {
    width: calc(100% - 2rem);
  }
}
</style>