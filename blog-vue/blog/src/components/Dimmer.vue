<template>
  <!-- 移动端遮罩层 -->
  <div :class="isShow ? show : dimmer" @click="close"></div>
</template>

<script>
export default {
  name: "Dimmer",
  data() {
    return {
      dimmer: "dimmer",
      show: "show",
    };
  },
  methods: {
    close() {
      this.$bus.$emit("handleToggle");
    },
  },
  computed: {
    isShow() {
      return this.$store.state.isCollapse;
    },
  },
};
</script>

<style scoped>
.dimmer {
  display: none;
}
@media (max-width: 991px) {
  .show {
    position: fixed;
    display: block;
    opacity: 0.3;
    transform: translateX(-100%);
    background: #000;
    width: 100%;
    height: 100%;
    left: 100%;
    top: 0;
    z-index: 9;
    transition: opacity 1s;
  }
}
</style>
