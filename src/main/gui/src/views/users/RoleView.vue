<template>
  <div>
    <el-table :data="$store.state.user.usersAsRoleView">
      <el-table-column prop="name" label="角色"/>
      <el-table-column prop="desc" label="描述"/>
      <el-table-column label="用户">
        <template slot-scope="scope">
          <el-tag v-for="user in scope.row.users" :key="user.id" :closable="scope.row.userDeletable" style="margin: 5px" @close="removeRole(user.name, scope.row.name, user.id, scope.row.id)">
            <span>{{ user.name }}</span>
          </el-tag>
          <el-popover
            trigger="click"
            @after-leave="selectedUser = ''">
            <el-select v-model="selectedUser">
              <el-option v-for="user in allowedUsers(scope.row)" :key="user.id" :value="user.id" :label="user.name"/>
            </el-select>
            <el-button :disabled="selectedUser === ''" type="primary" size="small" @click="addUser(scope.row.id, selectedUser)">确定</el-button>
            <el-button v-show="scope.row.userAddable" slot="reference" size="mini" type="primary" icon="el-icon-plus" plain circle/>
          </el-popover>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'RoleView',
  data() {
    return {
      selectedUser: ''
    }
  },
  mounted() {
    this.$store.dispatch('getUsersAsRoleView')
  },
  methods: {
    allowedUsers(role) {
      const all = this.$store.state.user.usersAsUserView
      const users = []
      for (const i in all) {
        if (all[i].statusCode === 'NORMAL') {
          users.push(all[i])
        }
      }
      const res = []
      for (const i in users) {
        let exists = false
        for (const j in role.users) {
          if (users[i].id === role.users[j].id) {
            exists = true
            break
          }
        }
        if (!exists) {
          res.push(users[i])
        }
      }
      return res
    },
    addUser(roleId, userId) {
      this.$store.dispatch('addUserRole', { userId, roleId }).then((res) => {
        this.$message.success('分配角色成功')
        this.$store.dispatch('getUsersAsUserView')
        this.$store.dispatch('getUsersAsRoleView')
      })
    },
    removeRole(userName, roleName, userId, roleId) {
      this.$confirm('确定要移除 “' + userName + '” 的 “' + roleName + '” 角色吗？', '移除角色', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('deleteUserRole', { userId, roleId }).then((res) => {
          this.$message.success('移除用户成功')
          this.$store.dispatch('getUsersAsUserView')
          this.$store.dispatch('getUsersAsRoleView')
        })
      })
    }
  }
}
</script>

<style scoped>

</style>
