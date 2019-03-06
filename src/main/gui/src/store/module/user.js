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
  }
}
