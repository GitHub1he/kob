<template>
  <ContentField>
    <div class="row">
      <div class="col-3">
        <UserProfileInfo @follow="follow" @unfollow="unfollow" :user="user" :posts="posts"/>
        <UserProfileWrite @submit_post="submit_post" v-if="is_me" />
      </div>
      <div class="col-9">
        <UserProfilePosts :user="user" :posts="posts" @del_post="del_post"/>
      </div>
    </div>
  </ContentField>
</template>
  
<script>
import ContentField from '@/components/ContentField';
import UserProfileInfo from '@/components/UserProfileInfo';
import UserProfilePosts from '@/components/UserProfilePosts';
import UserProfileWrite from '@/components/UserProfileWrite';
import { reactive } from 'vue';
import { useRoute } from 'vue-router';
import $ from 'jquery';
import { useStore } from 'vuex';
import { computed } from 'vue';

export default {
  name: 'UserProfileView',
  components: {
    ContentField,
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

    
    $.ajax({
      url: 'http://127.0.0.1:3000/user/account/info/get/',
      type: 'GET',
      data: {
        user_id: userId,
      },
      headers: {
        'Authorization': "Bearer " + store.state.user.token,
      },
      success(resp) {
        $.ajax({
          url: 'http://127.0.0.1:3000/user/myspace/friends/isfollow/',
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
      url: 'http://127.0.0.1:3000/user/myspace/posts/get/',
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

    const is_me = computed(() => userId === parseInt(store.state.user.id));
    
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

    return {
      user,
      follow,
      unfollow,
      posts,
      submit_post,
      userId,
      is_me,
      del_post,
    }
  }
}
</script>

<style scoped>
</style>