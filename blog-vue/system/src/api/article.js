import request from "@/utlis/request";

export function saveOrUpdateArticle(data) {
  return request({
    url: "/admin/articles",
    method: "post",
    data,
  });
}

export function getArticle(articleId) {
  return request({
    url: `/admin/articles/${articleId}`,
    method: "get",
  });
}

// 分页关键字查询
export function getArticles(
  current,
  size,
  keyword,
  status,
  articleType,
  categoryId,
  tagId,
  isDelete
) {
  return request({
    url: "/admin/articles",
    method: "get",
    params: {
      current,
      size,
      keyword,
      status,
      articleType,
      categoryId,
      tagId,
      isDelete,
    },
  });
}

export function changeTop(data) {
  return request({
    url: "/admin/articles/top",
    method: "put",
    data,
  });
}

export function updateArticleDelete(data) {
  return request({
    url: "/admin/articles",
    method: "put",
    data,
  });
}

export function exportArticles(articleId) {
  return request({
    url: `/download/${articleId}/article`,
    method: "get",
  });
}

export function deleteArticles(data) {
  return request({
    url: "/admin/articles",
    method: "delete",
    data,
  });
}

export function uploadArticleImg(data) {
  return request({
    url: "/admin/articles/images",
    method: "post",
    data,
  });
}
