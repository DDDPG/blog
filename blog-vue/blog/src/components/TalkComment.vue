<template>
  <div id="comments">
    <!-- 评论头部 -->
    <div class="comment-title">
      <i class="iconfont icon-pinglun" style="margin-right: 5px"></i>评论
    </div>
    <!-- 评论框 -->
    <div class="comment-input-wrap" :class="boxActive ? 'box-active' : ''">
      <div class="comment-normal">
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
            placeholder="发一条友善的评论"
            @input="inputActive"
            @focus="inputFocus"
          ></textarea>
        </div>
        <!-- 发布按钮 -->
        <div class="reply-box-send" :class="sendActive ? 'send-active' : ''" @click="addComment">
          <div class="send-text">发布</div>
        </div>
      </div>
      <div class="comment-expand" v-show="boxActive">
        <Emoji
          ref="emojiBtn"
          :chooseEmoji="chooseEmoji"
          @handleEmoji="handleEmoji"
          @addEmoji="addEmoji"
        ></Emoji>
      </div>
    </div>
    <!-- 评论列表 -->
    <div class="comment-list" v-if="count > 0 && reFresh">
      <!-- 评论 -->
      <div class="reply-item" v-for="(item, index) of commentList" :key="item.id">
        <!-- 父评论 -->
        <div class="root-reply-container">
          <!-- 父评论头像 -->
          <div class="root-reply-avatar">
            <img class="reply-avatar" style="width: 40px; height: 40px" :src="item.avatar" />
          </div>
          <!-- 父评论内容 -->
          <div class="content-wrap">
            <div class="user-info">
              <!-- 用户昵称 -->
              <div class="user-name">{{ item.fromNickname }}</div>
              <!-- 博主标识 -->
              <div class="blogger-icon" v-if="item.fromUid == 1">博主</div>
            </div>
            <div class="root-reply">
              <span class="reply-content" v-html="item.commentContent"></span>
              <div class="reply-info">
                <!-- 评论时间 -->
                <span class="reply-time">{{ item.createTime }}</span>
                <!-- 点赞量 -->
                <span class="reply-like" @click="like(item)">
                  <i
                    class="iconfont icon-dianzan"
                    :class="isLike(item.id)"
                    style="margin-right: 5px"
                  ></i>
                  <span v-show="item.likeCount > 0">{{ item.likeCount }}</span>
                </span>
                <span class="reply-btn" @click="replyComment(index, item)">回复</span>
              </div>
            </div>
          </div>
        </div>
        <!-- 子评论列表 -->
        <div class="sub-reply-container">
          <div class="sub-reply-item" v-for="reply of item.replyVOList" :key="reply.id">
            <div class="sub-user-info">
              <!-- 头像 -->
              <div class="sub-reply-avatar">
                <img class="reply-avatar" style="width: 24px; height: 24px" :src="reply.avatar" />
              </div>
              <!-- 昵称 -->
              <div class="sub-user-name">{{ reply.fromNickname }}</div>
              <div class="blogger-icon" v-if="reply.fromUid == 1" style="margin-left: 5px">
                博主
              </div>
            </div>
            <span class="reply-content">
              <template v-if="reply.fromUid !== reply.toUid"
                >回复 <span style="color: #008ac5">@{{ reply.toNickname }}</span> :</template
              >
              <span v-html="reply.commentContent"></span>
            </span>
            <div class="reply-info">
              <span class="reply-time">{{ reply.createTime }}</span>
              <span class="reply-like" @click="like(reply)">
                <i
                  class="iconfont icon-dianzan"
                  :class="isLike(reply.id)"
                  style="margin-right: 5px"
                ></i>
                <span v-show="reply.likeCount > 0">{{ reply.likeCount }}</span>
              </span>
              <span class="reply-btn" @click="replyComment(index, reply)">回复</span>
            </div>
          </div>
          <div class="view-more" v-show="item.replyCount > 3" ref="readMore">
            <span>共{{ item.replyCount }}条回复, </span>
            <span class="view-more-btn" @click="getReplies(index, item)">点击查看</span>
          </div>
          <div ref="page" style="display: none">
            <MinPagination
              ref="paging"
              :total="item.replyCount"
              :index="index"
              :commentId="item.id"
              @getCurrentPage="getCurrentPage"
            ></MinPagination>
          </div>
        </div>
        <Reply ref="reply" :commentType="commentType" @reloadReply="reloadReply"></Reply>
      </div>
      <!-- 加载按钮 -->
      <div class="load-wrapper">
        <v-btn outlined v-if="count > commentList.length" @click="getCommentList">
          加载更多...
        </v-btn>
      </div>
    </div>
    <div v-else style="padding: 1.25rem; text-align: center">来发评论吧~</div>
  </div>
</template>

<script>
import Emoji from "./Emoji.vue";
import Reply from "./Reply.vue";
import MinPagination from "../components/MinPagination.vue";
import { getComments, getReplies, addComment, likeComment } from "@/api/comment";
import huanglian from "../assets/js/emoji";
export default {
  name: "TalkComment",
  components: { Emoji, Reply, MinPagination },
  props: {
    commentType: {
      type: Number,
    },
  },
  created() {
    this.getCommentList();
  },
  data() {
    return {
      viewMore: true,
      boxActive: false,
      sendActive: false,
      chooseEmoji: false,
      reFresh: true,
      lineStyle: {
        lineHeight: "normal",
        borderColor: "#ed6ea0",
        backgroundColor: "var(--grey-0)",
      },
      commentList: [],
      commentContent: "",
      currentPage: 1,
      total: 0,
      count: 0,
    };
  },
  mounted() {
    // 全局点击事件
    document.addEventListener("mouseup", this.handleMouseUp, false);
  },
  methods: {
    like(comment) {
      // 判断登录
      if (!this.$store.state.userId) {
        this.$store.state.loginFlag = true;
        return false;
      }
      likeComment(comment.id).then(({ data }) => {
        if (data.flag) {
          // 判断是否点赞
          if (this.$store.state.commentLikeSet.indexOf(comment.id) != -1) {
            this.$set(comment, "likeCount", comment.likeCount - 1);
          } else {
            this.$set(comment, "likeCount", comment.likeCount + 1);
          }
          this.$store.commit("commentLike", comment.id);
        }
      });
    },
    reloadReply(index) {
      getReplies(this.commentList[index].id, {
        current: this.$refs.paging[index].current,
        size: 5,
      }).then(({ data }) => {
        this.commentList[index].replyCount++;
        // 回复大于5条展示分页
        if (this.commentList[index].replyCount > 5) {
          this.$refs.page[index].style.display = "flex";
        }
        this.$refs.readMore[index].style.display = "none";
        this.$refs.reply[index].$el.style.display = "none";
        this.commentList[index].replyVOList = data.data;
      });
    },
    addComment() {
      //判断登录
      if (!this.$store.state.userId) {
        this.$store.state.loginFlag = true;
        return false;
      }
      //判空
      if (this.commentContent.trim() == "") {
        this.$toast({ type: "error", message: "评论不能为空" });
        return false;
      }
      //解析表情
      var reg = /\[.+?\]/g;
      this.commentContent = this.commentContent.replace(reg, function (str) {
        return (
          "<img alt='' src= '" +
          huanglian[str] +
          "' width='24' height='24' style='margin: 0 1px;vertical-align: text-bottom'>"
        );
      });
      //发送请求
      const path = this.$route.path;
      const arr = path.split("/");
      var comment = {
        commentContent: this.commentContent,
        commentType: this.commentType,
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
          this.currentPage = 1;
          this.getCommentList();
          this.sendActive = false;
          const isCheck = this.$store.state.blogInfo.websiteDTO.isCommentCheck;
          if (isCheck) {
            this.$toast({ type: "warnning", message: "评论成功，正在审核中" });
          } else {
            this.$toast({ type: "success", message: "评论成功" });
          }
        }
      });
    },
    getCommentList() {
      //查看评论
      const path = this.$route.path;
      const arr = path.split("/");
      var params = {
        current: this.currentPage,
        commentType: this.commentType,
      };
      switch (this.commentType) {
        case 1:
        case 3:
          params.typeId = arr[2];
          break;
        default:
          break;
      }
      getComments(params).then(({ data }) => {
        if (this.currentPage == 1) {
          this.commentList = data.data.recordList;
        } else {
          this.commentList.push(...data.data.recordList);
        }
        this.currentPage++;
        this.count = data.data.count;
      });
    },
    replyComment(index, item) {
      this.$refs.reply.forEach((item) => {
        item.$el.style.display = "none";
      });
      //传值给回复框
      this.$refs.reply[index].commentContent = "";
      this.$refs.reply[index].nickname = item.fromNickname;
      this.$refs.reply[index].replyId = item.id;
      this.$refs.reply[index].toUid = item.fromUid;
      this.$refs.reply[index].parentId = this.commentList[index].id;
      this.$refs.reply[index].index = index;
      this.$refs.reply[index].$el.style.display = "block";
    },
    getReplies(index, item) {
      getReplies(item.id, { current: 1, size: 5 }).then(({ data }) => {
        this.$refs.readMore[index].style.display = "none";
        item.replyVOList = data.data;
        if (Math.ceil(item.replyCount / 5) > 1) {
          this.$refs.page[index].style.display = "flex";
        }
      });
    },
    handleEmoji(value) {
      this.chooseEmoji = value;
    },
    addEmoji(key) {
      this.commentContent += key;
      this.sendActive = true;
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
    inputFocus() {
      this.boxActive = true;
    },
    getCurrentPage(current, index, commentId) {
      getReplies(commentId, { current: current, size: 5 }).then(({ data }) => {
        this.commentList[index].replyVOList = data.data;
      });
      this.$refs.paging[index].current = current;
    },
  },
  computed: {
    isLike() {
      return function (commentId) {
        var commentLikeSet = this.$store.state.commentLikeSet;
        return commentLikeSet.indexOf(commentId) != -1 ? "like" : "";
      };
    },
  },
  watch: {
    commentList() {
      this.reFresh = false;
      this.$nextTick(() => {
        this.reFresh = true;
      });
    },
  },
  destroyed() {
    document.removeEventListener("mouseup", this.handleMouseUp);
  },
};
</script>

<style lang="scss" scoped>
.blogger-icon {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 12px;
  width: 30px;
  height: 15px;
  border-radius: 4px;
  color: #fff;
  background-color: #f66999;
}
.comment-title {
  display: flex;
  align-items: center;
  margin: 22px 0 22px 0;
  padding-left: 10px;
  font-size: 20px;
  font-weight: bold;
  line-height: 40px;
  color: rgb(87 86 86);
}
.comment-input-wrap {
  display: flex;
  flex-direction: column;
}
.comment-input-wrap.box-active .comment-normal {
  height: 65px;
}
.comment-normal {
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
.comment-expand {
  display: flex;
  margin-left: 80px;
  margin-top: 5px;
}
.reply-box-textarea {
  width: 100%;
  height: 100%;
  padding: 5px 10px;
  border: 1px solid #f1f2f3;
  border-radius: 6px;
  background-color: var(--comment-color);
  font-size: 12px;
  line-height: 38px;
  color: var(--text-color);
  resize: none;
  outline: none;
}
.reply-box-textarea:hover {
  background-color: var(--grey-0);
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
.send-text {
  position: absolute;
  z-index: 1;
  font-size: 16px;
  color: #ffffff;
}
.comment-list {
  padding-bottom: 20px;
}
.reply-item {
  position: relative;
  margin-top: 20px;
  font-weight: 400;
  border-radius: 0.5rem;
  box-shadow: 0 0.625rem 1.875rem -0.9375rem var(--box-bg-shadow);
  transition: all 0.2s ease-in-out 0s;
  .bottom-line {
    margin-left: 80px;
    border-bottom: 1px solid #e3e5e7;
    margin-top: 14px;
  }
  &:hover {
    box-shadow: 0 0 2rem var(--box-bg-shadow);
  }
}

.reply-content {
  overflow: hidden;
  word-wrap: break-word;
  word-break: break-word;
  white-space: pre-wrap;
  line-height: 24px;
  vertical-align: baseline;
}
.root-reply-container {
  padding: 22px 0 5px 80px;
  .root-reply-avatar {
    display: flex;
    justify-content: center;
    position: absolute;
    left: 0;
    width: 80px;
  }
  .content-wrap {
    flex: 1;
    position: relative;
    .user-info {
      display: flex;
      align-items: center;
      margin-bottom: 4px;
      .user-name {
        font-size: 13px;
        font-weight: 500;
        margin-right: 5px;
      }
    }
    .root-reply {
      font-size: 15px;
      line-height: 24px;
      position: relative;
      padding: 2px 0;
    }
  }
}
.sub-reply-container {
  padding-left: 72px;
}
.reply-info {
  display: flex;
  align-items: center;
  position: relative;
  margin-top: 2px;
  font-size: 13px;
  color: #9499a0;
  .reply-time {
    margin-right: 15px;
  }
  .reply-like {
    display: flex;
    align-items: center;
    margin-right: 17px;
    cursor: pointer;
    &:hover {
      .icon-good {
        color: #ed6ea0;
      }
    }
  }
  .reply-btn {
    cursor: pointer;
    &:hover {
      color: #ed6ea0;
    }
  }
}
.sub-reply-item {
  position: relative;
  padding: 8px 0 8px 42px;
  font-size: 15px;
  line-height: 24px;
  .sub-user-info {
    display: inline-flex;
    align-items: center;
    margin-right: 9px;
    line-height: 24px;
    .sub-reply-avatar {
      position: absolute;
      width: 24px;
      height: 24px;
      left: 8px;
    }
    .sub-user-name {
      font-size: 13px;
      line-height: 24px;
    }
  }
}
.sub-reply-user {
  color: #ed6ea0;
}
.view-more {
  padding-left: 8px;
  font-size: 13px;
  color: #9499a0;
  .view-more-btn {
    cursor: pointer;
    &:hover {
      color: #ed6ea0;
    }
  }
}
.like {
  color: #ed6ea0;
}
</style>