<template>
    <div class="card">
        <div class="card-body">
            <div class="row">
                <div class="col-4 img-field">
                    <img class="img-fluid" :src="user.photo" alt="">
                </div>
                <div class="col-1"></div>
                <div class="col-6">
                  <div class="username"> {{ user.username }}</div>
                  <div class="fans">粉丝: {{ user.followerCount }}</div>
                  <div class="postcount">发帖数: {{ posts.count }}</div>
                  <button @click="follow" v-if="!user.is_followed" type="button" class="btn btn-secondary btn-sm">+关注</button>
                  <button @click="unfollow" v-if="user.is_followed" type="button" class="btn btn-secondary btn-sm">取消关注</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import $ from 'jquery';
import { useStore } from 'vuex';

export default{
  name:'UserProfileInfo',
  props: {
    user: {
      type: Object,
      required: true,
    },
    posts: {
      type: Object,
      required: true,
    },
  },
  setup(props, context) {
    const store = useStore();
    const follow = () =>{
      $.ajax({
        url: "http://127.0.0.1:3000/api/user/myspace/friends/changefollow/",
        type: "post",
        data:{
          target_id: props.user.id,
        },
        headers: {
          'Authorization': "Bearer " + store.state.user.token,
        },
        success(resp) {
          if(resp.error_message === "success"){
            context.emit('follow');
          }
        }
      })
    };

    const unfollow = () => {
      $.ajax({
          url: "http://127.0.0.1:3000/api/user/myspace/friends/changefollow/",
          type: "post",
          data:{
              target_id: props.user.id,
          },
          headers: {
              'Authorization': "Bearer " + store.state.user.token,
          },
          success(resp) {
              if(resp.error_message === "success"){
                  context.emit('unfollow');
              }
          }
      })
    };
    return {
      follow,
      unfollow
    }
  }
}
</script>

<style scoped>
img {
    border-radius: 30%;
}
.username {
  font-size: large;
  font-weight: bold;
}
.fans {
    font-size: 14px;
    color:rgb(31, 239, 34);
}
button {
  margin-top: 12px;
    padding: 2px 4px;
    font-size: 12px;
}
.postcount {
    font-size: 14px;
    color:rgb(39, 213, 210);
}

.img-field{
    display: flex;
    flex-direction: column;
    justify-content: center;
}
</style>