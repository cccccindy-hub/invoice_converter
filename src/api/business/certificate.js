import request from '@/utils/request4Training';
import requestUploadFile from '@/utils/request4TrainingUploadFile';

// 查询证书列表
export function listCertificate(query) {
  return request({
    url: '/server/cret/list',
    method: 'post',
    params: query
  })
}

// 查询证书详细
export function getCertificate(id) {
  return request({
    url: '/server/cret/get/' + id,
    method: 'get'
  })
}

// 新增证书
export function addCertificate(data) {
  return request({
    url: '/server/cret/create',
    method: 'post',
    data: data
  })
}

// 修改证书
export function updateCertificate(data) {
  return request({
    url: '/server/cret/update',
    method: 'post',
    data: data
  })
}

// 删除证书
export function delCertificate(data) {
  return request({
    url: '/server/cret/del',
    method: 'post',
    data: data
  })
}
// 上传文件
export async function uploadFile(file) {
  //创建一个FormData对象
  let formData = new FormData();
  //添加文件
  formData.append('file', file);
  //添加文件
  return await requestUploadFile({
    url: '/file/upload',
    method: 'post',
    data: file,
    headers: { 'Content-Type':'multipart/form-data' }
  })
}