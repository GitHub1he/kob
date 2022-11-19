<template>
  <ContentField>
    <div class="row">
      <div class="col-3">
        <ChatList :users="users" :teams="teams"/>
      </div>
      <div class="col-9" v-if="$store.state.chat.chatuserid != ''">
        <ChatProfileInfo />
        <ChatProfileWrite />
      </div>
      
    </div>
  </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue";
import ChatList from '@/components/ChatList.vue';
import ChatProfileInfo from '@/components/ChatProfileInfo.vue';
import ChatProfileWrite from "@/components/ChatProfileWrite.vue";
import { onMounted, onUnmounted } from 'vue';
import { useStore } from 'vuex';
import { reactive } from 'vue';


export default{
  components: {
    ContentField,
    ChatList,
    ChatProfileInfo,
    ChatProfileWrite,
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
            store.state.chat.currentconents.push({
              content: data.receive.content.content,
              id: data.receive.content.id,
              user_name: data.receive.content.user_name,
              user_photo: data.receive.content.user_photo,
              is_oneself: data.receive.content.is_oneself,
              sendtime: data.receive.content.sendtime,
              status: data.receive.content.status,
            });
          }else {
            // 找到与当前用户对话的位置，将消息追加到contents中，
            store.state.chat.receivecontents.push({
              content: data.receive.content.content,
              id: data.receive.id,
              name: data.receive.name,
              photo: data.receive.photo,
              last_login_time: data.receive.last_login_time,
              is_oneself: data.receive.content.is_oneself,
              sendtime: data.receive.content.sendtime,
              status: data.receive.content.status,
            });
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
</style>