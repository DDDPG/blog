<template>
  <div class="collapse wrap">
    <timeline>
      <timeline-title bg-color="#f2d7e1"> 目前共计{{ count }}篇文章，继续努力... </timeline-title>
      <timeline-item
        bg-color="#f2d7e1"
        icon-size="small"
        v-for="item of archiveList"
        :key="item.id"
      >
        <span class="time">{{ item.createTime | date }}</span>
        <router-link :to="'/articles/' + item.id" style="color: #666; text-decoration: none">
          {{ item.articleTitle }}
        </router-link>
      </timeline-item>
    </timeline>
    <v-pagination
      color="#ed6ea0"
      v-model="currentPage"
      :length="Math.ceil(count / 10)"
      total-visible="7"
    ></v-pagination>
  </div>
</template>

<script>
import { Timeline, TimelineItem, TimelineTitle } from "vue-cute-timeline";
import { getArchives } from "@/api/article";
export default {
  name: "Archives",
  created() {
    this.listArchives();
  },
  components: {
    Timeline,
    TimelineItem,
    TimelineTitle,
  },
  data() {
    return {
      currentPage: 1,
      count: 0,
      archiveList: [],
    };
  },
  methods: {
    listArchives() {
      getArchives(this.currentPage).then(({ data }) => {
        this.archiveList = data.data.recordList;
        this.count = data.data.count;
      });
    },
  },
  watch: {
    currentPage(value) {
      getArchives(value).then(({ data }) => {
        this.archiveList = data.data.recordList;
        this.count = data.data.count;
      });
    },
  },
};
</script>

<style scoped>
.time {
  font-size: 0.75rem;
  color: #555;
  margin-right: 1rem;
}
</style>
