import Vue from "vue";
import Vuex from "vuex";
import createPersistedState from "vuex-persistedstate";
import getters from "./getters";
import tab from "./modules/tab";
import user from "./modules/user";

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    user,
    tab,
  },
  getters,
  plugins: [
    createPersistedState({
      storage: window.sessionStorage,
      paths: ["tab", "user"],
    }),
  ],
});
