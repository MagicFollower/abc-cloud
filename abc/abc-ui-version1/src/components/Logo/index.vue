<template>
  <div class="s-pro-components-sider-menu-index-logo">
    <a href="/#/">
      <img src="../../assets/img/sidebar-logo.png" alt="logo" />
      <img class="collapse-logo" src="../../assets/img/logo.png" alt="logo" />
    </a>
  </div>
</template>
<script>
export default {
  name: 'Logo'
};
</script>
<style lang="scss">
.s-pro-components-sider-menu-index-logo {
  height: 64px;
  position: relative;
  line-height: 64px;
  transition: all 0.3s;
  background: #2d2b2a;
  overflow: hidden;
  text-align: center;
  img {
    display: inline-block;
    vertical-align: middle;
    height: 35px;
  }
  h1 {
    color: #fff;
    display: inline-block;
    vertical-align: middle;
    font-size: 20px;
    margin: 0 0 0 12px;
    font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
    font-weight: 600;
  }
  .collapse-logo {
    display: none;
  }
}
</style>
