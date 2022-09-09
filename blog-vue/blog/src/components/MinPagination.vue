<template>
  <div class="view-more-pagination">
    <span class="pagination-page-count">共{{ totalPage }}页</span>
    <span class="pagination-btn" v-if="current != 1" @click="prePage()">上一页</span>
    <template v-for="(number, index) in visibleNumber">
      <span
        :key="index"
        @click="changePage(number)"
        v-if="typeof number == 'number'"
        class="pagination-page-number"
        :class="current == number ? 'current-page' : ''"
        >{{ number }}</span
      >
      <span class="pagination-page-do" :key="index" v-else>...</span>
    </template>
    <span class="pagination-btn" v-if="current != totalPage" @click="nextPage()">下一页</span>
  </div>
</template>

<script>
export default {
  name: "MinPagination",
  props: {
    total: {
      type: Number,
    },
    index: {
      type: Number,
    },
    commentId: {
      type: Number,
    },
  },
  data() {
    return {
      current: 1,
      pageSize: 5,
    };
  },
  methods: {
    prePage() {
      this.$emit("getCurrentPage", this.current - 1, this.index, this.commentId);
    },
    changePage(number) {
      this.$emit("getCurrentPage", number, this.index, this.commentId);
    },
    nextPage() {
      this.$emit("getCurrentPage", this.current + 1, this.index, this.commentId);
    },
  },
  computed: {
    totalPage() {
      return Math.ceil(this.total / this.pageSize);
    },
    visibleNumber() {
      let pages = this.totalPage;
      let current = this.current;
      if (pages <= 5) {
        return pages;
      } else {
        if (current <= 4) {
          return [1, 2, 3, 4, 5, "...", pages];
        } else if (current >= pages - 4) {
          return [1, "...", pages - 5, pages - 4, pages - 3, pages - 2, pages - 1, pages];
        } else {
          return [
            1,
            "...",
            current - 2,
            current - 1,
            current,
            current + 1,
            current + 2,
            "...",
            pages,
          ];
        }
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.view-more-pagination {
  padding-left: 8px;
  font-size: 13px;
  font-weight: 400;
  line-height: 19.5px;
  color: #18191c;
  .pagination-page-count {
    margin-right: 10px;
  }
  .pagination-btn {
    cursor: pointer;
    &:hover {
      color: #ed6ea0;
    }
  }
  .pagination-page-do {
    margin: 0 4px;
  }
  .pagination-page-number {
    margin: 0 4px;
    cursor: pointer;
    &:hover {
      color: #ed6ea0;
    }
  }
}
.pagination-page-number.current-page {
  color: #ed6ea0;
}
</style>
