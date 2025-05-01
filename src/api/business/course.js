import request from '@/utils/request4Training';
import requestUploadFile from '@/utils/request4TrainingUploadFile';

// 查询课件列表
export function listCourse(query) {
  return request({
    url: '/server/course/list',
    method: 'post',
    data: query
  })
}

// 查询课件详细
export function getCourse(id) {
  return request({
    url: '/server/course/get/' + id,
    method: 'get'
  })
}

// 新增课件
export function addCourse(data) {
  return request({
    url: '/server/course/create',
    method: 'post',
    data: data
  })
}

// 修改课件
export function updateCourse(data) {
  return request({
    url: '/server/course/update',
    method: 'post',
    data: data
  })
}

// 删除课件
export function delCourse(data) {
  return request({
    url: '/server/course/del',
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