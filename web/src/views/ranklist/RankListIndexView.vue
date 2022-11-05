<template>
  <ContentField>
    <table class="table table-striped table-hover" style="margin-top: 15px; text-align: center;">
      <thead>
        <tr>
          <th>玩家</th>
          <th>天梯分</th>
          <th>粉丝数</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.id" @click="open_user_profile(user.id)">
          <td>
            <img :src="user.photo" alt="" class="user-photo">
            &nbsp;
            <span class="user-username"> {{ user.username }}</span>
          </td>
          <td>
            {{ user.rating }}
          </td>
          <td>
            {{ user.followercount }}
          </td>
        </tr>
      </tbody>
    </table>
    <nav aria-label="Page navigation example">
      <ul class="pagination" style="float: right;">
        <li class="page-item" @click="click_page(-2)">
          <a class="page-link" href="#" aria-label="Previous">
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
        <li :class="'page-item ' + page.is_active" v-for="page in pages" :key="page.number" @click="click_page(page.number)">
          <a class="page-link" href="#">
            {{ page.number }}
          </a>
        </li>
        <li class="page-item" @click="click_page(-1)">
          <a class="page-link" href="#" aria-label="Next">
            <span aria-hidden="true">&raquo;</span>
          </a>
        </li>
      </ul>
    </nav>
  </ContentField>
</template>

<script>
import ContentField from "../../components/ContentField.vue";
import { useStore } from 'vuex';
import { ref } from 'vue';
import $ from 'jquery';
import router from "@/router";

export default{
  components: {
    ContentField,
  },
  setup() {
    const store = useStore();
    const users = ref([]);
    let current_page = 1;
    let total_users = 0;
    let pages = ref([]);

    const click_page = page => {
      let max_pages = parseInt(Math.ceil(total_users/5));

      if(page === -2) page = 1;
      else if (page === -1) page = max_pages;

      if(page >= 1 && page <= max_pages) {
        pull_page(page);
      }
    }

    const update_pages = () => {
      let max_pages = parseInt(Math.ceil(total_users/5));
      let new_pages = [];
      for(let i = current_page - 2; i <= current_page + 2 ; i++) {
        if(i >= 1 && i <= max_pages) {
          new_pages.push({
            number: i, 
            is_active: i === current_page ? 'active' : '',
          });
        }
      }
      pages.value = new_pages;
    }

    const pull_page = page => {
      current_page = page;
      $.ajax({
        url: "http://127.0.0.1:3000/ranklist/getlist/",
        type: "GET",
        data: {
          page,
        },
        headers:{
          'Authorization': "Bearer " + store.state.user.token,
        }, 
        success(resp) {
          users.value = resp.users;
          total_users = resp.users_count;
          update_pages();
        },
      })
    }
    pull_page(current_page);

    const open_user_profile = userId => {
        store.commit("updateFlag",0);
        router.push({
          name: 'userprofile',
          params: { userId }
        });
    };

    return {
      users,
      pages,
      click_page,
      open_user_profile,
    }
  }
}
</script>

<style scoped>
img.user-photo {
  width: 5vh;
  height: 5vh;
  border-radius: 50%;
}
.user-username {
  font-weight: bold;
}
</style>