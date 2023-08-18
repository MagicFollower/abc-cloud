<template>
  <!-- 首先使用v-if指令来判断localStorage中是否存在"Access-Token"，如果存在，则渲染<s-container>组件，否则渲染<router-view>组件。 -->
  <div>
    <s-container v-if="localStorage.getItem('Access-Token')">
      <!-- <s-container>组件包括三部分：el-breadcrumb面包屑导航、el-breadcrumb-item面包屑导航项和router-view路由视图。 -->
      <!-- el-breadcrumb是Element UI库的一个面包屑导航组件，通过:class绑定了"bread-wrap"样式类。它使用el-breadcrumb-item组件作为导航项。 -->
      <el-breadcrumb separator="/" class="bread-wrap">
        <!-- :to="{ path: '/' }"是Vue中动态绑定属性的语法。它通过v-bind指令将一个对象绑定到to属性上。
             这个对象的path属性值为'/'，表示导航项的链接指向根路径。 -->
        <!-- $t('common.home')是Vue国际化特性的使用方式。$t是Vue I18n插件提供的用于翻译文本的方法。
             传递给$t的参数是一个字符串'common.home'，表示要翻译的文本在语言包中的路径。
             它会根据当前语言环境找到对应的翻译文本，然后将翻译后的文本显示在面包屑导航项中。 -->
        <el-breadcrumb-item :to="{ path: '/' }">{{
            $t('common.home')
          }}
        </el-breadcrumb-item>
        <!-- el-breadcrumb-item使用v-for指令遍历menus数组，并使用{{ each }}来显示每个数组项的值。 -->
        <el-breadcrumb-item v-for="each in menus" :key="each">
          {{ each }}
        </el-breadcrumb-item>
      </el-breadcrumb>
      <!-- <router-view/>是Vue Router提供的一个占位符组件。
           它的作用是在Vue.js应用中展示匹配到的路由组件，用于实现SPA（单页面应用）的路由功能。
           它可以将不同的路径与对应的组件关联起来，实现路由切换和组件展示。 -->
      <!-- <router-view/>被放置在组件模板的合适位置。当浏览器URL与路由规则匹配时，匹配到的组件将会被渲染到<router-view/>中。 -->
      <!-- 在模板中使用<router-view/>很简单，只需将其放置在想要渲染路由组件的位置即可。
           Vue Router会自动根据当前URL匹配到的路由规则，将对应的组件渲染到<router-view/>中。 -->
      <router-view/>
    </s-container>
    <template v-else>
      <router-view/>
    </template>
  </div>
</template>

<script>
import SContainer from '@/components/Container/index.vue';

export default {
  name: 'App',
  components: {
    SContainer
  },
  data() {
    return {
      menus: [],
      localStorage: window.localStorage
    };
  },
  watch: {
    $route(to, from) {
      for (const parentMenuItem of this.$t('common').menuData) {
        if (!parentMenuItem.child) {
          if (parentMenuItem.href === to.path) {
            this.menus = [parentMenuItem.title];
            break;
          } else {
            continue;
          }
        }
        for (const childMenuItem of parentMenuItem.child) {
          if (childMenuItem.href === to.path) {
            this.menus = [parentMenuItem.title, childMenuItem.title];
            break;
          }
        }
      }
    }
  }
};
</script>
<style lang="scss" scoped>
.bread-wrap {
  margin-bottom: 15px;
}
</style>
