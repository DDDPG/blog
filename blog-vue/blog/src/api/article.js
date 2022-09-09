import request from "@/utils/request";

export function getArticles(current) {
  return request({
    url: "/articles",
    method: "get",
    params: {
      current,
    },
  });
}
export function getArticle(articleId) {
  return request({
    url: `/articles/${articleId}`,
    method: "get",
  });
}

export function likeArticle(articleId) {
  return request({
    url: `/articles/${articleId}/like`,
    method: "post",
  });
}

export function getArchives(current) {
  return request({
    url: "/articles/archives",
    method: "get",
    params: {
      current,
    },
  });
}

export function getRecentArticles() {
  return request({
    url: "/recent/articles",
    method: "get",
  });
}

export function searchArticles(current, keyword) {
  return request({
    url: "/articles/search",
    method: "get",
    params: {
      current,
      keyword,
    },
  });
}

export function getArticleCondition(categoryId, tagId, current) {
  return request({
    url: "/articles/condition",
    method: "get",
    params: {
      categoryId,
      tagId,
      current,
    },
  });
}
