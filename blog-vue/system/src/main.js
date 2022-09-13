
import dayjs from "dayjs";
import "echarts/lib/chart/bar";
import "echarts/lib/chart/line";
import "echarts/lib/chart/map";
import "echarts/lib/chart/pie";
import "echarts/lib/component/legend";
import "echarts/lib/component/title";
import "echarts/lib/component/tooltip";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import mavonEditor from "mavon-editor";
import "mavon-editor/dist/css/index.css";
import "nprogress/nprogress.css";
import Vue from "vue";
import VueCalendarHeatmap from "vue-calendar-heatmap";
import ECharts from "vue-echarts";
import App from "./App.vue";
import "./assets/font/iconfont.css";
import "./assets/css/index.css";
import config from "./assets/js/config";
import tagCloud from "./components/tag-cloud.js";
import router from "./router";
import store from "./store";

Vue.config.productionTip = false;
Vue.use(mavonEditor);
Vue.use(ElementUI);
Vue.use(VueCalendarHeatmap);
Vue.component("v-chart", ECharts);
Vue.use(tagCloud);
Vue.prototype.config = config;
Vue.prototype.$moment = dayjs;

Vue.filter("date", function (value, formatStr = "YYYY-MM-DD") {
  return dayjs(value).format(formatStr);
});

Vue.filter("dateTime", function (value, formatStr = "YYYY-MM-DD HH:mm:ss") {
  return dayjs(value).format(formatStr);
});

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
