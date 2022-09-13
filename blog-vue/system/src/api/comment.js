import request from "@/utlis/request";

export function getComments(current, size, keyword, commentType, isCheck) {
  return request({
    url: "/admin/comments",
    method: "get",
    params: {
      current,
      size,
      keyword,
      commentType,
      isCheck,
    },
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
