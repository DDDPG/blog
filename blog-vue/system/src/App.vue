<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
import { report } from "@/api/sitesetting";
export default {
  name: "App",
  created() {
    this.init();
  },
  methods: {
    init() {
      report();
    },
  },
};
</script>

<style>
#app {
  height: 100vh;
}
</style>