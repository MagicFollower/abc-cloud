import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export const constantRouterMap = [
    {
        path: '/',
        component: () => import('@/views/login'),
        hidden: true
    },
    {
        path: '/login',
        component: () => import('@/views/login'),
        name: 'Login',
        hidden: true
    },
    {
        path: '/registry-center',
        component: () => import('@/views/registry-center'),
        hidden: true,
        name: 'Registry center'
    },
    {
        path: '/data-source',
        component: () => import('@/views/data-source'),
        hidden: true,
        name: 'Data source'
    },
    {
        path: '/operation-jobs',
        component: () => import('@/views/operation-jobs'),
        hidden: true,
        name: 'Operation-jobs'
    },
    {
        path: '/operation-jobs/status-detail',
        component: () => import('@/views/operation-jobs-detail'),
        hidden: true,
        name: 'Operation-jobs-detail'
    },
    {
        path: '/operation-servers',
        component: () => import('@/views/operation-servers'),
        hidden: true,
        name: 'Operation-servers'
    },
    {
        path: '/operation-servers/status-detail',
        component: () => import('@/views/operation-servers-detail'),
        hidden: true,
        name: 'Operation-servers-detail'
    },
    {
        path: '/job-help',
        component: () => import('@/views/help'),
        hidden: true,
        name: 'Help'
    },
    {
        path: '/history-trace',
        component: () => import('@/views/history-trace'),
        hidden: true,
        name: 'History trace'
    },
    {
        path: '/history-status',
        component: () => import('@/views/history-status'),
        hidden: true,
        name: 'History status'
    }
];


/**
 * 这段代码使用了ES6的模块导出方式，导出了一个一个Vue Router的实例。
 * 现在解释每个属性的含义：
 *   1.scrollBehavior: 路由跳转时的滚动行为配置。这里使用了箭头函数，返回一个对象{ y: 0 }，表示每次路由切换时，页面会滚动到顶部。
 *   2.routes: 路由配置数组。constantRouterMap是一个常量，这里假设它是一个预定义的路由配置数组。该数组包含了所有的路由配置项。
 */
const router = new Router({
    mode: 'hash',
    scrollBehavior: () => ({y: 0}),
    routes: constantRouterMap
});

// 导航守卫
router.beforeEach((to, from, next) => {
    /* 根据具体的登录逻辑判断用户是否已登录 */
    const isAuthenticated = window.localStorage.getItem('Access-Token');
    // 判断用户是否已登录，如果已登录，允许导航到指定页面，否则跳转到登录页面
    if (to.name !== 'Login' && !isAuthenticated) {
        next({name: 'Login'});
    } else {
        next();
    }
});

export default router;

