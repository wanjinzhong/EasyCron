<template>
  <div id="calc">
    <el-form :model="form">
      <el-form-item style="margin-bottom:0px;">
        <cron v-model="form.cronExpression"/>
        <span style="color: #E6A23C; font-size: 12px;">corn从左到右（用空格隔开）：秒 分 小时 月份中的日期 月份 星期中的日期 年份</span>
      </el-form-item>
      <el-form-item>
        <el-col :span="2">
          <span style="font-weight: bold">Cron</span>
        </el-col>
        <el-col :span="19">
          <el-input id="cronExpression" v-model="form.cronExpression" auto-complete="off"/>
        </el-col>
        <el-col :span="3">
          <el-button v-if="asComponent" type="success" @click="choose">确定</el-button>
          <el-button v-else type="success" data-clipboard-target="#cronExpression" class="copy">复制</el-button>
        </el-col>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import cron from './cron/cron'
import Clipboard from 'clipboard'
import { Message } from 'element-ui'
const clipboard = new Clipboard('.copy')
clipboard.on('success', function(e) {
  Message.success('Copied to clipboard')
})
clipboard.on('error', function(e) {
  Message.error('Copy to clipboard failed')
})
export default {
  name: 'CronCalc',
  components: {
    cron
  },
  props: {
    asComponent: {
      type: Boolean,
      default: true
    }},
  data() {
    return {
      showCronBox: false,
      form: {
        cronExpression: ''
      }
    }
  },
  methods: {
    choose() {
      this.$emit('choose', this.form.cronExpression)
    }
  }
}
</script>

<style>
  #calc {
    /*font-family: 'Avenir', Helvetica, Arial, sans-serif;*/
    /*-webkit-font-smoothing: antialiased;*/
    /*-moz-osx-font-smoothing: grayscale;*/
    /*text-align: center;*/
    /*width: 700px;*/
    /*color: #2c3e50;*/
  }
</style>
