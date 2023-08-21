const {defineConfig} = require('@vue/cli-service');
module.exports = defineConfig({
  /* lintOnSave=false: 关闭每次保存都进行检测 */
  /* lintOnSave=true/'warning': 开启保存检测，会显示警告，但是编译不会失败 */
  /* lintOnSave='error'/'default': 开启保存检测，错误会导致编译失败（缺省是当前配置）*/
  lintOnSave: false,
  publicPath: "/",
  transpileDependencies: true,
  devServer: {
    proxy: {
      '/dev': {
        target: 'http://localhost:18880',
        pathRewrite: {'^/dev': ''},
        secure: false,
        changeOrigin: true,
        /* 默认值为true，会代理ws请求(支持WebSocket请求代理)，这在部分情况下会导致问题(当你代理了根目录并将其转发到了非本地vue服务时) */
        /* WebSocketClient.js:13  WebSocket connection to 'ws://192.168.xxx.xxx:8880/ws' failed: One or more reserved bits are on: reserved1 = 1, reserved2 = 0, reserved3 = 1 */
        /* 猜测：devServer生效时，会创建一个WebSocket服务器并存在一个WebSocket客户端使用心跳每秒与之建立连接以保持这个ws连接的可用性🤔️ */
        ws: false,
      }
    },
    disableHostCheck: true,
    port: 8880,
    open: true
  }
});
