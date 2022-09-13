import request from "@/utlis/request";

export function getAbout() {
  return request({
    url: "/about",
    method: "get",
  });
}

export function updateAbout(data) {
  return request({
    url: "/admin/about",
    method: "put",
    data
  });
}