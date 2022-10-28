import { createRouter, createWebHistory } from 'vue-router'
import PKIndexView from '@/views/pk/PKIndexView'
import RecordIndexView from '@/views/record/RecordIndexView'
import RankListIndexView from '@/views/ranklist/RankListIndexView'
import UserBotIndexView from '@/views/user/bot/UserBotIndexView'
import UserAccountLoginView from '@/views/user/account/UserAccountLoginView'
import UserAccountRegisterView from '@/views/user/account/UserAccountRegisterView'
import UserMyspaceUserProfileView from '@/views/user/myspace/UserMyspaceUserProfileView'
import NotFound from '@/views/error/NotFound'
import store from '@/store/index'

const routes = [
  {
    path: "/",
    name: "home",
    redirect: "/userbot/",
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/pk/",
    name: "pk_index",
    component: PKIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/record/",
    name: "record_index",
    component: RecordIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/ranklist/",
    name: "ranklist_index",
    component: RankListIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/userbot/",
    name: "userbot_index",
    component: UserBotIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: '/user/myspace/userprofile/:userId/',
    name: 'userprofile',
    component: UserMyspaceUserProfileView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/user/account/login/",
    name: "login",
    component: UserAccountLoginView,
  },
  {
    path: "/user/account/register/",
    name: "register",
    component: UserAccountRegisterView,
  },
  {
    path: "/404/",
    name: "404",
    component: NotFound,
  },
  {
    path: "/:catchAll(.*)",
    redirect: "/404/",
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) =>{
  let flag = 1;
  const jwt_token = localStorage.getItem("jwt_token");

  if (jwt_token) {
    store.commit("updateToken", jwt_token);
    store.dispatch("getinfo", {
      success() {
      },
      error() {
        store.dispatch("logout");
        alert("token无效,请重新登录！");
        router.push({ name: 'login' });
      }
    })
  } else {
    flag = 2;
  }

  if (to.meta.requestAuth && !store.state.user.is_login) {
    if (flag === 1) {
      next();
    } else {
      store.commit("logout");
      alert("请先进行登录！");
      next({name: "login"});
    }
  } else {
    next();
  }
})

export default router
