import { login, logout, getInfo, loginWithCode, sendCodeToEmail, verifyEmailAndRefreshTotp, loginByEmailCode } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'
import defAva from '@/assets/images/profile.jpg'
import { defineStore } from 'pinia'

export interface IUserState {
  token: string;
  name: string;
  avatar: string;
  roles: string[];
  roleIds: number[];
  permissions: string[];
}

const useUserStore = defineStore(
  'user',
  {
    state: () => (<IUserState>{
      token: getToken(),
      name: '',
      avatar: '',
      roles: [],
      roleIds: [],
      permissions: []
    }),
    actions: {
      // 登录
      login(userInfo:any) {
        const username = userInfo.username.trim()
        const password = userInfo.password
        const code = userInfo.code
        const uuid = userInfo.uuid
        return new Promise<any>((resolve, reject) => {
          login(username, password, code, uuid).then(res => {
            if(res.token) {
              setToken(res.token)
              this.token = res.token
            }
            resolve(res)
          }).catch(error => {
            reject(error)
          })
        })
      },
      loginWithCode(userInfo:any) {
        const username = userInfo.username.trim()
        const password = userInfo.password
        const code = userInfo.code
        const uuid = userInfo.uuid
        return new Promise<void>((resolve, reject) => {
          loginWithCode(username, password, code, uuid).then(res => {
            setToken(res.token)
            this.token = res.token
            resolve()
          }).catch(error => {
            reject(error)
          })
        })
      },
      loginByEmailCode(userInfo:any, emailCode:any) {
        const username = userInfo.username.trim()
        const password = userInfo.password
        const code = userInfo.code
        const uuid = userInfo.uuid
        return new Promise<void>((resolve, reject) => {
          loginByEmailCode(username, password, code, uuid, emailCode).then(res => {
            setToken(res.token)
            this.token = res.token
            resolve()
          }).catch(error => {
            reject(error)
          })
        })
      },
      verifyEmailAndRefreshTotp(userInfo:any, emailCode:string) {
        userInfo.username = userInfo.username.trim();
        return new Promise<void>((resolve, reject) => {
          verifyEmailAndRefreshTotp(userInfo, emailCode).then(res => {
            resolve(res as any)
          }).catch(error => {
            reject(error)
          })
        })
      },
      sendCodeToEmail(userInfo:any) {
        const username = userInfo.username.trim()
        const password = userInfo.password
        return new Promise<void>((resolve, reject) => {
          sendCodeToEmail(username, password).then(res => {
            resolve(res as any)
          }).catch((error) => {
            reject(error)
          })
        })
      },
      // 获取用户信息
      getInfo() {
        return new Promise((resolve, reject) => {
          getInfo().then((res:any) => {
            const user = res.user
            const avatar = (user.avatar == "" || user.avatar == null) ? defAva : import.meta.env.VITE_APP_BASE_API + user.avatar;

            if (res.roles && res.roles.length > 0) { // 验证返回的roles是否是一个非空数组
              this.roles = res.roles
              this.permissions = res.permissions
              this.roleIds = res.roleIds
            } else {
              this.roles = ['ROLE_DEFAULT']
            }
            this.name = user.userName
            this.avatar = avatar;
            resolve(res)
          }).catch(error => {
            reject(error)
          })
        })
      },
      // 退出系统
      logOut() {
        return new Promise<void>((resolve, reject) => {
          // logout(this.token).then(() => {
          logout().then(() => {
            this.token = ''
            this.roles = []
            this.permissions = []
            removeToken()
            resolve()
          }).catch(error => {
            reject(error)
          })
        })
      }
    }
  })

export default useUserStore
