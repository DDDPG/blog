import request from "@/utils/request";

export function getBlogInfo() {
  return request({
    url: "/",
    method: "get",
  });
}

export function report() {
  return request({
    url: "/report",
    method: "post",
  });
}
