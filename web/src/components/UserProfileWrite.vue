<template>
    <div class="card edit-field">
        <div class="card-body">
            <label for="edit-post" class="from-label">快来分享一下心情吧~~</label>
            <textarea v-model="content" class="form-control" row="10" id="edit-post"></textarea>
            <button @click="submit_post" type="button" class="btn btn-primary btn-sm">发帖</button>
        </div>
    </div>
</template>

<script>
import { ref } from 'vue';
import $ from 'jquery';
import { useStore } from 'vuex';

export default {
    name: "UserProfileWrite",
    setup(props,context) {
      const store = useStore();
      let content = ref('');

      const submit_post = () =>{
          $.ajax({
            url:"http://localhost:3000/user/myspace/posts/create/",
            type: "POST",
            data : {
              content: content.value,
            },
            headers:{
                'Authorization': "Bearer " + store.state.user.token,
            },
            success(resp) {
              if(resp.error_message === "success"){
                context.emit('submit_post',content.value);
                content.value = "";
              }
            }
          })
      };
      return {
        content,
        submit_post
      }
    }
}
</script>

<style scoped>
.edit-field{
    margin-top: 20px;
}
button {
    margin-top: 10px;
    float: right;
}
</style>