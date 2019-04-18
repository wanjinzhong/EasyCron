<template>
  <div v-loading="listLoading">
    <div v-for="log in logs" :key="log.id">{{ log.startTime }}</div>
  </div>
</template>

<script>
export default {
  props: {
    value: {
      type: Number,
      default:
        0
    }
  },
  data() {
    return {
      page: 1,
      size: 20,
      asc: false,
      latestId: 0,
      logs: {},
      listLoading: false
    }
  },
  watch: {
    'value'(a, b) {
      this.reload()
    }
  },
  mounted() {
    this.reload()
  },
  methods: {
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
        this.logs = resData.data
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
      })
    }
  }
}
</script>

<style scoped>

</style>
