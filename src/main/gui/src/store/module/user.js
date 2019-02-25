import {
  login,
  logout,
  getUserInfo
} from '@/api/user'
// import { setToken, getToken } from '@/libs/util'

export default {
  state: {
    userName: '',
    email: '',
    avatorImgPath: '',
    access: [],
    hasGetInfo: false
  },
  mutations: {
    setAvator (state, avatorPath) {
      state.avatorImgPath = avatorPath
    },
    setUserName (state, name) {
      state.userName = name
    },
    setEmail (state, email) {
      state.email = email
    },
    setAccess (state, access) {
      state.access = access
    },
    setHasGetInfo (state, status) {
      state.hasGetInfo = status
    }
  },
  getters: {
  },
  actions: {
    // 登录
    handleLogin ({ commit }, { email, password }) {
      email = email.trim()
      return new Promise((resolve, reject) => {
        login({
          email,
          password
        }).then(res => {
          resolve()
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 退出登录
    handleLogOut ({ state, commit }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('setAccess', [])
          resolve()
        }).catch(err => {
          reject(err)
        })
        // 如果你的退出登录无需请求接口，则可以直接使用下面三行代码而无需使用logout调用接口
        // commit('setToken', '')
        commit('setAccess', [])
        // resolve()
      })
    },
    // 获取用户相关信息
    getUserInfo ({ state, commit }) {
      return new Promise((resolve, reject) => {
        try {
          getUserInfo(state.token).then(res => {
            const data = res.data.data
            commit('setAvator', data.avatar)
            commit('setUserName', data.name)
            commit('setEmail', data.email)
            // commit('setAccess', data.access)
            commit('setAccess', true)
            commit('setHasGetInfo', true)
            resolve(data)
          }).catch(err => {
            reject(err)
          })
        } catch (error) {
          reject(error)
        }
      })
    }
  }
}
