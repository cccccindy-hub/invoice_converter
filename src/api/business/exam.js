import request from '@/utils/request4Training';
import requestImage from '@/utils/request4TrainingImage';

// ------------------------------考试管理----------

// 查询考卷列表
export function listExam(query) {
  return request({
    url: '/server/exam/list',
    method: 'post',
    params: query
  })
}

// 查询考卷详细
export function getExam(id) {
  return request({
    url: '/server/exam/get/' + id,
    method: 'get'
  })
}

// 新增考卷
export function addExam(data) {
  return request({
    url: '/server/exam/create',
    method: 'post',
    data: data
  })
}

// 修改考卷
export function updateExam(data) {
  return request({
    url: '/server/exam/update',
    method: 'post',
    data: data
  })
}

// 删除考卷
export function delExam(data) {
  return request({
    url: '/server/exam/del',
    method: 'post',
    data: data
  })
}

// ------------------------------章节管理----------

// 查询考卷章节列表
export function listExamSection(query) {
  return request({
    url: '/server/exam/section/list',
    method: 'post',
    params: query
  })
}
export function listExamSectionById(id) {
  return request({
    url: '/server/exam/section/list/' + id,
    method: 'post'
  })
}

// 查询考卷章节详细
export function getExamSection(id) {
  return request({
    url: '/server/exam/section/get/' + id,
    method: 'get'
  })
}

// 新增考卷章节
export function addExamSection(data) {
  return request({
    url: '/server/exam/section/create',
    method: 'post',
    data: data
  })
}

// 修改考卷章节
export function updateExamSection(data) {
  return request({
    url: '/server/exam/section/update',
    method: 'post',
    data: data
  })
}

// 删除考卷章节
export function delExamSection(data) {
  return request({
    url: '/server/exam/section/del',
    method: 'post',
    data: data
  })
}

// ------------------------------试题管理----------

// 查询考卷试题列表
export function listExamQuestion(query) {
  return request({
    url: '/server/exam/question/list',
    method: 'post',
    data: query
  })
}

// 查询考卷试题详细
export function getExamQuestion(id) {
  return request({
    url: '/server/exam/question/get/' + id,
    method: 'get'
  })
}

// 新增考卷试题
export function addExamQuestion(data) {
  return request({
    url: '/server/exam/question/create',
    method: 'post',
    data: data
  })
}

// 修改考卷试题
export function updateExamQuestion(data) {
  return request({
    url: '/server/exam/question/update',
    method: 'post',
    data: data
  })
}

// 删除考卷试题
export function delExamQuestion(data) {
  return request({
    url: '/server/exam/question/del',
    method: 'post',
    data: data
  })
}

// ------------------------------考生管理----------

// 查询考生列表
export function listExamUser(query) {
  return request({
    url: '/server/exam/user_exam/list',
    method: 'post',
    data: query
  })
}

export function delExamUser(userExamId) {
  return request({
    url: '/server/exam/user_exam/del/'+userExamId,
    method: 'post',
    data: null
  })
}

// 生产用户考卷
export function sendExamUser(examId,data) {
  return request({
    url: '/server/exam/send/'+examId,
    method: 'post',
    data: data
  })
}

// ------------------------------考生考试----------

// 查询考生列表
export function listExamClient(query) {
  return request({
    url: '/client/exam/list',
    method: 'post',
    data: query
  })
}

// 查询考试详情
export function getExamById(id) {
  return request({
    url: '/client/exam/get_by_id/'+id,
    method: 'get'
  })
}

// 获取试题列表
// export function userExamQuestionList(sectionId,data) {
//   return request({
//     url: '/client/exam/question/list/'+sectionId,
//     method: 'post',
//     data: data
//   })
// }
export function userExamQuestionList(data) {
  return request({
    url: '/client/exam/question/list',
    method: 'post',
    data: data
  })
}

// 用户答题
export function userExamAnswer(data) {
  return request({
    url: '/client/exam/answer',
    method: 'post',
    data: data
  })
}

// 答题列表
export function userExamAnswerList(exam,section) {
  return request({
    url: '/client/exam/answer/list/'+exam+'/'+section,
    method: 'get'
  })
}

// 提交试卷
export function userExamSubmit(userExamId) {
  return request({
    url: '/client/exam/submit/'+userExamId,
    method: 'post'
  })
}

// 下载证书
export function getCertificate(language,data) {
  return requestImage({
    url: '/cert2image/'+language,
    method: 'post',
    data: data
  })
}