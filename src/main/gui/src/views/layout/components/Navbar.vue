<template>
  <div class="navbar">
    <hamburger :toggle-click="toggleSideBar" :is-active="sidebar.opened" class="hamburger-container"/>

    <breadcrumb class="breadcrumb-container"/>

    <div class="right-menu">
      <template v-if="device!=='mobile'">
        <!--<search class="right-menu-item" />-->

        <!--<error-log class="errLog-container right-menu-item hover-effect"/>-->

        <screenfull class="right-menu-item hover-effect"/>

        <!--<el-tooltip :content="$t('navbar.size')" effect="dark" placement="bottom">-->
        <!--<size-select class="right-menu-item hover-effect"/>-->
        <!--</el-tooltip>-->

        <!--<lang-select class="right-menu-item hover-effect"/>-->

        <el-tooltip :content="$t('navbar.theme')" effect="dark" placement="bottom">
          <theme-picker class="right-menu-item hover-effect"/>
        </el-tooltip>
      </template>

      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          {{ name }}&nbsp;&nbsp;<svg-icon icon-class="down" style="font-size: 15px"/>
        </div>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item>
            <el-popover
              trigger="click"
              @after-leave="userName = $store.state.user.name">
              <div>
                <el-input v-model="userName" size="mini" style="width: 150px"/>
                <el-button :disabled="userName === ''" size="mini" type="success" @click="updateUserName">确定</el-button>
              </div>
              <span slot="reference" style="display:block;">
                <svg-icon icon-class="people"/>
                修改用户名</span>
            </el-popover>
          </el-dropdown-item>
          <el-dropdown-item>
            <span style="display:block;" @click="showChangePwd = true"><svg-icon icon-class="password"/> 修改密码</span>
          </el-dropdown-item>
          <el-dropdown-item>
            <span style="display:block;" @click="logout"><svg-icon icon-class="logout"/> {{ $t('navbar.logOut') }}</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <el-dialog :visible.sync="showChangePwd" title="修改密码" width="400px">
      <el-form v-model="changePwdData" size="small" label-width="70px" inline>
        <el-form-item label="验证码">
          <el-input v-model="valCode" style="width: 183px"/>
          <el-button :disabled="cutDown !== 0" type="small" style="width: 92px" @click="getValCode">{{ getValCodeTip }}</el-button>
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="pwd" style="width: 280px" type="password"/>
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="newPwd" style="width: 280px" type="password"/>
        </el-form-item>
        <div style="width: 100%; text-align: right">
          <el-button size="small" type="danger">取消</el-button>
          <el-button :disabled="passwordChangable" size="small" type="success" @click="changePwd">确定</el-button>
        </div>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import ErrorLog from '@/components/ErrorLog'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import LangSelect from '@/components/LangSelect'
import ThemePicker from '@/components/ThemePicker'
import Search from '@/components/HeaderSearch'

export default {
  components: {
    Breadcrumb,
    Hamburger,
    ErrorLog,
    Screenfull,
    SizeSelect,
    LangSelect,
    ThemePicker,
    Search
  },
  data() {
    return {
      userName: this.$store.state.user.name,
      changePwdData: {},
      cutDown: 0,
      showChangePwd: false,
      interval: undefined,
      valCode: '',
      pwd: '',
      newPwd: '',
      changePwdLoading: false
    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'name',
      'device'
    ]),
    getValCodeTip() {
      return this.cutDown === 0 ? '获取验证码' : this.cutDown
    },
    passwordChangable() {
      return this.valCode === '' || this.pwd === '' || this.pwd !== this.newPwd
    }
  },
  watch: {
    cutDown(a) {
      if (a === 0) {
        clearInterval(this.interval)
      }
    }
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('toggleSideBar')
    },
    logout() {
      this.$store.dispatch('LogOut').then(() => {
        location.reload()// In order to re-instantiate the vue-router object to avoid bugs
      })
    },
    updateUserName() {
      const data = {
        name: this.userName
      }
      this.$store.dispatch('updateUserName', data).then(() => {
        this.$store.dispatch('GetUserInfo')
      })
    },
    getValCode() {
      this.cutDown = 60
      this.interval = setInterval(() => {
        this.cutDown = this.cutDown - 1
      }, 1000)
      this.$store.dispatch('getValCode').catch(() => {
        this.cutDown = 0
      })
    },
    changePwd() {
      const data = {
        valCode: this.valCode,
        pwd: this.pwd
      }
      this.changePwdLoading = true
      this.$store.dispatch('changePwd', data).then(() => {
        this.$message.success('修改密码成功')
        this.changePwdLoading = false
        this.showChangePwd = false
      }).catch(reason => {
        this.changePwdLoading = false
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
