<template>
  <div class="reply-box-container" style="display: none" ref="reply">
    <div class="reply-box" :class="boxActive ? 'box-active' : ''">
      <div class="box-normal">
        <!-- 用户头像 -->
        <div class="reply-box-avatar">
          <img
            class="reply-avatar"
            v-if="this.$store.state.avatar"
            :src="this.$store.state.avatar"
          />
          <img
            class="reply-avatar"
            v-else
            :src="this.$store.state.blogInfo.websiteDTO.touristAvatar"
          />
        </div>
        <!-- 评论输入框 -->
        <div class="reply-box-warp" ref="textarea">
          <textarea
            class="reply-box-textarea"
            :style="sendActive ? lineStyle : ''"
            v-model="commentContent"
            :placeholder="'回复 @' + nickname + '：'"
            @input="inputActive"
            @focus="inputFocus"
          ></textarea>
        </div>
        <!-- 发布按钮 -->
        <div class="reply-box-send" :class="sendActive ? 'send-active' : ''" @click="replyComment">
          <div class="send-text">发布</div>
        </div>
      </div>
      <div class="box-expand" v-show="boxActive">
        <Emoji
          ref="emojiBtn"
          @addEmoji="addEmoji"
          :chooseEmoji="chooseEmoji"
          @handleEmoji="handleEmoji"
        ></Emoji>
      </div>
    </div>
  </div>
</template>

<script>
import { addComment } from "@/api/comment";
import Emoji from "./Emoji.vue";
import huanglian from "../assets/js/emoji";
export default {
  name: "Reply",
  components: { Emoji },
  props: {
    commentType: {
      type: Number,
    },
  },
  data() {
    return {
      boxActive: false,
      sendActive: false,
      chooseEmoji: false,
      lineStyle: {
        lineHeight: "normal",
        borderColor: "#ed6ea0",
        backgroundColor: "#fff",
      },
      index: 0,
      nickname: "",
      replyId: null,
      toUid: null,
      parentId: null,
      commentContent: "",
    };
  },
  mounted() {
    // 全局点击事件
    document.addEventListener("mouseup", this.handleMouseUp, false);
  },
  methods: {
    replyComment() {
      //判断登录
      if (!this.$store.state.userId) {
        this.$store.state.loginFlag = true;
        return false;
      }
      if (this.commentContent.trim() == "") {
        this.$toast({ type: "error", message: "回复不能为空" });
        return false;
      }
      //解析表情
      var reg = /\[.+?\]/g;
      this.commentContent = this.commentContent.replace(reg, function (str) {
        return (
          "<img src= '" +
          huanglian[str] +
          "' width='24'height='24' style='margin: 0 1px;vertical-align: text-bottom'>"
        );
      });
      const path = this.$route.path;
      const arr = path.split("/");
      var comment = {
        commentType: this.commentType,
        replyId: this.replyId,
        toUid: this.toUid,
        parentId: this.parentId,
        commentContent: this.commentContent,
      };
      switch (this.commentType) {
        case 1:
        case 3:
          comment.typeId = arr[2];
          break;
        default:
          break;
      }
      this.commentContent = "";
      addComment(comment).then(({ data }) => {
        if (data.flag) {
          this.$emit("reloadReply", this.index);
          this.$toast({ type: "success", message: "回复成功" });
          this.sendActive = false;
        }
      });
    },
    handleEmoji(value) {
      this.chooseEmoji = value;
    },
    handleMouseUp(e) {
      let emojiBtn = this.$refs.emojiBtn;
      let textarea = this.$refs.textarea;
      if (emojiBtn.$el) {
        if (!emojiBtn.$el.contains(e.target)) {
          this.chooseEmoji = false;
        }
      }
      if (textarea) {
        if (emojiBtn.$el.contains(e.target)) {
          this.boxActive = true;
          return;
        }
        if (!textarea.contains(e.target)) {
          this.boxActive = false;
        }
      }
    },
    inputActive() {
      if (this.commentContent.length == 0) {
        this.sendActive = false;
      } else {
        this.sendActive = true;
      }
    },
    addEmoji(key) {
      this.commentContent += key;
      this.sendActive = true;
    },
    inputFocus() {
      this.boxActive = true;
    },
  },
  destroyed() {
    document.removeEventListener("mouseup", this.handleMouseUp);
  },
};
</script>

<style scoped>
.reply-box-container {
  padding: 20px 0 10px 80px;
}
.reply-box {
  display: flex;
  flex-direction: column;
}
.reply-box.box-active .box-normal {
  height: 65px;
}
.box-normal {
  display: flex;
  height: 50px;
  transition: 0.2s;
}
.reply-box-avatar {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 80px;
  height: 50px;
}
.reply-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
}
.reply-box-warp {
  position: relative;
  flex: 1;
}
.reply-box-textarea {
  width: 100%;
  height: 100%;
  padding: 5px 10px;
  border: 1px solid #f1f2f3;
  border-radius: 6px;
  background-color: #f1f2f3;
  font-size: 12px;
  line-height: 38px;
  color: #18191c;
  resize: none;
  outline: none;
}
.reply-box-textarea:hover {
  background-color: #fff;
  border-color: #ed6ea0;
}
.reply-box-send {
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  flex-basis: 70px;
  margin-left: 10px;
  border-radius: 4px;
  background-color: #ed9abb;
  cursor: pointer;
}
.reply-box-send.send-active {
  background-color: #ed6ea0;
}
.reply-box-send:hover {
  background-color: #ed6ea0;
}
.box-expand {
  display: flex;
  margin-left: 80px;
  margin-top: 5px;
}
.send-text {
  position: absolute;
  z-index: 1;
  font-size: 16px;
  color: #ffffff;
}
@media (max-width: 767px) {
  .reply-box-container {
    padding: 25px 0 10px 0;
  }
}
</style>