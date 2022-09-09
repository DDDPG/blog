import request from "@/utils/request";

export function getAbout() {
  return request({
    url: "/about",
    method: "get",
  });
}
