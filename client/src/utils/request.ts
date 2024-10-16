import axios, { AxiosResponse } from 'axios';
import { useAlertStore, useAppStore, useUserStore } from './store';
import { SERVER } from '../config';

const request = axios.create({
    baseURL: SERVER,
    timeout: 10000,
});

export default request;

request.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers['Authorization'] = token;
        }
        return config;
    },
    (error) => {
        const store = useAlertStore();
        store.showMessage('error', '请求失败' + error.message);
        return Promise.reject(error);
    }
)

request.interceptors.response.use(
    (response: AxiosResponse) => {
        return response;
    },
    (error) => {
        const store = useAlertStore();

        if (error.response?.status === 401) {
            store.showMessage('warning', '未登录或已过期，请重新登录');
            localStorage.removeItem('token');
            localStorage.removeItem('username');
            localStorage.removeItem('level');
            localStorage.removeItem('departId');
            useUserStore().set('', '', '', -1);
            useAppStore().redirectTo('login');
            return Promise.reject(error);
        }

        let errorMessage = null;
        const responseData = error.response?.data;
        if (responseData)
            errorMessage = `${error.response?.status}(${responseData.errNo}): ${responseData.errMsg}`;
        if (!errorMessage)
            errorMessage = `${error.response?.status}: ${error.message}` || '请求失败';
        console.log(errorMessage);
        store.showMessage('error', errorMessage);
        return Promise.reject(error);
    }
);