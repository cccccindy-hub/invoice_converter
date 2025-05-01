import { ILogedUser } from '@/interface'
import request from '@/utils/request'
import { AxiosPromise } from 'axios'

// 登录方法
export async function login(username:string, password:string, code:any, uuid:any):Promise<ILogedUser> {
  const data = {
    username,
    password,
    code,
    uuid
  }
  let rst = await request({
    url: '/login',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  });
  return rst as any;
}

export async function loginWithCode(username:string, password:string, code:any, uuid:any):Promise<ILogedUser> {
  const data = {
    username,
    password,
    code,
    uuid
  }
  let rst = await request({
    url: '/loginWithCode',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  });
  return rst as any;
}

export async function loginByEmailCode(username:string, password:string, code:any, uuid:any, emailCode:any):Promise<ILogedUser> {
  const data = {
    username,
    password,
    code,
    uuid,
    emailCode,
  }
  let rst = await request({
    url: '/loginByEmailCode',
    headers: {
      isToken: false
    },
    method: 'post',
    params: data
  });
  return rst as any;
}


export async function sendCodeToEmail(username:string, password:string):Promise<ILogedUser> {
  const data = {
    username,
    password,
  }
  let rst = await request({
    url: '/sendCodeToEmail',
    headers: {
      isToken: false
    },
    method: 'post',
    params: data,
  });
  return rst as any;
}

export async function verifyEmailAndRefreshTotp(data:any, emailCode:string):Promise<ILogedUser> {
  data = {emailCode, ...data}
  let rst = await request({
    url: '/verifyEmailAndRefreshTotp',
    headers: {
      isToken: false
    },
    method: 'post',
    params: data,
  });
  return rst as any;
}

// 注册方法
export function register(data:any) {
  return request({
    url: '/register',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/getInfo',
    method: 'get'
  })
}

// 退出方法
export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/captchaImage',
    headers: {
      isToken: false
    },
    method: 'get',
    timeout: 20000
  })
}