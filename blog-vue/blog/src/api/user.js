import request from "@/utils/request";

export function getCode(username) {
  return request({
    url: "/code",
    method: "get",
    params: {
      username,
    },
  });
}

export function register(data) {
  return request({
    url: "/register",
    method: "post",
    data,
  });
}

export function updateUserInfo(data) {
  return request({
    url: "/users/info",
    method: "put",
    data,
  });
}

export function updateUserPassword(data) {
  return request({
    url: "/users/password",
    method: "put",
    data,
  });
}

export function updateEmail(data) {
  return request({
    url: "/users/email",
    method: "post",
    data,
  });
}
