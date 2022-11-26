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
  <ChatScreen class="chatscreen"/>
  <ChatSearch class="chatsearch"/>
</template>

<script>
import ChatList from '@/components/ChatList.vue';
import ChatProfileInfo from '@/components/ChatProfileInfo.vue';
import ChatProfileWrite from "@/components/ChatProfileWrite.vue";
import ChatScreen from "@/components/ChatScreen.vue"
import ChatSearch from "@/components/ChatSearch.vue"
import { onMounted, onUnmounted } from 'vue';
import { useStore } from 'vuex';
import { reactive } from 'vue';


export default{
  components: {
    ChatList,
    ChatProfileInfo,
    ChatProfileWrite,
    ChatScreen,
    ChatSearch
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
          console.log("接收信息");
          console.log(data);
          if(parseInt(store.state.chat.chatuserid) === data.receive.id){ //将别人发送的消息接收并显示出来
            store.commit("updateCurrentContents", data.receive.content);
          }else {
            // 找到与当前用户对话的位置，将消息追加到contents中，
            store.commit("updateReceiveContents", data.receive);
          }
          console.log(store.state.chat.currentconents);
        } else if (data.event === "history_chat") {
          console.log("获取历史消息");
          users.value = data.history;
          console.log(data);
        } else if (data.event === "history_team_chat") {
          console.log("获取team历史消息");
          teams.value = data.team_chat;
          console.log(data);
        } else if (data.event === "search_chat") {
          console.log("获取公屏信息");
          console.log(data.screencontent);
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

    return {
      users,
      teams,
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
</style>