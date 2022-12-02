<template>
  <div class="card text-center">
    <div class="card-header">
      <ul class="nav nav-tabs card-header-tabs">
        <li class="nav-item">
          <button @click="search_op_people" type="button" :class="$store.state.chat.searchoption === 'people' ? 'nav-link active' :'nav-link'">找人</button>
        </li>
        <li class="nav-item">
          <button @click="search_op_team" type="button" :class="$store.state.chat.searchoption === 'team' ? 'nav-link active' :'nav-link'">群</button>
        </li>
        <button @click="close_search" type="button" class="btn-close" aria-label="Close"></button>
      </ul>
    </div>
    <div class="card-body">
      <div class="input-group mb-3" v-if="$store.state.chat.searchoption === 'people'">
        <input v-model="name" type="text" class="form-control" placeholder="姓名">
        <input v-model="rating" type="number" class="form-control" placeholder="天梯分">
        <input v-model="follower_cnt" type="number" class="form-control" placeholder="粉丝数">
        <button @click="search_people" class="btn btn-outline-secondary" type="button">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-search" viewBox="0 0 16 16">
            <path d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z"/>
          </svg>
        </button>
      </div>
      <div class="input-group mb-3" v-if="$store.state.chat.searchoption === 'team'">
        <input v-model="name" type="text" class="form-control" placeholder="群名">
        <button @click="search_team" class="btn btn-outline-secondary" type="button">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-search" viewBox="0 0 16 16">
            <path d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z"/>
          </svg>
        </button>
        <button @click="add_team" class="btn btn-outline-secondary" type="button">
          +
        </button>
      </div>
      <div class="row search_res">
        <div class="col-sm-6" v-for="res in search_res.value" :key="res.id">
            <div class="card">
              <div class="card-body" :style="parseInt(res.ownerId) === parseInt($store.state.user.id) ? 'border: 1px solid red;' : ''">
                  <img :src="res.photo" alt="头像">
                  <div class="name"> {{ res.name }}</div>
                  <div v-if="$store.state.chat.searchoption === 'people'">天梯分：{{ res.rating }}</div>
                  <div v-if="$store.state.chat.searchoption === 'people'">粉丝数：{{ res.follower_cnt }}</div>
                  <div v-if="$store.state.chat.searchoption === 'people'">最后登录：{{ res.last_login_time }}</div>
                  <div v-if="$store.state.chat.searchoption === 'team'">人数{{ res.personCount }}</div>
                  <div v-if="$store.state.chat.searchoption === 'team'">创建时间{{ res.createtime }}</div>
                  <button @click="open_user_profile(res.id)" type="button" class="btn btn-outline-success" v-if="$store.state.chat.searchoption === 'people'">空间</button>
                  <button @click="join_team(res.id)" type="button" class="btn btn-outline-success" v-if="$store.state.chat.searchoption === 'team'">加入/退出</button>
                </div>
            </div>
        </div>
      </div>
    </div>

    <div class="card-footer text-muted">
      {{ error_message }}
    </div>
  </div>
  
</template>

<script>

import { useStore } from 'vuex';
import { ref,reactive } from 'vue';
import $ from 'jquery';
import router from '@/router';

export default {
  name: "ChatSearch",
  setup() {
    const store = useStore();
    let name = ref('');
    let rating = ref('');
    let follower_cnt = ref('');
    const search_res = reactive({});
    let error_message = ref('');
    
    const clear_input = () => {
      search_res.value = null;
      name.value = "";
      rating.value = "";
      follower_cnt.value = "";
      error_message.value = "";
    }
    const search_op_people = () => {
      clear_input();
      store.commit("updateSearchOption", 'people');
    }
    const search_op_team = () => {
      clear_input();
      store.commit("updateSearchOption", 'team');
    }
    const close_search = () => {
      clear_input();
      store.commit("updateIsSearch", false);
    }

    const search_people = () => {
      search_res.value = null;
      error_message.value = "";
      $.ajax({
        url:"http://localhost:3000/api/user/search/",
        type: "GET",
        data : {
          name: name.value,
          rating: rating.value,
          follower_cnt: follower_cnt.value,
        },
        headers:{
          'Authorization': "Bearer " + store.state.user.token,
        },
        success(resp) {
          if(resp.error_message === "success"){
            search_res.value = resp.search_res;
          }else {
            error_message.value = resp.error_message;
          }
          console.log(error_message.value);
        }
      })
    }
    const search_team = () => {
      search_res.value = null;
      error_message.value = "";
      $.ajax({
        url:"http://localhost:3000/api/team/search/",
        type: "GET",
        data : {
          name: name.value,
        },
        headers:{
          'Authorization': "Bearer " + store.state.user.token,
        },
        success(resp) {
          if(resp.error_message === "success"){
            search_res.value = resp.search_res;
          }else {
            error_message.value = resp.error_message;
          }
        }
      })
    }
    const add_team = () => {
      search_res.value = null;
      error_message.value = "";
      $.ajax({
        url:"http://localhost:3000/api/team/add/",
        type: "POST",
        data : {
          name: name.value,
        },
        headers:{
            'Authorization': "Bearer " + store.state.user.token,
        },
        success(resp) {
          error_message.value = resp.error_message;
        }
      })
    }
    const join_team = teamId => {
      error_message.value = "";
      $.ajax({
        url:"http://localhost:3000/api/team/join/",
        type: "POST",
        data : {
          team_id: teamId,
          user_id: store.state.user.id,
        },
        headers:{
            'Authorization': "Bearer " + store.state.user.token,
        },
        success(resp) {
          error_message.value = resp.error_message;
        }
      })
    }
    const open_user_profile = userId => {
      store.commit("updateFlag",0);
      router.push({
        name: 'userprofile',
        params: { userId }
      });
    };

    return {
      name,
      rating,
      follower_cnt,
      search_op_people, 
      search_op_team,
      search_people,
      search_team,
      add_team,
      search_res,
      error_message,
      open_user_profile,
      join_team,
      close_search,
    }
  }
}
</script>
<style>
.card-header {
  height: 15%;
  position: relative;
  user-select: none;
}
.btn-close {
  position: absolute;
  right: 1vw;
}
.card-body {
  height: 85%;
}
.input-group {
  height: 15%;
}
.search_res {
  height: 85%;
  overflow: auto;
}
.search_res img {
  border-radius: 50%;
  height: 8vh;
  width: 8vh;
}
.search_res .name {
  font-weight: bold;
}
</style>