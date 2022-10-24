<template>
  <div class="result-board">
    <div class="result-board-text" v-if="$store.state.pk.winer === 'all'">
      Draw!
    </div>
    <div class="result-board-text" v-else-if="$store.state.pk.winer === 'A' && $store.state.pk.a_id === parseInt($store.state.user.id)">
      Win!
    </div>
    <div class="result-board-text" v-else-if="$store.state.pk.winer === 'B' && $store.state.pk.b_id === parseInt($store.state.user.id)">
      Win!
    </div>
    <div class="result-board-text" v-else>
      Lose!
    </div>
    <div class="result-board-btn">
      <button @click="restart" type="button" class="btn btn-warning "> 
        不服再来！
      </button>
    </div>
  </div>
</template>

<script>
import { useStore } from 'vuex'

export default {
  setup() {
    const store = useStore();

    const restart = () => {
      store.commit("updateStatus","matching");
      store.commit("updateWiner","none");
      store.commit("updateOpponent",{
        username: "我的对手",
        photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png", 
      });
    }
    
    return {
      restart,
    }
  }
}
</script>

<style scoped>
.result-board{
  height: 30vh;
  width: 30vw;
  background-color: rgba(30 ,30 ,30 , 0.5);
  position: absolute;
  top: 30vh;
  left: 35vw;
}
.result-board-text{
  text-align: center;
  color: white;
  font-size: 60px;
  font-weight: 600;
  font-style: italic;
  padding-top: 5vh;
}
.result-board-btn{
  text-align: center;
  padding-top: 5vh;
}
</style>