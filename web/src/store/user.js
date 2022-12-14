import $ from 'jquery'
// import jwt_decode from 'jwt-decode';

export default {
  state: {
    id: "", 
    username: "", 
    photo: "",
    followercount: "",
    token: "",
    is_login: false,
    flag: 0, // 0->空间  2->发现 3->粉丝 4->关注
    is_me: false,
  },
  getters: {
  },
  mutations: {
    updateUser(state, user) {
      state.id = user.id;
      state.username = user.username;
      state.photo = user.photo;
      state.is_login = user.is_login;
      state.followercount = user.followercount;
    },
    updateToken(state, token) {
      state.token = token;
    },
    logout(state) {
      state.id = "";
      state.username = "";
      state.photo = "";
      state.followercount = "";
      state.token = "";
      state.is_login = false;
    },
    updateFlag(state, flag) {
      state.flag = flag;
    },
    updateIsme(state, is_me) {
      state.is_me = is_me;
    }
  },
  actions: {
    login(context, data) {
      $.ajax({
        url: "http://127.0.0.1:3000/api/user/account/token/",
        type: "post",
        data: {
          username: data.username,
          password: data.password,
        },
        success(resp) {
          if(resp.error_message === "success"){
            localStorage.setItem("jwt_token", resp.token);
            context.commit("updateToken", resp.token);
            data.success(resp);
          }else {
            data.error(resp);
          }
        },
        error(resp) {
          data.error(resp);
        }
      })
    },
    getinfo(context,data){
      $.ajax({
        url: "http://127.0.0.1:3000/api/user/account/info/",
        type: "GET",
        headers:{
          'Authorization': "Bearer " + context.state.token,
        }, 
        success(resp) {
          if(resp.error_message === "success") {
            context.commit("updateUser",{
              ...resp,
              is_login: true,
            });
            data.success(resp);
          } else {
            data.error(resp);
          }
        },
        error(resp) {
          data.error(resp);
        }
      })
    },
    logout(context) {
      localStorage.removeItem("jwt_token");
      context.commit("logout");
    },
  },
  modules: {
  }
};