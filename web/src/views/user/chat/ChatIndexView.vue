<template>
  <div class="container">
    <div class="card">
      <div class="card-body">
        <div class="row">
          <div class="col-3">
            <ChatList :users="users" :teams="teams"/>
          </div>
          <div class="col-9" v-if="$store.state.chat.chatuserid != ''">
            <ChatProfileInfo />
            <ChatProfileWrite />
          </div>
        </div>
      </div>
    </div>
  </div>
  <button @click="open_screen" class="btn btn-info screenbtn" type="button">
    公屏
  </button>
  <ChatScreen class="chatscreen"/>
  <button @click="open_search" class="btn btn-info searchbtn" type="button">
    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-search" viewBox="0 0 16 16">
      <path d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z"/>
    </svg>
  </button>
  <ChatSearch class="chatsearch" v-if="$store.state.chat.issearch"/>
</template>

<script>
import ChatList from '@/components/ChatList.vue';
import ChatProfileInfo from '@/components/ChatProfileInfo.vue';
import ChatProfileWrite from "@/components/ChatProfileWrite.vue";
import ChatScreen from "@/components/ChatScreen.vue";
import ChatSearch from "@/components/ChatSearch.vue";
import { onMounted, onUnmounted } from 'vue';
import { useStore } from 'vuex';
import { reactive } from 'vue';


export default{
  components: {
    ChatList,
    ChatProfileInfo,
    ChatProfileWrite,
    ChatScreen,
    ChatSearch,
  },
  setup() {
    const store = useStore();
    const socketUrl = `ws://127.0.0.1:3000/websocket/chat/${store.state.user.token}/`;
    let socket = null;
    const users = reactive({});
    const teams = reactive({});

    onMounted(() => {
      socket = new WebSocket(socketUrl);

      socket.onopen = () => {
        console.log("connected!");
        store.commit("updateChatSocket", socket);

        store.state.chat.chatsocket.send(JSON.stringify({
          event: "receive-message",
        }));
      }

      socket.onmessage = msg => {
        const data = JSON.parse(msg.data);
        if(data.event === "receive_chat") { //接收消息
          if(parseInt(store.state.chat.chatuserid) === data.receive.id){ //将别人发送的消息接收并显示出来
            store.commit("updateCurrentContents", data.receive.content);
          }else {
            // 找到与当前用户对话的位置，将消息追加到contents中，
            store.commit("updateReceiveContents", data.receive);
          }
        } else if (data.event === "history_chat") {
          users.value = data.history;
        } else if (data.event === "history_team_chat") {
          teams.value = data.team_chat;
        } else if (data.event === "search_chat") {
          store.commit("updateScreenContents", data.screencontent);
        } else {
          console.log("其他消息");
          console.log(data);
        }
      }

      socket.onclose = () => {
        console.log("disconnnected!");
      }

    });

    onUnmounted(() => {
      socket.close();
    });

    const open_search = () => {
      if(store.state.chat.issearch) {
        store.commit("updateIsSearch", false);
      } else {
        store.commit("updateIsSearch", true);
      }
    };
    const open_screen = () => {
      if(store.state.chat.isscreen) {
        store.commit("updateIsScreen", false);
      } else {
        store.commit("updateIsScreen", true);
      }
    }
    return {
      users,
      teams,
      open_search,
      open_screen,
    }
  }
}
</script>

<style scoped>
.container {
  margin-right: 25vw;
  margin-top: 2vh;
  width: 70%;
}
.chatscreen {
  width: 20%;
  height: 50%;
  background-color: rgba(50%, 50%, 50%, .4);
  position: fixed;
  top: 10vh;
  right: 5vw;
  border: 1px solid white;
  border-radius: 5% 5% 0 0;
  overflow: hidden;
}
.searchbtn {
  position: fixed;
  bottom: 1vh;
  right: 1vw;
}
.screenbtn {
  position: fixed;
  top: 8vh;
  right: 1vw;
}
.chatsearch {
  width: 24%;
  height: 36%;
  /* background-color: rgba(50%, 50%, 50%, .4); */
  position: fixed;
  bottom: 1vh;
  right: 1vw;
}
</style>