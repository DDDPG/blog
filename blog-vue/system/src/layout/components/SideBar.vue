<template>
  <el-menu
    class="side"
    router
    :default-active="$route.path"
    :collapse="isCollapse"
    background-color="#304156"
    text-color="#BFCBD9"
    active-text-color="#409EFF"
  >
    <template v-for="item in $router.options.routes">
      <!-- 二级菜单 -->
      <template v-if="item.name && item.children && !item.hidden">
        <el-submenu :key="item.path" :index="item.path">
          <!-- 二级菜单标题 -->
          <template slot="title">
            <i :class="item.icon"></i>
            <span slot="title">{{ item.name }}</span>
          </template>
          <!-- 二级菜单选项 -->
          <template v-for="(subitem, index) of item.children">
            <el-menu-item
              @click="clickMenu(subitem)"
              v-if="!subitem.hidden"
              :key="index"
              :index="subitem.path"
            >
              <i :class="subitem.icon"></i>
              <span slot="title">{{ subitem.name }}</span>
            </el-menu-item>
          </template>
        </el-submenu>
      </template>
      <!--一级菜单-->
      <template v-else-if="!item.hidden">
        <el-menu-item
          @click="clickMenu(item.children[0])"
          :index="item.path"
          :key="item.path"
        >
          <i :class="item.children[0].icon"></i>
          <span slot="title">{{ item.children[0].name }}</span>
        </el-menu-item>
      </template>
    </template>
  </el-menu>
</template>

<script>
import store from "@/store";
export default {
  name: "SideBar",
  methods: {
    clickMenu(item) {
      this.$store.commit("tab/saveTab", item);
    },
  },
  computed: {
    isCollapse() {
      return store.getters.isCollapse;
    },
  },
};
</script>

<style scoped>
.side:not(.el-menu--collapse) {
  width: 210px;
}
.side {
  overflow-x: hidden;
  overflow-y: auto;
}
.side::-webkit-scrollbar {
  width: 0.5rem;
  height: 1px;
}
.side::-webkit-scrollbar-thumb {
  border-radius: 0.5rem;
  background-color: rgba(144, 147, 153, 0.3);
}
.side i {
  margin-right: 0.75rem;
}
.el-menu {
  height: 100%;
  border: none;
}
</style>
