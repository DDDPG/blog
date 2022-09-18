import request from "@/utlis/request";

export function getTasks(params) {
  return request({
    url: "/admin/tasks",
    method: "get",
    params,
  });
}

export function addTask(data) {
  return request({
    url: "/admin/tasks",
    method: "post",
    data,
  });
}

export function updateTask(data) {
  return request({
    url: "/admin/tasks",
    method: "put",
    data,
  });
}

export function deleteTask(data) {
  return request({
    url: "/admin/tasks",
    method: "delete",
    data,
  });
}

export function changeTask(data) {
  return request({
    url: "/admin/tasks/changeStatus",
    method: "put",
    data,
  });
}

export function runTask(data) {
  return request({
    url: "/admin/tasks/run",
    method: "post",
    data,
  });
}
