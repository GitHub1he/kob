<template>
  <div class="card">
    <div class="screen_nav">公 屏</div>
    <div class="card-body" id="show_words">
      <div class="screen_chatlist" v-for="content in $store.state.chat.screencontents" :key="content.id">
        <div class="screen_chat">
          <img :src="content.photo" :class="$store.state.user.username === content.name ? 'myself' : 'no-myself' " alt="头像">
          <div class="info">
            <p class="time">
              {{ content.name }}
              {{ content.sendtime }}
            </p>
            <div class="info-content"> {{ content.content }} </div>
          </div>
        </div>
      </div>
    </div>
    
    <div class="screen_send">
      <input v-model="content" type="text" class="text">
      <button @click="send_screen" type="button" class="btn btn-outline-light">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-cursor" viewBox="0 0 16 16">
          <path d="M14.082 2.182a.5.5 0 0 1 .103.557L8.528 15.467a.5.5 0 0 1-.917-.007L5.57 10.694.803 8.652a.5.5 0 0 1-.006-.916l12.728-5.657a.5.5 0 0 1 .556.103zM2.25 8.184l3.897 1.67a.5.5 0 0 1 .262.263l1.67 3.897L12.743 3.52 2.25 8.184z"/>
        </svg>
      </button>
    </div>
    
  </div>
</template>

<script>
import { useStore } from 'vuex';
import { ref } from 'vue';

export default {
  name: "ChatScreen",
  updated : function(){
    this.$nextTick(function(){
      var div = document.getElementById('show_words');
      div.scrollTop = div.scrollHeight;
    })
  },
  setup() {
    let content = ref('');
    const store = useStore();

    const send_screen = () => {
      store.state.chat.chatsocket.send(JSON.stringify({
        event: "send-screen-message",
        sender_id: store.state.user.id,
        receiver_id: 0,
        content: content.value,
      }));
      content.value = "";
    }

    return {
      content, 
      send_screen,
    }
  }
}
</script>

<style scoped>
.screen_nav {
  height: 7%;
  background-color: whitesmoke;
  text-align: center;
  font-size: larger;
}
.card {
  padding: 0;
}
.card-body {
  padding: 0;
  overflow: auto;
  height: 86%;
}
.screen_send {
  height: 7%;
}
.text {
  width: 80%;
}

button {
  width: 20%;
  line-height: 100%;
  border-radius: 0;
}

.info {
  width: 90%;
  margin-left: 10px;
  text-align: left;
}

.info .time {
  font-size: 12px;
  color: rgba(51, 51, 51, 0.8);
  margin: 0;
  height: 20px;
  line-height: 20px;
  margin-top: -5px;
}

.info .info-content {
  max-width: 90%;
  padding: 10px;
  font-size: 14px;
  float: left;
  margin-left: 2px;
  position: relative;
  margin-top: 3px;
  margin-bottom: 10px;
  background: rgb(147, 211, 142);
  border-radius: 5px;
}

/* 小三角形 */
.info .info-content::before {
  position: absolute;
  left: -8px;
  top: 3px;
  content: '';
  border-right: 10px solid rgb(147, 211, 142);
  border-top: 8px solid transparent;
  border-bottom: 8px solid transparent;
}
img {
  width: 5vh;
  height: 5vh;
  border-radius: 50%;
} 
.myself {
  border: 1px solid red;
}
.screen_chat {
  display: flex;
}
</style>