export default {
  state: {
    chatsocket: null, 
    option: "friends", // friends/team
    chatuserid: "",
    chatusername: "",
    chatuserphoto: "",
    chatuserlastlogintime: "", 
    currentconents: null,
    receivecontents: [{}],
  },
  getters: {
  },
  mutations: {
    updateChatSocket(state, socket) {
      state.chatsocket = socket;
    },
    updateOption(state, option) {
      state.option = option;
    },
    updateChatUser(state, current) {
      state.chatuserid = current.id;
      state.chatusername = current.username;
      state.chatuserphoto = current.photo;
      state.chatuserlastlogintime = current.lastlogintime;
      state.currentconents = current.currentconents;
    },
    updatereceiveContents(state, receivecontents) {
      state.receivecontents = receivecontents;
    }
  },
  actions: {
    
  },
  modules: {
  }
};