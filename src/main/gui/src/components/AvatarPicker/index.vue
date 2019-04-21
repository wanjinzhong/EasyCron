<template>
  <div>
    <el-button size="small" class="btn" @click="toggleShow">设置头像</el-button>
    <img :src="imgDataUrl" style="width: 100px">
    <uploader
      v-model="show"
      :width="300"
      :height="300"
      field="img"
      url="/upload"
      img-format="png"
      @crop-success="cropSuccess"
      @crop-upload-success="cropUploadSuccess"
      @crop-upload-fail="cropUploadFail"/>
  </div>
</template>

<script>
import uploader from 'vue-image-crop-upload'

export default {
  comments: { uploader },
  data() {
    return {
      show: true,
      imgDataUrl: 'http://localhost:8089/easycron/public/api/resource/1' // the datebase64 url of created image
    }
  },
  methods: {
    toggleShow() {
      this.show = !this.show
    },
    /**
     * crop success
     *
     * [param] imgDataUrl
     * [param] field
     */
    cropSuccess(imgDataUrl, field) {
      console.log('-------- crop success --------')
      this.imgDataUrl = imgDataUrl
    },
    /**
     * upload success
     *
     * [param] jsonData   服务器返回数据，已进行json转码
     * [param] field
     */
    cropUploadSuccess(jsonData, field) {
      console.log('-------- upload success --------')
      console.log(jsonData)
      console.log('field: ' + field)
    },
    /**
     * upload fail
     *
     * [param] status    server api return error status, like 500
     * [param] field
     */
    cropUploadFail(status, field) {
      console.log('-------- upload fail --------')
      console.log(status)
      console.log('field: ' + field)
    }
  }
}
</script>

<style scoped>

</style>
