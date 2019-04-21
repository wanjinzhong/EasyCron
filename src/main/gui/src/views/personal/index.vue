<template>
  <div>
    <el-form label-width="100px" size="small">
      <el-form-item label="用户名" style="width: 400px">
        <el-input v-model="userName" :disabled="!editing"/>
      </el-form-item>
      <el-form-item label="头像">
        <avatar-picker/>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="email" :disabled="!editing"/>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import AvatarPicker from '@/components/AvatarPicker'
export default {
  components: { AvatarPicker },
  data() {
    return {
      userName: '',
      email: '',
      editing: false,
      avatar: ''
    }
  }
}
</script>

<style scoped>

</style>
