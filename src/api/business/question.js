import request from '@/utils/request4Training'

// 查询题目列表
export function listQuestion(query) {
  return request({
    url: '/server/question/list',
    method: 'post',
    data: query
  })
}

// 查询题目详细
export function getQuestion(questionId) {
  return request({
    url: '/server/question/get/' + questionId,
    method: 'get'
  })
}

// 新增题目
export function addQuestion(data) {
  return request({
    url: '/server/question/create',
    method: 'post',
    data: data
  })
}

// 修改题目
export function updateQuestion(data) {
  return request({
    url: '/server/question/update',
    method: 'post',
    data: data
  })
}

// 删除题目
export function delQuestion(data) {
  return request({
    url: '/server/question/del',
    method: 'post',
    data: data
  })
}
