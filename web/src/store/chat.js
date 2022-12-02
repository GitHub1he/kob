export default {
  state: {
    chatsocket: null, 
    option: "friends", // friends/teams
    chatuserid: "",
    chatusername: "",
    chatuserphoto: "",
    chatuserlastlogintime: "", 
    currentconents: null,
    receivecontents: [{}],
    screencontents: [],
    searchoption: "people", // people , team
    isscreen: false,
    issearch: false,
    isteaminfo: false,
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
    updateCurrentContents(state, currentconent) {
      state.currentconents.push({
        content: currentconent.content,
        id: currentconent.id,
        user_name: currentconent.user_name,
        user_photo: currentconent.user_photo,
        is_oneself: currentconent.is_oneself,
        sendtime: currentconent.sendtime,
        status: currentconent.status,
      });
    },
    setReceiveContents(state, receivecontents) {
      state.receivecontents = receivecontents;
    },
    updateReceiveContents(state, receivecontent) {
      state.receivecontents.push({
        content: receivecontent.content.content,
        id: receivecontent.id,
        name: receivecontent.name,
        photo: receivecontent.photo,
        last_login_time: receivecontent.last_login_time,
        is_oneself: receivecontent.content.is_oneself,
        sendtime: receivecontent.content.sendtime,
        status: receivecontent.content.status,
      });
    },
    removeReceiveToCurrentById(state, userid) {
      for(var receivecontent in state.receivecontents){
        var t = state.receivecontents[receivecontent];
        if(parseInt(t.id) === parseInt(userid)){
          state.currentconents.push({
            content: t.content,
            id: state.currentconents.length,
            is_oneself: t.is_oneself,
            sendtime: t.sendtime,
            status: t.status,
          });
        }
      }
      state.receivecontents.filter(function(item){
        return item.id != userid
      })
    },
    updateScreenContents(state, screencontent) {
      state.screencontents.push({
        id: screencontent.id,
        name: screencontent.name,
        photo: screencontent.photo,
        sendtime: screencontent.sendtime,
        content: screencontent.content,
      });
    },
    updateSearchOption(state, searchoption) {
      state.searchoption = searchoption;
    },
    updateIsSearch(state, issearch) {
      state.issearch = issearch;
    },
    updateIsScreen(state, isscreen) {
      state.isscreen = isscreen;
    },
    updateIsTeamInfo(state, isteaminfo) {
      state.isteaminfo = isteaminfo;
    }
  },
  actions: {
    
  },
  modules: {
  }
};