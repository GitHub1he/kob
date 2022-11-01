<template>
  <div class="flag-a flag" v-if="$store.state.pk.a_id === parseInt($store.state.user.id)">
    <img class="img-fluid img-sm" :src="$store.state.user.photo" alt="">
    自己
  </div>
  <div class="flag-b flag" v-if="$store.state.pk.b_id === parseInt($store.state.user.id)">
    <img class="img-fluid img-sm" :src="$store.state.pk.opponent_photo" alt="">
    自己
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

    console.log(store.state.pk.a_id, store.state.user.id);
    onMounted(() => {
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
.gamemap {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.flag {
  width: 80px;
  height: 75px;
  position: absolute;
  left: 8vh;
  background-color: rgba(50, 50, 50, 0.5);
}

img {
  border-radius: 50%;
  height: 30px;
  width: 30px;
}

.flag-a {
  background-color: #466f95;
}

.flag-b {
  background-color: #deaeb1;
}

.flag {
  float: left;
}
</style>