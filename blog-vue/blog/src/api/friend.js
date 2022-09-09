import request from "@/utils/request";

export function getFriends() {
  return request({
    url: "/friends",
    method: "get",
  });
}
