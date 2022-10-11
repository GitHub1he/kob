<template>
  <nav class="navbar navbar-expand-lg navbar-light bg-light">
  <div class="container">
    <router-link :to="{name: 'home'}" class="navbar-brand">King Of Bot</router-link> 
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarText">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item" v-if="$store.state.user.is_login">
        <router-link :class="route_name == 'pk_index' ? 'nav-link active' : 'nav-link'" aria-current="page" :to="{name: 'pk_index'}">对战</router-link>
        </li>
        <li class="nav-item">
        <router-link :class="route_name == 'record_index' ? 'nav-link active' : 'nav-link'" :to="{name: 'record_index'}">对战列表</router-link>
        </li>
        <li class="nav-item">
        <router-link :class="route_name == 'ranklist_index' ? 'nav-link active' : 'nav-link'" :to="{name: 'ranklist_index'}">排行榜</router-link>
        </li>
    </ul>
    <ul class="navbar-nav" v-if="$store.state.user.is_login">
      <li class="nav-item dropdown">
        <div class="nav-link dropdown-toggle" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
          {{ $store.state.user.username }}
        </div>
        <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
          <li><router-link class="dropdown-item" :to="{name: 'userbot_index'}">My Bot</router-link></li>
          <li><router-link class="dropdown-item" :to="{name: 'userlist'}">My Space</router-link></li>
          <li><hr class="dropdown-divider"></li>
          <li><router-link class="dropdown-item" :to="{name: 'login'}" @click="logout">退出</router-link></li>
        </ul>
      </li>
    </ul>
    <ul class="navbar-nav" v-else>
      <li class="nav-item">
        <router-link class="nav-link" :to="{name: 'login'}">
          登录
        </router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link" :to="{name: 'register'}">
          注册
        </router-link>
      </li>
    </ul>
  </div>
  </div>
  </nav>
    

</template>

<script>
import { useRoute } from 'vue-router'
import { computed } from 'vue'
import { useStore } from 'vuex';

export default {
  setup (){
    const route = useRoute();
    const store = useStore();
    let route_name = computed(() => route.name)

    const logout = () =>{
      store.dispatch("logout");
    }

    return {
      route_name,
      logout,
    }
  }
}
</script>

<style scoped>

</style>