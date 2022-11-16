<template>
  <ContentField>
    <div class="row justify-content-md-center">
      <div class="col-3">
        <form @submit.prevent="login">
          <div class="mb-3">
            <label for="email" class="form-label">Email address</label>
            <input v-model="username" type="text" class="form-control" id="email">
          </div>
          <div class="mb-3">
            <label for="password" class="form-label">Password</label>
            <input v-model="password" type="password" class="form-control" id="password">
          </div>
          <div class="error_message">{{ error_message }}</div>
          <button type="submit" class="btn btn-primary">Submit</button>
        </form>
      </div>
    </div>
  </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue";
import { useStore } from 'vuex'
import { ref } from 'vue'
import router from "@/router/index";

export default{
  components: {
    ContentField,
  },
  setup() {
    const store = useStore();
    let username = ref('');
    let password = ref('');
    let error_message = ref('');

    const login = () => {
      error_message.value = "";
      store.dispatch("login",{
        username: username.value,
        password: password.value,
        success() {
          store.dispatch("getinfo",{
            success() {
              router.push({ name: 'home' });
            },
            error() {
              console.log("获取用户失败,请重新输入");
            }
          }) 
        },
        error() {
          error_message.value = "用户名或密码错误";
        }
      })
    }

    return {
      username,
      password,
      error_message,
      login,
    }
  }
}
</script>

<style scoped>
button{
  width: 100%;
}

.error_message{
  color: red;
}
</style>