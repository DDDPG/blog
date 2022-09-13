const getters = {
  isCollapse: (state) => state.tab.isCollapse,
  tabList: (state) => state.tab.tabList,
  avatar: (state) => state.user.avatar,
  username: (state) => state.user.username,
  userId: (state) => state.user.userId,
};
export default getters;
