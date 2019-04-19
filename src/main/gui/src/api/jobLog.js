import request from '@/utils/request'

export function getJobLogs(data) {
  return request({
    url: '/logs',
    method: 'post',
    data
  })
}

export function getJobLogDetails(jobId) {
  return request({
    url: '/log/' + jobId + '/detail',
    method: 'get'
  })
}

export function cleanLog(jobId) {
  return request({
    url: '/log/job/' + jobId,
    method: 'delete'
  })
}
