<template>
  <div class="card my">
    <div class="card-body">
      <button @click="click_myspace" type="button" class="btn chatlist-btn btn-outline-info" style="height: 100%">
        <img class="img-fluid" :src="$store.state.user.photo" alt="用户头像">
        &nbsp;
        <div class="username"> {{ $store.state.user.username }} </div>
      </button>
    </div>
  </div>
  <button @click="click_option_friends" type="button" :class="$store.state.chat.option === 'friends' ? 'btn btn-check-isfriend btn-outline-success active' : 'btn btn-check-isfriend btn-outline-success'">好友</button>
  <button @click="click_option_team" type="button" :class="$store.state.chat.option === 'team' ? 'btn btn-check-isfriend btn-outline-success active' : 'btn btn-check-isfriend btn-outline-success'">群聊</button>
  <div class="card chatlistprofile">
    <div class="card-body"> 
      <div class="card chatlist-item" v-for="user in users.value" :key="user.id"> 
        <div class="card-body"> 
          <button @click="check_chatuser(user)" type="button" class="btn chatlist-btn btn-outline-info">
            <img :src="user.photo" alt="">
            <span >{{ user.name }}</span>
            <span class="status" :style="user.contents[user.contents.length-1].status === 0 ? 'color : red' : 'color : green'">*</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import router from '@/router';
import { useStore } from 'vuex';
export default {
  name: "ChatList",
  props: {
    users: {
      type: Object,
      required: true,
    },
  },
  setup() {
    const store = useStore();
    const click_myspace = () => {
      store.commit("updateFlag",0);
      router.push({
        name: 'userprofile',
        params: {userId: store.state.user.id},
      });
    };

    const click_option_friends = () => {
      store.state.chat.chatsocket.send(JSON.stringify({
        event: "receive-message",
      }));
      store.commit('updateOption', "friends");
    };
    const click_option_team = () => {
      store.commit('updateOption', "team");
    };

    const check_chatuser = user => {
      store.commit("updateChatUser", {
        id: user.id,
        username: user.name,
        photo: user.photo, 
        lastlogintime: user.last_login_time,
        currentconents: user.contents,
      });

      for(var receivecontent in store.state.chat.receivecontents){
        var t = store.state.chat.receivecontents[receivecontent];
        if(parseInt(t.id) === parseInt(user.id)){
          store.state.chat.currentconents.push({
            content: t.content,
            id: store.state.chat.currentconents.length,
            is_oneself: t.is_oneself,
            sendtime: t.sendtime,
            status: t.status,
          });
        }
      }

      store.commit("updatereceiveContents", store.state.chat.receivecontents.filter(function(item){
        return item.id != user.id
      }))
    };
    
    return {
      click_myspace,
      click_option_friends,
      click_option_team,
      check_chatuser,
    }
  }
}
</script>

<style scoped>
.my {
  height: 15vh;
}
.my .card-body {
  padding: 0;
  height: 100%;
}

.chatlistprofile {
  height: 65vh;
  overflow: auto;
}
.chatlistprofile .card-body{
  padding: 0;
}
img {
  border-radius: 50%;
  width: 9vh;
}
.chatlistprofile img {
  width: 5vh;
}
.chatlistprofile span {
  width: 5vh;
}

.username {
  font-weight: bold;
  font-size: 3vh;
  white-space:nowrap;
  overflow:hidden;
  text-overflow:ellipsis;
}
.btn-check-isfriend{
  width: 50%;
}

.chatlist-btn {
  width: 100%;
  height: 8vh;
}
.chatlist-item {
  margin-top: 1vh;
}
</style>