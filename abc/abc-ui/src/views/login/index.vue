<template>
  <div class="login-container">
    <el-form
        ref="loginForm"
        :model="loginForm"
        class="login-form"
        auto-complete="off"
        label-position="left"
    >
      <h3 class="title"/>
      <el-form-item prop="username">
        <el-input
            v-model="loginForm.username"
            name="username"
            type="text"
            auto-complete="off"
            :placeholder="labelUsername"
        >
          <i slot="prefix" class="icon-user icon-iem"/>
        </el-input>
      </el-form-item>
      <p style="color: white;" v-if="pUserNameTip">{{$t("login.inputTip.pUserName")}}</p>
      <el-form-item prop="password">
        <el-input
            :type="pwdType"
            v-model="loginForm.password"
            name="password"
            auto-complete="on"
            :placeholder="labelPassword"
            @keyup.enter.native="handleLogin"
        >
          <i slot="prefix" class="icon-password icon-iem"/>
        </el-input>
      </el-form-item>
      <p style="color: white;" v-if="pPasswordTip">{{$t("login.inputTip.pPassword")}}</p>
      <el-form-item class="btn-login">
        <el-button
            :loading="loading"
            type="primary"
            style="width:100%;"
            @click.native.prevent="handleLogin"
        >{{ $t("login.btnTxt") }}
        </el-button>
      </el-form-item>
    </el-form>
    <s-footer style="position: fixed;bottom: 0;"/>
  </div>
</template>

<script>
import SFooter from '../../components/Footer/index';
import API from './api';
import {sha512Base64} from '@/utils/sha512.js';
import _ from 'lodash';

export default {
  name: 'Login',
  components: {
    SFooter
  },
  data() {
    return {
      labelUsername: this.$t('login.labelUserName'),
      labelPassword: this.$t('login.labelPassword'),
      pUserNameTip: false,
      pPasswordTip: false,
      loginForm: {
        username: '',
        password: ''
      },
      loading: false,
      pwdType: 'password',
      redirect: undefined
    };
  },
  watch: {
    $route: {
      handler(route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  created() {
    if (window.localStorage.getItem('Access-Token')) {
      location.href = '#/registry-center';
    }
  },
  methods: {
    handleLogin: _.debounce(
      function () {
        // 数据检测
        this.pUserNameTip = _.trim(this.loginForm.username).length === 0;
        this.pPasswordTip = _.trim(this.loginForm.password).length === 0;
        if(this.pUserNameTip || this.pPasswordTip) return;
        const params = {
          username: this.loginForm.username,
          password: sha512Base64(this.loginForm.password)
        };
        API.getLogin(params).then(res => {
          const data = res.result;
          const store = window.localStorage;
          store.setItem('Access-Token', data.accessToken);
          store.setItem('username', data.username);
          location.href = '#/registry-center';
        });
      }, 200
    )
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
$bg: #000000;
$dark_gray: #889aa4;

/* reset element-ui css */
.login-container {
  /* 数据自动填充时，input框改变颜色 */
  .el-input {
    display: inline-block;
    // height: 47px;
    width: 100%;

    input {
      background: transparent;
      border: 0;
      -webkit-appearance: none;
      border-radius: 6px;
      padding: 12px 5px 12px 60px;
      color: $dark_gray;
      // height: 47px;
      &:-webkit-autofill {
        -webkit-box-shadow: 0 0 0 1000px $bg inset !important;
        /* 自动填充的文本颜色 */
        -webkit-text-fill-color: #fff !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 6px;
    color: #454545;
  }

  .el-form-item__content {
    background: #070601;
    border-radius: 6px;
  }

  .icon-iem {
    margin: 8px 7px;
    width: 24px;
    height: 24px;
    display: inline-block;
  }

  .icon-user {
    background: url('../../assets/img/user.png') no-repeat left center;
  }

  .icon-password {
    background: url('../../assets/img/password.png') no-repeat left center;
  }

  .btn-login {
    margin-top: 50px;
  }
}
</style>

<style rel="stylesheet/scss" lang="scss" scoped>
$dark_gray: #889aa4;
.login-container {
  position: fixed;
  height: 100%;
  width: 100%;
  // background-color: $bg;
  background: url('../../assets/img/bg.png') no-repeat center center / cover;

  .login-form {
    position: absolute;
    left: 0;
    right: 0;
    width: 520px;
    max-width: 100%;
    padding: 35px 35px 15px 35px;
    margin: 120px auto;
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title {
    margin: 0px auto 40px auto;
    height: 86px;
    background: url('../../assets/img/login-logo.png') no-repeat center center;
  }
}

.footer-copy-right {
  width: 100%;
  line-height: 30px;
  position: absolute;
  bottom: 0;
  text-align: center;
}
</style>
