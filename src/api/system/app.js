import request from '@/utils/request'

// 查询应用管理列表
export function listApp(query) {
  return request({
    url: '/system/app/list',
    method: 'get',
    params: query
  })
}

// 查询应用管理详细
export function getApp(appId) {
  return request({
    url: '/system/app/' + appId,
    method: 'get'
  })
}

// 新增应用管理
export function addApp(data) {
  return request({
    url: '/system/app',
    method: 'post',
    data: data
  })
}

// 修改应用管理
export function updateApp(data) {
  return request({
    url: '/system/app',
    method: 'put',
    data: data
  })
}

// 删除应用管理
export function delApp(appId) {
  return request({
    url: '/system/app/' + appId,
    method: 'delete'
  })
}

export function calColumnWidth(text, sortedAble) {
  const canvas = document.createElement('canvas');
  const context = canvas.getContext('2d');
  context.font = '14px Arial'; // 设置字体样式
  let width = 20 + context.measureText(text).width + (sortedAble ? 30 : 0);
  return width < 100 ? 100 : Math.min(width, 300);
}

export function objectToFormData(obj, form = new FormData(), namespace = "") {
  for (const key in obj) {
    if (obj.hasOwnProperty(key)) {
      const formKey = namespace ? `${namespace}[${key}]` : key;
      if (typeof obj[key] === "object" && !(obj[key] instanceof File)) {
        // 递归处理子对象
        objectToFormData(obj[key], form, formKey);
      } else {
        form.append(formKey, obj[key]);
      }
    }
  }
  return form;
}

export function genTestToken(appId) {
  return request({
    url: '/system/app/gen_token/' + appId,
    method: 'get'
  })
}
