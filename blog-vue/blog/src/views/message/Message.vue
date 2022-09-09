<template>
  <div>
    <!-- 弹幕输入框 -->
    <div class="message-container">
      <h1 class="message-title">留言板</h1>
      <div class="animated fadeInUp message-input-wrapper">
        <input
          v-model="messageContent"
          @click="show = true"
          @keyup.enter="send"
          placeholder="说点什么吧"
        />
        <button class="animated bounceInLeft" @click="send" v-show="show">发送</button>
      </div>
    </div>
    <!-- 弹幕列表 -->
    <div class="barrage-container">
      <vue-danmaku ref="danmaku" class="message" use-slot :danmus="danmus" :speeds="250">
        <!-- 弹幕slot -->
        <template v-slot:dm="{ danmu }">
          <span class="barrage-items">
            <img :src="danmu.avatar" width="30" height="30" style="border-radius: 50%" />
            <span class="ml-2">{{ danmu.nickname }} :</span>
            <span class="ml-2">{{ danmu.messageContent }}</span>
          </span>
        </template>
      </vue-danmaku>
    </div>
    <slot name="sider"></slot>
    <slot name="dimmer"></slot>
  </div>
</template>

<script>
import { addMessage, getMessage } from "@/api/message";
import vueDanmaku from "vue-danmaku";
export default {
  name: "Message",
  components: {
    vueDanmaku,
  },
  created() {
    this.getMessageList();
  },

  data() {
    return {
      show: false,
      messageContent: "",
      danmus: [],
    };
  },
  methods: {
    getMessageList() {
      getMessage().then(({ data }) => {
        if (data.flag) {
          this.danmus = data.data;
        }
      });
    },
    send() {
      if (this.messageContent.trim() == "") {
        this.$toast({ type: "error", message: "留言不能为空" });
        return false;
      }
      const userAvatar = this.$store.state.avatar
        ? this.$store.state.avatar
        : this.$store.state.blogInfo.websiteDTO.touristAvatar;
      const userNickname = this.$store.state.nickname ? this.$store.state.nickname : "游客";
      var message = {
        avatar: userAvatar,
        nickname: userNickname,
        messageContent: this.messageContent,
      };
      this.messageContent = "";
      addMessage(message).then(({ data }) => {
        if (data.flag) {
          this.danmus.push(message);
          this.$toast({ type: "success", message: "留言成功" });
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.barrage-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  width: 100%;
  background: url("https://static.ttkwsd.top/config/e3408389cb0d4ea1b5f651873dab2a19.jpg") center
    no-repeat;
}
.message {
  position: fixed;
  top: 50px;
  width: 100%;
  height: 100%;
}
.message-title {
  color: #eee;
  animation: title-scale 1s;
}
.message-container {
  position: fixed;
  width: 360px;
  top: 35%;
  left: 0;
  right: 0;
  text-align: center;
  z-index: 5;
  margin: 0 auto;
  color: #fff;
}
.message-input-wrapper {
  display: flex;
  justify-content: center;
  height: 2.5rem;
  margin-top: 2rem;
}
.message-input-wrapper input {
  outline: none;
  width: 70%;
  border-radius: 20px;
  height: 100%;
  padding: 0 1.25rem;
  color: #eee;
  border: #fff 1px solid;
}
.message-input-wrapper input::-webkit-input-placeholder {
  color: #eeee;
}
.message-input-wrapper button {
  outline: none;
  border-radius: 20px;
  height: 100%;
  padding: 0 1.25rem;
  border: #fff 1px solid;
}
.barrage-items {
  display: flex;
  align-items: center;
  padding: 0 10px 0 5px;
  background: rgb(0, 0, 0, 0.7);
  border-radius: 100px;
  color: #fff;
}
.ml-2 {
  margin-left: 8px;
}
</style>