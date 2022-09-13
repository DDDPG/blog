import { getUserInfo, login, logout } from "@/api/login";
import { removeToken, setToken } from "@/utlis/token";

const user = {
  namespaced: true,
  state: {
    userId: null,
    username: null,
    avatar: null,
    nickname: null,
  },
  mutations: {
    SET_USERID(state, userId) {
      state.userId = userId;
    },
    SET_USERNAME(state, username) {
      state.username = username;
    },
    SET_AVATAR(state, avatar) {
      state.avatar = avatar;
    },
    SET_NICKNAME(state, nickname) {
      state.nickname = nickname;
    },
  },
  actions: {
    updateUserInfo({ commit }, user) {
      commit("SET_USERNAME", user.username);
      commit("SET_NICKNAME", user.nickname);
    },
    // 获取用户详细信息
    getUserInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getUserInfo()
          .then(({data}) => {
            commit("SET_USERID", data.data.id);
            commit("SET_AVATAR", data.data.avatar);
            commit("SET_USERNAME", data.data.username);
            commit("SET_NICKNAME", data.data.nickname);
            resolve(data);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    // 登录
    LogIn({ commit }, user) {
      return new Promise((resolve, reject) => {
        login(user)
          .then(({ data }) => {
            if (data.flag) {
              setToken(data.data);
              resolve();
            } else {
              reject(data.msg);
            }
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    // 退出
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout()
          .then(() => {
            commit("SET_USERID", null);
            commit("SET_AVATAR", null);
            commit("SET_USERNAME", null);
            commit("SET_NICKNAME", null);
            removeToken();
            resolve();
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    // 更新头像
    updateAvatar(state, avatar) {
      state.avatar = avatar;
    },
  },
};

export default user;
