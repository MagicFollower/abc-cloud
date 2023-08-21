export default {
  common: {
    home: 'Home',
    homeInfo: '🚀🚀🚀 Welcome Home 🚀🚀🚀',
    menuData: [
      {
        title: 'Product',
        child: [
          {
            title: 'ProductInfo',
            href: '/product-info'
          },
          {
            title: 'ProductType',
            href: '/product-type'
          }
        ]
      },
      {
        title: 'Help',
        child: [
          {
            title: 'HelpDocument',
            href: '/help'
          }
        ]
      }
    ],
    loginOut: 'Sign Out',
    dropdownList: [
      {
        title: '中文',
        command: 'zh-CN'
      },
      {
        title: 'English',
        command: 'en-US'
      }
    ],
    tableDataEmptyText: 'Empty',
  },
  btn: {
    add: 'Add',
  },
  login: {
    btnTxt: 'Login',
    labelUserName: 'Username',
    labelPassword: 'Password',
    inputTip: {
      pUserName: 'Please enter user name',
      pPassword: 'Please enter your password'
    },
  },
  loginExpireDialog: {
    msg: 'Login status expired, please log in again'
  },
  productInfo: {
    grid: {
      code: 'code',
      name: 'name',
      typeName: 'type',
      actPrice: 'actPrice',
      salePrice: 'salePrice',
      memo: 'memo',
      createUser: 'createUser',
      createTime: 'createTime',
      updateUser: 'updateUser',
      updateTime: 'updateTime'
    },
    addDialog: {
      title: 'add product',
      code: 'code',
      name: 'name',
      typeName: 'type',
      actPrice: 'actPrice',
      salePrice: 'salePrice',
      memo: 'memo',
      btnConfirmTxt: 'Confirm',
      btnCancelTxt: 'Cancel'
    },
    table: {
      operate: 'Operate',
      operateConnect: 'Connect',
      operateConnected: 'Connected',
      operateDel: 'Del',
      operateEdit: 'Edit'
    },
    rules: {
      code: 'Please enter the product code',
      name: 'Please enter the product code',
      typeName: 'Please enter the product type',
      actPrice: 'Please enter the product actPrice',
      salePrice: 'Please enter the product salePrice',
      memo: 'Please enter the product memo',
    }
  },
  productType: {
    grid: {
      code: 'code',
      name: 'name',
      memo: 'memo',
    },
    table: {
      operate: 'Operate',
      operateConnect: 'Connect',
      operateConnected: 'Connected',
      operateDel: 'Del',
      operateEdit: 'Edit'
    },
    rules: {
      code: 'Please enter the product type code',
      name: 'Please enter the product type code',
      memo: 'Please enter the product type memo',
    }
  },
  help: {
    design_concept_title: 'Design concept',
    design_concept_info_1: 'Concept1',
    design_concept_info_2: 'Concept2',
    major_features_title: 'Major features',
    major_features_info_1: 'Feature1',
    major_features_info_2: 'Feature2',
    major_features_info_3: 'Feature3',
    major_features_info_4: 'Feature4',
    major_features_info_5: 'Feature5',
    unsupported_title: 'Unsupported',
    unsupported_info: 'Unsupported1'
  }
};
