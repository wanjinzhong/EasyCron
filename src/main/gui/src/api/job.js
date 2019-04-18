import request from '@/utils/request'

export function getJobs(data) {
  return request({
    url: '/jobs',
    method: 'post',
    data
  })
}

export function getConfig(id) {
  return request({
    url: '/configs/' + id,
    method: 'get'
  })
}

export function saveConfig(id, data) {
  return request({
    url: '/configs/' + id,
    method: 'put',
    data
  })
}

export function createNewJob(data) {
  return request({
    url: '/job',
    method: 'post',
    data
  })
}

export function deleteJob(id) {
  return request({
    url: '/job/' + id,
    method: 'delete'
  })
}

export function startJob(id) {
  return request({
    url: '/job/' + id + '/start',
    method: 'post'
  })
}

export function stopJob(id) {
  return request({
    url: '/job/' + id + '/stop',
    method: 'post'
  })
}
