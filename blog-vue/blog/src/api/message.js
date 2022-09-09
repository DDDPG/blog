import request from "@/utils/request";

export function addMessage(data) {
  return request({
    url: "/messages",
    method: "post",
    data,
  });
}


export function getMessage() {
  return request({
    url: "/messages",
    method: "get",
  });
}