// import {ApiResponse} from '@/interface';
import axios, {AxiosRequestConfig, AxiosRequestHeaders, AxiosResponse} from 'axios';
// import { env } from 'process';
import {ElLoading, ElMessage} from 'element-plus'
import {getToken} from '@/utils/auth';
import useUserStore from '@/store/modules/user';

// console.log(process.env);
export class PayApiHandler {
    constructor(private basePath:string='') {

    }
    private getHeaders():AxiosRequestHeaders{
        return {
            Authorization:'Bearer ' + getToken()
        }
    }
    private async requestApi(config: AxiosRequestConfig<any>,showLoding:boolean=true){
        config.baseURL=import.meta.env.VITE_APP_BASE_API+this.basePath;
        config.headers=Object.assign(this.getHeaders(),config.headers)
        let loading;
        if(showLoding){
            loading = ElLoading.service({
                lock: true,
                text: 'Loading',
                fullscreen: true,
                background: 'rgba(0, 0, 0, 0.7)',
            });
        }
        try{
            let {data}:AxiosResponse<any> = await axios(config);
            if(data.code != 200){
                if (data['code'] === 401) {
                    useUserStore().logOut().then(() => {
                        location.href = '/index';
                    })
                    return Promise.reject('无效的会话，或者会话已过期，请重新登录。')
                }else{
                    throw new Error(data['msg']+`(错误代码:${data['code']})`);
                }
            }
            if(loading) loading.close();

            return data.data;
        }catch(err:any){
            if(loading) loading.close();
            ElMessage.error(err.message);
            throw err;
            // throw new Error('网络错误,请检查网络');
        }
    }
    async get(uri: string, data?:{[key:string]:any}):Promise<any> {
        return await this.requestApi(Object.assign({
            method:"get",
            url:uri,
            params:data
        }));
    }

    async post(options:{url: string; data?: {[key:string]:any};headers?:{[key:string]:string}}):Promise<any> {
        return await this.requestApi(Object.assign({
            method:"post"
        },options));
    }

    async put(options:{url: string; data?: {[key:string]:any};headers?:{[key:string]:string}}):Promise<any> {
        return await this.requestApi(Object.assign({
            method:"put"
        },options));
    }

    async delete(url: string):Promise<any> {
        return await this.requestApi({
            url: url,
            method:"delete",
        });
    }
}