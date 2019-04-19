<template>
  <div style="height: 500px">
    <div v-loading="listLoading" style="width: 20%; overflow: auto; display: inline-block">
      <div style="margin-bottom: 20px">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="cleanLog()">清空日志</el-button>
      </div>
      <div v-if="logs === undefined || logs.length === 0" style="text-align: center;">
        没有数据
      </div>
      <div v-else>
        <div v-for="log in logs" :key="log.id" :id="'log' + log.id" class="logItem" @click="getLogDetail(log.id)">
          <svg-icon :icon-class="log.icon" :color="log.color" style="font-size: 25px"/>
          {{ new Date(log.startTime) | formatDate('DATETIME') }}
        </div>
        <el-pagination
          :total="total"
          :small="true"
          :current-page.sync="page"
          :page-size="size"
          :pager-count="paperCount"
          layout="prev, pager, next"
          style=" margin-top: 20px; text-align: center"
          @current-change="reload"/>
      </div>
    </div>
    <div v-loading="logDetailLoading" style="display: inline-block; width: 75%; margin-left: 3%; vertical-align: top; height: 100%">
      <el-alert :closable="false" :show-icon="true" :type="jobInfoType" style="height: 7%">
        <template slot="title">
          <div v-if="start === undefined">
            没有选中日志
          </div>
          <div v-else>{{ new Date(start) | formatDate('DATETIME') }}&nbsp;&nbsp;-&nbsp;&nbsp;{{ new Date(end) | formatDate('DATETIME') }}&nbsp;&nbsp;&nbsp;&nbsp;{{ during }}秒</div>
        </template>
      </el-alert>
      <div style="line-height: 23px; background: #4d4d4d; color: white; padding-left: 10px; padding-right: 10px; border-radius: 5px; height: 93%; overflow: auto;">
        <pre style="font-family: 'Consolas'">{{ logDetails }}</pre>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    value: {
      type: Number,
      default: 0
    },
    show: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      page: 1,
      size: 10,
      paperCount: 5,
      asc: false,
      latestId: 0,
      logs: {},
      chosenLogId: 0,
      listLoading: false,
      logDetails: '',
      total: 0,
      logDetailLoading: false,
      start: undefined,
      end: undefined,
      during: 0,
      jobInfoType: 'info'
    }
  },
  watch: {
    'value'(a, b) {
      this.reload()
    },
    'show'(a, b) {
      if (a) {
        this.reload()
      } else {
        this.clean()
      }
    },
    'chosenLogId'(a, b) {
      const ele1 = document.getElementById('log' + b)
      if (ele1 !== null) {
        ele1.classList.remove('chosenItem')
      }
      const ele2 = document.getElementById('log' + a)
      if (ele2 !== null) {
        ele2.classList.add('chosenItem')
      }
    }
  },
  mounted() {
    this.clean()
    this.reload()
  },
  methods: {
    clean() {
      this.start = undefined
      this.end = undefined
      this.during = 0
      this.logDetails = ''
      this.jobInfoType = 'info'
      this.chosenLogId = 0
      this.page = 1
      this.size = 10
      this.latestId = 0
      this.total = 0
    },
    reload() {
      const data = {
        jobId: this.value,
        asc: this.asc,
        page: this.page,
        size: this.size,
        latestId: this.latestId
      }
      this.listLoading = true
      this.$store.dispatch('getJobLogs', data).then(res => {
        const resData = res.data.data
        this.page = resData.page
        this.total = resData.totalCount
        this.logs = resData.data
        for (const i in this.logs) {
          switch (this.logs[i].status) {
            case 'SUCCEED':
              this.logs[i].icon = 'sun'
              this.logs[i].color = '#E6A23C'
              break
            case 'FAILED':
              this.logs[i].icon = 'thunder'
              this.logs[i].color = '#F56C6C'
              break
            case 'WARNED':
              this.logs[i].icon = 'rain'
              this.logs[i].color = '#606266'
              break
          }
        }
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
      })
    },
    getLogDetail(logId) {
      this.chosenLogId = logId
      this.logDetailLoading = true
      this.$store.dispatch('getJobLogDetails', logId).then(res => {
        const data = res.data.data
        this.start = data.startTime
        this.end = data.endTime
        this.during = data.during
        this.logDetails = data.message
        switch (data.status) {
          case 'SUCCEED':
            this.jobInfoType = 'success'
            break
          case 'FAILED':
            this.jobInfoType = 'error'
            break
          case 'WARNED':
            this.jobInfoType = 'warning'
            break
        }
        this.logDetailLoading = false
      }).catch(() => {
        this.logDetailLoading = false
      })
    },
    cleanLog() {
      this.listLoading = true
      this.$store.dispatch('cleanLog', this.value).then(res => {
        this.clean()
        this.reload()
      }).catch(() => {
        this.listLoading = false
      })
    }
  }
}
</script>

<style scoped>
  .logItem {
    height: 40px;
    line-height: 40px;
    vertical-align: middle;
    border-bottom: solid #DCDFE6 1px;
    cursor: pointer;
  }

  .logItem:hover {
    background-color: #F2F6FC;
  }

  .chosenItem{
    background-color: #EBEEF5;
  }
</style>
