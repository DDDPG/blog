import request from "@/utlis/request";

export function getMessages(params) {
  return request({
    url: "/admin/messages",
    method: "get",
    params,
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
