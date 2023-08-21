<template>
  <!-- 左右结构：el-aside + el-container -->
  <el-container style="height: 100%;">
    <el-aside width="auto" style="height: 100vh">
      <s-menu :is-collapse="isCollapse" />
    </el-aside>
    <el-container>
      <!-- 主体三部分结构: el-header + el-main + el-footer -->
      <el-header style="padding: 0">
        <s-head class="s-head" @on-togger="onTogger" />
      </el-header>

      <el-main>
        <slot />
      </el-main>

      <el-footer>
        <s-footer />
      </el-footer>
    </el-container>
  </el-container>
</template>

<script>
import SMenu from '../Menu/index.vue';
import SHead from '../Head/index.vue';
import SFooter from '../Footer/index.vue';
export default {
  name: 'Container',
  components: {
    SMenu,
    SHead,
    SFooter
  },
  data() {
    return {
      isCollapse: false
    };
  },
  methods: {
    onTogger(val) {
      this.isCollapse = val;
    }
  }
};
</script>

<style lang="scss" scoped>
.el-main {
  overflow: inherit;
}
</style>
