<template>
  <div>
    <div>
      <el-radio-group v-model="view" size="small">
        <el-radio-button value="1" label="用户视图"/>
        <el-radio-button value="2" label="角色视图"/>
      </el-radio-group>
      <el-button size="small" style="float: right;" type="success" icon="el-icon-plus" @click="showAddUser=true">添加帐户</el-button>
    </div>
    <div style="margin-top: 20px">
      <UserView v-show="view==='用户视图'"/>
      <RoleView v-show="view==='角色视图'"/>
    </div>
    <el-dialog
      :visible.sync="showAddUser"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :v-loading="adding"
      title="添加帐户"
      width="30%"
      @closed="close">
      <div v-if="addingStatus == 'FILLING'">
        <el-form ref="user" :model="user" :rules="rules">
          <el-form-item label="用户名" prop="name">
            <el-input v-model="user.name" size="small"/>
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="user.email" size="small"/>
          </el-form-item>
        </el-form>
        <div style="text-align: right">
          <el-button size="small" type="danger" @click="close">取消</el-button>
          <el-button size="small" type="success" @click="addUser">确定</el-button>
        </div>
      </div>
      <div v-else-if="addingStatus == 'SENDING'" style="text-align: center"><img :src="sending_img" style="width: 200px"></div>
      <div v-else-if="addingStatus == 'COMPLETE'" >
        <div style="display: inline-block; vertical-align: top"><svg-icon icon-class="yes" style="color: #67C23A; font-size: 40px"/></div>
        <div style="display: inline-block; margin-left: 15px">
          <div style="font-size: 18px; margin-bottom: 20px; font-weight: bold;">用户添加成功</div>
          <div>已将用户名密码发送至<strong><i>{{ addUserRes.email }}</i></strong></div>
        </div>
      </div>
      <div v-else>
        <div style="display: inline-block; vertical-align: top"><svg-icon icon-class="warning" style="color: #E6A23C; font-size: 40px"/></div>
        <div style="display: inline-block; margin-left: 15px">
          <div style="font-size: 18px; margin-bottom: 20px; font-weight: bold;">用户添加成功</div>
          <div style="margin-bottom: 5px">由于配置或者网络原因，邮件发送失败</div>
          <div style="margin-bottom: 5px">账号：<strong>{{ addUserRes.email }}</strong></div>
          <div style="margin-bottom: 5px">密码：<strong style="color: red">{{ addUserRes.password }}</strong> (只显示一次)</div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import UserView from './UserView'
import RoleView from './RoleView'
import SendingImg from '@/assets/mail_sending.gif'
export default {
  components: { UserView, RoleView },
  data() {
    return {
      view: '用户视图',
      showAddUser: false,
      user: {},
      adding: false,
      addingStatus: 'FILLING',
      sending_img: SendingImg,
      addUserRes: {},
      rules: {
        name: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    close() {
      // this.$refs['user'].resetFields()
      this.user = {}
      this.showAddUser = false
      this.addingStatus = 'FILLING'
    },

    addUser() {
      this.adding = true
      this.addingStatus = 'SENDING'
      this.$store.dispatch('addUser', this.user).then((res) => {
        this.adding = false
        const data = res.data.data
        this.addUserRes = data
        console.log(data)
        if (data.emailSuccess) {
          this.addingStatus = 'COMPLETE'
        } else {
          this.addingStatus = 'COMPLETE_WITH_ERROR'
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
