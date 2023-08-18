const {defineConfig} = require('@vue/cli-service');
module.exports = defineConfig({
  publicPath: process.env.NODE_ENV === 'production' ? '/' : '/dev',
  transpileDependencies: true,
  devServer: {
    proxy: {
      '/dev/api': {
        target: 'http://localhost:18880',
        pathRewrite: {'^/dev/api': ''},
        secure: false,
        changeOrigin: true,
      },
      /* http://localhost:8880/api/login 👉 http://localhost:18880/login */
      '/api': {
        target: 'http://localhost:18880',
        pathRewrite: {'^/api': ''},
        /* secure选项的作用是控制是否验证SSL证书。
           默认情况下，如果代理目标使用了https协议，secure选项将会设置为true，这意味着代理服务将需要验证SSL证书。
           如果你的代理目标使用了自签名的SSL证书或无效的SSL证书，可能会导致请求失败。
           在开发过程中，你可以将secure选项设置为false来禁用SSL证书验证 */
        secure: false,
        /* changeOrigin选项的作用是更改请求头中的"origin"字段为代理目标的URL。
           默认情况下，changeOrigin选项会设置为true，意味着请求的Origin字段将被更改为代理目标的URL。
           这在处理跨域请求时非常有用，因为有些服务器可能会根据Origin字段来做安全限制。
           如果你希望保留请求头中的"origin"字段不变，你可以将changeOrigin选项设置为false */
        changeOrigin: true,
      }
    },
    port: 8880,  // 端口号的配置
  }
});
