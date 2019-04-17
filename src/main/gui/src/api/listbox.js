import request from '@/utils/request'

export function getJobStatus() {
  return request({
    url: '/jobStatus',
    method: 'get'
  })
}

