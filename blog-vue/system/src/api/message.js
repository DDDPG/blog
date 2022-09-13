import request from "@/utlis/request";

export function getMessages(current, size, keyword, isCheck) {
  return request({
    url: "/admin/messages",
    method: "get",
    params: {
      current,
      size,
      keyword,
      isCheck,
    },
  });
}

export function deleteMessage(data) {
  return request({
    url: "/admin/messages",
    method: "delete",
    data,
  });
}

export function updateMessageCheck(data) {
  return request({
    url: "/admin/messages/pass",
    method: "put",
    data,
  });
}
