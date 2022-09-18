import downloadService from "@/utlis/downloadService";
import request from "@/utlis/request";

export function getOperations(params) {
  return request({
    url: "/admin/operation/logs",
    method: "get",
    params,
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

export function getExceptions(params) {
  return request({
    url: "/admin/exception/logs",
    method: "get",
    params,
  });
}

export function deleteException(data) {
  return request({
    url: "/admin/exception/logs",
    method: "delete",
    data,
  });
}
