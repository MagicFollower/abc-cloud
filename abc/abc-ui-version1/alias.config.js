/**
 * tip: WebStorm解析使用
 *
 * @param dir
 * @returns {string}
 */
const resolve = dir => require('path').join(__dirname, dir);

module.exports = {
  resolve: {
    alias: {
      '@': resolve('src')
    }
  }
};