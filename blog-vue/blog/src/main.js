import "@/assets/css/index.css";
import "@/assets/css/md.css";
import "@/assets/font/iconfont.css";
import '@/assets/font/font.css';
import "animate.css";
import dayjs from "dayjs";
import ElementResizeDetectorMaker from "element-resize-detector";
import "highlight.js/styles/atom-one-dark.css";
import "nprogress/nprogress.css";
import Viewer from "v-viewer";
import "viewerjs/dist/viewer.css";
import Vue from "vue";
import "vue-cute-timeline/dist/index.css";
import App from "./App.vue";
import Toast from "./components/toast/index";
import vuetify from "./plugins/vuetify";
import router from "./router";
import store from "./store";
import config from "./assets/js/config";

Vue.use(Viewer);
Vue.use(Toast);

Vue.prototype.config = config;
Vue.config.productionTip = false;
Vue.prototype.$erd = ElementResizeDetectorMaker();

Vue.filter("date", function (value) {
  return dayjs(value).format("YYYY-MM-DD");
});

Vue.filter("year", function (value) {
  return dayjs(value).format("YYYY");
});

Vue.filter("num", function (value) {
  if (value >= 1000) {
    return (value / 1000).toFixed(1) + "k";
  }
  return value;
});

new Vue({
  beforeCreate() {
    Vue.prototype.$bus = this;
  },
  router,
  store,
  vuetify,
  render: (h) => h(App),
}).$mount("#app");
