<template>
  <meting-js
    server="netease"
    type="playlist"
    :id="musicId"
    fixed="true"
    theme="#e9546b"
  ></meting-js>
</template>

<script>
export default {
  name: "MusicPlayer",
  computed: {
    musicId() {
      return this.$store.state.blogInfo.websiteDTO.musicId;
    },
  },
};
</script>

<style></style>