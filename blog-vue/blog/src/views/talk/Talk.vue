<template>
  <div class="collapse wrap">
    <!-- 说说 -->
    <div class="talk-item">
      <!-- 用户头像 -->
      <div class="user-avatar-box">
        <img class="user-avatar" :src="talk.avatar" />
      </div>
      <!-- 说说内容 -->
      <div class="talk-content-wrap">
        <!-- 用户信息 -->
        <div class="user-info">
          <!-- 用户昵称 -->
          <div class="user-name">{{ talk.nickname }}</div>
        </div>
        <!-- 说说信息 -->
        <div class="talk-info">
          <!-- 评论时间 -->
          <span class="talk-time">{{ talk.createTime }}</span>
          <!-- 是否置顶 -->
          <span style="color: #ec8c69" v-if="talk.isTop == 1">
            <i class="iconfont icon-zhiding" style="margin-right: 3px"></i>置顶
          </span>
        </div>
        <!-- 说说内容 -->
        <span class="talk-content" v-html="talk.talkContent"></span>
        <!-- 说说图片 -->
        <div class="talk-images" v-viewer v-if="talk.imgList">
          <img
            @click.prevent
            class="image"
            v-for="(img, index) in talk.imgList"
            :key="index"
            :src="img"
          />
        </div>
        <!-- 说说信息 -->
        <div class="talk-info" style="margin-top: 10px">
          <!-- 点赞量 -->
          <span class="talk-like" @click.prevent="like(talk)">
            <i class="iconfont icon-dianzan" :class="isLike(talk.id)" style="margin-right: 5px"></i>
            <span>{{ talk.likeCount }}</span>
          </span>
          <!-- 评论量 -->
          <span class="talk-comment">
            <i class="iconfont icon-pinglun" style="margin-right: 5px"></i>
            <span>{{ commentCount }}</span>
          </span>
        </div>
      </div>
    </div>
    <TalkComment :commentType="commentType"></TalkComment>
  </div>
</template>

<script>
import { getTalk, likeTalk } from "@/api/talk";
import { getCommentCount } from "@/api/comment";
import TalkComment from "../../components/TalkComment.vue";
export default {
  name: "Talk",
  components: {
    TalkComment,
  },
  created() {
    this.getCommentCount();
    this.getTalkById();
  },
  data() {
    return {
      talk: {},
      commentCount: 0,
      commentType: 3,
    };
  },
  methods: {
    getCommentCount() {
      const path = this.$route.path;
      const arr = path.split("/");
      var params = {
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
      getCommentCount(params).then(({ data }) => {
        this.commentCount = data.data;
      });
    },
    getTalkById() {
      getTalk(this.$route.params.talkId).then(({ data }) => {
        this.talk = data.data;
      });
    },
    like(talk) {
      // 判断登录
      if (!this.$store.state.userId) {
        this.$store.state.loginFlag = true;
        return false;
      }
      likeTalk(talk.id).then(({ data }) => {
        if (data.flag) {
          // 判断是否点赞
          if (this.$store.state.talkLikeSet.indexOf(talk.id) != -1) {
            this.$set(talk, "likeCount", talk.likeCount - 1);
          } else {
            this.$set(talk, "likeCount", talk.likeCount + 1);
          }
          this.$store.commit("talkLike", talk.id);
        }
      });
    },
  },
  computed: {
    isLike() {
      return function (talkId) {
        var talkLikeSet = this.$store.state.talkLikeSet;
        return talkLikeSet.indexOf(talkId) != -1 ? "like" : "";
      };
    },
  },
};
</script>

<style lang="scss" scoped>
.talk-item {
  position: relative;
  padding: 15px 10px 15px 70px;
  border-radius: 0.5rem;
  box-shadow: 0 0.625rem 1.875rem -0.9375rem var(--box-bg-shadow);
  transition: all 0.2s ease-in-out 0s;
  .user-avatar-box {
    display: flex;
    justify-content: center;
    position: absolute;
    left: 0;
    width: 70px;
  }
  .talk-content-wrap {
    flex: 1;
    position: relative;
    .user-info {
      display: flex;
      align-items: center;
      .user-name {
        font-size: 14px;
        font-weight: 600;
        margin-right: 5px;
      }
    }
  }
  &:hover {
    box-shadow: 0 0 2rem var(--box-bg-shadow);
  }
  &:not(:first-child) {
    margin-top: 20px;
  }
}
.talk-info {
  display: flex;
  align-items: center;
  position: relative;
  font-size: 13px;
  color: #9499a0;
  .talk-time {
    margin-right: 15px;
  }
  .talk-like {
    cursor: pointer;
    display: flex;
    align-items: center;
    margin-right: 20px;
  }
  .talk-comment {
    display: flex;
    align-items: center;
    margin-right: 20px;
  }
}
.user-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
}
.talk-content {
  overflow: hidden;
  word-wrap: break-word;
  word-break: break-word;
  white-space: pre-wrap;
  font-size: 15px;
  line-height: 24px;
  vertical-align: baseline;
}
.talk-images {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  align-content: flex-start;
  flex-wrap: wrap;
  margin-top: 5px;
  .image {
    cursor: pointer;
    object-fit: cover;
    max-width: 250px;
    max-height: 200px;
    border-radius: 4px;
    padding: 2px;
  }
}
.like {
  color: #ed6ea0;
}
</style>