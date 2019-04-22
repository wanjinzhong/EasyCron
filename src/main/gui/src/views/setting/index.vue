<template>
  <div v-loading="loading">
    <div style="margin-bottom: 25px; border-bottom: #dcdfe6 1px solid">
      <div style="margin-bottom: 15px; font-weight: bold; font-size: 16px">邮箱配置&nbsp;&nbsp;
        <el-tooltip content="如果不正确配置邮箱，则用户无法修改密码。目前仅测试通过163邮箱" effect="light">
          <svg-icon style="color: grey" icon-class="ask"/>
        </el-tooltip>
      </div>
      <el-form inline label-width="80px" size="small">
        <el-form-item label="用户名">
          <el-input v-model="settings.email"/>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="settings.emailPwd" type="password"/>
        </el-form-item>
        <el-form-item label="服务器">
          <el-input v-model="settings.emailHost"/>
        </el-form-item>
        <el-form-item label="端口">
          <el-input-number v-model="settings.emailPort" :min="0" type="number"/>
        </el-form-item>
      </el-form>
    </div>
    <div style="width: 100%; text-align: right">
      <el-tooltip v-model="showReset" effect="light">
        <div slot="content">
          <svg-icon icon-class="warning" style="color: #E6A23C;"/>
          重置会丢失您的修改，确定重置吗？
          <el-button size="mini" type="success" @click="reset()">确定</el-button>
        </div>
        <el-button size="small" type="danger" @click="showReset = true">重置</el-button>
      </el-tooltip>
      <el-button size="small" type="success" @click="save()">保存</el-button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      settings: {},
      loading: false,
      showReset: false
    }
  },
  mounted() {
    this.reload()
  },
  methods: {
    reload() {
      this.loading = true
      this.$store.dispatch('getSettings').then(res => {
        this.loading = false
        this.settings = res.data.data
      }).catch(() => {
        this.loading = false
      })
    },
    reset() {
      this.reload()
      this.showReset = false
    },
    save() {
      this.loading = true
      this.$store.dispatch('saveSettings', this.settings).then(res => {
        if (res.data.data) {
          this.$message.success('保存成功')
        } else {
          this.$notify.warning('保存成功，但是邮件配置有误')
        }
        this.reload()
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style scoped>

</style>
