<template>
  <div>
    <el-table :data="users" :row-class-name="isRowDisabled">
      <el-table-column label="用户名">
        <template slot-scope="scope">
          <span style="margin-left: 5px">{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="email" label="邮箱"/>
      <el-table-column label="状态">
        <template slot-scope="scope">
          <svg-icon v-if="scope.row.statusCode === 'DISABLED'" icon-class="disabled" style="color: #F56C6C"/>
          <span style="margin-left: 2px">{{ scope.row.status }}</span>
        </template>
      </el-table-column>
      <el-table-column label="角色">
        <template slot-scope="scope">
          <el-tooltip v-for="role in scope.row.roles" :key="role.id" :content="role.desc" :open-delay="500" effect="light" placement="top">
            <el-tag :closable="role.deletable" type="success" style="margin: 5px 5px; cursor: default" @close="removeRole(scope.row.name, role.name, scope.row.id, role.id)">{{ role.name }}</el-tag>
          </el-tooltip>
          <el-popover
            trigger="click"
            @after-leave="selectedRole = ''">
            <el-select v-model="selectedRole">
              <el-option v-for="role in allowedRoles(scope.row)" :key="role.id" :value="role.id" :label="role.name"/>
            </el-select>
            <el-button :disabled="selectedRole === ''" type="primary" size="small" @click="addRole(scope.row.id, selectedRole)">确定</el-button>
            <el-button v-show="scope.row.roleAddable" slot="reference" size="mini" type="primary" icon="el-icon-plus" plain circle/>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-tooltip :open-delay="500" :content="scope.row.statusCode === 'DISABLED' ? '启用': '禁用'" class="item" effect="light" placement="top-start">
            <svg-icon
              v-if="scope.row.statusCode === 'DISABLED'"
              class="optBtn"
              icon-class="unlock"
              style="color: #F56C6C"
              @click="enableUser(scope.row)"/>
            <svg-icon
              v-else
              icon-class="lock"
              class="optBtn"
              style="color: #F56C6C"
              @click="disableUser(scope.row)"/>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'UserView',
  data() {
    return {
      selectedRole: ''
    }
  },
  computed: {
    users(val) {
      return this.$store.state.user.usersAsUserView
    }
  },
  mounted() {
    this.$store.dispatch('getUsersAsUserView')
  },
  methods: {
    isRowDisabled({ row, index }) {
      return row.statusCode === 'DISABLED' ? 'disabledLine' : ''
    },
    removeRole(userName, roleName, userId, roleId) {
      this.$confirm('确定要移除 “' + userName + '” 的 “' + roleName + '” 角色吗？', '移除角色', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('deleteUserRole', { userId, roleId }).then((res) => {
          this.$message.success('移除角色成功')
          this.$store.dispatch('getUsersAsUserView')
          this.$store.dispatch('getUsersAsRoleView')
        })
      })
    },
    allowedRoles(user) {
      const all = this.$store.state.user.usersAsRoleView
      const res = []
      for (const i in all) {
        let exists = false
        for (const j in user.roles) {
          if (all[i].id === user.roles[j].id) {
            exists = true
            break
          }
        }
        if (!exists) {
          res.push(all[i])
        }
      }
      return res
    },
    addRole(userId, roleId) {
      this.$store.dispatch('addUserRole', { userId, roleId }).then((res) => {
        this.$message.success('分配角色成功')
        this.$store.dispatch('getUsersAsUserView')
        this.$store.dispatch('getUsersAsRoleView')
      })
    },
    disableUser(user) {
      this.$confirm('确定要禁用 "' + user.name + '" 吗？', '禁用帐户', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const userId = user.id
        console.log(userId)
        this.$store.dispatch('disableUser', userId).then((res) => {
          this.$message.success('禁用帐户成功')
          this.$store.dispatch('getUsersAsUserView')
          this.$store.dispatch('getUsersAsRoleView')
        })
      })
    },
    enableUser(user) {
      this.$confirm('确定要启用 "' + user.name + '" 吗？', '启用帐户', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const userId = user.id
        console.log(userId)
        this.$store.dispatch('enableUser', userId).then((res) => {
          this.$message.success('启用帐户成功')
          this.$store.dispatch('getUsersAsUserView')
          this.$store.dispatch('getUsersAsRoleView')
        })
      })
    }
  }
}
</script>

<style>
.el-table .disabledLine{
  background: rgb(254, 240, 240)
}
.optBtn {
  margin-left: 5px;
  margin-right: 5px;
  font-size: 18px;
  cursor: pointer;
}

</style>
