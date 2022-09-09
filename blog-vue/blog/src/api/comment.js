import request from "@/utils/request";

export function getComments(params) {
  return request({
    url: "/comments",
    method: "get",
    params,
  });
}

export function getCommentCount(params) {
  return request({
    url: "/comments/count",
    method: "get",
    params,
  });
}

export function getReplies(commentId, params) {
  return request({
    url: `/comments/${commentId}/replies`,
    method: "get",
    params,
  });
}

export function addComment(data) {
  return request({
    url: "/comments",
    method: "post",
    data,
  });
}

export function getRecentComments() {
  return request({
    url: "/recent/comments",
    method: "get",
  });
}

export function likeComment(commentId) {
  return request({
    url: `/comments/${commentId}/like`,
    method: "post",
  });
}