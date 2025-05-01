import axios, {AxiosRequestConfig, AxiosRequestHeaders, AxiosResponse} from 'axios';
import {ElLoading, ElMessage} from 'element-plus'
import {getToken} from '@/utils/auth';
import useUserStore from '@/store/modules/user';

// console.log(process.env);
export class ApiHandle {
    constructor(private basePath: string = '') {

    }

    private getHeaders(): AxiosRequestHeaders {
        return {
            Authorization: 'Bearer ' + getToken()
        }
    }

    private async requestApi(config: AxiosRequestConfig<any>, showLoding: boolean = true) {
        config.baseURL = import.meta.env.VITE_APP_BASE_API + this.basePath;
        config.headers = Object.assign(this.getHeaders(), config.headers)
        let loading;
        if (showLoding) {
            loading = ElLoading.service({
                lock: true,
                text: 'Loading',
                fullscreen: true,
                background: 'rgba(0, 0, 0, 0.7)',
            });
        }
        try {
            let {data}: AxiosResponse<ApiResponse> = await axios(config);
            let code = data.code;
            if (code === 401) {
                useUserStore().logOut().then(() => {
                    location.href = '/index';
                })
                return Promise.reject('无效的会话，或者会话已过期，请重新登录。')
            }
            if (code != 200 && code !=0) throw new Error(data.msg);
            if (loading) loading.close();
            return data;
        } catch (err: any) {
            if (loading) loading.close();
            ElMessage.error(err.message);
            throw err;
            // throw new Error('网络错误,请检查网络');
        }
    }
    async get(path: string): Promise<any> {
        return await this.requestApi({
            method: "get",
            url: path
        });
    }

    async post(options: {
        url: string;
        data?: { [key: string]: any };
        headers?: { [key: string]: string }
    }): Promise<any> {
        return await this.requestApi(Object.assign({
            method: "post"
        }, options));
    }

    async delete(path: string, options?: {
        params?: { [key: string]: any };
        headers?: { [key: string]: string }
    }): Promise<any> {
        return await this.requestApi(Object.assign({
            method: "delete",
            url: path,
        }, options));
    }

    async download(config: AxiosRequestConfig<any>, showLoding: boolean = true, showErrMsg: boolean = true) {
        config.baseURL = import.meta.env.VITE_APP_BASE_API + this.basePath;
        config.headers = Object.assign(this.getHeaders(), config.headers)
        let loading;
        if (showLoding) {
            loading = ElLoading.service({
                lock: true,
                text: 'Loading',
                fullscreen: true,
                background: 'rgba(0, 0, 0, 0.7)',
            });
        }
        try {
            config.responseType = "blob";
            let result: AxiosResponse<Blob> = await axios(config);
            this.downloadFormResult(result.data, decodeURIComponent(this.getFileNameFromResponseHeaders(result)));
        } catch (err: any) {
            if (loading) loading.close();
            if (!showErrMsg || axios.isCancel(err)) {
            } else {
                showError(err.message);
            }
            return Promise.reject(err);
        } finally {
            if (loading) loading.close();
        }
    }

    private downloadFormResult(blob: Blob, filename: String) {
        // 创建下载链接
        const downloadLink = document.createElement('a');
        downloadLink.href = window.URL.createObjectURL(blob);
        downloadLink.setAttribute('download', filename);
        // 点击下载链接
        document.body.appendChild(downloadLink);
        downloadLink.click();
        // 清理
        document.body.removeChild(downloadLink);
    }

    private getFileNameFromResponseHeaders(response: AxiosResponse): string {
        const contentDisposition = response.headers['content-disposition'];
        let filename = 'downloadedFile';  // 默认文件名
        if (contentDisposition) {
            const filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
            const matches = filenameRegex.exec(contentDisposition);
            if (matches != null && matches[1]) {
                filename = matches[1].replace(/['"]/g, '');
            }
        }
        return filename;
    }

}
