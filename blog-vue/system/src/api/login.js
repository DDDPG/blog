import request from "@/utlis/request";

// 登录
export function login(data) {
  return request({
    url: "/login",
    method: "post",
    data,
  });
}

// 退出
export function logout() {
  return request({
    url: "/logout",
    method: "post",
  });
}

// 获取用户详细信息
export function getUserInfo() {
  return request({
    url: "/getUserInfo",
    method: "get",
  });
}
