module.exports = {
  root: true,
  env: {
    // 表示在当前代码环境中启用了 Node.js 相关的全局变量和规则。
    //   → 设置为 { node: true } 可以让 ESLint 知道代码被执行在 Node.js 的环境中，从而正确地处理 Node.js 相关的全局变量和规则。
    //   → 一个环境定义了一个一组预定义的全局变量。比如node环境的全局变量是global，但是浏览器环境的全局对象是window。
    node: true,
    browser: true,
    es6: true,
  },

  // extends用于扩展和继承其他 ESLint 配置。
  extends: [
    // 表示扩展了名为 vue/essential 的插件。这个插件提供了一组用于 Vue.js 项目的基本规则。通过扩展该插件，可以启用适用于 Vue.js 项目的默认规则。
    'plugin:vue/essential',
    // 表示继承了名为 eslint:recommended 的配置。这是 ESLint 官方推荐的一组规则，涵盖了一系列常见的 JavaScript 最佳实践和代码质量规则。
    // "eslint:recommended" 规则集加载并启用了一组常见的规则，但在 rules 中你仍然需要显式地根据自己的需求定义和配置规则的行为。
    'eslint:recommended'
  ],
  parserOptions: {
    'parser': '@babel/eslint-parser'
  },
  rules: {
    // 允许代码中使用console
    'no-console': 'off',

    // 空行最多不能超过2行
    'no-multiple-empty-lines': [1, {'max': 2}],

    // Vue ESLint 插件提供的规则: 禁用对无效结束标签的解析错误的检查。即当出现无效结束标签时，ESLint 不会将其视为解析错误。
    'vue/no-parsing-error': [2, {
      'x-invalid-end-tag': false
    }],
    // 组件名必须要多个单词检测（关闭）
    'vue/multi-word-component-names': 'off',

    // 禁止对const变量重新赋值
    'no-const-assign': 2,

    // 变量声明后却不使用检测[0关闭，1警告，2错误]
    'no-unused-vars': [0, {
      'vars': 'all',
      'args': 'none'
    }],
    'quotes': [2, 'single'],    // 单引号
    'semi': [2, 'always'],      // 强制分号
    'indent': ['error', 2],     // 缩进量('indent': [2, 2]效果是一致的)
    'no-var': 2,                // 禁用var，用let和const代替
    'camelcase': 0,             // 强制驼峰法命名（0关闭）
    'eqeqeq': 1,                // 要求使用 === 和 !== 代替 == 和 != 操作符
    'no-eq-null': 2             // 禁止对null使用==或!=运算符,相应地，推荐使用严格相等运算符（=== 或 !==）来进行比较。

  }
};
