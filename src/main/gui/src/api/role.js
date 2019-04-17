import request from '@/utils/request'

export function getRoles() {
  return request({
    url: '/roles',
    method: 'get'
  })
}

export function deleteUserRole(userId, roleId) {
  return request({
    url: '/user/' + userId + '/role/' + roleId,
    method: 'delete'
  })
}

export function addUserRole(userId, roleId) {
  return request({
    url: '/user/' + userId + '/role/' + roleId,
    method: 'post'
  })
}
