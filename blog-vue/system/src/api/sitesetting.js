import request from "@/utlis/request";

export function getSiteConfig() {
  return request({
    url: "/admin/site/setting",
    method: "get",
  });
}

export function updateWebsiteConfig(data) {
  return request({
    url: "/admin/site/setting",
    method: "put",
    data,
  });
}

export function getAdminInfo() {
  return request({
    url: "/admin",
    method: "get",
  });
}

export function report() {
  return request({
    url: "/report",
    method: "post",
  });
}


