import request from "@/utlis/request";

export function getComments(params) {
  return request({
    url: "/admin/comments",
    method: "get",
    params,
  });
}

export function deleteComment(data) {
  return request({
    url: "/admin/comments",
    method: "delete",
    data,
  });
}

export function updateCommentCheck(data) {
  return request({
    url: "/admin/comments/pass",
    method: "put",
    data,
  });
}
