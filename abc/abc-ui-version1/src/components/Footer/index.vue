<template>
  <div
    class="footer-wrapper"
  >Copyright © The Apache Software Foundation, Licensed under the Apache License 2</div>
</template>
<script>
export default {
  name: 'Footer'
};
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;

.footer-wrapper {
  color: $dark_gray;
  width: 100%;
  font-size: 14px;
  line-height: 60px;
  text-align: center;
}
</style>
