import store from "@/store";
import axios from "axios";
import Vue from "vue";
import { getToken } from "./token";

const requests = axios.create({
  baseURL: "/api",
  timeout: 10000,
  // 请求头
  headers: {
    "Content-Type": "application/json;charset=UTF-8",
  },
});

// 配置请求拦截器
requests.interceptors.request.use(
  (config) => {
    // 请求带token
    if (getToken()) {
      config.headers["Authorization"] = getToken();
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 配置响应拦截器
requests.interceptors.response.use(
  (response) => {
    switch (response.data.code) {
      case 500:
        Vue.prototype.$toast({ type: "error", message: response.data.msg });
        break;
      case 501:
        Vue.prototype.$toast({ type: "error", message: "系统异常" });
        break;
      case 402:
        Vue.prototype.$toast({ type: "warning", message: "登录已过期" });
        store.dispatch("LogOut");
        break;
      case 417:
        Vue.prototype.$toast({ type: "error", message: response.data.msg });
        break;
    }
    return response;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 对外暴露
export default requests;
