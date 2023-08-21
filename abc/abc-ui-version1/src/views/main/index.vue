<template>
  <el-container>
    <el-header>
      <el-card class="box-card" style="width: 100%;text-align: center;">
        {{ $t('common.homeInfo') }}
      </el-card>
    </el-header>
  </el-container>
</template>

<script>

export default {
  name: 'Main',
  components: {},
  data() {
    return {};
  },
  watch: {
    $route: {
      handler(route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  methods: {}
};
</script>

<style rel="stylesheet/scss" lang="scss">

</style>
