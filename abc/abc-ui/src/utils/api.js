import axios from 'axios';
import {Message} from 'element-ui';
import C from './conf';
import Vue from 'vue';
const vm = new Vue();

axios.defaults.headers.post['Content-Type'] = 'application/json; charset=UTF-8';
axios.defaults.withCredentials = true;

function ajax(url, type, options, config) {
  return new Promise((resolve, reject) => {
    axios({
      method: type,
      url: config ? C[config.host] + C.GLOBAL_URL_PREFIX + url : C.HOST + C.GLOBAL_URL_PREFIX + url,
      timeout: 10000,
      headers: {
        'Access-Token': window.localStorage.getItem('Access-Token') || ''
      },
      params: type === 'get' ? options : null,
      data: options
    })
      .then(result => {
        const data = result.data;
        const success = data.success;
        if (success) {
          resolve(data);
          return;
        }

        if (!success) {
          // → 未登录访问非登录页交互逻辑修改
          //   → 当前：根据发送请求的结果进行判断跳转，这样会导致存在页面跳转时闪动的问题😟
          //   → 新版：在页面弹窗登录过期弹窗，弹窗存在一个确认按钮，点击跳转至登录页😊
          // // 约定: 任何API都将返回200, 状态码通过响应数据中的code+success进行判断
          // if (data.result.errorCode === '009401') {
          //   const store = window.localStorage;
          //   store.removeItem('Access-Token');
          //   store.removeItem('username');
          //   location.href = '#/login';
          //   return;
          // }
          if(data.result.errorCode === '009401') {
            const store = window.localStorage;
            store.removeItem('Access-Token');
            store.removeItem('username');
            vm.$confirm('Login status expired, please log in again', 'Prompt', {
              confirmButtonText: 'confirm',
              cancelButtonText: 'cancel',
              type: 'warning'
            }).then(() => {
              location.href = '#/login';
            }).catch(() => {
              location.href = '#/login';
            });
          } else {
            Message({
              message: data.message,
              type: 'error',
              duration: 2 * 1000
            });
          }
        }
      })
      .catch(error => {
        Message({
          message: error,
          type: 'error',
          duration: 2 * 1000
        });
      });
  });
}

const config = {
  get(url, options, config) {
    return new Promise((resolve, reject) => {
      ajax(url, 'get', options, config).then(
        data => {
          resolve(data);
        },
        error => {
          reject(error);
        }
      );
    });
  },

  post(url, options, config) {
    return new Promise((resolve, reject) => {
      ajax(url, 'post', options, config).then(
        data => {
          resolve(data);
        },
        error => {
          reject(error);
        }
      );
    });
  },

  put(url, options) {
    return new Promise((resolve, reject) => {
      ajax(url, 'put', options).then(
        data => {
          resolve(data);
        },
        error => {
          reject(error);
        }
      );
    });
  },

  delete(url, options) {
    return new Promise((resolve, reject) => {
      ajax(url, 'delete', options).then(
        data => {
          resolve(data);
        },
        error => {
          reject(error);
        }
      );
    });
  }
};

export default config;
