<template>
  <router-link to="/talks" class="swiper-container">
    <v-icon size="20" color="#4c4948">mdi-chat-outline</v-icon>
    <div
      :style="{ height: height * lineNum + 'px' }"
      class="rollScreen_container"
      id="rollScreen_container"
    >
      <ul
        class="rollScreen_list"
        :style="{ transform: transform }"
        :class="{ rollScreen_list_unanim: num === 0 }"
      >
        <li
          class="rollScreen_once"
          v-for="(item, index) in contentArr"
          :key="index"
          :style="{ height: height + 'px' }"
        >
          <span class="item" v-html="item"></span>
        </li>
        <li
          class="rollScreen_once"
          v-for="(item, index) in contentArr"
          :key="index + contentArr.length"
          :style="{ height: height + 'px' }"
        >
          <span class="item" v-html="item"></span>
        </li>
      </ul>
    </div>
    <v-icon size="20" color="#4c4948" class="arrow"> mdi-chevron-double-right </v-icon>
  </router-link>
</template>

<script>
export default {
  name: "Swiper",
  props: {
    height: {
      default: 25,
      type: Number,
    },
    lineNum: {
      default: 1,
      type: Number,
    },
    contentArr: {
      type: Array
    }
  },
  data() {
    return {
      num: 0,
    };
  },
  computed: {
    transform: function () {
      return "translateY(-" + this.num * this.height + "px)";
    },
  },
  created: function () {
    let _this = this;
    setInterval(function () {
      if (_this.num !== _this.contentArr.length) {
        _this.num++;
      } else {
        _this.num = 0;
      }
    }, 3000);
  },
};
</script>

<style scoped>
.swiper-container {
  margin: 5px 15px 0 15px;
  padding: 0.6rem 1rem;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 15px;
  border-radius: 0.5rem;
  box-shadow: 0 0.625rem 1.875rem -0.9375rem var(--box-bg-shadow);
  transition: all 0.2s ease-in-out 0s;
}
.swiper-container:hover {
  box-shadow: 0 0 2rem var(--box-bg-shadow);
}
.rollScreen_container {
  width: 100%;
  line-height: 25px;
  text-align: center;
  display: inline-block;
  position: relative;
  overflow: hidden;
}
.rollScreen_list {
  transition: 1s linear;
}
.rollScreen_list_unanim {
  transition: none;
}
.item {
  width: 100%;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  transition: all 0.3s;
}
.rollScreen_list:hover .item {
  color: #8e8cd8;
}
.arrow {
  animation: 1.5s passing infinite;
}
@keyframes passing {
  0% {
    transform: translateX(-50%);
    opacity: 0;
  }
  50% {
    transform: translateX(0);
    opacity: 1;
  }
  100% {
    transform: translateX(50%);
    opacity: 0;
  }
}
</style>
