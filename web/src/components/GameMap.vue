<template>
  <div class="flag">
    <div class="flag-a f">
      <img class="img-fluid img-sm" :src="$store.state.user.photo" alt="">
      <div class="color-flag" style="background-color: #466f95" v-if="$store.state.pk.a_id === parseInt($store.state.user.id)">自己</div>
      <div class="color-flag" style="background-color: #deaeb1" v-else>对手</div>
    </div>
    <div class="flag-b f">
      <img class="img-fluid img-sm" :src="$store.state.pk.opponent_photo" alt="">
      <div class="color-flag" style="background-color: #466f95" v-if="$store.state.pk.b_id === parseInt($store.state.user.id)">自己</div>
      <div class="color-flag" style="background-color: #deaeb1" v-else>对手</div>
    </div>
  </div>
  <div ref="parent" class="gamemap">
    <canvas ref="canvas" tabindex="0"></canvas>
  </div>
</template>

<script>
import { GameMap } from "@/assets/script/GameMap";
import { ref, onMounted } from 'vue';
import { useStore } from "vuex";

export default {
  setup() {
    let parent = ref(null);
    let canvas = ref(null);
    const store = useStore();

    console.log(store.state.pk.a_id,store.state.user.id);
    onMounted(() =>{
      store.commit(
        "updateGameObject",
        new GameMap(canvas.value.getContext('2d'), parent.value, store)
      )
    })
    return {
      parent,
      canvas,
    }
  }
}

</script>

<style scoped>
.gamemap{
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
.flag{
  width: 80px;
  height: 75px;
  position: absolute;
  left: 8vh;
  background-color: rgba(50, 50, 50, 0.5);
}
img{
  border-radius: 50%;
  height: 30px;
  width: 30px;
}
.f{
  border-radius: 50%;
  padding-top: 5px ;
  line-height: 30px;
}
.color-flag{
  font-size: small;
  display:inline-block;
  height: 20px;
  width: 30px;
  text-align: center;
  margin-left: 10px;
}
</style>