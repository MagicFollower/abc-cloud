/* 引入Vue */
import Vue from 'vue';
/* 引入App和SPA路由(SAP: Single Page Application) */
import App from './App';
import router from './router';
/* 引入ElementUI并配置多语言 */
import ElementUI from 'element-ui';
import locale from 'element-ui/lib/locale/lang/en';
import VueI18n from 'vue-i18n';
import Language from './lang/index';
/* Vue状态管理 */
import store from './store';
import Vuex from 'vuex';
/* css样式 */
import 'element-ui/lib/theme-chalk/index.css';
import 'element-ui/lib/theme-chalk/base.css';
import 'normalize.css/normalize.css';
import '@/assets/styles/theme.scss';
import '@/assets/styles/index.scss';

Vue.config.productionTip = false;
Vue.use(ElementUI, { locale });
Vue.use(VueI18n);
Vue.use(Vuex);

// language setting init
const lang = localStorage.getItem('language') || 'en-US';
Vue.config.lang = lang;

// language setting
const locales = Language;
const mergeZH = locales['zh-CN'];
const mergeEN = locales['en-US'];

const i18n = new VueI18n({
  locale: lang,
  messages: {
    'zh-CN': mergeZH,
    'en-US': mergeEN
  }
});

new Vue({
  router,
  store,
  i18n,
  render: h => h(App)
}).$mount('#app');
