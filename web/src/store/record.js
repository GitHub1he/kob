export default {
  state: {
    is_record: false,
    a_steps: "",
    b_steps: "",
    record_winer: "",
  },
  getters: {
  },
  mutations: {
    updateIsRecord(state, is_record) {
      state.is_record = is_record;
    },
    updateSteps(state, data){
      state.a_steps = data.a_steps;
      state.b_steps = data.b_steps;
    },
    updateRecordWiner(state, winer){
      state.record_winer = winer;
    }
  },
  actions: {
    
  },
  modules: {
  }
};