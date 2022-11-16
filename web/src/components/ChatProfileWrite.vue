<template>
  <div class="card card-text">
    <div class="card-body">
      <textarea v-model="content" class="form-control" row="10" placeholder="主动一点，世界会更大！"></textarea>
      <button @click="click_send_btn" type="button" class="btn btn-warning btn-sendcontent btn-sm"> biu~~ </button>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';
import { useStore } from 'vuex';

export default {
  name: "ChatProfileWrite",
  setup() {
    const store = useStore();
    let content = ref('');
    let content_length = ref();
    const click_send_btn = () => {
      store.state.chat.chatsocket.send(JSON.stringify({
        event: "send-message",
        sender_id: store.state.user.id,
        receiver_id: store.state.chat.chatuserid,
        content: content.value,
      }));
      content_length.value = store.state.chat.currentconents.length;
      store.state.chat.currentconents.push({
        content: content.value,
        id: content_length.value,
        is_oneself: true,
        sendtime: "",
        status: 0,
      });
      content.value = "";
    };
    
    return {
      content,
      click_send_btn,
    }
  }
}
</script>

<style scoped>
.card-text .card-body {
  padding: 0;
}
.card-text {
  height: 20vh;
}
.card-text textarea {
  height: 80%;
}
.btn-sendcontent {
  float: right;
}
</style>