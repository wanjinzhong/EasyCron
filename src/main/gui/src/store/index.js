import Vue from 'vue'
import Vuex from 'vuex'
import app from './modules/app'
import errorLog from './modules/errorLog'
import permission from './modules/permission'
import tagsView from './modules/tagsView'
import user from './modules/user'
import job from './modules/job'
import listbox from './modules/listbox'
import plugin from './modules/plugin'
import role from './modules/role'
import getters from './getters'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    errorLog,
    permission,
    tagsView,
    user,
    job,
    listbox,
    plugin,
    role
  },
  getters
})

export default store
