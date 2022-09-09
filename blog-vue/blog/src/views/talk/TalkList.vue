<template>
  <div class="collapse wrap">
    <!-- 说说 -->
    <div class="talk-item" v-for="item of talkList" :key="item.id">
      <router-link :to="`/talks/${item.id}`">
        <!-- 用户头像 -->
        <div class="user-avatar-box">
          <img class="user-avatar" :src="item.avatar" />
        </div>
        <!-- 说说内容 -->
        <div class="talk-content-wrap">
          <!-- 用户信息 -->
          <div class="user-info">
            <!-- 用户昵称 -->
            <div class="user-name">{{ item.nickname }}</div>
          </div>
          <!-- 说说信息 -->
          <div class="talk-info">
            <!-- 评论时间 -->
            <span class="talk-time">{{ item.createTime }}</span>
            <!-- 是否置顶 -->
            <span style="color: #ec8c69" v-if="item.isTop == 1">
              <i class="iconfont icon-zhiding" style="margin-right: 3px"></i>置顶
            </span>
          </div>
          <!-- 说说内容 -->
          <span class="talk-content" v-html="item.talkContent"></span>
          <!-- 说说图片 -->
          <div class="talk-images" v-viewer v-if="item.imgList">
            <img
              @click.prevent
              class="image"
              v-for="(img, index) in item.imgList"
              :key="index"
              :src="img"
            />
          </div>
          <!-- 说说信息 -->
          <div class="talk-info" style="margin-top: 10px">
            <!-- 点赞量 -->
            <span class="talk-like">
              <i class="iconfont icon-dianzan" style="margin-right: 5px"></i>
              <span>{{ item.likeCount == null ? 0 : item.likeCount }}</span>
            </span>
            <!-- 评论量 -->
            <span class="talk-comment">
              <i class="iconfont icon-pinglun" style="margin-right: 5px"></i>
              <span>{{ item.commentCount == null ? 0 : item.commentCount }}</span>
            </span>
          </div>
        </div>
      </router-link>
    </div>
    <!-- 加载更多 -->
    <div class="load-wrapper" v-if="talkList && count > talkList.length" @click="listTalks">
      <v-btn outlined> 加载更多... </v-btn>
    </div>
  </div>
</template>

<script>
import { getTalks } from "@/api/talk";
export default {
  name: "TalkList",
  data() {
    return {
      currentPage: 1,
      pageSize: 10,
      talkList: [],
      count: 0,
      previewList: ["https://i0.hdslb.com/bfs/album/18c34c89bb4b79674594af68c21bdfdf0a2b7dcb.jpg"],
    };
  },
  created() {
    this.listTalks();
  },

  methods: {
    listTalks() {
      getTalks(this.currentPage, this.pageSize).then(({ data }) => {
        if (this.currentPage == 1) {
          this.talkList = data.data.recordList;
        } else {
          this.talkList.push(...data.data.recordList);
        }
        this.talkList.forEach((item) => {
          if (item.imgList) {
            this.previewList.push(...item.imgList);
          }
        });
        this.currentPage++;
        this.count = data.data.count;
      });
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
  .talk-like,
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

</style>
