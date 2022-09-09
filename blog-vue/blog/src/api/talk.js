import request from "@/utils/request";

export function getHomeTalk() {
  return request({
    url: "/home/talks",
    method: "get",
  });
}

export function getTalks(current, size) {
  return request({
    url: "/talks",
    method: "get",
    params: {
      current,
      size,
    },
  });
}

export function getTalk(talkId) {
  return request({
    url: `/talks/${talkId}`,
    method: "get",
  });
}

export function likeTalk(talkId) {
  return request({
    url: `/talks/${talkId}/like`,
    method: "post",
  });
}
