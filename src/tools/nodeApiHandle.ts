import axios, { AxiosPromise, AxiosRequestConfig, AxiosResponse } from 'axios'
import { ElNotification, ElMessageBox, ElMessage, ElLoading } from 'element-plus'
import { getToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import { tansParams, blobValidate } from '@/utils/ruoyi'
import cache from '@/plugins/cache'
import { saveAs } from 'file-saver'
import useUserStore from '@/store/modules/user'
import { IAdminApiResponse } from '@/interface'


// Create Axios instance
axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'


const api = axios.create({
  baseURL: import.meta.env.VITE_APP_DOCX_API, // API base URL
  timeout: 10000, // Request timeout in milliseconds
  headers: {
    'Content-Type': 'application/json',
  },
  responseType: 'blob',
});
console.log('VITE_APP_DOCX_API:', import.meta.env.VITE_APP_DOCX_API);


// Function to trigger export (API call)
export const triggerExport = async (data) => {
    try {
      // Send data to the "export" endpoint
      const response = await api.post('/export', data);
      return response;
    } catch (error) {
      console.error('Error during export:', error);
      throw error; // Or handle it based on your use case
    }
};