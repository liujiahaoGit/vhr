import {Message} from 'element-ui';
import axios from 'axios';
import router from '../router'

/*对请求后响应的结果拦截处理*/
axios.interceptors.response.use(success => {
        console.log("success " + success.data)
        if (success.status && success.status == 200 && success.data.success == false) {
            Message.error({message: success.data.message})
            return;
        }
        if (success.data.message) {
            Message.success({message: success.data.message})
        }
        return success.data;

    }, error => {
        console.log("error " + error)
        if (error.response.status == 504 || error.response.status == 404) {
            Message.error({message: "服务器被吃了（´Д`）"})
        } else if (error.response.status == 403) {
            Message.error({message: "权限不足,请联系管理员"})
        } else if (error.response.status == 401) {
            Message.error({message: "尚未登录,请登录"})
            router.replace("/")
        } else {
            if (error.response.data.message) {
                Message.error({message: error.response.data.message})
            } else {
                Message.error({message: "未知错误"});

            }
        }
        return;
    }
);

let base = '';

/*对请求为post 并参数为key/value形式参数的方法封装*/
export const postKeyValueRequest = (url, params) => {
    return axios({
        method: 'post',
        url: `${base}${url}`,
        data: params,
        transformRequest: [function (data) {
            let ret = '';
            for (let i in data) {
                ret += encodeURIComponent(i) + '=' + encodeURIComponent(data[i]) + '&'
            }
            console.log("ret: " + ret);
            return ret;
        }],
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    });
};

/*post请求*/
export const postRequest = (url, params) => {
    return axios({
        method: 'post',
        url: `${base}${url}`,
        data: params
    })
};
/*put请求*/
export const putRequest = (url, params) => {
    return axios({
        method: 'put',
        url: `${base}${url}`,
        data: params
    })
};
/*get请求*/
export const getRequest = (url, params) => {
    return axios({
        method: 'get',
        url: `${base}${url}`,
        data: params
    })
};
/*delete请求*/
export const deleteRequest = (url, params) => {
    return axios({
        method: 'delete',
        url: `${base}${url}`,
        data: params
    })
};
