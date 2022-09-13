import request from "@/utlis/request";

export function searchCategory(keyword) {
  return request({
    url: "/admin/categories/search",
    method: "get",
    params: {
      keyword,
    },
  });
}

// 分页关键字查询
export function getCategories(current, size, keyword) {
  return request({
    url: "/admin/categories",
    method: "get",
    params: {
      current,
      size,
      keyword,
    },
  });
}

// 添加或修改分类
export function addOrEditCategory(data) {
  return request({
    url: "/admin/categories",
    method: "post",
    data,
  });
}

// 删除分类
export function deleteCategory(data) {
  return request({
    url: "/admin/categories",
    method: "delete",
    data,
  });
}
