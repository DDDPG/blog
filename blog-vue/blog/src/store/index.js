import { getUserInfo, login, logout } from "@/api/login";
import { removeToken, setToken } from "@/utils/token";
import Vue from "vue";
import Vuex from "vuex";
import createPersistedState from "vuex-persistedstate";

Vue.use(Vuex);
export default new Vuex.Store({
  state: {
    isCollapse: false,
    searchFlag: false,
    loginFlag: false,
    registerFlag: false,
    forgetFlag: false,
    emailFlag: false,
    loginUrl: "",
    userId: null,
    avatar: null,
    nickname: null,
    email: null,
    loginType: null,
    articleLikeSet: [],
    commentLikeSet: [],
    talkLikeSet: [],
    blogInfo: { websiteDTO: { socialUrlList: [], socialLoginList: [] } },
  },
  mutations: {
    setBlogInfo(state, blogInfo) {
      state.blogInfo = blogInfo;
    },
    toggle(state) {
      state.isCollapse = !state.isCollapse;
    },
    closeModel(state) {
      state.registerFlag = false;
      state.loginFlag = false;
      state.emailFlag = false;
      state.searchFlag = false;
    },
    articleLike(state, articleId) {
      var articleLikeSet = state.articleLikeSet;
      if (articleLikeSet.indexOf(articleId) != -1) {
        articleLikeSet.splice(articleLikeSet.indexOf(articleId), 1);
      } else {
        articleLikeSet.push(articleId);
      }
    },
    commentLike(state, commentId) {
      var commentLikeSet = state.commentLikeSet;
      if (commentLikeSet.indexOf(commentId) != -1) {
        commentLikeSet.splice(commentLikeSet.indexOf(commentId), 1);
      } else {
        commentLikeSet.push(commentId);
      }
    },
    talkLike(state, talkId) {
      var talkLikeSet = state.talkLikeSet;
      if (talkLikeSet.indexOf(talkId) != -1) {
        talkLikeSet.splice(talkLikeSet.indexOf(talkId), 1);
      } else {
        talkLikeSet.push(talkId);
      }
    },
    updateAvatar(state, avatar) {
      state.avatar = avatar;
    },
    updateUserInfo(state, user) {
      state.nickname = user.nickname;
    },
    saveLoginUrl(state, url) {
      state.loginUrl = url;
    },
    SET_USERID(state, userId) {
      state.userId = userId;
    },
    SET_AVATAR(state, avatar) {
      state.avatar = avatar;
    },
    SET_NICKNAME(state, nickname) {
      state.nickname = nickname;
    },
    SET_LOGINTYPE(state, loginType) {
      state.loginType = loginType;
    },
    SET_EMAIL(state, email) {
      state.email = email;
    },
    SET_ARTICLE_LIKE(state, articleLikeSet) {
      state.articleLikeSet = articleLikeSet;
    },
    SET_COMMENT_LIKE(state, commentLikeSet) {
      state.commentLikeSet = commentLikeSet;
    },
    SET_TALK_LIKE(state, talkLikeSet) {
      state.talkLikeSet = talkLikeSet;
    },
  },
  actions: {
    // 获取用户详细信息
    getUserInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getUserInfo()
          .then(({ data }) => {
            commit("SET_USERID", data.data.id);
            commit("SET_AVATAR", data.data.avatar);
            commit("SET_NICKNAME", data.data.nickname);
            commit("SET_LOGINTYPE", data.data.loginType);
            commit("SET_EMAIL", data.data.email);
            commit("SET_ARTICLE_LIKE", data.data.articleLikeSet);
            commit("SET_COMMENT_LIKE", data.data.commentLikeSet);
            commit("SET_TALK_LIKE", data.data.talkLikeSet);
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
            commit("SET_NICKNAME", null);
            commit("SET_LOGINTYPE", null);
            commit("SET_EMAIL", null);
            commit("SET_ARTICLE_LIKE", []);
            commit("SET_COMMENT_LIKE", []);
            commit("SET_TALK_LIKE", []);
            removeToken();
            resolve();
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
  },
  getters: {
    userId(state) {
      return state.userId;
    },
  },
  plugins: [
    createPersistedState({
      storage: window.sessionStorage,
    }),
  ],
});
