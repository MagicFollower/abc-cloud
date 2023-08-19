const {defineConfig} = require('@vue/cli-service');
module.exports = defineConfig({
  lintOnSave: true,
  publicPath: "/",
  transpileDependencies: true,
  devServer: {
    proxy: {
      '/dev': {
        target: 'http://localhost:18880',
        pathRewrite: {'^/dev': ''},
        secure: false,
        changeOrigin: true,
        ws: false,
      }
    },
    port: 8880
  }
});
