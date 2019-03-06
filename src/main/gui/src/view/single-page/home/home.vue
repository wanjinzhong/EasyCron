<template>
  <div>

  </div>
</template>

<script>
export default {
  name: 'home',
  components: {},
  data () {
    return {
    }
  },
  mounted () {
    this.axios.get('userInfo').then(res => {
      const data = res.data.data
      this.$store.commit('setAvator', data.avatar)
      this.$store.commit('setUserName', data.name)
      this.$store.commit('setEmail', data.email)
      // commit('setAccess', data.access)
      this.$store.commit('setAccess', true)
      this.$store.commit('setHasGetInfo', true)
    })
  }
}
</script>

<style lang="less">
.count-style{
  font-size: 50px;
}
</style>
