<template>
  <v-snackbar v-model="show" top :timeout="2000" :color="color">
    <i :class="icon" style="margin-right: 10px"></i>{{ message }}
  </v-snackbar>
</template>

<script>
export default {
  name: "Toast",
  data: function () {
    return {
      show: false,
      message: "",
      type: "normal",
      color: "#49b1f5",
      icon: "",
    };
  },
  watch: {
    type(value) {
      switch (value) {
        case "error":
          this.color = "#F56C6C";
          this.icon = "iconfont icon-cuowu";
          break;
        case "success":
          this.color = "#52C41A";
          this.icon = "iconfont icon-chenggong";
          break;
        case "warning":
          this.color = "#F57C00";
          this.icon = "iconfont icon-jinggao";
      }
    },
  },
};
</script>