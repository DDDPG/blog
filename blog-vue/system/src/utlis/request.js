import store from "@/store";
import axios from "axios";
import { Message, MessageBox, Notification } from "element-ui";
import { getToken } from "./token";

// 是否显示重新登录
export let isRelogin = { show: false };

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
      case 401:
        Notification.error({
          title: "失败",
          message: response.data.msg,
        });
        break;
      case 402:
        if (!isRelogin.show) {
          isRelogin.show = true;
          MessageBox.confirm(
            "登录状态已过期，您可以继续留在该页面，或者重新登录",
            "系统提示",
            {
              confirmButtonText: "重新登录",
              cancelButtonText: "取消",
              type: "warning",
            }
          )
            .then(() => {
              isRelogin.show = false;
              store.dispatch("user/LogOut").then(() => {
                location.href = "/login";
              });
            })
            .catch(() => {
              isRelogin.show = false;
            });
        }
        break;
      case 417:
        Notification.error({
          title: "失败",
          message: response.data.msg,
        });
        break;
      case 500:
        Notification.error({
          title: "失败",
          message: response.data.msg,
        });
    }
    return response;
  },
  (error) => {
    let { message } = error;
    if (message == "Network Error") {
      message = "后端接口连接异常";
    } else if (message.includes("timeout")) {
      message = "系统接口请求超时";
    } else if (message.includes("Request failed with status code")) {
      message = "系统接口" + message.substr(message.length - 3) + "异常";
    }
    Message({
      message: message,
      type: "error",
      duration: 5 * 1000,
    });
    return Promise.reject(error);
  }
);
// 对外暴露
export default requests;
