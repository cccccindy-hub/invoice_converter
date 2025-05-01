import request from '@/utils/request4Training'

// 查询分类列表
export function listCategory(query) {
  return request({
    url: '/server/question/cat/list',
    method: 'get',
    params: query
  })
}

// 查询分类列表（排除节点）
export function listCategoryExcludeChild(categoryId) {
  return request({
    url: '/business/category/list/exclude/' + categoryId,
    method: 'get'
  })
}

// 查询分类详细
export function getCategory(categoryId) {
  return request({
    url: '/server/question/cat/get/' + categoryId,
    method: 'get'
  })
}

// 新增分类
export function addCategory(data) {
  return request({
    url: '/server/question/cat/add',
    method: 'post',
    data: data
  })
}

// 修改分类
export function updateCategory(data) {
  return request({
    url: '/server/question/cat/edit',
    method: 'post',
    data: data
  })
}

// 删除分类
export function delCategory(categoryId) {
  return request({
    url: '/server/question/cat/del/' + categoryId,
    method: 'post'
  })
}