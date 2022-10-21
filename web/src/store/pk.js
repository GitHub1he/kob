
export default {
  state: {
    status: "matching", //matching 表示匹配界面,playing表示对战界面
    socket: null, 
    opponent_username: "", 
    opponent_photo: "",
    gamemap: null,
  },
  getters: {
  },
  mutations: {
    updateSocket(state, socket) {
      state.socket = socket;
    },
    updateOpponent(state, opponent) {
      state.opponent_username = opponent.username;
      state.opponent_photo = opponent.photo;
    },
    updateStatus(state, status) {
      state.status = status;
    },
    updateGamemap(state, gamemap) {
      state.gamemap = gamemap;
    }
  },
  actions: {
    
  },
  modules: {
  }
};