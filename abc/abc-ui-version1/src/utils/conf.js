const HOST = process.env.NODE_ENV === 'production' ? '' : '';
/* 测试环境publicPath设置为/dev，API请求同步变更 */
const GLOBAL_URL_PREFIX = process.env.NODE_ENV === 'production' ? '' : '/dev';

export default {
  HOST,
  GLOBAL_URL_PREFIX
};
