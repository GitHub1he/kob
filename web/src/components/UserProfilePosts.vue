<template>
    <div class="card">
        <div class="card-body">
            <div v-for="post in posts.posts" :key="post.id">
                <div class="card single-post">
                    <div class="card-body">
                        {{ post.post }}
                        <button @click="del_post(post.id)" type="button" class="btn btn-danger btn-sm" v-if="is_me">删除</button>
                    </div>
                </div>
                
            </div>
        </div>
    </div>
</template>

<script>
import { computed } from 'vue';
import { useStore } from 'vuex';
import $ from 'jquery';

export default {
    name: "UserprofilePosts",
    props: {
        posts: {
            type: Object,
            required: true,
        },
        user: {
            type: Object,
            required: true,
        }
    },
    setup(props, context) {
        const store = useStore();
        let is_me = computed(() => store.state.user.id === props.user.id);

        const del_post = post_id =>{
            $.ajax({
              url: "http://localhost:3000/api/user/myspace/posts/del/",
              type: "DELETE",
              data:{
                post_id
              },
              headers: {
                'Authorization': "Bearer " + store.state.user.token,
              },
              success(resp){
                if(resp.error_message === "success") {
                    context.emit('del_post',post_id);
                }
              }
            });
        };

        
        return {
            is_me,
            del_post,
        }
    }
}
</script>

<style scoped>
.single-post{
    margin-bottom: 10px;
}

button{
    width: 10%;
    float: right;
}
</style>