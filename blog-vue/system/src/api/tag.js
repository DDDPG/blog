import request from "@/utlis/request";

export function searchTag(keyword) {
  return request({
    url: "/admin/tags/search",
    method: "get",
    params: {
      keyword,
    },
  });
}

// 分页关键字查询
export function getTags(current, size, keyword) {
  return request({
    url: "/admin/tags",
    method: "get",
    params: {
      current,
      size,
      keyword,
    },
  });
}

// 添加或修改标签
export function addOrEditTag(data) {
  return request({
    url: "/admin/tags",
    method: "post",
    data,
  });
}

// 删除标签
export function deleteTag(data) {
  return request({
    url: "/admin/tags",
    method: "delete",
    data,
  });
}
