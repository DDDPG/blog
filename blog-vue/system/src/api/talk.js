import request from "@/utlis/request";

export function getTalks(current, size, status) {
  return request({
    url: "/admin/talks",
    method: "get",
    params: {
      current,
      size,
      status,
    },
  });
}

export function deleteTalk(talkId) {
  return request({
    url: `/admin/talks/${talkId}`,
    method: "delete",
  });
}

export function saveOrUpdateTalk(data) {
  return request({
    url: "/admin/talks",
    method: "post",
    data,
  });
}

export function getTalk(talkId) {
  return request({
    url: `/admin/talks/${talkId}`,
    method: "get",
  });
}