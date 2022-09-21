<template>
  <div>
    <div>姓名： {{ my_name }}</div>
    <div>年龄： {{ my_age }}</div>
  </div>
  <router-view/>
</template>

<script>

import $ from 'jquery';
import { ref } from 'vue';

export default{
  name:"App",
  setup: () => {
    let my_name = ref("");
    let my_age = ref("");

    $.ajax({
      url: "http://localhost:3000/pk/getuserinfo/",
      type: "get",
      success: resp => {
        my_name.value = resp.name;
        my_age.value = resp.age;
      }
    });
    return {
      my_name,
      my_age
    }
  }
} 
</script>

<style>
  body{
    background-image: url("@/assets/background.jpg");
    background-size: cover;
  }
</style>
