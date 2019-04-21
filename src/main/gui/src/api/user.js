import request from '@/utils/request'

export function getUsersAsUserView() {
  return request({
    url: '/users/userView',
    method: 'get'
  })
}

export function getUsersAsRoleView() {
  return request({
    url: '/users/roleView',
    method: 'get'
  })
}

export function disableUser(userId) {
  return request({
    url: 'user/' + userId + '/disable',
    method: 'put'
  })
}

export function enableUser(userId) {
  return request({
    url: 'user/' + userId + '/enable',
    method: 'put'
  })
}

export function addUser(data) {
  return request({
    url: 'user',
    method: 'post',
    data
  })
}

export function updateUserName(data) {
  return request({
    url: 'user/name',
    method: 'put',
    data
  })
}

export function getValCode(userId) {
  return request({
    url: 'user/' + userId + '/valCode',
    method: 'get'
  })
}
