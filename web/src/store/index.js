import { createStore } from 'vuex'
import ModuleUser from '@/store/user'
import MoudlePK from '@/store/pk'

export default createStore({
  state: {
  },
  getters: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    user: ModuleUser,
    pk: MoudlePK,
  }
})
