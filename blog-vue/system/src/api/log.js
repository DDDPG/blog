import request from "@/utlis/request";
import downloadService from "@/utlis/downloadService";

export function getOperations(current, size, optModule, keyword) {
  return request({
    url: "/admin/operation/logs",
    method: "get",
    params: {
      current,
      size,
      optModule,
      keyword,
    },
  });
}

export function deleteOperation(data) {
  return request({
    url: "/admin/operation/logs",
    method: "delete",
    data,
  });
}

export function exportOperation() {
  return downloadService({
    url: "/admin/operation/logs/export",
    method: "get",
  });
}

export function getExceptions(current, size, optModule, keyword) {
  return request({
    url: "/admin/exception/logs",
    method: "get",
    params: {
      current,
      size,
      optModule,
      keyword,
    },
  });
}

export function deleteException(data) {
  return request({
    url: "/admin/exception/logs",
    method: "delete",
    data,
  });
}
