<template>
  <div class="card card-profile">
      <button @click="open_user_profile($store.state.chat.chatuserid)" type="button" class="btn btn-user-space btn-outline-info" v-if="$store.state.chat.option === 'friends'">
        <img class="img-fluid" :src="$store.state.chat.chatuserphoto" alt="对面头像">
        &nbsp;
        <span style="font-weight: bold;font-size: large;">{{ $store.state.chat.chatusername }}</span>
        &nbsp;
        <span style="color: rgb(133, 133, 133);font-size: small;">{{ $store.state.chat.chatuserlastlogintime }}</span>
      </button>
      <button @click="open_team_profile" type="button" class="btn btn-user-space btn-outline-info" v-if="$store.state.chat.option === 'teams'">
        <img class="img-fluid" :src="$store.state.chat.chatuserphoto" alt="对面头像">
        {{ $store.state.chat.chatuserid }}
        &nbsp;
        <span style="font-weight: bold;font-size: large;">{{ $store.state.chat.chatusername }}</span>
        &nbsp;
        <span style="color: rgb(133, 133, 133);font-size: small;">{{ $store.state.chat.chatuserlastlogintime }}</span>
      </button>
    <div class="card-body" id="show_words" updated v-if="$store.state.chat.option === 'friends'">
      <div v-for="content in $store.state.chat.currentconents" :key="content.id"> <!-- :style="content.is_oneself ? 'float: right' : 'float: left'" -->
        <div class="mysend" v-if="content.is_oneself">
          <div class="info">
            <p class="time">
              <span class="status" :style="content.status === 0 ? 'color : red' : 'color : green'">```</span>
              {{ content.sendtime }} {{ $store.state.user.username }}
            </p>
            <div class="info-content">{{ content.content }}</div>
          </div>
          <img :src="$store.state.user.photo" alt="我的头像">
        </div>
        <div class="myreceive" v-else>
          <img :src="$store.state.chat.chatuserphoto" alt="对面的头像">
          <div class="info">
            <p class="time"> 
              {{ $store.state.chat.chatusername }} {{ content.sendtime }}
              <span class="status" :style="content.status === 0 ? 'color : red' : 'color : green'">```</span>
            </p>
            <div class="info-content">{{ content.content }}</div>
          </div>
        </div>
      </div>
    </div>
    <div class="card-body" id="show_words" updated v-if="$store.state.chat.option === 'teams'">
      <ChatTeamInfo/>
      <div v-for="content in $store.state.chat.currentconents" :key="content.id">
        <div class="mysend" v-if="content.is_oneself">
          <div class="info">
            <p class="time">
              {{ content.sendtime }} {{ $store.state.user.username }}
            </p>
            <div class="info-content">{{ content.content }}</div>
          </div>
          <img :src="$store.state.user.photo" alt="我的头像">
        </div>
        <div class="myreceive" v-else>
          <img :src="content.user_photo" alt="对面的头像">
          <div class="info">
            <p class="time"> 
              {{ content.user_name }} {{ content.sendtime }}
              <span class="status" :style="content.status === 0 ? 'color : red' : 'color : green'">```</span>
            </p>
            <div class="info-content">{{ content.content }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ChatTeamInfo from '@/components/ChatTeamInfo.vue';
import router from '@/router';
import { useStore } from 'vuex';
import { ref } from 'vue';

export default {
  name: "ChatProfileInfo",
  components: {
    ChatTeamInfo,
  },
  updated : function(){
    this.$nextTick(function(){
      var div = document.getElementById('show_words');
      div.scrollTop = div.scrollHeight;
    })
  },
  setup() {
    const teaminfo = ref();
    const store = useStore();

    const open_user_profile = userId => {
      store.commit("updateFlag",0);
      router.push({
        name: 'userprofile',
        params: { userId }
      });
    };
    
    const open_team_profile = () => {
      console.log("显示team人员");
      
      if(store.state.chat.isteaminfo) store.commit("updateIsTeamInfo", false);
      else store.commit("updateIsTeamInfo", true);
    }

    return {
      teaminfo,
      open_user_profile,
      open_team_profile,
    }
  }
};
</script>

<style scoped>
img {
  width: 7vh;
  height: 7vh;
}  
.card-profile {
  height: 65vh;
}
.card-profile .card-body {
  padding: 0;
  overflow: auto;
}
.btn-user-space {
  width: 100%;
  height: 15%;
}
.mysend {
  display: flex;
  justify-content: flex-end;
}
.mysend .info {
  width: 90%;
  margin-left: 10px;
  text-align: right;
}
.mysend .info .time{
  font-size: 12px;
  color: rgba(51,51,51,0.8);
  margin: 0;
  height: 20px;
  line-height: 20px;
  margin-top: -5px;
  margin-right: 10px;
}
.mysend .info .info-content{
  max-width: 70%;
  padding: 10px;
  font-size: 14px;
  float: right;
  margin-right: 10px;
  position: relative;
  margin-top: 3px;
  margin-bottom: 10px;
  background: #A3C3F6;
  text-align: left;
  border-radius: 5px;
}
/* 小三角形 */
.mysend .info .info-content::after{
    position: absolute;
    right: -8px;
    top: 8px;
    content: '';
    border-left: 10px solid #A3C3F6;
    border-top: 8px solid transparent;
    border-bottom: 8px solid transparent;
}

.myreceive {
  display: flex;
}
.myreceive .info{
  width: 90%;
  margin-left: 10px;
  text-align: left;
}
.myreceive .info .time{
  font-size: 12px;
  color: rgba(51,51,51,0.8);
  margin: 0;
  height: 20px;
  line-height: 20px;
  margin-top: -5px;
}
.myreceive .info .info-content{
  max-width: 70%;
  padding: 10px;
  font-size: 14px;
  float: left;
  margin-left: 10px;
  position: relative;
  margin-top: 3px;
  margin-bottom: 10px;
  background: rgb(147, 211, 142);
  border-radius: 5px;
}
/* 小三角形 */
.myreceive .info .info-content::before{
  position: absolute;
  left: -8px;
  top: 8px;
  content: '';
  border-right: 10px solid rgb(147, 211, 142);
  border-top: 8px solid transparent;
  border-bottom: 8px solid transparent;
}

</style>