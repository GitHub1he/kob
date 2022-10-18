<template>
    <div class="card"  v-for="user in users.value" :key="user.id" @click="open_user_profile(user.id)">
      <div class="card-body">
        <div class="row">
          <div class="col-1 img-field">
            <img class="img-fluid" :src="user.photo" alt="用户头像">
          </div>
          <div class="col-10">
            <div class="username">{{ user.username }}</div>
            <div class="follower-count">粉丝数: {{ user.followercount}}</div>
          </div>
        </div>
      </div>
    </div>
</template>
  
<script>
import router from '@/router';
import { useStore } from 'vuex';

export default {
  name: 'UserListView',
  components: {
  },
  props: {
    users: {
      type: Object,
      required: true,
    }
  },
  setup() {
    const store = useStore();

    // $.ajax({
    //   url: 'http://127.0.0.1:3000/user/account/userlist/',
    //   type: "get",
    //   headers:{
    //     'Authorization': "Bearer " + store.state.user.token,
    //   },
    //   success(resp) {
    //     users.value = resp;
    //   }
    // });

    const open_user_profile= userId => {
      if(store.state.user.is_login){
        router.push({
          name: 'userprofile',
          params: {
            userId
          }
        })
      }else{
        router.push({
          name: 'login',
        })
      }
      
    }
    return {
      open_user_profile,
    };
  }
}
</script>
  
<style scoped>
img{
  border-radius: 50%;
}

.username {
  font-weight: bold;
  height: 50%;
}

.follower-count {
  font-size: 12px;
  color: gray;
  height: 50%;
}

.card{
  margin-bottom: 10px;
  cursor: pointer;
}

.card:hover {
  box-shadow: 2px 2px 10px lightgray;
  transition: 500ms;
}

.img-field{
    display: flex;
    flex-direction: column;
    justify-content: center;
}
</style>