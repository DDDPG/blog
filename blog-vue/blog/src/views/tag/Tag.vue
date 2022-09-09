<template>
  <div class="collapse wrap">
    <h2 class="item title">目前共计 {{ count }} 个标签</h2>
    <div class="tags cloud">
      <router-link
        class="tag"
        :style="{ 'font-size': Math.floor(Math.random() * 10) + 18 + 'px' }"
        v-for="item of tagList"
        :key="item.id"
        :to="`/tags/${item.id}`"
      >
        {{ item.tagName }}
      </router-link>
    </div>
  </div>
</template>

<script>
import { getTags } from "@/api/tag";
export default {
  name: "Tag",
  data() {
    return {
      tagList: [],
      count: 0,
    };
  },
  created() {
    this.getTagList();
  },
  methods: {
    getTagList() {
      getTags().then(({ data }) => {
        this.tagList = data.data.recordList;
        this.count = data.data.count;
      });
    },
  },
};
</script>

<style scoped>
.collapse .item {
  position: relative;
  padding: 1.25rem 1.875rem;
  margin: 0;
}
.collapse .item.title {
  text-align: center;
  font-size: 29px;
}
.tags.cloud {
  text-align: center;
}
.tags.cloud .tag {
  display: inline-block;
  margin: 0.625rem;
}
.tags.cloud .tag:hover {
  color: var(--primary-color) !important;
}
</style>