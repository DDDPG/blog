import request from "@/utils/request";

export function login(data) {
  return request({
    url: "/login",
    method: "post",
    data,
  });
}

export function getUserInfo() {
  return request({
    url: "/getUserInfo",
    method: "get",
  });
}

export function logout() {
  return request({
    url: "/logout",
    method: "post",
  });
}

export function weiBoLogin(data) {
  return request({
    url: "/oauth/weibo",
    method: "post",
    data,
  });
}

export function giteeLogin(data) {
  return request({
    url: "/oauth/gitee",
    method: "post",
    data,
  });
}

export function githubLogin(data) {
  return request({
    url: "/oauth/github",
    method: "post",
    data,
  });
}

export function qqLogin(data) {
  return request({
    url: "/oauth/qq",
    method: "post",
    data,
  });
}

