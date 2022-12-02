<template>
  <div class="teaminfo" v-if="$store.state.chat.isteaminfo">
    <div class="row" style="margin: 0;">
      <div class="col-sm-4" v-for="user in users.value" :key="user.id" style="padding: 2px 3px;">
        <div class="card" style="height: 8vw">
          <div class="card-body" style="padding: 0;">
            
              <img :src="user.photo" alt="头像">
            
            <div class="card-body-info">
              <div class="name"> &nbsp; {{ user.name }}</div>
              <button @click="open_user_profile(user.id)" type="button" class="btn btn-outline-success">空间</button>
            </div>
          </div>
        </div>
      </div>

      <nav aria-label="Page navigation">
        <button @click="pull_page(current_page)" type="button" class="btn btn-light refresh">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
            class="bi bi-arrow-clockwise" viewBox="0 0 16 16">
            <path fill-rule="evenodd" d="M8 3a5 5 0 1 0 4.546 2.914.5.5 0 0 1 .908-.417A6 6 0 1 1 8 2v1z" />
            <path
              d="M8 4.466V.534a.25.25 0 0 1 .41-.192l2.36 1.966c.12.1.12.284 0 .384L8.41 4.658A.25.25 0 0 1 8 4.466z" />
          </svg>
        </button>
        <ul class="pagination" style="float: right;">
          <li class="page-item" @click="click_page(-2)">
            <a class="page-link" href="#" aria-label="Previous">
              <span aria-hidden="true">&laquo;</span>
            </a>
          </li>
          <li :class="'page-item ' + page.is_active" v-for="page in pages" :key="page.number"
            @click="click_page(page.number)">
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
    </div>
  </div>
</template>

<script>
import { useStore } from 'vuex';
import { reactive, ref } from 'vue';
import $ from 'jquery';
import router from "@/router";

export default {
  name: "ChatTeamInfo",
  setup() {
    const store = useStore();
    const users = reactive([]);
    let current_page = 1;
    let total_users = 0;
    let pages = ref([]);

    const click_page = page => {
      let max_pages = parseInt(Math.ceil(total_users / 6));

      if (page === -2) page = 1;
      else if (page === -1) page = max_pages;

      if (page >= 1 && page <= max_pages) {
        pull_page(page);
      }
    }

    const update_pages = () => {
      let max_pages = parseInt(Math.ceil(total_users / 6));
      let new_pages = [];
      for (let i = current_page - 2; i <= current_page + 2; i++) {
        if (i >= 1 && i <= max_pages) {
          new_pages.push({
            number: i,
            is_active: i === current_page ? 'active' : '',
          });
        }
      }
      pages.value = new_pages;
    }

    const pull_page = page => {
      console.log("拉取信息");

      current_page = page;
      $.ajax({
        url: "http://127.0.0.1:3000/api/team/getusers/",
        type: "GET",
        data: {
          team_id: store.state.chat.chatuserid,
          page: page,
        },
        headers: {
          'Authorization': "Bearer " + store.state.user.token,
        },
        success(resp) {
          console.log(resp);
          users.value = resp.users;
          total_users = resp.users_count;
          update_pages();
        }
      })
    }
    // pull_page(current_page);

    const open_user_profile = userId => {
      store.commit("updateFlag", 0);
      router.push({
        name: 'userprofile',
        params: { userId }
      });
    };
    const close_team_info = () => {
      store.commit("updateIsTeamInfo", false);
    }

    return {
      users,
      pages,
      current_page,
      pull_page,
      click_page,
      open_user_profile,
      close_team_info,
    }
  }
};
</script>

<style scoped>
.teaminfo {
  background-color: rgba(0.5, 0.5, 0.5, 0.1);
  user-select: none;
}

img {
  width: 5vw;
  height: 5vw;
  border-radius: 50%;
}

.name {
  width: 8vw;
  font-weight: bold;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}

.refresh {
  float: left;
  margin: 1vh 0;
}

ul.pagination {
  margin: 1vh 0;
}

.card-body-info {
  display: inline-block;
  margin-left: 1vw;
  margin-top:  3vh;
}
.card-body-info .btn-outline-success {
  width: 5vw;
  height: 4vh;
  margin-top: 1vh;
}
</style>