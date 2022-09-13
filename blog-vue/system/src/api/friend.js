import request from "@/utlis/request";

export function getFriends(current, size, keyword) {
  return request({
    url: "/admin/friends",
    method: "get",
    params: {
      current,
      size,
      keyword,
    },
  });
}

export function addOrEditFriend(data) {
  return request({
    url: "/admin/friends",
    method: "post",
    data,
  });
}

export function deleteFriend(data) {
  return request({
    url: "/admin/friends",
    method: "delete",
    data,
  });
}
