const tab = {
  namespaced: true,
  state: {
    isCollapse: false,
    tabList: [
      {
        name: "首页",
        path: "/",
      },
    ],
  },
  mutations: {
    trigger(state) {
      state.isCollapse = !state.isCollapse;
    },
    saveTab(state, tab) {
      if (state.tabList.findIndex((item) => item.path === tab.path) == -1) {
        state.tabList.push({
          name: tab.name,
          path: tab.path,
        });
      }
    },
    removeTab(state, tab) {
      const index = state.tabList.findIndex((item) => item.name === tab.name);
      state.tabList.splice(index, 1);
    },
    resetTab(state) {
      state.tabList = [
        {
          path: "/",
          name: "首页",
        },
      ];
    },
  },
};
export default tab;
