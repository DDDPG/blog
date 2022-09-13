import axios from "axios";
import { Message } from "element-ui";
import { getToken } from "@/utlis/token"

axios.defaults.headers["Content-Type"] = "application/json;charset=utf-8";
// 创建axios实例
const downloadService = axios.create({
  baseURL: "/api",
  timeout: 10000,
  responseType: "blob",
});

// request拦截器
downloadService.interceptors.request.use(
  (config) => {
    // 请求带token
    if (getToken()) {
      config.headers["Authorization"] = getToken();
    }
    // get请求映射params参数
    if (config.method === "get" && config.params) {
      let url = config.url + "?";
      for (const propName of Object.keys(config.params)) {
        const value = config.params[propName];
        var part = encodeURIComponent(propName) + "=";
        if (value !== null && typeof value !== "undefined") {
          if (typeof value === "object") {
            for (const key of Object.keys(value)) {
              if (value[key] !== null && typeof value[key] !== "undefined") {
                const params = propName + "[" + key + "]";
                const subPart = encodeURIComponent(params) + "=";
                url += subPart + encodeURIComponent(value[key]) + "&";
              }
            }
          } else {
            url += part + encodeURIComponent(value) + "&";
          }
        }
      }
      url = url.slice(0, -1);
      config.params = {};
      config.url = url;
    }
    return config;
  },
  (error) => {
    Promise.reject(error);
  }
);

// 响应拦截器
downloadService.interceptors.response.use(
  (res) => {
    if (!res.data) {
      return;
    }
    var blob = new Blob([res.data]);
    const contentDisposition = res.headers["content-disposition"];
    var fileName = "test";
    if (contentDisposition) {
      fileName = window.decodeURI(res.headers["content-disposition"].split("=")[1], "UTF-8");
    }
    var url = window.URL.createObjectURL(blob);
    var aLink = document.createElement("a");
    aLink.style.display = "none";
    aLink.href = url;
    aLink.setAttribute("download", fileName);
    document.body.appendChild(aLink);
    aLink.click();
    document.body.removeChild(aLink); // 下载完成移除元素
    window.URL.revokeObjectURL(url); // 释放掉blob对象
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

export default downloadService;
