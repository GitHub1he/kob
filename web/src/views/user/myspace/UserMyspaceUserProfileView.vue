<template>
  <div class="container content-field" style="margin-top: 15px;">
    <div class="myspace" >
      <div class="row" v-if="$store.state.user.flag == 0" :key="1">
        <div class="col-3">
          <UserProfileInfo @follow="follow" @unfollow="unfollow" :user="user" :posts="posts"/>
          <UserProfileWrite @submit_post="submit_post" v-if="$store.state.user.is_me"/>
        </div>
        <div class="col-8">
          <UserProfilePosts :user="user" :posts="posts" @del_post="del_post"/>
        </div>
        
      </div>

      <div class="card" v-else v-for="user in users.value" :key="user.id" @click="open_user_profile(user.id)">
        <div class="card-body">
          <div class="row row-cols-auto">
            <div class="col-1 img-field">
              <img class="img-fluid" :src="user.photo" alt="用户头像">
            </div>
            <div class="col-6">
              <div class="username">{{ user.username }}</div>
              <div class="follower-count">粉丝数: {{ user.followercount}}</div>
            </div>
          </div>
        </div>
      </div>

      <div class="list-group">
        <button @click="myspace" type="button" :class="$store.state.user.flag == 0 ? 'list-group-item list-group-item-action active' : 'list-group-item list-group-item-action'">
          <img class="img-fluid img-sm img-myspace-bar" :src="$store.state.user.photo" alt="">
          空间
        </button>
        <button @click="mydiscover" type="button" :class="$store.state.user.flag == 2 ? 'list-group-item list-group-item-action active' : 'list-group-item list-group-item-action'">
          发现
        </button>
        <button @click="myfollower" type="button" :class="$store.state.user.flag == 3 ? 'list-group-item list-group-item-action active' : 'list-group-item list-group-item-action'">
          粉丝
        </button>
        <button @click="myfocuser" type="button" :class="$store.state.user.flag == 4 ? 'list-group-item list-group-item-action active' : 'list-group-item list-group-item-action'">
          关注
        </button>
      </div>
    </div>
  </div>
</template>
  
<script>
import UserProfileInfo from '@/components/UserProfileInfo';
import UserProfilePosts from '@/components/UserProfilePosts';
import UserProfileWrite from '@/components/UserProfileWrite';
import { reactive } from 'vue';
import { useRoute } from 'vue-router';
import $ from 'jquery';
import { useStore } from 'vuex';
import { computed } from 'vue';
import router from '@/router';

export default {
  name: 'UserProfileView',
  components: {
    UserProfileInfo,
    UserProfilePosts,
    UserProfileWrite,
  },
  setup() {
    const store = useStore();
    const route = useRoute();
    const userId = parseInt(route.params.userId);
    const user = reactive({});
    const posts = reactive({});
    const users = reactive({});
    let flag = computed(() => store.state.user.flag);

    const init = (userId) => {
      $.ajax({
        url: 'http://127.0.0.1:3000/api/user/account/info/get/',
        type: 'GET',
        data: {
          user_id: userId,
        },
        headers: {
          'Authorization': "Bearer " + store.state.user.token,
        },
        success(resp) {
          $.ajax({
            url: 'http://127.0.0.1:3000/api/user/myspace/friends/isfollow/',
            type: 'GET',
            data: {
              target_id: userId,
            },
            headers: {
              'Authorization': "Bearer " + store.state.user.token,
            },
            success(resp){
              if(resp.state === "follow") user.is_followed = true;
              else user.is_followed = false;
            }
          })
          user.id = resp.id;
          user.username = resp.username;
          user.photo = resp.photo;
          user.followerCount = resp.followercount;
        }
      });

      $.ajax({
        url: 'http://127.0.0.1:3000/api/user/myspace/posts/get/',
        type: 'GET',
        data: {
          user_id: userId,
        },
        headers: {
          'Authorization': "Bearer " + store.state.user.token,
        },
        success(resp) {
          posts.posts = resp;
          posts.count = resp.length;
        }
      });

      $.ajax({
        url: 'http://127.0.0.1:3000/api/user/account/userlist/',
        type: "get",
        headers:{
          'Authorization': "Bearer " + store.state.user.token,
        },
        success(resp) {
          users.value = resp;
        }
      });
      store.commit("updateIsme", parseInt(userId) === parseInt(store.state.user.id));
      store.commit("updateFlag",0);
    }

    init(userId);

    const follow = () => {
      if (user.is_followed) return ;
      user.is_followed = true ;
      user.followerCount ++ ;
    };
    const unfollow = () => {
      if (!user.is_followed) return ;
      user.is_followed = false ;
      user.followerCount -- ;
    };
  
    const submit_post = (content) => { 
      posts.count ++ ;
      posts.posts.unshift({
        id: posts.count,
        user_id: store.state.user.id,
        post: content,
      })
    };
    const del_post = (post_id) => { 
      posts.posts = posts.posts.filter(post => post.id != post_id);
      posts.count = posts.posts.length;
    };

    const open_user_profile = userId => {
      if(store.state.user.is_login){
        store.commit("updateFlag",0);
        router.push({
          name: 'userprofile',
          params: { userId }
        });
        init(userId);
      }else{
        router.push({
          name: 'login',
        })
      }
    };

    const myspace = () =>{
      store.commit("updateFlag",0);
      router.push({
        name: 'userprofile',
        params: {userId: store.state.user.id},
      });
      init(store.state.user.id);
    }

    const mydiscover = () => {
      $.ajax({
        url: 'http://127.0.0.1:3000/api/user/myspace/friends/unfocus/',
        type: 'GET',
        data: {
          follower_id: store.state.user.id,
        },
        headers: {
          'Authorization': "Bearer " + store.state.user.token,
        },
        success(resp) {
          users.value = resp;
          store.commit("updateFlag",2);
        }
      })
    };

    const myfollower = () => {
      $.ajax({
        url: 'http://127.0.0.1:3000/api/user/myspace/friends/follower/',
        type: 'GET',
        data: {
          target_id: store.state.user.id,
        },
        headers: {
          'Authorization': "Bearer " + store.state.user.token,
        },
        success(resp) {
          users.value = resp;
          store.commit("updateFlag",3);
        }
      })
    };

    const myfocuser = () => {
      $.ajax({
        url: 'http://127.0.0.1:3000/api/user/myspace/friends/focuser/',
        type: 'GET',
        data: {
          follower_id: store.state.user.id,
        },
        headers: {
          'Authorization': "Bearer " + store.state.user.token,
        },
        success(resp) {
          users.value = resp;
          store.commit("updateFlag",4);
        },
      })
    };

    return {
      user,
      users,
      follow,
      unfollow,
      posts,
      submit_post,
      userId,
      del_post,
      open_user_profile,
      myspace,
      mydiscover,
      myfollower,
      myfocuser,
      flag,
    }
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
  width: 90%;
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

.list-group{
  font-size: 12px;
  position: fixed;
  top: 30%;
  right: 2%;
 
  text-align: center;
  width: 5vw;
}
.img-myspace-bar {
  width: 30px;
  height: 30px;
}
</style>