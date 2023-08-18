import axios from 'axios';
import {Message} from 'element-ui';
import C from './conf';

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
          if (data.errorCode === 403) {
            const store = window.localStorage;
            store.removeItem('Access-Token');
            store.removeItem('username');
            location.href = '#/login';
            return;
          }
          reject(data);
          Message({
            message: data.errorMsg,
            type: 'error',
            duration: 2 * 1000
          });
          return;
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
