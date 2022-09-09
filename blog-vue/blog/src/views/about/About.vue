<template>
  <div class="wrap">
    <div class="about">
      <div class="avatar-box">
        <img class="author-avatar" :src="avatar" />
      </div>
      <div class="about-content md" v-html="aboutContent" v-viewer></div>
    </div>
  </div>
</template>

<script>
import Clipboard from "clipboard";
import markdownToHtml from "@/utils/markdown";
import { getAbout } from "@/api/about";
export default {
  name: "About",
  created() {
    this.getAboutContent();
  },

  data() {
    return {
      aboutContent: "",
      clipboard: null,
    };
  },
  methods: {
    getAboutContent() {
      getAbout().then(({ data }) => {
        this.aboutContent = markdownToHtml(data.data);
        this.$nextTick(() => {
          // 添加代码复制功能
          this.clipboard = new Clipboard(".copy-btn");
          this.clipboard.on("success", () => {
            this.$toast({ type: "success", message: "复制成功" });
          });
        });
      });
    },
  },
  computed: {
    avatar() {
      return this.$store.state.blogInfo.websiteDTO.authorAvatar;
    },
  },
  destroyed() {
    this.clipboard.destroy();
  },
};
</script>

<style scoped>
.about {
  padding: 2rem;
}
.about-content {
  margin-top: 2rem;
  word-break: break-word;
  line-height: 1.8;
}
.avatar-box {
  text-align: center;
}
.author-avatar {
  width: 110px;
  height: 110px;
  border-radius: 50%;
  transition: all 0.5s;
}
.author-avatar:hover {
  transform: rotate(360deg);
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
    margin: 0 10px !important;
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
    top: 12px;
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
</style>