<template>
  <div>
    <el-form ref="job" :model="job" :rules="rules" size="small" label-width="100px">
      <el-form-item label="任务名称" prop="name">
        <el-input v-model="job.name"/>
      </el-form-item>
      <el-form-item label="Cron表达式" prop="cron">
        <el-col :span="23">
          <el-input v-model="job.cron" style="display: inline"/>
        </el-col>
        <el-col :span="1">
          <el-popover effect="light" placement="left" trigger="click">
            <CronCalc @choose="(val) => job.cron=val"/>
            <svg-icon slot="reference" icon-class="calc" style="cursor: pointer; margin-left: 10px"/>
          </el-popover>
      </el-col></el-form-item>
      <el-form-item label="插件" prop="plugin">
        <el-select v-model="job.pluginId">
          <el-option v-for="plugin in $store.state.plugin.plugins" :key="plugin.id" :label="plugin.name" :value="plugin.id">
            <img :src="plugin.picture" style="width: 18px; vertical-align: middle"><span style="margin-left: 5px">{{ plugin.name }}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="描述" prop="desc">
        <el-input v-model="job.desc" type="textarea"/>
      </el-form-item>
    </el-form>
    <div style="text-align: right; margin-top: 20px">
      <el-button type="danger" @click="close">取消</el-button>
      <el-button type="primary" @click="save">保存</el-button>
    </div>
  </div>
</template>

<script>
import CronCalc from '@/components/CronCalc'
export default {
  components: { CronCalc },
  data() {
    return {
      job: {
        name: '',
        cron: '',
        desc: '',
        pluginId: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入任务名称', trigger: 'blur' }
        ],
        cron: [
          { required: true, message: '请输入Cron表达式', trigger: 'blur' }
        ],
        pluginId: [
          { required: true, message: '请选择插件', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    close() {
      this.job = {
        name: '',
        cron: '',
        desc: '',
        pluginId: ''
      }
      this.$emit('close')
    },
    save() {
      this.loading = true
      this.$store.dispatch('createNewJob', this.job).then(res => {
        this.$message({ type: 'success', during: 1000, message: '新建任务成功' })
        this.$store.dispatch('getJobs')
        this.loading = false
        this.close()
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style scoped>

</style>
