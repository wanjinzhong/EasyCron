<template>
  <div v-loading="loading" class="jobs">
    <div>
      <div class="search">
        <el-input v-model="keyword" placeholder="任务名称" size="small" style="width: 200px;" clearable/>
        <el-select
          v-model="searchStatus"
          placeholder="状态"
          size="small"
          style="width: 200px; margin-left: 20px"
          multiple
          clearable
          collapse-tags
          automatic-dropdown>
          <el-option
            v-for="status in $store.state.listbox.jobStatus"
            :label="status.name"
            :key="status.id"
            :value="status.id">
            <svg-icon :icon-class="status.icon" :class="status.code" style="font-size: 18px"/><span style="margin-left: 5px">{{ status.name }}</span>
          </el-option>
        </el-select>
        <el-select
          v-model="selectedPlugin"
          placeholder="插件"
          size="small"
          style="width: 200px; margin-left: 20px"
          multiple
          clearable
          collapse-tags
          automatic-dropdown>
          <el-option
            v-for="plugin in $store.state.plugin.plugins"
            :key="plugin.id"
            :value="plugin.id"
            :label="plugin.name">
            <img :src="plugin.picture" style="width: 18px; vertical-align: middle"><span style="margin-left: 5px">{{ plugin.name }}</span>
          </el-option>
        </el-select>
        <el-button type="primary" size="small" style="margin-left: 20px" icon="el-icon-search" @click="reload">搜索
        </el-button>
      </div>
      <div style="float: right;">
        <el-button v-if="cronAddable" type="success" size="small" icon="el-icon-circle-plus-outline" @click="showNewJob = true">新建任务
        </el-button>
      </div>
    </div>
    <el-table :data="jobs">
      <el-table-column prop="name" label="名称"/>
      <el-table-column prop="cronReg" label="Cron表达式"/>
      <el-table-column prop="desc" label="描述"/>
      <el-table-column label="插件">
        <template slot-scope="scope">
          <img :src="scope.row.pluginPicId" style="width: 18px; vertical-align: middle"><span style="margin-left: 5px">{{ scope.row.plugin }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态">
        <template slot-scope="scope">
          <div v-if="scope.row.status === 'RUNNING'">
            <svg-icon icon-class="yes" style="color: #67C23A; font-size: 18px"/>&nbsp;&nbsp;正在运行
          </div>
          <div v-if="scope.row.status === 'STOPPED'">
            <svg-icon icon-class="stop" style="color: #F56C6C; font-size: 18px"/>&nbsp;&nbsp;已停止
          </div>
          <div v-if="scope.row.status === 'CONFIG_ERROR'">
            <svg-icon icon-class="warning" style="color: #F56C6C; font-size: 18px"/>&nbsp;&nbsp;配置错误
          </div>
          <div v-if="scope.row.status === 'WAITING_CONFIG'">
            <svg-icon icon-class="waiting" style="color: #E6A23C; font-size: 18px"/>&nbsp;&nbsp;等待配置
          </div>
        </template>
      </el-table-column>
      <el-table-column label="总运行次数" prop="count"/>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <div style="width: 124px; text-align: right">
            <el-tooltip :open-delay="500" class="item" effect="light" content="启动" placement="top-start">
              <svg-icon
                v-if="scope.row.status === 'STOPPED'&& scope.row.operable "
                icon-class="start"
                class="optBtn"
                style="color: #67C23A"
                @click="start(scope.row.id, scope.row.name)"/>
            </el-tooltip>
            <el-tooltip :open-delay="500" class="item" effect="light" content="停止" placement="top-start">
              <svg-icon
                v-if="scope.row.status === 'RUNNING' && scope.row.operable "
                icon-class="stop"
                class="optBtn"
                style="color: #F56C6C"
                @click="stop(scope.row.id, scope.row.name)"/>
            </el-tooltip>
            <el-tooltip :open-delay="500" class="item" effect="light" content="配置" placement="top-start">
              <svg-icon
                v-if=" scope.row.editable "
                icon-class="edit"
                class="optBtn"
                style="color: #409EFF"
                @click="config(scope.row.id)"/>
              &nbsp;&nbsp;
            </el-tooltip>
            <el-tooltip :open-delay="500" class="item" effect="light" content="查看日志" placement="top-start">
              <svg-icon
                v-if="scope.row.logVisiable"
                icon-class="log"
                class="optBtn"
                @click="showLogDialog(scope.row.id)"/>
            </el-tooltip>
            <el-tooltip :open-delay="500" class="item" effect="light" content="删除" placement="top-start">
              <svg-icon
                v-if="scope.row.editable"
                icon-class="delete"
                class="optBtn"
                style="color: #F56C6C"
                @click="toDelete(scope.row.id, scope.row.name)"/>
            </el-tooltip>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :current-page.sync="$store.state.job.page"
      :page-sizes="[5, 10, 20, 50, 100]"
      :page-size="$store.state.job.size"
      :total="$store.state.job.totalCount"
      style="margin-top: 10px"
      layout="sizes, prev, pager, next"
      @size-change="sizeChange"
      @current-change="currentChange"
    />
    <el-dialog
      :visible.sync="showConfig"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      title="配置任务"
      class="configDialog"
      width="80%">
      <JobConfig :id="configId" @close="showConfig=false"/>
    </el-dialog>
    <el-dialog
      :visible.sync="showNewJob"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      title="新建任务"
      width="50%">
      <NewJob @close="showNewJob=false"/>
    </el-dialog>
    <el-dialog
      :visible.sync="showDelete"
      title="删除任务"
      width="30%"
      @closed="closeDeleteDialog">
      <span style="font-weight: bold">删除任务会立即停止任务，同时删除对应的配置文件，日志等信息，且不可恢复。<br>如果确定要删除该任务，请在输入框中输入任务名称，并点击删除按钮</span>
      <el-input v-model="confirmDeleteName" size="small" style="margin-top: 10px"/>
      <el-button
        :disabled="confirmDeleteName !== deleteName"
        type="danger"
        style="margin-top: 10px; width: 100%;"
        @click="confirmedDelete">确定删除
      </el-button>
    </el-dialog>
    <el-dialog
      :visible.sync="showLog"
      title="日志"
      width="70%"
      @closed="closeDeleteDialog">
      <JobLog v-model="jobIdForLog" :show="showLog"/>
    </el-dialog>
  </div>
</template>

<script>
import JobConfig from '@/components/Job/JobConfig'
import NewJob from '@/components/Job/NewJob'
import { hasRole } from '@/utils/permission'
import JobLog from '@/components/JobLog'

export default {
  name: 'JobList',
  components: { JobConfig, NewJob, JobLog },
  data() {
    return {
      loading: false,
      interval: undefined,
      keyword: this.$store.state.job.keyword,
      searchStatus: this.$store.state.job.searchStatus,
      selectedPlugin: this.$store.state.job.selectedPlugin,
      showConfig: false,
      showNewJob: false,
      showDelete: false,
      showLog: false,
      jobIdForLog: 0,
      configId: 0,
      deleteId: '',
      deleteName: '',
      confirmDeleteName: '',
      cronAddable: hasRole('CRON_EDITOR')
    }
  },
  computed: {
    jobs() {
      return this.$store.state.job.jobs
    }
  },
  watch: {
    keyword(val) {
      this.$store.commit('SET_KEY_WORD', val)
    },
    searchStatus(val) {
      this.$store.commit('SET_SEARCH_STATUS', val)
    },
    selectedPlugin(val) {
      this.$store.commit('SET_SEARCH_PLUGINS', val)
    }
  },
  mounted() {
    this.reload(true)
    this.interval = setInterval(() => {
      this.reload(false)
    }, 5000)
  },
  methods: {
    reload(loadingNeed) {
      this.loading = loadingNeed
      this.$store.dispatch('getJobs').then((res) => {
        this.loading = false
      })
    },
    sizeChange(val) {
      this.$store.commit('SET_SIZE', val)
      this.reload(true)
    },
    currentChange(val) {
      this.$store.commit('SET_PAGE', val)
      this.reload(true)
    },
    config(id) {
      this.loading = true
      this.$store.dispatch('getConfig', id).then(res => {
        this.loading = false
        this.configId = id
        this.showConfig = true
      }).catch(() => {
        this.loading = false
      })
    },
    toDelete(id, name) {
      this.deleteId = id
      this.deleteName = name
      this.showDelete = true
    },
    confirmedDelete() {
      this.loading = true
      this.$store.dispatch('deleteJob', this.deleteId).then(res => {
        this.loading = false
        this.$message.success('删除成功')
        this.showDelete = false
        this.reload(true)
      }).catch(() => {
        this.loading = false
      })
    },
    closeDeleteDialog() {
      this.confirmDeleteName = ''
      this.deleteId = ''
      this.deleteName = ''
    },
    start(jobId, name) {
      this.$store.dispatch('startJob', jobId).then(res => {
        this.$message.success('已启动任务：' + name)
        this.reload(true)
      })
    },
    stop(jobId, name) {
      this.$store.dispatch('stopJob', jobId).then(res => {
        this.$message.success('已停止任务：' + name)
        this.reload(true)
      })
    },
    showLogDialog(jobId) {
      this.jobIdForLog = jobId
      this.showLog = true
    }
  }
}
</script>

<style scoped>

  .optBtn {
    margin-left: 5px;
    margin-right: 5px;
    font-size: 18px;
    cursor: pointer;
  }

  .search {
    float: left;
    margin-bottom: 10px;
  }

  .RUNNING{
    color: #67C23A
  }

  .STOPPED{
    color: #F56C6C
  }

  .CONFIG_ERROR{
    color: #F56C6C
  }

  .WAITING_CONFIG{
    color: #409EFF
  }

</style>
