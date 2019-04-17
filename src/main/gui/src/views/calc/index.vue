<template>
  <div id="calc">
    <Calc :as-component="false"/>
  </div>
</template>

<script>
import Calc from '@/components/CronCalc'

export default {
  name: 'CronCalc',
  components: {
    Calc
  },
  data() {
    return {
      showCronBox: false,
      form: {
        cronExpression: ''
      }
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
