import request from "@/utlis/request";

export function uploadPictures(data) {
  return request({
    url: "/admin/pictures/bili",
    headers: {
      "Content-Type": "multipart/form-data",
    },
    method: "post",
    data,
  });
}

export function savePictures(data) {
  return request({
    url: "/admin/pictures",
    method: "post",
    data,
  });
}

export function selectPictures(current, size, isDelete) {
  return request({
    url: "/admin/pictures",
    method: "get",
    params: {
      current,
      size,
      isDelete,
    },
  });
}

export function updatePictureDelete(data) {
  return request({
    url: "/admin/pictures/delete",
    method: "put",
    data,
  });
}

export function deletePictures(data) {
  return request({
    url: "/admin/pictures",
    method: "delete",
    data,
  });
}

export function updatePicture(data) {
  return request({
    url: "/admin/pictures",
    method: "put",
    data,
  });
}
