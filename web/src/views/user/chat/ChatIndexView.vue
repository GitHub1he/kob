<template>
  <ContentField>
    ChatIndexView
    <button @click="click_send_btn" type="button" class="btn btn-warning "> biu~~ </button>
  </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue";
import { onMounted, onUnmounted } from 'vue';
import { useStore } from 'vuex';

export default{
  components: {
    ContentField,
  },
  setup() {
    const store = useStore();
    const socketUrl = `ws://127.0.0.1:3000/websocket/chat/${store.state.user.token}/`;
    let socket = null;
    onMounted(() => {
      socket = new WebSocket(socketUrl);

      socket.onopen = () => {
        console.log("connected!");
        store.commit("updateChatSocket", socket);
      }

      socket.onmessage = msg => {
        const data = JSON.parse(msg.data);
        if(data.event === "send-message") { //接收消息
          console.log("接收信息");
          console.log(data);
        } 
      }

      socket.onclose = () => {
        console.log("disconnnected!");
      }

    });

    onUnmounted(() => {
      socket.close();
      store.commit("updateStatus", "matching");
    });

    const click_send_btn = () => {
      store.state.chat.chatsocket.send(JSON.stringify({
        event: "send-message",
        sender_id: store.state.user.id,
        receiver_id: 90001,
        content: "群发测试，全体目光向我看齐，我是个帅哥",
      }));
    }

    return {
      click_send_btn,
    }
  }
}
</script>

<style scoped>
  
</style>