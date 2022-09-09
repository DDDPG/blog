<template>
  <div class="collapse wrap">
    <h2 class="item title">目前共计 {{ count }} 个分类</h2>
    <div v-for="item of categoryList" :key="item.id">
      <h2 class="item header">
        <router-link :to="`/categories/${item.id}`">
          {{ item.categoryName }}
          <small class="count">( {{ item.articleCount }} )</small>
        </router-link>
      </h2>
    </div>
  </div>
</template>

<script>
import { getCategories } from "@/api/category";
export default {
  name: "Category",
  data() {
    return {
      categoryList: [],
      count: 0,
    };
  },
  created() {
    this.getCategoryList();
  },
  methods: {
    getCategoryList() {
      getCategories().then(({ data }) => {
        this.categoryList = data.data.recordList;
        this.count = data.data.count;
      });
    },
  },
};
</script>

<style scoped>
.collapse .item {
  position: relative;
  padding: 1.25rem 2.875rem;
  margin: 0;
}
.collapse .item.title {
  text-align: center;
  font-size: 29px;
}

.collapse .item:not(.title) {
  padding: 0.875rem 3.875rem;
}
.collapse .item.header:hover .category {
  border-bottom: 0.0625rem dashed var(--grey-4);
  color: var(--color-blue);
}
.collapse .item:not(.title):before {
  content: "";
  position: absolute;
  z-index: 1;
  transition: all 0.2s ease-in-out 0s;
  box-sizing: unset;
  top: 1.6rem;
  left: 2rem;
  width: 0.6rem;
  height: 0.6rem;
  border: 0.15rem solid var(--primary-color);
  border-radius: 50%;
  background: var(--grey-1);
}
.category {
  border-bottom: 0.0625rem dashed var(--grey-4);
}
.count {
  font-size: 80%;
  color: var(--grey-4);
  margin: auto 0.3125rem;
}
</style>