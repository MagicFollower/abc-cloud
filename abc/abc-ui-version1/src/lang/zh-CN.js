export default {
  common: {
    home: '主页',
    homeInfo: '🚀🚀🚀 欢迎 🚀🚀🚀',
    menuData: [
      {
        title: '商品',
        child: [
          {
            title: '商品信息',
            href: '/product-info'
          },
          {
            title: '商品类型',
            href: '/product-type'
          }
        ]
      },
      {
        title: '帮助',
        child: [
          {
            title: '帮助文档',
            href: '/help'
          }
        ]
      }
    ],
    loginOut: '退出登录',
    dropdownList: [
      {
        title: '中文',
        command: 'Chinese'
      },
      {
        title: 'English',
        command: 'English'
      }
    ],
    tableDataEmptyText: '暂无数据',
  },
  btn: {
    add: '添加',
  },
  login: {
    btnTxt: '登录',
    labelUserName: '用户名',
    labelPassword: '密码',
    inputTip: {
      pUserName: '请输入用户名',
      pPassword: '请输入密码'
    },
  },
  loginExpireDialog: {
    msg: '登录状态过期，请重新登录'
  },
  productInfo: {
    grid: {
      code: '编码',
      name: '名称',
      typeName: '类型',
      actPrice: '实际价格',
      salePrice: '销售价格',
      memo: '备注',
      createUser: '创建用户',
      createTime: '创建时间',
      updateUser: '更新用户',
      updateTime: '更新时间'
    },
    addDialog: {
      title: '添加商品',
      code: '编码',
      name: '名称',
      typeName: '类型',
      actPrice: '实际价格',
      salePrice: '销售价格',
      memo: '备注',
      btnConfirmTxt: '确定',
      btnCancelTxt: '取消'
    },
    table: {
      operate: '操作',
      operateConnect: '连接',
      operateConnected: '已连接',
      operateDel: '删除',
      operateEdit: '编辑'
    },
    rules: {
      code: '请输入商品编码',
      name: '请输入商品名称',
      typeName: '请选择商品类型',
      actPrice: '请输入商品实际价格',
      salePrice: '请输入商品销售价格',
      memo: '请输入商品备注',
    }
  },
  productType: {
    grid: {
      code: '编码',
      name: '名称',
      memo: '备注',
    },
    table: {
      operate: '操作',
      operateConnect: '连接',
      operateConnected: '已连接',
      operateDel: '删除',
      operateEdit: '编辑'
    },
    rules: {
      code: '请输入商品类型编码',
      name: '请输入商品类型名称',
      memo: '请输入商品类型备注',
    }
  },
  help: {
    design_concept_title: '设计理念',
    design_concept_info_1: '理念1',
    design_concept_info_2: '理念2',
    major_features_title: '主要功能',
    major_features_info_1: '功能1',
    major_features_info_2: '功能2',
    major_features_info_3: '功能3',
    major_features_info_4: '功能4',
    major_features_info_5: '功能5',
    unsupported_title: '不支持项',
    unsupported_info: '不支持项1'
  }
};
