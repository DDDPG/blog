<template>
  <div class="reply-box-emoji">
    <!-- 表情按钮 -->
    <div class="emoji-btn" @click="emojiBtn">
      <i class="iconfont icon-biaoqing" style="margin-right: 5px"></i>
      <span>表情</span>
    </div>
    <div class="emoji-panel" v-show="chooseEmoji">
      <!-- 表情内容 -->
      <div class="emoji-content">
        <div
          class="emoji-info"
          v-for="(value, key, index) of emojiList"
          :key="index"
          @click="addEmoji(key, value)"
        >
          <img class="normal-small-emoji" :title="key" :src="value" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import huangList from "../assets/js/emoji";
export default {
  name: "Emoji",
  data() {
    return {
      emojiList: huangList,
    };
  },
  props: {
    chooseEmoji: {
      type: Boolean,
    },
  },
  methods: {
    addEmoji(key) {
      this.$emit("addEmoji", key);
    },
    emojiBtn() {
      this.$emit("handleEmoji", true);
    },
  },
};
</script>

<style lang="scss" scoped>
.reply-box-emoji {
  width: 67px;
  height: 24px;
  position: relative;
}
.emoji-btn {
  display: flex;
  justify-content: center;
  font-size: 12px;
  align-items: center;
  width: 100%;
  height: 100%;
  border: 1px solid #f1f2f3;
  border-radius: 4px;
  color: #9499a0;
  cursor: pointer;
}
.emoji-panel {
  position: absolute;
  top: 30px;
  width: 250px;
  border: 1px solid #e3e5e7;
  border-radius: 5px;
  font-size: 12px;
  color: #61666d;
  z-index: 8;
  .emoji-content {
    height: 196px;
    padding: 7px 7px 0 7px;
    background-color: var(--grey-1);
    overflow: auto;
    .emoji-info {
      display: inline-block;
      line-height: 8px;
      border-radius: 4px;
      cursor: pointer;
      .normal-small-emoji {
        width: 24px;
        height: 24px;
        margin: 4px;
      }
      &:hover {
        background-color: #e3e5e7;
      }
    }
  }
}
</style>
