<template>
  <div v-loading="loading">
    <el-form :model="data" inline>
      <div v-for="(config, index) in data.configs" :key="index" style="margin-bottom: 25px; border-bottom: #dcdfe6 1px solid">
        <div style="margin-bottom: 15px; font-weight: bold; font-size: 16px">{{ config.groupName }}</div>
        <el-form-item v-for="(item, childIndex) in config.items" :key="childIndex" :label="item.name" style="margin: 5px 10px" size="small">
          <div v-if="config.groupName === '基本' && item.name ==='表达式'">
            <el-col :span="20">
              <el-input v-if="item.type === 'string'" v-model="item.value"/>
            </el-col>
            <el-col :span="1">
              <el-popover effect="light" placement="right" trigger="click">
                <CronCalc @choose="(val) => item.value = val"/>
                <svg-icon slot="reference" icon-class="calc" style="cursor: pointer; margin-left: 10px"/>
              </el-popover>
            </el-col>
          </div>
          <el-input v-else-if="item.type === 'string'" v-model="item.value"/>
          <el-input v-else-if="item.type === 'text'" v-model="item.value" type="textarea"/>
          <el-input v-else-if="item.type === 'password'" v-model="item.value" type="password" placeholder="******"/>
          <el-input-number
            v-else-if="item.type === 'integer' || item.type ==='decimal'"
            v-model="item.value"
            :max="item.max === null?Infinity:item.max"
            :min="item.min === null?-Infinity:item.min"
            controls-position="right"/>
          <el-switch v-else-if="item.type === 'bool'" v-model="item.value"/>
          <el-select
            v-else-if="item.type === 'single' || item.type === 'multiple'"
            :multiple="item.type === 'multiple'"
            v-model="item.value"
            :collapse-tags="item.type === 'multiple'"
            clearable
            size="small"
            automatic-dropdown>
            <el-option
              v-for="(option, optionIndex) in item.optional"
              :key="optionIndex"
              :value="option.id"
              :label="option.name"/>
          </el-select>
        </el-form-item>
      </div>
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
  props: {
    id: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      loading: false
    }
  },
  computed: {
    data() {
      return this.$store.state.job.configs
    }
  },
  watch: {
    configs(val) {
    }
  },
  methods: {
    close() {
      this.$emit('close')
    },
    save() {
      const data = { id: this.id, data: this.data }
      this.loading = true
      this.$store.dispatch('saveConfig', data).then(res => {
        this.$message({ type: 'success', during: 1000, message: '保存成功' })
        this.$store.dispatch('getConfig', this.id)
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
